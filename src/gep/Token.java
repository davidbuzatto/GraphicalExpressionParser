package gep;

/**
 * Expression token.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Token {
    
    public static enum Type {
        PLUS,
        MINUS,
        TIMES,
        DIVIDE,
        LEFT_PAREN,
        RIGHT_PAREN,
        NUMBER;
    }
    
    public Type type;
    public String value;

    @Override
    public String toString() {
        return "Token{" + "type=" + type + ", value=" + value + '}';
    }
    
}
