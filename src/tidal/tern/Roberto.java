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

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.content.res.Resources;
import android.util.Log;

import tidal.tern.rt.Robot;


/**
 * Roberto implementation of Robot
 */
public class Roberto implements Robot {
   
   public static final String TAG = "Roberto";
   
   /** Reference to the View object */
   protected ProgramView view;
   
   /** Roberto picture to draw */
   protected Drawable pose = null;
   
   
   public Roberto(ProgramView view) {
      this.view = view;
   }
   
   
   public boolean isConnected() { return true; }
   public void setAddress(String address) {  }
   public void openConnection() { }
   public void closeConnection() { }
   public void allStop() { }
   
   
   public void draw(Canvas canvas) {
      int w = view.getWidth();
      int h = view.getHeight();
      int dx, dy, dw, dh;
      float ds;
      
      if (this.pose != null) {
         dw = pose.getIntrinsicWidth() / 3;
         dh = pose.getIntrinsicHeight() / 3;
         dx = w/2 - dw/2;
         dy = h/2 - dh/2;
         pose.setBounds(dx, dy, dx + dw, dy + dh);
         pose.draw(canvas);
      }
   }

   
   private void changePicture(int d) {
      Resources res = view.getResources();
      this.pose = (d > 0)? res.getDrawable(d) : null;
      view.repaint();
   }
   
   
   public int doJump(int [] args) {
      changePicture(R.drawable.r_jump);
      return 0;
   }
   
   
   public int doRun(int [] args) {
      changePicture(R.drawable.r_run);
      return 0;
   }
   
   
   public int doWalk(int [] args) {
      changePicture(R.drawable.r_walk);
      return 0;
   }
   
   
   public int doWiggle(int [] args) {
      changePicture(R.drawable.r_wiggle);
      return 0;
   }
   
   
   public int doSleep(int [] args) {
      changePicture(R.drawable.r_sleep);
      return 0;
   }
   
   
   public int doSit(int [] args) {
      changePicture(R.drawable.r_sit);
      return 0;
   }
   
   
   public int doYawn(int [] args) {
      changePicture(R.drawable.r_yawn);
      return 0;
   }
   
   
   public int doStand(int [] args) {
      changePicture(R.drawable.r_stand);
      return 0;
   }
   
   
   public int doSpin(int [] args) {
      changePicture(R.drawable.r_spin);
      return 0;
   }
   
   
   public int doDance(int [] args) {
      return 0;
   }

}