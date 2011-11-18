/*
 * @(#) StartStatement.java
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


/**
 * This interface is used to identify statements in a program
 * that begins the flow-of-control chain.  Every valid program must
 * have at least one StartStatement.  Tangible language
 * implementations should have at least one statement type that
 * implements this interface.
 *
 * @author Michael Horn
 * @version $Revision: 1.2 $, $Date: 2008/11/08 16:56:57 $
 */
public interface StartStatement {
	
}
