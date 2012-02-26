/*
 * @(#) Roberto.java
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


/**
 * Roberto implementation of Robot
 */
public class Roberto extends Robot {
   
   public static final String TAG = "Roberto";
   
   /** Name of the current action */
   protected String message = "";
   
   /** Roberto picture to draw */
   protected Drawable pose = null;
   
   
   public Roberto(Context context) {
      super(context);
   }
   
   
   public Roberto(Context context, AttributeSet attribs) {
      super(context, attribs);
   }
   
   
   protected void onDraw(Canvas canvas) {
      int w = getWidth();
      int h = getHeight();
      int dx, dy, dw, dh;
      float ds;
      
      Resources res = getResources();
      
      drawBackground(canvas);
      
      if (isRunning() && this.pose != null) {
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

}