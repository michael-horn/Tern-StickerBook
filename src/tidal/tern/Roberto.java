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

import android.util.Log;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import tidal.tern.rt.Debugger;
import tidal.tern.rt.Process;


/**
 * This is an "robot" interface file. The interpreter calls
 * functions on this class when it runs tern code. 
 */
public class Roberto implements Debugger {
   
   private Resources res;
   private ProgramView view;
   
   public Roberto(Resources res, ProgramView view) {
      this.res = res;
      this.view = view;
   }
   
   
   private void changePicture(int d) {
      this.view.setPicture(d);
      this.view.repaint();
   }
   
   
   public void doJump(int [] args) {
      changePicture(R.drawable.r_jump);
   }
   
   
   public void doRun(int [] args) {
      changePicture(R.drawable.r_run);
   }
   
   
   public void doWalk(int [] args) {
      changePicture(R.drawable.r_walk);
   }
   
   
   public void doWiggle(int [] args) {
      changePicture(R.drawable.r_wiggle);
   }
   
   
   public void doSleep(int [] args) {
      changePicture(R.drawable.r_sleep);
   }
   
   
   public void doSit(int [] args) {
      changePicture(R.drawable.r_sit);
   }
   
   
   public void doStand(int [] args) {
      changePicture(R.drawable.r_stand);
   }
   
   
   public void doSpin(int [] args) {
      changePicture(R.drawable.r_spin);
   }
   
   
   public void doDance(int [] args) {
   }
   
   
   public void processStarted(Process p) {
   }
   
   public void processStopped(Process p) {
      changePicture(-1);
   }
   
   public void trace(Process p, String message) { }
   
   public void print(Process p, String message) { }
   
   public void error(Process p, String message) { }
   
}