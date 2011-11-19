/*
 * @(#) Program.java
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

import java.util.Map;
import java.util.List;
import android.graphics.RectF;



/**
 * A program is simply a list of instructions created by the compiler.
 * Programs may be translated into some other text-based language or
 * interpreted by a virtual machine.
 *
 * @author Michael Horn
 * @version $Revision: 1.7 $, $Date: 2008/11/05 22:44:29 $
 */
public class Program {

	/** Rectangle that frames a program in the image */
	protected float xmin, ymin, xmax, ymax;
	
	/** List of statements recognized in an image */
	protected List<Statement> statements;
	
   /** High level text-based representation of the tangible program */
   protected String tcode;
   
   /** Assembly code generated from the text-based code */
   protected String pcode;
   
	
	public Program() {
		this.xmin       = 1600;
		this.ymin       = 1200;
		this.xmax       = 0;
		this.ymax       = 0;
		this.statements = new java.util.ArrayList<Statement>();
      this.tcode      = null;
      this.pcode      = null;
	}
   
   
   public void addStatement(Statement s) {
      this.statements.add(s);
      expandBoundingBox(s.getTopCode().getCenterX(),
                        s.getTopCode().getCenterY());
   }


   public List<Statement> getStatements() {
      return this.statements;
   }


/**
 * Returns a statement by compile-time ID number
 */
   public Statement getStatement(int c_id) {
      for (Statement s : statements) {
         if (s.getCompileID() == c_id) {
            return s;
         }
      }
      return null;
   }


	public boolean isEmpty() {
		return (statements.isEmpty());
	}


   public boolean hasStartStatement() {
      for (Statement s : statements) {
         if (s instanceof StartStatement) {
            return true;
         }
      }
      return false;
   }
   
   
   public String getTextCode() {
      return this.tcode;
   }
   
   
   public void setTextCode(String tcode) {
      this.tcode = tcode;
   }
   
   
   public String getAssemblyCode() {
      return this.pcode;
   }
   
   
   public void setAssemblyCode(String pcode) {
      this.pcode = pcode;
   }
   
   
/**
 * Called by individual statements.  Expands the bounding box that
 * frames a program in a bitmap image.
 */
   public void expandBoundingBox(float cx, float cy) {
      xmax = (cx > xmax)? cx : xmax;
      ymax = (cy > ymax)? cy : ymax;
      xmin = (cx < xmin)? cx : xmin;
      ymin = (cy < ymin)? cy : ymin;
   }


/**
 * Returns a bounding box around a program in a bitmap image.
 */
   public RectF getBounds() {
      if (xmin > xmax || ymin > ymax) {
         return new RectF(0, 0, 1600, 1200);
      } else {
         return new RectF(
            (float)(xmin - 100),
            (float)(ymin - 100),
            (float)(xmax - xmin + 200),
            (float)(ymax - ymin + 200));
      }
   }
}
