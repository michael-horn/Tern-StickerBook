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
import android.content.res.XmlResourceParser;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;



public class StatementFactory {


   protected static List<Statement> stypes = new java.util.ArrayList<Statement>();
   
	
/**
 * Called by the tangible compiler to generate new statements
 * from topcodes found in an image.
 */
   public static Statement createStatement(TopCode top) {
      for (Statement s : stypes) {
         if (s.getCode() == top.getCode()) {
            return s.newInstance(top);
         }
      }
      return null;
   }
   
   
/**
 *  Load statements from XML resource file
 */
   public static void loadStatements(XmlResourceParser xml) throws CompileException {
      stypes.clear();
      
      try {
         Statement s = null;
         
         while (true) {
            
            xml.next();
            switch (xml.getEventType()) {
               
               case XmlPullParser.START_TAG:
                  if ("statement".equals(xml.getName())) {
                     s = newStatement(xml);
                  }
                  else if (s != null) {
                     if ("code".equals(xml.getName())) {
                        s.setCompileText(cleanWhitespace(xml.nextText()));
                     }
                     else if ("plug".equals(xml.getName()) ||
                              "socket".equals(xml.getName()) ||
                              "param".equals(xml.getName())) {
                        s.addConnector(newConnector(s, xml));
                     }
                  }
                  break;
   
               
               case XmlPullParser.END_TAG:
                  if ("statement".equals(xml.getName())) {
                     stypes.add(s);
                     s = null;
                  }
                  break;
               
               
               case XmlPullParser.END_DOCUMENT:
                  return;
            }
         }
      } catch (Exception x) {
         throw new CompileException(x);
      }
   }
   
   
   private static Statement newStatement(XmlResourceParser xml) throws Exception {
         
      // Create a Statement of the appropriate type
      String cname = xml.getAttributeValue(null, "class");
      if (cname == null) cname = "tidal.tern.compiler.Statement";
      Statement s = (Statement)(Class.forName(cname)).newInstance();
      

      // Set the name and topcode value
      s.setName(xml.getAttributeValue(null, "name"));
      s.setTopCode(new TopCode(xml.getAttributeIntValue(null, "code", 0)));
      s.setStartStatement(toBoolean(xml.getAttributeValue(null, "start")));
      return s;
   }
   
   
   private static Connector newConnector(Statement s, XmlResourceParser xml) {
      int type = Connector.TYPE_IN;
      if ("socket".equals(xml.getName())) {
         type = Connector.TYPE_IN;
      } else if ("plug".equals(xml.getName())) {
         type = Connector.TYPE_OUT;
      } else if ("param".equals(xml.getName())) {
         type = Connector.TYPE_PARAM;
      }
      
      String name = xml.getAttributeValue(null, "name");
      if (name == null) name = "";
      
      String sdx = xml.getAttributeValue(null, "dx");
      String sdy = xml.getAttributeValue(null, "dy");
      
      return new Connector(s, type, name, toFloat(sdx), toFloat(sdy));
   }
   
   
   private static float toFloat(String s) {
      if (s == null) return 0.0f;
      try {
         return Float.parseFloat(s);
      } catch (Exception x) {
         return 0.0f;
      }
   }
   
   
   private static boolean toBoolean(String s) {
      if ("true".equals(s) || "yes".equals(s)) {
         return true;
      } else {
         return false;
      }
   }
   

   private static String cleanWhitespace(String s) {
      if (s == null) return "";
      String [] lines = s.split("\n");
      String result = "";
      
      for (int i=0; i<lines.length; i++) {
         result += lines[i].trim() + "\n";
      }
      return result;
   }
}

