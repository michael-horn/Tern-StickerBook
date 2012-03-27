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
package tidal.tern.rt;

import android.graphics.Canvas;


/**
 * This is a "robot" interface. The interpreter calls
 * functions on implementations of this class when it runs tern
 * code. In turn, this object displays animations of the Robot
 * on the screen.
 */
public interface Robot {
   
   public boolean isConnected();
   
   public void setAddress(String address);
   
   public void openConnection();
   
   public void closeConnection();
   
   /** Called when a program is stopped or paused by the user */
   public void allStop();
   
   public void draw(Canvas canvas);
   
}