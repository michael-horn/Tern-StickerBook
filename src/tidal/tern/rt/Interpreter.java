/*
 * @(#) Interpreter.java
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

import java.util.Map;
import java.util.List;
import java.lang.reflect.Method;
import java.io.Reader;
import java.io.IOException;
import java.io.StringReader;
import java.io.BufferedReader;


public class Interpreter implements Runnable {
   
   /** Robot controlled by this interpreter */
   protected Object robot;

   /** Assembly instructions */
   protected List<String> code;

   /** All code is executed in semi-concurrent processes */
   protected List<Process> processes;

   /** Global variables shared between processes */
   protected List<Integer> vars;

   /** Global labels / addresses for things like functions */
   protected Map<String, Integer> labels;

   /** List of debuggers to send trace events */
   protected List<Debugger> debuggers;

   /** Flag to stop the interpreter thread */
   protected boolean stop;

   /** Whether or not processes are running */
   protected boolean running;


   public Interpreter() {
      this.robot     = null;
      this.code      = new java.util.ArrayList<String>();
      this.processes = new java.util.ArrayList<Process>();
      this.vars      = new java.util.ArrayList<Integer>();
      this.labels    = new java.util.HashMap<String, Integer>();
      this.debuggers = new java.util.ArrayList<Debugger>();
      this.stop      = false;
      this.running   = false;
   }


/**
 * Start the interpreter in a separate thread
 */
   public synchronized void start() {
      if (!running) {
         this.running = true;
         (new Thread(this)).start();
      }
   }


/**
 * Stop the interpreter thread
 */
   public synchronized void stop() {
      if (!running) return;
      this.stop = true;
      this.running = false;
   }


/**
 * Stops the interpreter and clears all code, processes, and variables
 */
   public synchronized void clear() {
      stop();
      this.code.clear();
      this.processes.clear();
      this.vars.clear();
      this.labels.clear();
   }

   
   public void load(Reader pcode) throws IOException {
      BufferedReader in = new BufferedReader(pcode);
      String line;
      while ((line = in.readLine()) != null) {
         loadLine(line);
      }
   }
   
   
   public void load(String pcode) throws IOException {
      load(new StringReader(pcode));
   }


/**
 * Returns true if the interpreter is running
 */
   public boolean isRunning() {
      return this.running;
   }


   public void addDebugger(Debugger d) {
      this.debuggers.add(d);
   }
   
   
   public Object getRobot() {
      return this.robot;
   }
   
   
   public void setRobot(Object robot) {
      this.robot = robot;
   }

   
/**
 * Called by a process or the interpreter to start/restart a process
 */
   protected void startProcess(String pname) {
      Process p = getProcess(pname);
      if (p != null) {
         p.restart();
         notifyProcessStarted(p);
      }
   }
   

/**
 * Called by a process or the interpreter to stop a process
 */
   protected void stopProcess(String pname) {
      Process p = getProcess(pname);
      if (p != null) {
         p.stop();
         notifyProcessStopped(p);
      }
   }
   
   
/**
 * Gets a Process by name or null if there is no match
 */
   public Process getProcess(String name) {
      for (Process p : processes) {
         if (p.getName().equals(name)) {
            return p;
         }
      }
      return null;
   }


/**
 * Returns true if a process is currently running
 */
   public boolean isProcessRunning(String name) {
      Process p = getProcess(name);
      return (p != null && p.isRunning());
   }


/**
 * Invokes a built-in robot command
 */
   public void invokeFunction(Process p, String func, int [] args) {
      if (robot == null) return;
      try {
         Method m = robot.getClass().getMethod(func, args.getClass());
         m.invoke(robot, args);
      } catch (Exception x) {
         error(p, "Undefined robot function: " + func);
      }
   }


   public void run() {
      
      long temp, clock = System.currentTimeMillis();

      while (!stop) {

         // service each process
         for (Process p : processes) {
            if (p.isRunning()) {
               if (!p.run()) {
                  notifyProcessStopped(p);
               }
            }
         }
         
         if (stop) break;
         
         // breathe
         try { Thread.sleep(20); }
         catch (InterruptedException ix) { ; }

         // update timers
         temp = clock;
         clock = System.currentTimeMillis();
         updateTimers((int)(clock - temp));
      }
      this.stop = false;
      this.running = false;
   }
   
   
   protected String getLine(int index) {
      if (index >= 0 && index < code.size()) {
         return code.get(index);
      } else {
         return null;
      }
   }


   protected int getLineNumber(String label) {
      if (labels.containsKey(label)) {
         return labels.get(label);
      } else {
         return -1;
      }
   }


   protected boolean hasLabel(String label) {
      return (labels.containsKey(label));
   }


   protected int getVar(int addr) {
      if (addr >= 0 && addr < vars.size()) {
         return vars.get(addr);
      } else {
         return 0;
      }
   }

   
   protected void setVar(int addr, int value) {
      if (addr >= 0 && addr < vars.size()) {
         vars.set(addr, value);
      } else if (addr == vars.size()) {
         vars.add(value);
      }
   }
   
   
   private void loadLine(String line) {
      if (isRunning()) return;
      if (line == null || line.length() == 0) return;
      
      line = line.trim();
      String [] instr = line.split(" ");
      
      if (instr.length == 0) {
         return;
      } else if (instr[0].startsWith(";")) {  // skip comments
         return;
      } else {
         code.add(line);
      }
      int count = code.size();
      
      if (instr[0].startsWith(":")) {
         this.labels.put(instr[0], count - 1);
      }
      else if ("process".equals(instr[0])) {
         this.processes.add(new Process(this, instr[1], count - 1));
      }
      else if ("function".equals(instr[0])) {
         this.labels.put(instr[1], count);
      }
   }


   private void updateTimers(int elapsed) {
      for (Process p : processes) {
         p.timerEvent(elapsed);
      }
   }


   protected void trace(Process p, String message) {
      for (Debugger d : debuggers) { 
         d.trace(p, message);
      }
   }
   
   
   protected void print(Process p, String message) {
      for (Debugger d : debuggers) { 
         d.print(p, message);
      }
   }
   
   
   protected void error(Process p, String err) {
      for (Debugger d : debuggers) {
         d.error(p, err);
      }
   }


   protected void notifyProcessStopped(Process p) {
      for (Debugger d : debuggers) { 
         d.processStopped(p);
      }
   }


   protected void notifyProcessStarted(Process p) {
      for (Debugger d : debuggers) { 
         d.processStarted(p);
      }
   }


   public String toString() {
      String s = "";
      for (String line : code) {
         s += line + "\n";
      }
      return s;
   }
}
