/*
 * @(#) StatementFactory.java
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


public class StatementFactory {


   protected static List<Statement> stypes = new java.util.ArrayList<Statement>();
   
   protected static int COMPILE_ID = 0;
	
	
/**
 * Registers a new statement type.  This must be called for each
 * statement type when an application is loaded.
 */
   public static void registerStatementType(Statement s) {
      stypes.add(s);
   }
	
	
/**
 * Called by the tangible compiler to generate new statements
 * from topcodes found in an image.
 */
   public static Statement createStatement(TopCode top) {
      for (Statement s : stypes) {
         if (s.getCode() == top.getCode()) {
            Statement clone = s.newInstance(top);
            clone.setCompileID(COMPILE_ID++);
            return clone;
         }
      }
      return null;
   }
}
