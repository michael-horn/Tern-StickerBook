/*
 * @(#) ConnectionMap.java
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

import java.util.List;
import topcodes.*;


/**
 * This class facilitates the process of pairing sockets and
 * connectors of statements in a tangible program. 
 * 
 * @author Michael Horn
 * @version $Revision: 1.1 $, $Date: 2007/06/23 16:08:24 $
 */
public class ConnectionMap {

   protected List sockets;
   protected List connectors;
   
   
   
/**
 * Default constructor.  Creates an empty connection map.
 */
   public ConnectionMap() {
      this.sockets = new java.util.ArrayList();
      this.connectors = new java.util.ArrayList();
   }
   
   
   
/**
 * Clears all registered parameters, sockets, and connectors.
 */
   public void clear() {
      this.sockets.clear();
      this.connectors.clear();
   }
   
   
   
/**
 * Adds a socket location to the connection map and links it back
 * to its originating statement.
 */
   public void addSocket(Statement statement, float dx, float dy) {
      TopCode top = statement.getTopCode();
      if (top == null) return;
      float unit = top.getDiameter();
      float theta = top.getOrientation();
      float x = top.getCenterX();
      float y = top.getCenterY();
      x += (dx * unit * Math.cos(theta) - dy * unit * Math.sin(theta));
      y += (dx * unit * Math.sin(theta) + dy * unit * Math.cos(theta));
      float u = (float) (unit * 0.8);
      this.sockets.add(new TSocket(statement, x, y, u));
   }
   
   
   
/**
 * Adds a connector location to the connection map and links it back
 * to its originating statement.
 */
   public void addConnector(Statement statement, String name, float dx, float dy) {
      TopCode top = statement.getTopCode();
      if (top == null) return;
      float unit = top.getDiameter();
      float theta = top.getOrientation();
      float x = top.getCenterX();
      float y = top.getCenterY();
      x += (dx * unit * Math.cos(theta) - dy * unit * Math.sin(theta));
      y += (dx * unit * Math.sin(theta) + dy * unit * Math.cos(theta));
      this.connectors.add(new TConnector(statement, name, x, y));
   }
   
   
   
/**
 * Uses an inefficient (O(n^2)) algorithm to find and connect all
 * sockets and connectors that overlap in the digital image.
 */
   public void formConnections() {
      TSocket s;
      TConnector c;
      for (int j=0; j<connectors.size(); j++) {
         c = (TConnector)connectors.get(j);
         for (int i=0; i<sockets.size(); i++) {
            s = (TSocket)sockets.get(i);
            if (!c.attached && s.isConnectedTo(c)) {
               c.attached = true;
               //c.statement.connect(s.statement, c.name);
               break;
            }
         }
      }
   }
   
   
   
//-------------------------------------------------------
// Inner class: TSocket
//-------------------------------------------------------
   class TSocket {
      public float x;
      public float y;
      public float r;
      public Statement statement;
      
      public TSocket(Statement s, float x, float y, float r) {
         this.statement = s;
         this.x = x;
         this.y = y;
         this.r = r;
      }
      
      public boolean isConnectedTo(TConnector c) {
         float dx = (c.x - this.x);
         float dy = (c.y - this.y);
         return ((dx * dx + dy * dy) <= (r * r));
      }
   }
   
   
   
//-------------------------------------------------------
// Inner class: TConnector
//-------------------------------------------------------
   class TConnector {
      public float x;
      public float y;
      public String name;
      public Statement statement;
      public boolean attached;
      
      public TConnector(Statement statement, String name,
                    float x, float y) {
         this.statement = statement;
         this.name = name;
         this.x = x;
         this.y = y;
         this.attached = false;
      }
   }
}
