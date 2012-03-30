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
import java.util.Map;

import tidal.tern.rt.Robot;


/**
 * Roberto implementation of Robot
 */
public class Roberto implements Robot {
   
   public static final String TAG = "Roberto";

   /** Frame rate constant (milliseconds) */   
   private static final int DURATION = 200;
   
   
   /** Reference to the View object */
   protected ProgramView view;
   
   /** Current frame index to be drawn */
   private int frame = 0;
   
   /** Frame count for current animation pose */
   private int fcount = 0;
      
   /** Name of current animation */   
   private String pose = null;
   
   private long last_tick = 0;
   
   
   public Roberto(ProgramView view) {
      this.view = view;
   }
   
   
   /**
    * These functions are inherited from the Robot interface but not
    * needed for Roberto.
    */
   public boolean isConnected() { return true; }
   public void setAddress(String address) {  }
   public void openConnection() { }
   public void closeConnection() { }
   public void allStop() {
      frame = fcount;
   }
   
   
   private void drawFrame(Canvas canvas) {
      if (pose != null)  {
      
         // Determine the resource id and load the drawable
         Resources res = view.getResources();
         String name = pose + "0" + (int)Math.min(frame, fcount);
         Log.i(TAG, name);
         int id = res.getIdentifier(name, "drawable", "tidal.tern");
         Drawable current = res.getDrawable(id);
         
         // Draw the frame
         if (current != null) {
            int w = view.getWidth();
            int h = view.getHeight();
            int dw = current.getIntrinsicWidth();
            int dh = current.getIntrinsicHeight();
            int dx = w/2 - dw/2;
            int dy = h/2 - dh/2;
            current.setBounds(dx, dy, dx + dw, dy + dh);
            current.draw(canvas);
         }
      }
   }
   
   
   public void draw(Canvas canvas) {
      
      long elapsed = (System.currentTimeMillis() - last_tick);
      
      if (elapsed >= DURATION) {
         if (frame <= fcount) {
            last_tick = System.currentTimeMillis();
            frame++;
         }
      }
      drawFrame(canvas);
      view.repaint(DURATION);  // repaint after delay of 200 ms
   }

   
   private void changePicture(String pose, int frame_count) {
      this.pose = pose;
      this.fcount = frame_count;
      this.frame = 1;
      this.last_tick = 0;
      view.repaint();
   }
   
   
   public int doJump(int [] args) {
      changePicture("jump", 5);
      return 0;
   }
   
   
   public int doRun(int [] args) {
      changePicture("run", 1);
      return 0;
   }
   
   
   public int doWalk(int [] args) {
      changePicture("walk", 5);
      return 0;
   }
   
   
   public int doWiggle(int [] args) {
      changePicture("wiggle", 1);
      return 0;
   }
   
   
   public int doSleep(int [] args) {
      changePicture("sleep", 1);
      return 0;
   }
   
   
   public int doSit(int [] args) {
      changePicture("sit", 1);
      return 0;
   }
   
   
   public int doYawn(int [] args) {
      changePicture("yawn", 1);
      return 0;
   }
   
   
   public int doStand(int [] args) {
      changePicture("stand", 1);
      return 0;
   }
   
   
   public int doSpin(int [] args) {
      changePicture("spin", 1);
      return 0;
   }
   
   
   public int doDance(int [] args) {
      return 0;
   }

}