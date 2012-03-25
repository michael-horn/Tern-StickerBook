/*
 * @(#) TangibleCompiler.java
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
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;


import android.util.Log;
import android.graphics.Bitmap;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import topcodes.*;


/**
 * Compiles a tangible program.
 *
 * @author Michael Horn
 */
public class TangibleCompiler {
   
   public static final String TAG = "TangibleCompiler";

   /** Scans image bitmap files for topcodes */
   protected Scanner scanner;
   
   /** Converts high-level text-based code to assembly code */
   protected TextCompiler tcompiler;
   
   /** Header include for generated text-based code */
   protected String header;
   
   
   public TangibleCompiler(Resources res, int statements, int driver) {
      this.scanner    = new Scanner();
      this.tcompiler  = new TextCompiler();
      this.header     = "";
      
      try {
         
         // Initialize tangible StatementFactory
         StatementFactory.loadStatements(res.getXml(statements));

         // Load driver code
         BufferedReader in = new BufferedReader(
            new InputStreamReader(res.openRawResource(driver)));
         String line;
         while ((line = in.readLine()) != null) {
            header += line + "\n";
         }
      } catch (java.io.IOException iox) {
         Log.e(TAG, "Error reading header file", iox);
      } catch (CompileException cx) {
         Log.e(TAG, "Error configuring statements", cx);
      }
   }
   

/**
 * Tangible compile function: generate a program from a bitmap image
 */
   public Program compile(Bitmap image) throws CompileException {
      
      Program program = new Program();

      
      //-----------------------------------------------------------
      // 1. Create a list of topcodes from the bitmap image
      //-----------------------------------------------------------
      List<TopCode> spots = scanner.scan(image);


      //-----------------------------------------------------------
      // 2. Convert topcodes to statements
      //-----------------------------------------------------------
      for (TopCode top : spots) {
         Statement s = StatementFactory.createStatement(top);
         if (s != null) {
            program.addStatement(s);
            Log.i(TAG, "Found: " + s);
         }
      }


      //-----------------------------------------------------------
      // 3. Connect chains of statements together
      //-----------------------------------------------------------
      for (Statement a : program.getStatements()) {
         for (Statement b : program.getStatements()) {
            if (a != b) {
               a.connect(b);
            }
         }
      }
      
      
      //-----------------------------------------------------------
      // 4. Convert the tangible program to a text-based program
      //-----------------------------------------------------------
      StringWriter sw = new StringWriter();
      PrintWriter out = new PrintWriter(sw);

      for (Statement s : program.getStatements()) {
         if (s.isStartStatement()) {
            s.compile(out, true);
         }
      }
      String tcode = header + "\n" + sw.toString();
      program.setTextCode(tcode);
      Log.i(TAG, tcode);

      
      //-----------------------------------------------------------
      // 5. Convert the text-based code to assembly code
      //-----------------------------------------------------------
      String pcode = tcompiler.compile(tcode);
      program.setAssemblyCode(pcode);
      Log.i(TAG, pcode);

      return program;
   }
}