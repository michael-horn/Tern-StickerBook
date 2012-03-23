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
   protected Robot robot;

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



   public Interpreter() {
      this.robot     = null;
      this.code      = new java.util.ArrayList<String>();
      this.processes = new java.util.ArrayList<Process>();
      this.vars      = new java.util.ArrayList<Integer>();
      this.labels    = new java.util.HashMap<String, Integer>();
      this.debuggers = new java.util.ArrayList<Debugger>();
   }
   
   
   public boolean isRunning() {
      for (Process p : processes) {
         if (p.isRunning()) return true;
      }
      return false;
   }
   
   
   public boolean isStopped() {
      return !isRunning();
   }
   

   public boolean isPaused() {
      for (Process p : processes) {
         if (p.isPaused()) return true;
      }
      return false;
   }


/**
 * Start the interpreter in a separate thread
 */
   public void start() {
      for (Process p : processes) {
         p.restart();
      }
      (new Thread(this)).start();
   }


/**
 * Stop the interpreter thread
 */
   public void stop() {
      for (Process p : processes) {
         if (p.isRunning()) {
            p.stop();
         }
      }
      if (robot != null) {
         robot.allStop();
      }
      sleep(100);
   }
   
   
   public void restart() {
      stop();
      start();
   }
   
   
   public void pause() {
      if (isRunning()) {
         for (Process p : processes) {
            p.pause();
         }
         if (robot != null) {
            robot.allStop();
         }
      }
   }


   public void resume() {
      if (isRunning()) {
         for (Process p : processes) {
            p.resume();
         }
      }
   }


/**
 * Stops the interpreter and clears all code, processes, and variables
 */
   public void clear() {
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


   public void addDebugger(Debugger d) {
      this.debuggers.add(d);
   }
   
   
   public Robot getRobot() {
      return this.robot;
   }
   
   
   public void setRobot(Robot robot) {
      this.robot = robot;
   }

   
/**
 * Called by a process or the interpreter to start/restart
 * all processes with the matching name
 */
   protected void startProcess(String pname) {
      for (Process p : processes) {
         if (p.getName().equals(pname)) {
            p.restart();
            notifyProcessStarted(p);
         }
      }
   }
   

/**
 * Called by a process or the interpreter to stop all
 * processes with the matching name
 */
   protected void stopProcess(String pname) {
      for (Process p : processes) {
         if (p.getName().equals(pname)) {
            p.stop();
            notifyProcessStopped(p);
         }
      }
   }
   
   
/**
 * Returns true if a process is currently running
 */
   public boolean isProcessRunning(String name) {
      for (Process p : processes) {
         if (p.getName().equals(name)) {
            if (p.isRunning()) return true;
         }
      }
      return false;
   }


/**
 * Invokes a built-in robot command
 */
   public int invokeFunction(Process p, String func, int [] args) {
      if (robot == null) return 0;
      try {
         Method m = robot.getClass().getMethod(func, args.getClass());
         Integer result = (Integer)m.invoke(robot, args);
         return (result != null)? result.intValue() : 0;
      } catch (Exception x) {
         error(p, "Undefined robot function: " + func);
         return 0;
      }
   }
   

   public void run() {
      
      long temp, clock = System.currentTimeMillis();
      boolean stopped = false;

      while (!stopped) {
         
         // service each process
         stopped = true;
         for (Process p : processes) {
            if (p.isRunning()) {
               if (p.run()) {
                  stopped = false;
               } else {
                  notifyProcessStopped(p);
               }
            }
         }
         if (stopped) break;
         
         // breathe
         try { Thread.sleep(20); }
         catch (InterruptedException ix) { ; }

         // update timers
         temp = clock;
         clock = System.currentTimeMillis();
         updateTimers((int)(clock - temp));
      }
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
         if (instr.length == 2) {
            this.processes.add(new Process(this, instr[1], count - 1));
         } else {
            this.processes.add(new Process(this, count - 1));
         }
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
   
   
   protected void sleep(int ms) {
      try { Thread.sleep(ms); }
      catch (InterruptedException ix) { ; }
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
