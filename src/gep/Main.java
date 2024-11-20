package gep;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * 
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Main extends EngineFrame {
    
    private ExpressionParser ep;
    private String expression;
    private double result;
    
    public Main() {
        super ( 800, 450, "Graphical Expression Parser", 60, true );
    }
    
    @Override
    public void create() {
        expression = "1 + 2 - 2 * 4 - ( 5 / 10 )";
        ep = new ExpressionParser( expression );
        result = ep.getValue();
    }
    
    @Override
    public void update( double delta ) {
        
        if ( isMouseButtonPressed( MOUSE_BUTTON_LEFT ) ) {
            SwingUtilities.invokeLater( () -> {
                expression = JOptionPane.showInputDialog( "Expression" );
                ep = new ExpressionParser( expression );
                result = ep.getValue();
            });
        }
        
    }
    
    @Override
    public void draw() {
        clearBackground( WHITE );
        ep.draw( this, 100, 100, 60, 20 );
        drawText( String.format( "%s = %.2f", expression, result ), 10, 10, 20, BLACK );
    }
    
    public static void main( String[] args ) {
        new Main();
    }
    
}
