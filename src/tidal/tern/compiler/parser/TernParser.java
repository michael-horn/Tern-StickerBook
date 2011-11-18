/*
 * TernParser.java
 *
 * THIS FILE HAS BEEN GENERATED AUTOMATICALLY. DO NOT EDIT!
 */

package tidal.tern.compiler.parser;

import java.io.Reader;

import net.percederberg.grammatica.parser.ParserCreationException;
import net.percederberg.grammatica.parser.ProductionPattern;
import net.percederberg.grammatica.parser.ProductionPatternAlternative;
import net.percederberg.grammatica.parser.RecursiveDescentParser;
import net.percederberg.grammatica.parser.Tokenizer;

/**
 * A token stream parser.
 *
 * @author   Michael S. Horn
 * @version  1.0
 */
public class TernParser extends RecursiveDescentParser {

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_1 = 3001;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_2 = 3002;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_3 = 3003;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_4 = 3004;

    /**
     * Creates a new parser with a default analyzer.
     *
     * @param in             the input stream to read from
     *
     * @throws ParserCreationException if the parser couldn't be
     *             initialized correctly
     */
    public TernParser(Reader in) throws ParserCreationException {
        super(in);
        createPatterns();
    }

    /**
     * Creates a new parser.
     *
     * @param in             the input stream to read from
     * @param analyzer       the analyzer to use while parsing
     *
     * @throws ParserCreationException if the parser couldn't be
     *             initialized correctly
     */
    public TernParser(Reader in, TernAnalyzer analyzer)
        throws ParserCreationException {

        super(in, analyzer);
        createPatterns();
    }

    /**
     * Creates a new tokenizer for this parser. Can be overridden by a
     * subclass to provide a custom implementation.
     *
     * @param in             the input stream to read from
     *
     * @return the tokenizer created
     *
     * @throws ParserCreationException if the tokenizer couldn't be
     *             initialized correctly
     */
    protected Tokenizer newTokenizer(Reader in)
        throws ParserCreationException {

        return new TernTokenizer(in);
    }

    /**
     * Initializes the parser by creating all the production patterns.
     *
     * @throws ParserCreationException if the parser couldn't be
     *             initialized correctly
     */
    private void createPatterns() throws ParserCreationException {
        ProductionPattern             pattern;
        ProductionPatternAlternative  alt;

        pattern = new ProductionPattern(TernConstants.PROGRAM,
                                        "Program");
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_1, 1, -1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.COMMAND,
                                        "Command");
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.CONTROL_STRUCTURE, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.PROCEDURE_CALL, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.ASSIGNMENT, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.BUILT_IN_COMMAND, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.BLOCK,
                                        "Block");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.INDENT, 1, 1);
        alt.addProduction(TernConstants.COMMAND, 0, -1);
        alt.addToken(TernConstants.DEDENT, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.PROCEDURE_DEF,
                                        "ProcedureDef");
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.PROCEDURE_DECLARATION, 1, 1);
        alt.addProduction(TernConstants.BLOCK, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.PROCEDURE_DECLARATION,
                                        "ProcedureDeclaration");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.DEF, 1, 1);
        alt.addProduction(TernConstants.PROCEDURE_NAME, 1, 1);
        alt.addToken(TernConstants.LEFT_PAREN, 1, 1);
        alt.addProduction(TernConstants.FORMAL_PARAM_LIST, 0, 1);
        alt.addToken(TernConstants.RIGHT_PAREN, 1, 1);
        alt.addToken(TernConstants.COLON, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.IMPORT_DEF,
                                        "ImportDef");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.IMPORT, 1, 1);
        alt.addProduction(TernConstants.PROCEDURE_NAME, 1, 1);
        alt.addToken(TernConstants.LEFT_PAREN, 1, 1);
        alt.addProduction(TernConstants.FORMAL_PARAM_LIST, 0, 1);
        alt.addToken(TernConstants.RIGHT_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.PROCEDURE_NAME,
                                        "ProcedureName");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.IDENTIFIER, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.FORMAL_PARAM_LIST,
                                        "FormalParamList");
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.FORMAL_PARAM, 1, 1);
        alt.addProduction(SUBPRODUCTION_2, 0, -1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.FORMAL_PARAM,
                                        "FormalParam");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.IDENTIFIER, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.PROCESS_DEF,
                                        "ProcessDef");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.PROCESS, 1, 1);
        alt.addProduction(TernConstants.PROCESS_NAME, 1, 1);
        alt.addToken(TernConstants.COLON, 1, 1);
        alt.addProduction(TernConstants.BLOCK, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.PROCESS_NAME,
                                        "ProcessName");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.IDENTIFIER, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.CONTROL_STRUCTURE,
                                        "ControlStructure");
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.WHILE_LOOP, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.WHILE_LOOP,
                                        "WhileLoop");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.WHILE, 1, 1);
        alt.addProduction(TernConstants.WHILE_CONDITION, 1, 1);
        alt.addToken(TernConstants.COLON, 1, 1);
        alt.addProduction(SUBPRODUCTION_3, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.WHILE_CONDITION,
                                        "WhileCondition");
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.EXPRESSION, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.PROCEDURE_CALL,
                                        "ProcedureCall");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.IDENTIFIER, 1, 1);
        alt.addToken(TernConstants.LEFT_PAREN, 1, 1);
        alt.addProduction(TernConstants.ACTUAL_PARAM_LIST, 0, 1);
        alt.addToken(TernConstants.RIGHT_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.ACTUAL_PARAM_LIST,
                                        "ActualParamList");
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.EXPRESSION, 1, 1);
        alt.addProduction(SUBPRODUCTION_4, 0, -1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.ASSIGNMENT,
                                        "Assignment");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.IDENTIFIER, 1, 1);
        alt.addToken(TernConstants.ASSIGN, 1, 1);
        alt.addProduction(TernConstants.EXPRESSION, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.BUILT_IN_COMMAND,
                                        "BuiltInCommand");
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.WAIT_COMMAND, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.PRINT_COMMAND, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.START_COMMAND, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.STOP_COMMAND, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.WAIT_COMMAND,
                                        "WaitCommand");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.WAIT, 1, 1);
        alt.addProduction(TernConstants.EXPRESSION, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.PRINT_COMMAND,
                                        "PrintCommand");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.PRINT, 1, 1);
        alt.addProduction(TernConstants.EXPRESSION, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.START_COMMAND,
                                        "StartCommand");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.START, 1, 1);
        alt.addToken(TernConstants.IDENTIFIER, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.STOP_COMMAND,
                                        "StopCommand");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.STOP, 1, 1);
        alt.addToken(TernConstants.IDENTIFIER, 0, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.VARIABLE_NAME,
                                        "VariableName");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.IDENTIFIER, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.EXPRESSION,
                                        "Expression");
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.EX_FUNCTION, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.EX_ATOM, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.EX_FUNCTION,
                                        "ExFunction");
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.BINARY_FUNCTION, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.UNARY_FUNCTION, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.BINARY_FUNCTION,
                                        "BinaryFunction");
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.EX_TERM, 1, 1);
        alt.addProduction(TernConstants.BINARY_OPERATOR, 1, 1);
        alt.addProduction(TernConstants.EX_TERM, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.BINARY_OPERATOR,
                                        "BinaryOperator");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.AND, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.OR, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.MUL, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.DIV, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.ADD, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.SUB, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.GREATER_THAN, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.LESS_THAN, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.GREATER_THAN_EQ, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.LESS_THAN_EQ, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.NOT_EQUAL, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.EQUAL, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.UNARY_FUNCTION,
                                        "UnaryFunction");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.NOT, 1, 1);
        alt.addProduction(TernConstants.EX_TERM, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.EX_TERM,
                                        "ExTerm");
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.EX_ATOM, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.LEFT_PAREN, 1, 1);
        alt.addProduction(TernConstants.EXPRESSION, 1, 1);
        alt.addToken(TernConstants.RIGHT_PAREN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(TernConstants.EX_ATOM,
                                        "ExAtom");
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.TRUE, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.FALSE, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.NUMBER, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.VARIABLE_NAME, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_1,
                                        "Subproduction1");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.PROCESS_DEF, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.IMPORT_DEF, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.PROCEDURE_DEF, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.COMMAND, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.EXPRESSION, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_2,
                                        "Subproduction2");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.COMMA, 1, 1);
        alt.addProduction(TernConstants.FORMAL_PARAM, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_3,
                                        "Subproduction3");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.COMMAND, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(TernConstants.BLOCK, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_4,
                                        "Subproduction4");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(TernConstants.COMMA, 1, 1);
        alt.addProduction(TernConstants.EXPRESSION, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);
    }
}
