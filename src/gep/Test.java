package gep;

/**
 *
 * @author Prof. Dr. David Buzatto
 */
public class Test {
    
    public static void main( String[] args ) {
        
        String[] expressions = {
            "1 + 2",
            "1 - 4",
            "1 + 2 - 3",
            "1 - 2 + 3",
            "2 * 2 - 3",
            "2 - 2 * 3",
            "( 4 - 2 ) * 3",
            "2 / 2 * 3",
            "3 * 3 + 4 * 4",
        };
        
        for ( String expr : expressions ) {
            System.out.printf( "%s = %.2f\n", expr, ExpressionParser.eval( expr ) );
            System.out.println( ExpressionParser.toString( expr ) );
            System.out.println();
        }
        
    }
        
}
