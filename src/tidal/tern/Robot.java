/*
 * @(#) Robot.java
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
 * This is an "robot" view class. The interpreter calls
 * functions on this class when it runs tern code. In turn,
 * this object displays animations of the Robot on the screen.
 */
public class Robot extends View implements Debugger {
   
   public static final String TAG = "Robot";
   
   /** Is there a program running */
   protected boolean running = false;
   
   /** Touch sensor latch */
   protected boolean tsensor = false;
   
   /** Link back to the main activity */
   protected Tern tern = null;
   
   /** Name of the current action */
   protected String message = "";
   
   /** ID of current statement */
   protected int trace_id = -1;
   
   
   public Robot(Context context) {
      super(context);
   }
   
   
   public Robot(Context context, AttributeSet attribs) {
      super(context, attribs);
   }
   
   
   public void setTern(Tern tern) {
      this.tern = tern;
   }
   
   
   public boolean isRunning() {
      return this.running;
   }
   
   
   public void openConnection(String address) {
      
   }
   
   
   public void closeConnection() {
      
   }
   
   
/**
 * Compile ID number of current executing statement
 */
   public int getTraceID() {
      return this.trace_id;
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


   protected void drawBackground(Canvas canvas) {
      int w = getWidth();
      int h = getHeight();
      int dx, dy, dw, dh;
      float ds;
      
      Resources res = getResources();
      Drawable logo = res.getDrawable(R.drawable.logo);
      Drawable button = res.getDrawable(R.drawable.play_button_up);

      // clear background 
      canvas.drawRGB(210, 210, 210);
      
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
      
      
      // draw message for current statement      
      if (isRunning()) {
         Paint font = new Paint(Paint.ANTI_ALIAS_FLAG);
         font.setColor(Color.BLACK);
         font.setStyle(Style.FILL);
         font.setTextSize(30);
         font.setTextAlign(Paint.Align.CENTER);
         canvas.drawText(this.message, w/2, h - 15, font);
      }
   }
   
   
   protected void drawProgramBitmap(Canvas canvas) {
      this.tern.draw(canvas);
   }

   
   protected void onDraw(Canvas canvas) {
      drawBackground(canvas);
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
   }

   
   public void trace(Process p, String message) {
      try {
         this.trace_id = Integer.parseInt(message);
         repaint();
      } catch (Exception x) {
         this.trace_id = -1;
      }
   }

   
   public void print(Process p, String message) {
      Log.i(TAG, message);
      this.message = message;
      repaint();
   }
   
   
   public void error(Process p, String message) {
      Log.i(TAG, message);
      this.running = false;
   }
}