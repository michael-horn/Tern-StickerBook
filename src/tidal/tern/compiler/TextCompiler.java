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


import tidal.tern.compiler.parser.*;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.IOException;
import java.util.Map;
import java.util.LinkedList;

import net.percederberg.grammatica.parser.*;


/**
 * Compiles a text-based Tern program.
 *
 * @author Michael Horn
 * @version $Revision: 1.8 $, $Date: 2008/11/08 16:56:57 $
 */
public class TextCompiler extends TernAnalyzer {

   private StringWriter out = null;
   
   private Scope global;

   private Scope local;
   
   private int label_gen;
   
   private LinkedList<Integer> labels;
   

   public TextCompiler() {
      this.out = new StringWriter();
      this.global = new Scope();
      this.local = null;
      this.label_gen = 0;
      this.labels = new LinkedList<Integer>();
   }
   
   
   public Reader compile(Reader in) throws CompileException {
      try {
         this.out = new StringWriter();
         this.label_gen = 0;
         this.labels.clear();
         this.global.clear();
         this.local = null;
         
         TernParser parser = new TernParser(in, this);
         parser.parse();
         return new StringReader(this.out.toString());
      } catch (ParserLogException plx) {
         throw new CompileException(plx);
      } catch (ParserCreationException pcx) {
         throw new CompileException(pcx);
      }
   }
   
   
   public String compile(String code) throws CompileException {
      compile(new StringReader(code));
      return this.out.toString();
   }
   
   
//--------------------------------------------------------------------------
// Processes
//--------------------------------------------------------------------------
   protected Node exitProcessName(Production node) throws ParseException {
      Token name = (Token)node.getChildAt(0);
      out.write("process " + name.getImage() + "\n");
      return node;
   }
   
   protected Node exitProcessDef(Production node) throws ParseException {
      out.write("stop\n");
      return node;
   }
   

//--------------------------------------------------------------------------
// Expressions
//--------------------------------------------------------------------------

   protected Node exitNumber(Token node) throws ParseException {
      out.write("push " + node.getImage() + "\n");
      return node;
   }
   
   protected Node exitTrue(Token node) throws ParseException {
      out.write("push 1\n");
      return node;
   }

   protected Node exitFalse(Token node) throws ParseException {
      out.write("push 0\n");
      return node;
   }
   
   protected Node exitVariableName(Production node) throws ParseException {
      Token t = (Token)node.getChildAt(0);
      String name = t.getImage();
      if (local != null && local.contains(name)) {
         out.write("push " + local.get(name) + "\n");
         out.write("load-frame\n");
      }
      else if (global.contains(name)) {
         out.write("push " + global.get(name) + "\n");
         out.write("load-global\n");
      }
      else {
         throw new ParseException(
            ParseException.ANALYSIS_ERROR,
            "Unbound variable: " + name, 0, 0);
      }
      return node;
   }
   
   protected Node exitBinaryFunction(Production node) throws ParseException {
      Node child = node.getChildAt(1); // Binary Operator
      Token token = (Token)child.getChildAt(0);
      out.write(token.getImage() + "\n");
      return node;
   }
   
   protected Node exitUnaryFunction(Production node) throws ParseException {
      out.write("not\n");
      return node;
   }
   
   
//--------------------------------------------------------------------------
// Built-in Commands
//--------------------------------------------------------------------------
   
   protected Node exitWaitCommand(Production node) throws ParseException {
      int l = label_gen++;
      out.write("timer\n");
      out.write(":sleep" + l + "\n");
      out.write("yield\n");
      out.write("load-address :done" + l + "\n");
      out.write("if-timer\n");
      out.write("load-address :sleep" + l + "\n");
      out.write("goto\n");
      out.write(":done" + l + "\n");
      return node;
   }
   
   protected Node exitPrintCommand(Production node) throws ParseException {
      out.write("print\n");
      return node;
   }
   
   protected Node exitStartCommand(Production node) throws ParseException {
      Token process = (Token)node.getChildAt(1);
      out.write("start " + process.getImage() + "\n");
      return node;
   }
   
   protected Node exitStopCommand(Production node) throws ParseException {
      if (node.getChildCount() == 1) {
         out.write("stop\n");
      } else {
         Token process = (Token)node.getChildAt(1);
         out.write("stop " + process.getImage() + "\n");
      }
      return node;
   }
   
   
//--------------------------------------------------------------------------
// While loops
//--------------------------------------------------------------------------

   protected void enterWhileLoop(Production node) {
      this.labels.add(label_gen++);
   }
   
   
   protected Node exitWhileLoop(Production node) throws ParseException {
      int l = this.labels.getLast();
      out.write("load-address :while" + l + "\n");
      out.write("goto\n");
      out.write(":done" + l + "\n");
      this.labels.removeLast();
      return node;
   }
   
   
   protected void enterWhileCondition(Production node) {
      int l = this.labels.getLast();
      out.write(":while" + l + "\n");
   }
   
   protected Node exitWhileCondition(Production node) throws ParseException {
      int l = this.labels.getLast();
      out.write("load-address :done" + l + "\n");
      out.write("if-false\n");
      return node;
   }

   
//--------------------------------------------------------------------------
// Procedure calls
//--------------------------------------------------------------------------

   protected Node exitProcedureCall(Production node) throws ParseException {
      out.write("pop\n");
      return node;
   }

   protected Node exitFunctionCall(Production node) throws ParseException {
      Token t = (Token)node.getChildAt(0);
      out.write("trace " + t.getImage() + "\n");
      out.write("load-address " + t.getImage() + "\n");
      out.write("call\n");
      return node;
   }
   
   protected Node exitProcedureDeclaration(Production node) throws ParseException {
      Node child = node.getChildAt(1);
      Token t = (Token)child.getChildAt(0);
      out.write("function " + t.getImage() + "\n");
      out.write("push " + local.size() + "\n");  // argument count
      out.write("frame\n");
      return node;
   }
   
   protected void enterImportDef(Production node) {
      this.local = new Scope();
   }
   
   protected Node exitImportDef(Production node) throws ParseException {
      Node child = node.getChildAt(1);
      Token t = (Token)child.getChildAt(0);
      out.write("function " + t.getImage() + "\n");
      out.write("push " + local.size() + "\n");  // argument count
      out.write("frame\n");
      out.write("push " + local.size() + "\n");
      out.write("remote " + t.getImage() + "\n");
      out.write("return\n");
      this.local = null;
      return node;
   }
   
   protected void enterProcedureDef(Production node) {
      this.local = new Scope();
   }
   
   protected Node exitProcedureDef(Production node) throws ParseException {
      out.write("push 0\n");
      out.write("return\n");
      
      this.local = null;
      return node;
   }
   
   protected Node exitFormalParam(Production node) throws ParseException {
      Token name = (Token)node.getChildAt(0);
      
      // push the local variable and stack offset onto the local scope
      this.local.add(name.getImage());
      return node;
   }

   
//--------------------------------------------------------------------------
// Variable assignment
//--------------------------------------------------------------------------

   protected Node exitAssignment(Production node) throws ParseException {
      Token t = (Token)node.getChildAt(0);
      String name = t.getImage();
      
      if (local != null) {
         if (local.contains(name)) {
            out.write("push " + local.get(name) + "\n");
            out.write("store-frame\n");
         } else {
            local.add(name);
         }
      }
      else {
         if (!global.contains(name)) {
            global.add(name);
         }
         out.write("push " + global.get(name) + "\n");
         out.write("store-global\n");
      }
      return node;
   }
   
   
   class Scope {
      
      private Map<String, Integer> vars;
      
      public Scope() {
         this.vars = new java.util.HashMap<String, Integer>();
      }
      
      public void clear() {
         this.vars.clear();
      }
      
      public boolean contains(String name) {
         return this.vars.containsKey(name);
      }
      
      public int get(String name) {
         return this.vars.get(name);
      }
      
      public void add(String name) {
         int addr = this.vars.size();
         this.vars.put(name, addr);
      }
      
      public void remove(String name) {
         this.vars.remove(name);
      }
      
      public int size() {
         return this.vars.size();
      }
   }
}
