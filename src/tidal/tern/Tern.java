/*
 * @(#) CompileException.java
 * 
 * Tern Tangible Programming Language
 * Copyright (c) 2011 Michael S. Horn
 * 
 *           Michael S. Horn (michael.horn@tufts.edu)
 *           Northwestern University
 *           2120 Campus Drive
 *           Evanston, IL 60613
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (version 2) as
 * published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package tidal.tern;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.net.Uri;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import tidal.tern.rt.Interpreter;
import tidal.tern.rt.Debugger;
import tidal.tern.rt.Process;



public class Tern extends Activity implements OnClickListener, Runnable, Debugger {
   
   public static final String TAG = "TernMob";
   
   public static final int CAMERA_PIC_REQUEST = 2500;
   
   public static final int COMPILE_SUCCESS = 100;
   public static final int COMPILE_FAILURE = 101;
   
   
   protected ProgramView view;
   
   protected File path = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES);
   protected File temp = new File(path, "capture.jpg");

   /** Used to run tern programs */
   protected Interpreter interp = new Interpreter();
   
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
      
      try {
         Log.i(TAG, "Loading statements");
         XmlResourceParser xml = getResources().getXml(R.xml.statements);
         Log.i(TAG, "got resource file");
         StatementFactory.loadStatements(xml);
         Log.i(TAG, "loaded");
      } catch (CompileException cx) {
         Log.e(TAG, cx.getMessage());
      }

      this.compiler.setHeader(loadDriverFile());

      this.view = (ProgramView)findViewById(R.id.ProgramView);
      this.view.setTern(this);
      this.interp.addDebugger(this);
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
         /*
         BitmapDrawable test = (BitmapDrawable)getResources().getDrawable(R.drawable.test);
         this.bitmap = test.getBitmap();
         this.view.setBitmap(bitmap);
         startCompile();
         */
         
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
   
   
   public Program getProgram() {
      return this.program;
   }
   
   protected void startCompile() {
      this.compiling = true;      
      this.pd = ProgressDialog.show(this, "Tern", "Compiling Program...", true, false);
      (new Thread(this)).start();
   }
   
   
   protected void finishCompile(boolean success) {
      this.pd.dismiss();
      this.compiling = false;
      this.view.invalidate();
      
      if (!success) return;
      
      Log.i(TAG, "Compile Finished");
      Log.i(TAG, program.getTextCode());
      Log.i(TAG, program.getAssemblyCode());
      try {
         this.interp.clear();
         this.interp.load(program.getAssemblyCode());
         this.interp.start();
      } catch (Exception x) {
         Log.e(TAG, "Interpreter error", x);
      }
   }
   
   
   public void run() {
      try {
         Log.i(TAG, "Running...");
         this.program = compiler.compile(this.bitmap);
         handler.sendEmptyMessage(COMPILE_SUCCESS);
      }
      catch (CompileException cx) {
         Log.e(TAG, cx.getMessage());
         
         // TODO Signal the error to the user
         handler.sendEmptyMessage(COMPILE_FAILURE);
      }
   }
   
   
   private Handler handler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
         finishCompile(msg.what == COMPILE_SUCCESS);
      }
   };
   
   
   private String loadDriverFile() {
      String result = "";
      try {
         BufferedReader in = new BufferedReader(
            new InputStreamReader(
               getResources().openRawResource(R.raw.driver)));
         String line;
         while ((line = in.readLine()) != null) {
            result += line + "\n";
         }
      } catch (IOException iox) {
         Log.e(TAG, "Error reading header file", iox);
      }
      return result;
   }
   
   
   public void processStarted(Process p) {
      Log.i(TAG, "Process started");
   }
   
   public void processStopped(Process p) {
      Log.i(TAG, "Process stopped");
      this.view.setMessage("DONE!");
      this.view.repaint();
   }
   
   public void trace(Process p, String message) {
      Log.i(TAG, "Trace: " + message);
      this.view.setMessage(message.toUpperCase());
      this.view.repaint();
   }
   
   public void print(Process p, String message) { }
   
   public void error(Process p, String message) {
      Log.i(TAG, "Error: " + message);
   }
   
}
