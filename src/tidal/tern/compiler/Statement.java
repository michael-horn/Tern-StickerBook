/*
 * @(#) Statement.java
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
import java.io.PrintWriter;
import topcodes.TopCode;


/**
 * A base class for all tangible language statements.  A statement is
 * any element that can be connected in a program's flow-of-control.
 * A statement must have at least one socket or one connector (most
 * have both a socket and a connector).  Statements have no implicit
 * data type and carry no return value.
 *
 * @author Michael Horn
 * @version $Revision: 1.8 $, $Date: 2008/03/18 15:08:39 $
 */
public class Statement {
   
   
   private static int COMPILE_ID = 0;


   /** Name of the statement */
   protected String name;

   /** TopCode for this statement */
   protected TopCode top;
   
   /** Code that this statement generates */
   protected String text;
   
   /** Is this statement a start statement */
   protected boolean start;

   /** Statement's unique compile-time ID number */
   protected int c_id;
   
   /** List of connectors (ingoing, outgoing, and params) for this statement */
   protected List<Connector> connectors;
   
   
   
   public Statement() {
      this.name = "";
      this.top  = null;
      this.text = "";
      this.start = false;
      this.c_id = COMPILE_ID++;
      this.connectors = new java.util.ArrayList();
   }

   
   public Statement(TopCode top) {
      this();
      this.top = top;
   }


   public void addConnector(Connector con) {
      this.connectors.add(con);
   }
   
   
   public List<Connector> getConnectors() {
      return this.connectors;
   }

   
/**
 * Translates a tangible statement into a text-based representation
 */
   public void compile(PrintWriter out) throws CompileException {
      out.println(this.text);
      compileNext(out);
   }
   
   
   protected void compileNext(PrintWriter out) throws CompileException {
      for (Connector c : connectors) {
         if (c.isOutgoing() && c.hasConnection()) {
            c.getConnection().compile(out);
         }
      }
   }


/**
 * Factory method. Creates a new statement of the correct type.
 */
   public Statement newInstance(TopCode top) {
      Statement s = new Statement(top);
      s.name = this.name;
      s.text = this.text;
      s.start = this.start;
      for (Connector c : connectors) {
         s.addConnector(c.clone(s));
      }
      return s;
   }


   public String toString() {
      return getName();
   }


   public String getName() {
      return name;
   }
   
   
   public void setName(String name) {
      this.name = name;
   }


   public TopCode getTopCode() {
      return top;
   }
   
   
   public void setTopCode(TopCode top) {
      this.top = top;
   }
   
   
   public int getCode() {
      return (top == null)? 0 : top.getCode();
   }
   
   
   public boolean hasTopCode() {
      return this.top != null;
   }
   
   
   public String getCompileText() {
      return this.text;
   }

   
   public void setCompileText(String text) {
      this.text = text;
   }
   
   
   public boolean isStartStatement() {
      return this.start;
   }
   
   
   public void setStartStatement(boolean start) {
      this.start = start;
   }


   public int getCompileID() {
      return this.c_id;
   }
   
   
   public void connect(Statement other) {
      for (Connector plug : connectors) {
         if (plug.isOutgoing()) {
            for (Connector socket : other.connectors) {
               if (socket.isIncoming()) {
                  if (socket.overlaps(plug)) {
                     plug.setConnection(other);
                     socket.setConnection(other);
                  }
               }
            }
         }
      }
   }
}   

