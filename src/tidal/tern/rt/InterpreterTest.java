package tidal.tern.rt;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.Reader;
import java.io.FileReader;
import java.io.BufferedReader;
import tidal.tern.compiler.TextCompiler;


public class InterpreterTest extends TestCase {

   protected Interpreter in;
   
   protected void setUp() {
      TestRobot r = new TestRobot();
      this.in = new Interpreter();
      this.in.setRobot(r);
      this.in.addDebugger(r);
   }
   
   protected void runTest(String filename) throws Exception {
      
      TextCompiler compiler = new TextCompiler();
      
      Reader pcode = compiler.compile(new FileReader(filename));
      
      in.clear();
      in.load(pcode);
      in.start();
      
      while (in.isRunning()) {
         try {
            Thread.sleep(50);
         } catch (Exception x) { ; }
      }
   }


   @Test public void testNumericExpressions() throws Exception {
      runTest("tests/interp.tern");
   }
   
   
   
   public class TestRobot implements Debugger {
      
      public void processStarted(Process p) {
         System.out.println("starting " + p.getName());   
      }
      
      public void processStopped(Process p) {
         in.stop();
         System.out.println("stopping " + p.getName());
      }
      
      public void left(int [] args) {
         System.out.println("LEFT " + args[0]);
      }
      
      public void right(int [] args) {
         System.out.println("RIGHT " + args[0]);
      }
      
      public void trace(Process p, String message) {  }
      
      public void print(Process p, String message) {
         System.out.println("-- " + message);
      }
      
      public void error(Process p, String message) {  }
   }
}

