/*
 * TernConstants.java
 *
 * THIS FILE HAS BEEN GENERATED AUTOMATICALLY. DO NOT EDIT!
 */

package tidal.tern.compiler.parser;

/**
 * An interface with constants for the parser and tokenizer.
 *
 * @author   Michael S. Horn
 * @version  1.0
 */
public interface TernConstants {

    /**
     * A token identity constant.
     */
    public static final int INDENT = 1001;

    /**
     * A token identity constant.
     */
    public static final int DEDENT = 1002;

    /**
     * A token identity constant.
     */
    public static final int LEFT_BRACE = 1003;

    /**
     * A token identity constant.
     */
    public static final int RIGHT_BRACE = 1004;

    /**
     * A token identity constant.
     */
    public static final int LEFT_PAREN = 1005;

    /**
     * A token identity constant.
     */
    public static final int RIGHT_PAREN = 1006;

    /**
     * A token identity constant.
     */
    public static final int COLON = 1007;

    /**
     * A token identity constant.
     */
    public static final int ASSIGN = 1008;

    /**
     * A token identity constant.
     */
    public static final int COMMA = 1009;

    /**
     * A token identity constant.
     */
    public static final int IMPORT = 1010;

    /**
     * A token identity constant.
     */
    public static final int IF = 1011;

    /**
     * A token identity constant.
     */
    public static final int DEF = 1012;

    /**
     * A token identity constant.
     */
    public static final int PROCESS = 1013;

    /**
     * A token identity constant.
     */
    public static final int WHILE = 1014;

    /**
     * A token identity constant.
     */
    public static final int WAIT = 1015;

    /**
     * A token identity constant.
     */
    public static final int PRINT = 1016;

    /**
     * A token identity constant.
     */
    public static final int RAND = 1017;

    /**
     * A token identity constant.
     */
    public static final int START = 1018;

    /**
     * A token identity constant.
     */
    public static final int STOP = 1019;

    /**
     * A token identity constant.
     */
    public static final int LESS_THAN = 1020;

    /**
     * A token identity constant.
     */
    public static final int LESS_THAN_EQ = 1021;

    /**
     * A token identity constant.
     */
    public static final int GREATER_THAN = 1022;

    /**
     * A token identity constant.
     */
    public static final int GREATER_THAN_EQ = 1023;

    /**
     * A token identity constant.
     */
    public static final int NOT_EQUAL = 1024;

    /**
     * A token identity constant.
     */
    public static final int EQUAL = 1025;

    /**
     * A token identity constant.
     */
    public static final int ADD = 1026;

    /**
     * A token identity constant.
     */
    public static final int SUB = 1027;

    /**
     * A token identity constant.
     */
    public static final int MUL = 1028;

    /**
     * A token identity constant.
     */
    public static final int DIV = 1029;

    /**
     * A token identity constant.
     */
    public static final int RANDOM = 1030;

    /**
     * A token identity constant.
     */
    public static final int OR = 1031;

    /**
     * A token identity constant.
     */
    public static final int NOT = 1032;

    /**
     * A token identity constant.
     */
    public static final int AND = 1033;

    /**
     * A token identity constant.
     */
    public static final int TRUE = 1034;

    /**
     * A token identity constant.
     */
    public static final int FALSE = 1035;

    /**
     * A token identity constant.
     */
    public static final int NUMBER = 1036;

    /**
     * A token identity constant.
     */
    public static final int IDENTIFIER = 1037;

    /**
     * A token identity constant.
     */
    public static final int WHITESPACE = 1038;

    /**
     * A production node identity constant.
     */
    public static final int PROGRAM = 2001;

    /**
     * A production node identity constant.
     */
    public static final int COMMAND = 2002;

    /**
     * A production node identity constant.
     */
    public static final int BLOCK = 2003;

    /**
     * A production node identity constant.
     */
    public static final int PROCEDURE_DEF = 2004;

    /**
     * A production node identity constant.
     */
    public static final int PROCEDURE_DECLARATION = 2005;

    /**
     * A production node identity constant.
     */
    public static final int IMPORT_DEF = 2006;

    /**
     * A production node identity constant.
     */
    public static final int PROCEDURE_NAME = 2007;

    /**
     * A production node identity constant.
     */
    public static final int FORMAL_PARAM_LIST = 2008;

    /**
     * A production node identity constant.
     */
    public static final int FORMAL_PARAM = 2009;

    /**
     * A production node identity constant.
     */
    public static final int PROCESS_DEF = 2010;

    /**
     * A production node identity constant.
     */
    public static final int PROCESS_NAME = 2011;

    /**
     * A production node identity constant.
     */
    public static final int CONTROL_STRUCTURE = 2012;

    /**
     * A production node identity constant.
     */
    public static final int WHILE_LOOP = 2013;

    /**
     * A production node identity constant.
     */
    public static final int WHILE_CONDITION = 2014;

    /**
     * A production node identity constant.
     */
    public static final int PROCEDURE_CALL = 2015;

    /**
     * A production node identity constant.
     */
    public static final int FUNCTION_CALL = 2016;

    /**
     * A production node identity constant.
     */
    public static final int ACTUAL_PARAM_LIST = 2017;

    /**
     * A production node identity constant.
     */
    public static final int ASSIGNMENT = 2018;

    /**
     * A production node identity constant.
     */
    public static final int BUILT_IN_COMMAND = 2019;

    /**
     * A production node identity constant.
     */
    public static final int WAIT_COMMAND = 2020;

    /**
     * A production node identity constant.
     */
    public static final int PRINT_COMMAND = 2021;

    /**
     * A production node identity constant.
     */
    public static final int START_COMMAND = 2022;

    /**
     * A production node identity constant.
     */
    public static final int STOP_COMMAND = 2023;

    /**
     * A production node identity constant.
     */
    public static final int VARIABLE_NAME = 2024;

    /**
     * A production node identity constant.
     */
    public static final int EXPRESSION = 2025;

    /**
     * A production node identity constant.
     */
    public static final int EX_FUNCTION = 2026;

    /**
     * A production node identity constant.
     */
    public static final int BINARY_FUNCTION = 2027;

    /**
     * A production node identity constant.
     */
    public static final int BINARY_OPERATOR = 2028;

    /**
     * A production node identity constant.
     */
    public static final int UNARY_FUNCTION = 2029;

    /**
     * A production node identity constant.
     */
    public static final int EX_TERM = 2030;

    /**
     * A production node identity constant.
     */
    public static final int EX_ATOM = 2031;
}
