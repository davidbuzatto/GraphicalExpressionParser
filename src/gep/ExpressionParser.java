package gep;

import java.util.ArrayList;
import java.util.List;

/**
 * LL(1) Parser for arithmetic expressions.
 * 
 * It does not check for expression validity so, it assumes that the
 * expression is syntactically correct.
 * 
 * Grammar (EBNF):
 * 
 *     expression -> term ( addingOp term )* .
 *     addingOp -> "+" | "-" .
 * 
 *     term -> factor ( multiplyingOp factor )* . 
 *     multiplyingOp -> "*" | "/" .
 * 
 *     factor -> constValue | "(" expression ")" .
 *     constValue -> [0..1]+ .
 * 
 * @author Prof. Dr. David Buzatto
 */
public class ExpressionParser {
    
    private class Expression {
    }
    
    private class BinaryExpression extends Expression {
        
        Expression op1;
        Token operator;
        Expression op2;
        
        BinaryExpression( Expression op1, Token operator, Expression op2 ) {
            this.op1 = op1;
            this.operator = operator;
            this.op2 = op2;
        }
        
        @Override
        public String toString() {
            return op1 + " " + operator.type + " " + op2;
        }
        
    }
    
    private class AddingExpression extends BinaryExpression {

        AddingExpression( Expression op1, Token operator, Expression op2 ) {
            super( op1, operator, op2 );
        }
        
    }
    
    private class MultiplyingExpression extends BinaryExpression {

        MultiplyingExpression( Expression op1, Token operator, Expression op2 ) {
            super( op1, operator, op2 );
        }
        
    }
    
    private class ConstValue extends Expression {

        String value;
        
        ConstValue( String value ) {
            this.value = value;
        }
        
        @Override
        public String toString() {
            return value;
        }
        
    }
    
    private List<Token> tokens;
    private int pos;
    private int level = -1;
    private Expression resultExpression;
    
    public static double eval( String expression ) {
        return new ExpressionParser( expression ).getValue();
    }
    
    public static String toString( String expression ) {
        return new ExpressionParser( expression ).toString();
    }
    
    private ExpressionParser( String expression ) {
        this.tokens = getTokens( expression );
        resultExpression = parseExpression();
    }
    
    private Token nextToken() {
        if ( pos < tokens.size() ) {
            return tokens.get( pos++ );
        }
        return null;
    }
    
    private Token currentToken() {
        if ( pos < tokens.size() ) {
            return tokens.get( pos );
        }
        return null;
    }
    
    // expression -> term ( addingOp term )* .
    // addingOp -> "+" | "-" .
    private Expression parseExpression() {
        
        Expression term = null;
        Expression term2 = null;
        Token operator = null;
        
        term = parseTerm();
        
        while ( currentToken() != null && (
                currentToken().type == Token.Type.PLUS ||
                currentToken().type == Token.Type.MINUS ) ) {
            operator = nextToken();
            term2 = parseTerm();
            term = new AddingExpression( term, operator, term2 );
        }
        
        return term;
        
    }
    
    // term -> factor ( multiplyingOp factor )* . 
    // multiplyingOp -> "*" | "/" .
    private Expression parseTerm() {
        
        Expression factor = null;
        Expression factor2 = null;
        Token operator = null;
        
        factor = parseFactor();
        
        while ( currentToken() != null && (
                currentToken().type == Token.Type.TIMES ||
                currentToken().type == Token.Type.DIVIDE ) ) {
            operator = nextToken();
            factor2 = parseFactor();
            factor = new MultiplyingExpression( factor, operator, factor2 );
        }
        
        return factor;
        
    }
    
    // factor -> constValue | "(" expression ")" .
    // constValue -> [0..1]+ .
    private Expression parseFactor() {
        
        Expression expr = null;
        
        Token t = nextToken();
        
        if ( t.type == Token.Type.NUMBER ) {
            expr = new ConstValue( t.value );
        } else if ( t.type == Token.Type.LEFT_PAREN ) {
            expr = parseExpression();
            nextToken();
        }
        
        return expr;
        
    }
    
    // all tokens must be separated by at least one space
    private List<Token> getTokens( String expression ) {
        
        List<Token> tokens = new ArrayList<>();
        
        for ( String s : expression.split( "\\s+" ) ) {
            
            Token t = new Token();
            t.value = s;
            
            switch ( s ) {
                case "+": 
                    t.type = Token.Type.PLUS;
                    break;
                case "-": 
                    t.type = Token.Type.MINUS;
                    break;
                case "*": 
                    t.type = Token.Type.TIMES;
                    break;
                case "/": 
                    t.type = Token.Type.DIVIDE;
                    break;
                case "(": 
                    t.type = Token.Type.LEFT_PAREN;
                    break;
                case ")": 
                    t.type = Token.Type.RIGHT_PAREN;
                    break;
                default:
                    t.type = Token.Type.NUMBER;
                    t.value = s;
                    break;
            }
            
            tokens.add( t );
            
        }
        
        return tokens;
        
    }
    
    public double getValue() {
        return getValue( resultExpression );
    }
    
    // visitor
    private double getValue( Expression e ) {
        
        if ( e instanceof ConstValue c ) {
            return Double.parseDouble( c.value );
        } else if ( e instanceof AddingExpression a ) {
            double op1 = getValue( a.op1 );
            double op2 = getValue( a.op2 );
            if ( a.operator.type == Token.Type.PLUS ) {
                return op1 + op2;
            } else {
                return op1 - op2;
            }
        } else if ( e instanceof MultiplyingExpression m ) {
            double op1 = getValue( m.op1 );
            double op2 = getValue( m.op2 );
            if ( m.operator.type == Token.Type.TIMES ) {
                return op1 * op2;
            } else {
                return op1 / op2;
            }
        }
        
        return 0.0;
        
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        printToBuilder( resultExpression, sb, 0 );
        return sb.toString().trim();
    }
    
    // visitor
    private void printToBuilder( Expression e, StringBuilder sb, int level ) {
        
        String spacing = level > 0 ? " |   ".repeat( level - 1 ) : "";
        String line = level > 0 ? " |___" : "";
        
        if ( e instanceof ConstValue ) {
            sb.append( "\n" ).append( spacing ).append( line ).append( e );
        } else if ( e instanceof AddingExpression a ) {
            sb.append( "\n" ).append( spacing ).append( line ).append( "(" ).append( a.operator.value ).append( ")" );
            printToBuilder( a.op1, sb, level + 1 );
            printToBuilder( a.op2, sb, level + 1 );
        } else if ( e instanceof MultiplyingExpression m ) {
            sb.append( "\n" ).append( spacing ).append( line ).append( "(" ).append( m.operator.value ).append( ")" );
            printToBuilder( m.op1, sb, level + 1 );
            printToBuilder( m.op2, sb, level + 1 );
        }
        
    }
    
}
