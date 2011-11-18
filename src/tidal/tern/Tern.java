package tidal.tern;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.net.Uri;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;

import android.provider.MediaStore;
import android.util.Log;
import android.os.Handler;
import android.os.Message;
import android.os.Environment;

import topcodes.*;
import tidal.tern.compiler.*;



public class Tern extends Activity implements OnClickListener, Runnable
{
   public static final String TAG = "TernMob";
   
   public static final int CAMERA_PIC_REQUEST = 2500;
   
   
   protected ProgramView view;
   
   protected File path = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES);
   protected File temp = new File(path, "capture.jpg");

   
   /** Used to compile bitmap images into programs */
   protected TangibleCompiler compiler = new TangibleCompiler();

   /** Most recently compiled program */   
   protected Program program = null;
   
   /** Whether or not we're in the middle of a compile */
   protected boolean compiling = false;
   
   /** Current captured bitmap image */
   protected Bitmap bitmap = null;

   /** Progress dialog for compiles */   
   protected ProgressDialog pd = null;
   
   /** Program execution status */
   protected String status = "";
   

   //----------------------------------------------------------------   
   // onCreate
   //----------------------------------------------------------------   
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      // Hide the window title
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
      
      setContentView(R.layout.main);
      
      this.view = (ProgramView)findViewById(R.id.ProgramView);
      this.view.setTern(this);
   }
   
   protected void onPause() {
      super.onPause();
   }
    
    
   //----------------------------------------------------------------
   // onClick -- Called by the compile/camera button
   //----------------------------------------------------------------   
   public void onClick(View view) {
      if (compiling) return;
      try {
         Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp));
         Log.i(TAG, String.valueOf(Uri.fromFile(temp)));
         startActivityForResult(intent, CAMERA_PIC_REQUEST);
      } catch (Exception x) {
         Log.e(TAG, "Save file error " + x);
      }
   }
   
   
   //----------------------------------------------------------------
   // onActivityResult -- Called by the ImageCapture intent
   //----------------------------------------------------------------
   protected void onActivityResult(int request, int result, Intent data) {
      switch (request) {
         
         case CAMERA_PIC_REQUEST:
            if (result == RESULT_OK) {
               try {
                  if (this.bitmap != null) {
                     this.bitmap.recycle();
                     this.bitmap = null;
                  }
                  this.bitmap = Media.getBitmap(getContentResolver(), Uri.fromFile(temp) );
                  view.setBitmap(bitmap);
                  startCompile();
                  
               } catch (FileNotFoundException e) {
                  Log.e(TAG, "File not found " + e);
               } catch (IOException e) {
                  Log.e(TAG, "Error reading file " + e);
               }
            }
            break;
      }
   }
   
   
   protected void startCompile() {
      this.compiling = true;      
      this.pd = ProgressDialog.show(this, "Tern", "Compiling Program...", true, false);
      (new Thread(this)).start();
   }
   
   
   protected void finishCompile() {
      this.pd.dismiss();
      this.compiling = false;
      try {
      } catch (Exception x) {
         Log.e(TAG, "Error running program " + x);
      }
   }
   
   
   public void run() {
      try {
         Log.i(TAG, "Running...");
         this.program = compiler.compile(this.bitmap);

         handler.sendEmptyMessage(0);
      }
      catch (CompileException cx) {
         Log.e(TAG, cx.getMessage());
         handler.sendEmptyMessage(1);
      }
   }
   
   
   private Handler handler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
         finishCompile();
      }
   };
   
   
   //------------------------------------------------------------
   // Program execution callbacks
   //------------------------------------------------------------
   public synchronized void programStarted() {
      this.status = "Running";
      Log.i(TAG, status);
   }
   
   public synchronized void programStopped() {
      this.status = "Done";
   }
   
   public synchronized void programTrace(int cid) {
      Log.i(TAG, "Code: " + cid);
      if (this.program != null && cid >= 0) {
         Statement s = program.getStatement(cid);
         if (s != null) {
            this.status = s.toString();
         }
      }
   }
   
   public synchronized String getProgramStatus() {
      return this.status;
   }
}
