package tidal.tern.compiler;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.FileReader;
import net.percederberg.grammatica.parser.ParserLogException;



public class TextCompilerTest extends TestCase {
   
   protected TextCompiler compiler = new TextCompiler();
   
   
   protected void runTests(String filename) throws Exception {
      BufferedReader fin = new BufferedReader(new FileReader(filename));
      String in, out;
      String line;
      while (true) {
         
         in = "";
         out = "";
         line = fin.readLine();
         if (line == null) break;
         
         // Read in Tern test code
         while (line != null && !line.startsWith("-----")) {
            in += line + "\n";
            line = fin.readLine();
         }
         
         if (line != null) line = fin.readLine();
            
            
         // Read in what the compiler should generate
         while (line != null && !line.startsWith("=====")) {
            out += line + "\n";
            line = fin.readLine();
         }
         
         // Send the input code to the parser
         try {
            String pcode = compiler.compile(in);
            //System.out.println(pcode);
            if (!pcode.equals(out)) {
               System.err.println("TEST: " + in);
               System.err.println(out);
               System.err.println("------------------------");
               System.err.println(pcode);
               System.err.println(out.length());
               System.err.println(pcode.length());
               System.err.println("========================");
            }
            assertTrue("Parser Test", out.equals(pcode));  
         } catch (CompileException x) {
            assertTrue("Parser Exception", out.startsWith("$"));
         }
      }
   }


   public void testNumericExpressions() throws Exception {
      runTests("tests/test1.tern");
   }
   
   public void testBooleanExpressions() throws Exception {
      runTests("tests/test2.tern");
   }
   
   public void testBuiltInExpressions() throws Exception {
      runTests("tests/test3.tern");
   }
   
   public void testProcedureCalls() throws Exception {
      runTests("tests/test4.tern");
   }
   
   public void testProcedureDecls() throws Exception {
      runTests("tests/test5.tern");
   }
   
   public void testWhileLoops() throws Exception {
      runTests("tests/test6.tern");
   }
}

