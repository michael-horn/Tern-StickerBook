/*
 * TernTokenizer.java
 *
 * THIS FILE HAS BEEN GENERATED AUTOMATICALLY. DO NOT EDIT!
 */

package tidal.tern.compiler.parser;

import java.io.Reader;

import net.percederberg.grammatica.parser.ParserCreationException;
import net.percederberg.grammatica.parser.TokenPattern;
import net.percederberg.grammatica.parser.Tokenizer;

/**
 * A character stream tokenizer.
 *
 * @author   Michael S. Horn
 * @version  1.0
 */
public class TernTokenizer extends Tokenizer {

    /**
     * Creates a new tokenizer for the specified input stream.
     *
     * @param input          the input stream to read
     *
     * @throws ParserCreationException if the tokenizer couldn't be
     *             initialized correctly
     */
    public TernTokenizer(Reader input) throws ParserCreationException {
        super(input, false);
        createPatterns();
    }

    /**
     * Initializes the tokenizer by creating all the token patterns.
     *
     * @throws ParserCreationException if the tokenizer couldn't be
     *             initialized correctly
     */
    private void createPatterns() throws ParserCreationException {
        TokenPattern  pattern;

        pattern = new TokenPattern(TernConstants.INDENT,
                                   "INDENT",
                                   TokenPattern.STRING_TYPE,
                                   "{");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.DEDENT,
                                   "DEDENT",
                                   TokenPattern.STRING_TYPE,
                                   "}");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.LEFT_BRACE,
                                   "LEFT_BRACE",
                                   TokenPattern.STRING_TYPE,
                                   "[");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.RIGHT_BRACE,
                                   "RIGHT_BRACE",
                                   TokenPattern.STRING_TYPE,
                                   "]");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.LEFT_PAREN,
                                   "LEFT_PAREN",
                                   TokenPattern.STRING_TYPE,
                                   "(");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.RIGHT_PAREN,
                                   "RIGHT_PAREN",
                                   TokenPattern.STRING_TYPE,
                                   ")");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.COLON,
                                   "COLON",
                                   TokenPattern.STRING_TYPE,
                                   ":");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.ASSIGN,
                                   "ASSIGN",
                                   TokenPattern.STRING_TYPE,
                                   "=");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.COMMA,
                                   "COMMA",
                                   TokenPattern.STRING_TYPE,
                                   ",");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.IMPORT,
                                   "IMPORT",
                                   TokenPattern.STRING_TYPE,
                                   "import");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.IF,
                                   "IF",
                                   TokenPattern.STRING_TYPE,
                                   "if");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.DEF,
                                   "DEF",
                                   TokenPattern.STRING_TYPE,
                                   "def");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.PROCESS,
                                   "PROCESS",
                                   TokenPattern.STRING_TYPE,
                                   "process");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.WHILE,
                                   "WHILE",
                                   TokenPattern.STRING_TYPE,
                                   "while");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.WAIT,
                                   "WAIT",
                                   TokenPattern.STRING_TYPE,
                                   "wait");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.PRINT,
                                   "PRINT",
                                   TokenPattern.STRING_TYPE,
                                   "print");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.TRACE,
                                   "TRACE",
                                   TokenPattern.STRING_TYPE,
                                   "trace");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.RAND,
                                   "RAND",
                                   TokenPattern.STRING_TYPE,
                                   "rand");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.START,
                                   "START",
                                   TokenPattern.STRING_TYPE,
                                   "start");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.STOP,
                                   "STOP",
                                   TokenPattern.STRING_TYPE,
                                   "stop");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.LESS_THAN,
                                   "LESS_THAN",
                                   TokenPattern.STRING_TYPE,
                                   "<");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.LESS_THAN_EQ,
                                   "LESS_THAN_EQ",
                                   TokenPattern.STRING_TYPE,
                                   "<=");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.GREATER_THAN,
                                   "GREATER_THAN",
                                   TokenPattern.STRING_TYPE,
                                   ">");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.GREATER_THAN_EQ,
                                   "GREATER_THAN_EQ",
                                   TokenPattern.STRING_TYPE,
                                   ">=");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.NOT_EQUAL,
                                   "NOT_EQUAL",
                                   TokenPattern.STRING_TYPE,
                                   "!=");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.EQUAL,
                                   "EQUAL",
                                   TokenPattern.STRING_TYPE,
                                   "==");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.ADD,
                                   "ADD",
                                   TokenPattern.STRING_TYPE,
                                   "+");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.SUB,
                                   "SUB",
                                   TokenPattern.STRING_TYPE,
                                   "-");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.MUL,
                                   "MUL",
                                   TokenPattern.STRING_TYPE,
                                   "*");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.DIV,
                                   "DIV",
                                   TokenPattern.STRING_TYPE,
                                   "/");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.RANDOM,
                                   "RANDOM",
                                   TokenPattern.STRING_TYPE,
                                   "random");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.OR,
                                   "OR",
                                   TokenPattern.STRING_TYPE,
                                   "or");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.NOT,
                                   "NOT",
                                   TokenPattern.STRING_TYPE,
                                   "not");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.AND,
                                   "AND",
                                   TokenPattern.STRING_TYPE,
                                   "and");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.TRUE,
                                   "TRUE",
                                   TokenPattern.STRING_TYPE,
                                   "true");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.FALSE,
                                   "FALSE",
                                   TokenPattern.STRING_TYPE,
                                   "false");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.NUMBER,
                                   "NUMBER",
                                   TokenPattern.REGEXP_TYPE,
                                   "[\\-]?[0-9]+|[\\-]?[0-9]*\\.[0-9]+");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.IDENTIFIER,
                                   "IDENTIFIER",
                                   TokenPattern.REGEXP_TYPE,
                                   "[a-zA-Z_\\%][a-zA-Z0-9_\\-\\?\\%]*");
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.WHITESPACE,
                                   "WHITESPACE",
                                   TokenPattern.REGEXP_TYPE,
                                   "[ \\t\\r\\n]+");
        pattern.setIgnore();
        addPattern(pattern);

        pattern = new TokenPattern(TernConstants.STRING,
                                   "STRING",
                                   TokenPattern.REGEXP_TYPE,
                                   "(\".*\")|('.*')");
        addPattern(pattern);
    }
}
