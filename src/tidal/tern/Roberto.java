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

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.MotionEvent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.content.Context;
import android.content.res.Resources;

import tidal.tern.rt.Debugger;
import tidal.tern.rt.Process;


/**
 * This is an "robot" interface file. The interpreter calls
 * functions on this class when it runs tern code. In turn,
 * this object displays animations of Roberto on the screen.
 */
public class Roberto extends View implements Debugger {
   
   public static final String TAG = "Roberto";
   
   /** Name of the current action */
   protected String message = "";
   
   /** Roberto picture to draw */
   protected Drawable pose = null;
   
   /** Is there a program running */
   protected boolean running = false;
   
   /** Touch sensor latch */
   protected boolean tsensor = false;
   
   /** Link back to the main activity */
   protected Tern tern = null;
   
   
   public Roberto(Context context) {
      super(context);
   }
   
   
   public Roberto(Context context, AttributeSet attribs) {
      super(context, attribs);
   }
   
   
   public void setTern(Tern tern) {
      this.tern = tern;
   }
   
   
   public boolean onTouchEvent(MotionEvent event) {
      int action = event.getAction();
      if (action == MotionEvent.ACTION_DOWN) {
         this.tsensor = true;
      } else if (action == MotionEvent.ACTION_UP) {
         if (! running ) tern.onClick(this);
      }
      invalidate();
      return true;
   }

   
   protected void onDraw(Canvas canvas) {
      int w = getWidth();
      int h = getHeight();
      int dx, dy, dw, dh;
      float ds;
      
      Resources res = getResources();
      Drawable logo = res.getDrawable(R.drawable.logo);
      Drawable button = res.getDrawable(R.drawable.play_button_up);

      // clear background 
      canvas.drawRGB(210, 210, 210);
      
      if (!this.running) {

         // draw logo
         dw = logo.getIntrinsicWidth();
         dh = logo.getIntrinsicHeight();
         ds = Math.min(0.8f, 0.8f * w / dw);
         dw *= ds;
         dh *= ds;
         logo.setBounds(w/2 - dw/2, 70, w/2 + dw/2, 70 + dh);
         logo.draw(canvas);

         // draw button
         dw = button.getIntrinsicWidth();
         dh = button.getIntrinsicHeight();
         dx = w - dw - 10;
         dy = h - dh - 10;
         button.setBounds(dx, dy, dx + dw, dy + dh);
         button.draw(canvas);
      }
      
      // Draw roberto 
      else if (this.pose != null) {
         dw = pose.getIntrinsicWidth() / 3;
         dh = pose.getIntrinsicHeight() / 3;
         dx = w/2 - dw/2;
         dy = h/2 - dh/2;
         pose.setBounds(dx, dy, dx + dw, dy + dh);
         pose.draw(canvas);

         Paint font = new Paint(Paint.ANTI_ALIAS_FLAG);
         font.setColor(Color.BLACK);
         font.setStyle(Style.FILL);
         font.setTextSize(40);
         font.setTextAlign(Paint.Align.CENTER);
         canvas.drawText(this.message, w/2, h - 25, font);
      }
   }
   
   
/**
 * Thread safe invalidate function
 */
   public void repaint() {
      repaintHandler.sendEmptyMessage(0);
   }
   
   
   private Handler repaintHandler = new Handler() {
      @Override public void handleMessage(Message msg) {
         invalidate();
      }
   };
   
   
   private void changePicture(int d) {
      Resources res = getResources();
      this.pose = (d > 0)? res.getDrawable(d) : null;
      repaint();
   }
   
   
   public int doJump(int [] args) {
      changePicture(R.drawable.r_jump);
      this.message = "Jump";
      return 0;
   }
   
   
   public int doRun(int [] args) {
      changePicture(R.drawable.r_run);
      this.message = "Run";
      return 0;
   }
   
   
   public int doWalk(int [] args) {
      changePicture(R.drawable.r_walk);
      this.message = "Walk";
      return 0;
   }
   
   
   public int doWiggle(int [] args) {
      changePicture(R.drawable.r_wiggle);
      this.message = "Wiggle";
      return 0;
   }
   
   
   public int doSleep(int [] args) {
      changePicture(R.drawable.r_sleep);
      this.message = "Sleep";
      return 0;
   }
   
   
   public int doSit(int [] args) {
      changePicture(R.drawable.r_sit);
      this.message = "Sit";
      return 0;
   }
   
   
   public int doYawn(int [] args) {
      changePicture(R.drawable.r_yawn);
      this.message = "Yawn";
      return 0;
   }
   
   
   public int doStand(int [] args) {
      changePicture(R.drawable.r_stand);
      this.message = "Stand";
      return 0;
   }
   
   
   public int doSpin(int [] args) {
      changePicture(R.drawable.r_spin);
      this.message = "Spin";
      return 0;
   }
   
   
   public int doDance(int [] args) {
      return 0;
   }
   
   
   public int getTouchSensor(int [] args) {
      int result = tsensor ? 1 : 0;
      tsensor = false;
      return result;
   }
   
   
   public void processStarted(Process p) {
      this.running = true;
      this.tsensor = false;
   }
   
   public void processStopped(Process p) {
      this.running = false;
      changePicture(-1);
   }
   
   public void trace(Process p, String message) { }
   
   public void print(Process p, String message) { }
   
   public void error(Process p, String message) {
      Log.i(TAG, message);
      this.running = false;
   }
}