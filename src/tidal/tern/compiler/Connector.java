/*
 * @(#) Connector.java
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
package tidal.tern.compiler;

import topcodes.*;
import java.io.PrintWriter;


/**
 * Represents an ingoing or outgoing connection from one statement
 * (the owner) to another.
 *
 * @author Michael Horn
 */
 public class Connector {


   public static final int TYPE_IN    = 0;   // incoming connection
   public static final int TYPE_OUT   = 1;   // outgoing connection
   public static final int TYPE_PARAM = 2;   // parameter connection
   
   
   protected int type;
   protected String name;
   protected float dx, dy;
   protected Statement other;
   
   
   public Connector(int type) {
      this(type, "", 0, 0);
   }
   
   public Connector(int type, String name, float dx, float dy) {
      this.type = type;
      this.name = name;
      this.dx = dx;
      this.dy = dy;
      this.other = null;
   }
   
   public Connector clone() {
      return new Connector(this.type, this.name, this.dx, this.dy);
   }
   
   public String getName() {
      return this.name;
   }
   
   public void setName(String name) {
      this.name = name;
   }
   
   public int getType() {
      return this.type;
   }
   
   public boolean isOutgoing() {
      return this.type == TYPE_OUT;
   }
   
   public boolean isIncoming() {
      return this.type == TYPE_IN;
   }
   
   public boolean isParameter() {
      return this.type == TYPE_PARAM;
   }
   
   public float getDeltaX() {
      return this.dx;
   }
   
   public void setDeltaX(float dx) {
      this.dx = dx;
   }
   
   public float getDeltaY() {
      return this.dy;
   }
   
   public void setDeltaY(float dy) {
      this.dy = dy;
   }
   
   public void setDelta(float dx, float dy) {
      this.dx = dx;
      this.dy = dy;
   }
   
   public float getTargetX(TopCode top) {
      float d = top.getDiameter();
      float o = top.getOrientation();
      float x = top.getCenterX();
      return (float)(x + dx * d * Math.cos(o) - dy * d * Math.sin(o));
   }
   
   public float getTargetY(TopCode top) {
      float d = top.getDiameter();
      float o = top.getOrientation();
      float y = top.getCenterY();
      return (float)(y + dx * d * Math.sin(o) + dy * d * Math.cos(o));
   }
   
   public Statement getConnection() {
      return this.other;
   }
   
   public void setConnection(Statement other) {
      this.other = other;
   }
   
   public boolean hasConnection() {
      return this.other != null;
   }
   
   public String toString() {
      String s = "";
      switch (type) {
         case TYPE_IN: s += "IN: "; break;
         case TYPE_OUT: s += "OUT: "; break;
         case TYPE_PARAM: s += "PARAM: "; break;
      }
      s += name + " (" + dx + "," + dy + ")";
      return s;
   }
}
