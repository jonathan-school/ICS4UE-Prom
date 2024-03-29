/**
 * TicketingSystem.java
 * Version 1.0
 * @author David Bao, Jonathan Xu
 * February 13, 2019
 * Basis for ticketing system UML
 **/
// GUI & Graphics imports
import javax.swing.JFrame;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
// IO imports
import java.io.IOException;

public class TicketingSystem extends JFrame {
    //Class variables
    private static JFrame window;
    private HeaderPanel headerPanel;
    private ContentPanel  contentPanel;
    // Fit to screen
    private final int MAX_WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private final int MAX_HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    private FileIOManager io = new FileIOManager(this);

    //Main
    public static void main(String[] args) throws IOException, FontFormatException {
        window = new TicketingSystem();
    }

    //Constructor - this runs first
    public TicketingSystem() throws IOException, FontFormatException{
        super("Prom Ticketing System");

        // Set the frame to full screen
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setResizable(false);

        //Set up fonts
        Font robotoThin = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("Roboto-Thin.ttf"));
        Font robotoLight = Font.createFont(Font.TRUETYPE_FONT, getClass().getResource("Roboto-Light.ttf").openStream());
        Font robotoRegular = Font.createFont(Font.TRUETYPE_FONT, getClass().getResource("Roboto-Regular.ttf").openStream());

        GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        genv.registerFont(robotoThin);
        genv.registerFont(robotoLight);
        genv.registerFont(robotoRegular);

        //Set up look and feel
        UIManager.put("TabbedPane.selected", Color.decode("#8F8F8F"));

        /*
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }*/

        //Set up the inner panels (where we put our graphics)``
        headerPanel = new HeaderPanel("Prom Design", MAX_WIDTH, MAX_HEIGHT/10, 1);
        this.add(headerPanel, BorderLayout.NORTH);

        contentPanel = new ContentPanel(MAX_WIDTH, MAX_HEIGHT/10*9, io);
        this.add(contentPanel, BorderLayout.CENTER);

        this.pack();
        contentPanel.addChildren();

        this.requestFocusInWindow(); //make sure the frame has focus

        this.setVisible(true);


    }// End of constructor

}
