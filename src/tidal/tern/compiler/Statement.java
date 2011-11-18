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

import topcodes.*;
import java.io.PrintWriter;


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
public abstract class Statement {

   /** Next statement in the flow-of-control chain */
   protected Statement next;

   /** TopCode for this statement */
   protected TopCode top;

   /** Statement's compile-time ID number */
   protected int c_id;
   
   
   public Statement(TopCode top) {
      this.next  = null;
      this.top   = top;
      this.c_id  = -1;
   }

   
/**
 * Name of the statement
 */
   public abstract String getName();


/**
 * The unique identifier for this statement type.  This number
 * is encoded in a statement's topcode.
 */
   public abstract int getCode();

   
/**
 * Translates a tangible statement into a text-based representation
 */
   public abstract void compile(PrintWriter out) throws CompileException;


/**
 * Factory method. Creates a new statement of the correct type.
 */
   public abstract Statement newInstance(TopCode top);


   public String toString() {
      return getName();
   }


   public TopCode getTopCode() {
      return top;
   }


   public void setCompileID(int c_id) {
      this.c_id = c_id;
   }


   public int getCompileID() {
      return this.c_id;
   }

   
/**
 * Used by compiler to connect the next statement in the flow chain.
 */
   protected void connect(Statement next) {
	   this.next = next;
   }


	protected void connect(Statement next, String name) {
		this.next = next;
	}


/**
 * Called by the compiler. Registers the top-relative location of
 * this statement's socket and connector.  Statements with
 * non-standard shapes should override this function.
 */
   public void registerConnections(ConnectionMap map) {
      map.addSocket(this, (float)(-1.6), (float)(0.25));
      map.addConnector(this, "next", (float)(1.8), (float)(0.25));
   }
}   

