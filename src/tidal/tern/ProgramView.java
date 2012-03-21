/*
 * @(#) ProgramView.java
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

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.MotionEvent;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.content.res.Resources;
import android.media.SoundPool;
import android.media.AudioManager;
import android.util.Log;
import android.util.AttributeSet;

import tidal.tern.rt.*;
import tidal.tern.compiler.*;
import topcodes.TopCode;


/**
 * This class is responsible for painting the screen and debug
 * visuals
 */
public class ProgramView extends View implements Debugger, Runnable {
   
   public static final String TAG = "Tern";
   
   private int POP_SOUND = 0;
   public static final int COMPILE_SUCCESS = 100;
   public static final int COMPILE_FAILURE = 101;
   
   
   
   /** Used to compile bitmap images into programs */
   protected TangibleCompiler compiler;
   
   /** Used to run tern programs */
   protected Interpreter interp;
   
   /** Most recently compiled program */   
   protected Program program = null;
   
   /** Whether or not we're in the middle of a compile */
   protected boolean compiling = false;
   
   /** Current captured bitmap image */
   protected Bitmap bitmap = null;

   /** Robot that "executes" the interpreter commands */
   protected Robot robot;

   /** Sound effects */
   protected SoundPool sounds;
   
   /** Link back to the main activity */
   protected Tern tern = null;
   
   /** Name of the current statement (action) */
   protected String message = "";
   
   /** ID of the current statement */
   protected int trace_id = -1;
   
   /** Is a program running */
   protected boolean running = false;
   
   /** Progress dialog for compiles */   
   protected ProgressDialog pd = null;
   
   /** Compile (camera) button */
   protected TButton camera;
   
   /** Compile (gallery) button */
   protected TButton gallery;

   /** Resume (play) button */   
   protected TButton play;
   
   /** Pause button */
   protected TButton pause;
   
   /** Restart button */
   protected TButton restart;
   
   /** Connection config button */
   protected TButton config;

   
   public ProgramView(Context context) {
      super(context);
   }
      
      
   public ProgramView(Context context, AttributeSet attribs) {
      super(context, attribs);
   }
   
/**
 * Called from Activity.onCreate. Initialize everything...
 */
   public void init(Context context, Tern tern) {
      this.tern = tern;
      
      //------------------------------------------------------
      // Initialize the tangible compiler
      //------------------------------------------------------
      this.compiler = new TangibleCompiler(getResources(),
                                           R.xml.statements,
                                           R.raw.driver);
      
      //------------------------------------------------------
      // Initialize the "robot" connection manager
      //------------------------------------------------------
      this.robot = new NXTRobot(this);

      //------------------------------------------------------
      // Initialize the runtime interpreter
      //------------------------------------------------------
      this.interp = new Interpreter();
      this.interp.setRobot(robot);
      this.interp.addDebugger(this);
      
      //------------------------------------------------------
      // Initialize sound effects
      //------------------------------------------------------
      this.sounds = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
      POP_SOUND = sounds.load(context, R.raw.pop, 1);

      //------------------------------------------------------
      // Create UI buttons
      //------------------------------------------------------
      this.camera =
      new TButton(getResources(),
                  R.drawable.go,
                  R.drawable.go_dn,
                  R.drawable.go_off,
                  cameraHandler);
      
      this.gallery =
      new TButton(getResources(),
                  R.drawable.gallery,
                  R.drawable.gallery_dn,
                  R.drawable.gallery_off,
                  galleryHandler);
      
      this.play =
      new TButton(getResources(),
                  R.drawable.play,
                  R.drawable.play_dn,
                  R.drawable.play_off,
                  playHandler);
      
      this.pause =
      new TButton(getResources(),
                  R.drawable.pause,
                  R.drawable.pause_dn,
                  R.drawable.pause_off,
                  pauseHandler);
      
      this.restart =
      new TButton(getResources(),
                  R.drawable.restart,
                  R.drawable.restart_dn,
                  R.drawable.restart_off,
                  restartHandler);
      
      this.config =
      new TButton(getResources(),
                  R.drawable.config,
                  R.drawable.config_dn,
                  R.drawable.config_off,
                  configHandler);
   }
   
   
/**
 * Called from Activity.onDestroy()
 */
   public void destroy() {
      this.robot.closeConnection();
      if (this.bitmap != null) {
         this.bitmap.recycle();
         this.bitmap = null;
      }
   }
   
   
   public void setBluetoothDevice(String address) {
      this.robot.setAddress(address);
      this.robot.openConnection();
   }
   
   
/**
 * Called from the GO/CAMERA button. Starts a tangible compile
 * by launching a camera intent
 */
   public void startCompile(boolean capture) {
      if (compiling) return;
      try {
         if (this.bitmap != null) {
            this.bitmap.recycle();
            this.bitmap = null;
         }
         if (capture) {
            tern.captureBitmap();
         } else {
            tern.selectBitmap();
         }
      } catch (Exception x) {
         Log.e(TAG, "Save file error.", x);
      }
   }
   
   
   public void loadBitmap(Bitmap bitmap) {
      if (bitmap != null) {
         this.compiling = true;
         this.bitmap = bitmap;
         showProgressDialog("Compiling Program...");
         (new Thread(this)).start();
      }
   }
   
   
   protected void finishCompile(boolean success) {
      hideProgressDialog();
      this.compiling = false;
      if (!success) return;
      
      Log.i(TAG, "Compile Finished");
      try {
         this.interp.stop();
         this.interp.clear();
         this.interp.load(program.getAssemblyCode());
         this.interp.start();
      } catch (Exception x) {
         Log.e(TAG, "Interpreter error", x);
      }
   }

   
   public void run() {
      try {
         this.program = compiler.compile(this.bitmap);
         compileHandler.sendEmptyMessage(COMPILE_SUCCESS);
      }
      catch (CompileException cx) {
         Log.e(TAG, cx.getMessage());
         compileHandler.sendEmptyMessage(COMPILE_FAILURE);
      }
   }
   
   
   public boolean isRunning() {
      return this.running;
   }
   
   
/**
 * Handle button presses
 */
   public boolean onTouchEvent(MotionEvent event) {
      
      if (
         this.camera.touchEvent(event) ||
         this.gallery.touchEvent(event) ||
         this.play.touchEvent(event) ||
         this.pause.touchEvent(event) ||
         this.restart.touchEvent(event) ||
         this.config.touchEvent(event)) {
         repaint();
      }
      return true;
   }
   
   
   protected void onDraw(Canvas canvas) {
      int w = getWidth();
      int h = getHeight();
      int dx, dy, dw, dh;
      float ds;
      
      Resources res   = getResources();
      Drawable logo   = res.getDrawable(R.drawable.logo);
      Drawable status = res.getDrawable(robot.getIcon());

      // clear background 
      canvas.drawRGB(210, 210, 210);
      
      // draw logo
      dw = logo.getIntrinsicWidth();
      dh = logo.getIntrinsicHeight();
      ds = Math.min(0.8f, 0.8f * w / dw);
      dw *= ds;
      dh *= ds;
      dx = w/2 - dw/2;
      dy = h/2 - dh/2;
      logo.setBounds(dx, dy, dx + dw, dy + dh);
      logo.draw(canvas);

      // draw robot indicator
      dw = status.getIntrinsicWidth();
      dh = status.getIntrinsicHeight();
      dx = 10;
      dy = h - dh - 10;
      status.setBounds(dx, dy, dx + dw, dy + dh);
      status.draw(canvas);
      
      // draw message for current statement      
      if (isRunning()) {
         Paint font = new Paint(Paint.ANTI_ALIAS_FLAG);
         font.setColor(Color.BLACK);
         font.setStyle(Style.FILL);
         font.setTextSize(30);
         font.setTextAlign(Paint.Align.CENTER);
         canvas.drawText(this.message, w/2, 27, font);
      }
      
      // Draw program bitmap with debug info
      if (bitmap != null) {
         dw = bitmap.getWidth();
         dh = bitmap.getHeight();
         ds = ((float)h / dh) * 0.85f;
         dw *= ds;
         dh *= ds;
         dx = w/2 - dw/2;
         dy = h/2 - dh/2;

         RectF dest = new RectF(dx, dy, dx + dw, dy + dh);
         canvas.drawBitmap(bitmap, null, dest, null);
      }
      
      // Draw debug info for the program
      if (program != null) {
         canvas.save();
         canvas.translate(w/2 - dw/2, h/2 - dh/2);
         canvas.scale(ds, ds, 0, 0);
         for (Statement s : program.getStatements()) {
            TopCode top = new TopCode(s.getTopCode());
            if (s.getCompileID() == trace_id) {
               top.setDiameter( top.getDiameter() * 2.5f );
            } else {
               top.setDiameter( top.getDiameter() * 1.5f );
            }
            top.draw(canvas);
         }
         canvas.restore();
      }
      
      // Draw buttons
      this.camera.setLocation(w - 110, 10);
      this.gallery.setLocation(w - 110, 115);
      this.restart.setLocation(w - 110, 220);
      this.play.setLocation(w - 110, 325);
      this.pause.setLocation(w - 110, 325);
      this.config.setLocation(w - 110, 430);
      
      this.camera.setEnabled( !compiling );
      this.gallery.setEnabled( !compiling );
      this.restart.setEnabled( !compiling && bitmap != null );
      this.play.setEnabled( isRunning() );
      this.pause.setEnabled( isRunning() );
      this.config.setEnabled( !compiling );
      
      this.camera.draw(canvas);
      this.gallery.draw(canvas);
      this.restart.draw(canvas);
      this.pause.draw(canvas);
      this.config.draw(canvas);
   }

   
   public void showProgressDialog(String message) {
      this.pd = ProgressDialog.show(tern, TAG, message, true, false);
   }
   
   
   public void hideProgressDialog() {
      this.pd.dismiss();
   }
   
   
/**
 * DEBUGGER IMPLEMENTATION
 */
   public void processStarted(tidal.tern.rt.Process p) {
      this.running = true;
      repaint();
   }
   
   
   public void processStopped(tidal.tern.rt.Process p) {
      this.running = false;
      repaint();
   }

   
   public void trace(tidal.tern.rt.Process p, String message) {
      try {
         this.trace_id = Integer.parseInt(message);
         sounds.play(POP_SOUND, 1, 1, 1, 0, 1);
         repaint();
      } catch (Exception x) {
         this.trace_id = -1;
      }
   }

   
   public void print(tidal.tern.rt.Process p, String message) {
      Log.i(TAG, message);
      this.message = message;
      repaint();
   }
   
   
   public void error(tidal.tern.rt.Process p, String message) {
      Log.i(TAG, message);
      this.message = message;
      this.running = false;
      repaint();
   }
   
   
/**
 * Thread-safe invalidate function
 */
   public void repaint() {
      repaintHandler.sendEmptyMessage(0);
   }
   
   private Handler compileHandler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
         finishCompile(msg.what == COMPILE_SUCCESS);
      }
   };
   
   private Handler repaintHandler = new Handler() {
      @Override public void handleMessage(Message msg) {
         invalidate();
      }
   };
   
   private Handler cameraHandler = new Handler() {
      @Override public void handleMessage(Message msg) {
         startCompile(true);
      }
   };
   
   private Handler galleryHandler = new Handler() {
      @Override public void handleMessage(Message msg) {
         startCompile(false);
      }
   };
   
   private Handler playHandler = new Handler() {
      @Override public void handleMessage(Message msg) {
      }
   };
   
   private Handler pauseHandler = new Handler() {
      @Override public void handleMessage(Message msg) {
      }
   };
   
   private Handler restartHandler = new Handler() {
      @Override public void handleMessage(Message msg) {
         finishCompile(true);
      }
   };
   
   private Handler configHandler = new Handler() {
      @Override public void handleMessage(Message msg) {
         tern.selectBluetoothDevice();
      }
   };
}