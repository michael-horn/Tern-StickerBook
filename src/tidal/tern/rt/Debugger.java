/*
 * @(#) Debugger.java
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


/**
 * A listener interface for debug events from the Interpreter.
 *
 * @author Michael Horn
 * @version $Revision: 1.1 $, $Date: 2008/04/23 00:58:29 $
 */
public interface Debugger {

   public void trace(Process proc, String message);

   public void print(Process proc, String value);
   
   public void error(Process proc, String message);
   
   public void processStarted(Process proc);

   public void processStopped(Process proc);

}
