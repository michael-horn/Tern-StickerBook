/*
 * @(#) Song.java
 * 
 * Tern Tangible Programming System
 * Copyright (C) 2009 Michael S. Horn 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tidal.tern.language;

import java.io.PrintWriter;
import java.io.StringWriter;
import tidal.tern.compiler.Statement;
import tidal.tern.compiler.CompileException;
import topcodes.TopCode;


public class Begin extends Statement {


   public Begin() {
      super();
   }
   
   
   public Begin(TopCode top) {
      super(top);
   }
   
   
   public void compile(PrintWriter out, boolean debug) throws CompileException {
      Statement.NEST = 0;

      //-------------------------------------------
      // Compile a subroutine
      //-------------------------------------------
      if (hasConnection("param")) {
         StringWriter sw = new StringWriter();
         PrintWriter pout = new PrintWriter(sw);
         pout.println("def do" + getConnection("param").getName() + "():");
         pout.println("{");
         compileNext(pout, false);
         pout.println("}");
      }
      
      //-------------------------------------------
      // Compile a process
      //-------------------------------------------
      else {
         out.println("process main:");
         out.println("{");
         if (debug) out.println("trace " + getCompileID());
         compileNext(out, true);
         out.println("}");
      }
   }
}
