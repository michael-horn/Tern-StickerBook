/*
 * @(#) Process.java
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

import java.util.Random;


/**
 * @author Michael Horn
 */
public class Process {

   static final int MAX_STACK = 64;

   
   /** Instruction pointer */
   protected int ip;

   /** Frame pointer */
   protected int fp;

   /** Stack pointer */
   protected int sp;

   /** Points to the first line of code for the process */
   protected int start;

   /** Millisecond countdown timer */
   protected int timer;

   /** Name of the process */
   protected String name;

   /** Each process has its own stack */
   protected int [] stack;

   /** Reference back to the parent interpreter */
   protected Interpreter in;

   /** Used to generate random numbers */
   protected Random rand;

   
   
   public Process(Interpreter in, String name, int start) {
      this.ip       = start;
      this.fp       = 0;
      this.sp       = 0;
      this.in       = in;
      this.name     = name;
      this.start    = start;
      this.rand     = new java.util.Random();
      this.stack    = new int[MAX_STACK];
   }
   
   
/**
 * Creates an unnamed (anonymous) process
 */
   public Process(Interpreter in, int start) {
      this(in, "__anonymous__", start);
   }


   public String getName() {
      return this.name;
   }


   public boolean isRunning() {
      return this.ip >= 0;
   }


   public void restart() {
      this.ip    = start;
      this.fp    = 0;
      this.sp    = 0;
      this.timer = 0;
   }
   
   
   public void stop() {
      this.ip = -1;
   }


   public void timerEvent(int elapsed) {
      if (timer >= elapsed) {
         timer -= elapsed;
      } else if (timer > 0) {
         timer = 0;
      }
   }


   public boolean run() {
      String line;
      String [] instr;

      while (ip >= 0) {

         line = in.getLine(ip++);
         if (line == null) {
            ip = -1;
            return false;
         }
         
         instr = line.split(" ");

         //-----------------------------------------------------
         // Blank lines, labels, comments
         //-----------------------------------------------------
         if (instr.length == 0 || "".equals(instr[0])) {
            continue;
         } else if (instr[0].startsWith(":")) {
            continue;
         } else if (instr[0].startsWith(";")) {
            continue;
         }
         String opcode = instr[0];
         
         //-----------------------------------------------------
         // Stop instructions (2 forms)
         //-----------------------------------------------------
         if ("stop".equals(opcode)) {
            if (instr.length == 1) {
               STOP();   // stop this process
               return false;
            } else if (instr.length > 1) {
               STOP(instr[1]);   // stop a named process
            }
         }
         
         else if ("print".equals(opcode)) {
            if (instr.length == 1) {
               PRINT();
            } else {
               PRINT(instr[1]);
            }
         }
         

         //-----------------------------------------------------
         // Ugly instruction mapping
         //-----------------------------------------------------
         else if ("nop".equals(opcode))          { ; }
         else if ("function".equals(opcode))     { ; }
         else if ("process".equals(opcode))      { in.notifyProcessStarted(this); }
         else if ("push".equals(opcode))         { PUSH(instr[1]); }
         else if ("trace".equals(opcode))        { TRACE(instr[1]); }
         else if ("remote".equals(opcode))       { REMOTE(instr[1]); }
         else if ("start".equals(opcode))        { START(instr[1]); }
         else if ("load-address".equals(opcode)) { LOAD_ADDRESS(instr[1]); }
         else if ("exit".equals(opcode))         { EXIT(); return false; }
         else if ("yield".equals(opcode))        { YIELD(); return true; }
         else if ("frame".equals(opcode))        { FRAME(); }
         else if ("goto".equals(opcode))         { GOTO(); }
         else if ("call".equals(opcode))         { CALL(); }
         else if ("return".equals(opcode))       { RETURN(); }
         else if ("pop".equals(opcode))          { POP(); }
         else if ("dup".equals(opcode))          { DUP(); }
         else if ("load-global".equals(opcode))  { LOAD_GLOBAL(); }
         else if ("store-global".equals(opcode)) { STORE_GLOBAL(); }
         else if ("load-frame".equals(opcode))   { LOAD_FRAME(); }
         else if ("store-frame".equals(opcode))  { STORE_FRAME(); }
         else if ("and".equals(opcode))          { AND(); }
         else if ("or".equals(opcode))           { OR(); }
         else if ("not".equals(opcode))          { NOT(); }
         else if ("=".equals(opcode))            { EQ(); }
         else if (">".equals(opcode))            { GT(); }
         else if ("<".equals(opcode))            { LT(); }
         else if (">=".equals(opcode))           { GTE(); }
         else if ("<=".equals(opcode))           { LTE(); }
         else if ("!=".equals(opcode))           { NE(); }
         else if ("+".equals(opcode))            { ADD(); }
         else if ("-".equals(opcode))            { SUB(); }
         else if ("*".equals(opcode))            { MULT(); }
         else if ("/".equals(opcode))            { DIV(); }
         else if ("rand".equals(opcode))         { RAND(); }
         else if ("if-true".equals(opcode))      { IF_TRUE(); }
         else if ("if-false".equals(opcode))     { IF_FALSE(); }
         else if ("if-timer".equals(opcode))     { IF_TIMER(); }
         else if ("timer".equals(opcode))        { TIMER(); }
         else {
            ERROR("Unknown opcode " + opcode);
            return false;
         }
      }
      return false;
   }

   
   public boolean equals(Object other) {
      if (other instanceof Process) {
         return this.name.equals(((Process)other).getName());
      } else {
         return false;
      }
   }


   public void stackTrace() {
      int tfp = fp;
      String line;
      System.out.println("Process stack: " + getName());
      System.out.println();
      for (int i = sp - 1; i >= 0; i--) {
         line = String.format("  %2d   | %5d   |", i, stack[i]);
         System.out.print(line);
         if (i == tfp) {
            System.out.println(" <FP>");
            System.out.println("       |---------|");
            tfp = stack[i];
         } else {
            System.out.println();
         }
      }
      System.out.println("       |---------|");
   }


   protected int pop() {
      return stack[--sp];
   }


   protected void push(int v) {
      stack[sp++] = v;
   }


/**
 * Replace the stack value at the given frame offset (fp + offset)
 */
   protected void replace(int value, int offset) {
      int i = offset + fp;
      stack[i] = value;
   }


   protected void index(int offset) {
      int i = offset + fp;
      push(stack[i]);
   }
         

   protected void FRAME() {
      int acount = pop();  // number of formal arguments
      int addr = pop();    // return address 
      push(0);             // room for the return address
      push(0);             // room for the frame pointer
      
      // move all of the parameters up two slots to make room
      // for the fp and return address at the bottom of the frame
      for (int i=1; i<=acount; i++) {
         stack[sp - i] = stack[sp - i - 2];
      }
      
      // move the frame pointer and return addr to the bottom of the frame
      stack[sp - acount - 2] = addr;
      stack[sp - acount - 1] = fp;
      fp = sp - acount;
   }


   protected void YIELD() { }


   protected void PRINT() {
      int value = pop();
      in.print(this, String.valueOf(value));
   }
   
   
   protected void PRINT(String s) {
      in.print(this, s);
   }
   
   
   protected void REMOTE(String func) {
      int acount = pop();  // argument count
      int result = 0;
      
      if (acount <= 64) {
         int [] args = new int[acount];
         for (int i=acount-1; i >= 0; i--) {
            args[i] = pop();
         }
         result = in.invokeFunction(this, func, args);
      }
      
      push(result);
   }


   protected void STOP() {
      this.ip = -1;
   }


   protected void STOP(String process) {
      in.stopProcess(process);
   }
   
   
   protected void START(String process) {
      in.startProcess(process);
   }


   protected void EXIT() {
      this.ip = -1;
      in.stop();
   }


   protected void GOTO() {
      ip = pop();
   }


   protected void CALL() {
      int return_addr = ip;
      ip = pop();
      push(return_addr);
   }


   protected void RETURN() {
      int value = pop();  // save return value
      sp = fp;
      fp = pop();
      ip = pop();
      push(value);
   }


   protected int POP() {
      return pop();
   }


   protected void PUSH(String arg) {
      push(parseInt(arg));
   }


   protected void LOAD_GLOBAL() {
      int addr = pop();
      push(in.getVar(addr));
   }


   protected void STORE_GLOBAL() {
      int addr = pop();
      int value = pop();
      in.setVar(addr, value);
   }      


   protected void AND() {
      boolean b2 = (pop() != 0);
      boolean b1 = (pop() != 0);
      push((b1 && b2) ? 1 : 0);
   }
   

   protected void OR() {
      boolean b2 = (pop() != 0);
      boolean b1 = (pop() != 0);
      push((b1 || b2) ? 1 : 0);
   }
   

   protected void NOT() {
      boolean b = (pop() != 0);
      push(b ? 0 : 1);
   }


   protected void IF_TRUE() {
      int addr = pop();
      int c = pop();
      if (c != 0) ip = addr;
   }


   protected void IF_FALSE() {
      int addr = pop();
      int c = pop();
      if (c == 0) ip = addr;
   }


   protected void IF_TIMER() {
      int addr = pop();
      if (timer == 0) ip = addr;
   }


   protected void TIMER() {
      timer = pop();
   }


   protected void RAND() {
      int b = pop();
      int a = pop();
      int range = b - a + 1;
      push(a + rand.nextInt(range));
   }

   
   protected void LOAD_ADDRESS(String arg) {
      int addr = in.getLineNumber(arg);
      push(addr);
      if (addr < 0) {
         ERROR("Invalid Address: " + arg);
      }
   }


   protected void STORE_FRAME() {
      int index = pop();
      int value = pop();
      replace(value, index);
   }


   protected void LOAD_FRAME() {
      index(pop());
   }

   
   protected void EQ() {
      int b = pop();
      int a = pop();
      push((a == b) ? 1 : 0);
   }


   protected void GT() {
      int b = pop();
      int a = pop();
      push((a > b) ? 1 : 0);
   }


   protected void LT() {
      int b = pop();
      int a = pop();
      push((a < b) ? 1 : 0);
   }

   
   protected void GTE() {
      int b = pop();
      int a = pop();
      push((a >= b) ? 1 : 0);
   }


   protected void LTE() {
      int b = pop();
      int a = pop();
      push((a <= b) ? 1 : 0);
   }


   protected void NE() {
      int b = pop();
      int a = pop();
      push((a != b) ? 1 : 0);
   }
   
   
   protected void ADD() {
      int b = pop();
      int a = pop();
      push(a + b);
   }
   

   protected void SUB() {
      int b = pop();
      int a = pop();
      push(a - b);
   }
   

   protected void MULT() {
      int b = pop();
      int a = pop();
      push(a * b);
   }
   

   protected void DIV() {
      int b = pop();
      int a = pop();
      if (b == 0) {
         push(Short.MAX_VALUE);
         ERROR("Divide by zero");
      } else {
         push(a / b);
      }
   }
   

   protected void TRACE(String message) {
      this.in.trace(this, message);
   }
   
   
   protected void ERROR(String err) {
      this.in.error(this, err);
   }


/**
 * Pushes a copy the top stack value onto the stack
 */
   protected void DUP() {
      int a = pop();
      push(a);
      push(a);
   }
   

   protected static int parseInt(String s) {
      try {
         return Integer.parseInt(s);
      } catch (NumberFormatException nfx) {
         return -1;
      }
   }
}
