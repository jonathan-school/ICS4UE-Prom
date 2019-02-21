import javax.swing.*;
import java.awt.*;

public class ContentPanel extends JTabbedPane {
    // Layouts
    private DashboardLayout dashboard;
    private StudentManagerLayout editStudent;
    private SeatingGenLayout seatGen;
    private TableLayout tableLayout;

    // File manager
    private FileIOManager io;


    public ContentPanel(int x, int y, FileIOManager fileManager){
        this.setPreferredSize(new Dimension(x, y));
        this.setBackground(Color.decode("#C4C4C4"));
        this.setTabPlacement(LEFT);

        this.io = fileManager;
    }

    public void addChildren(){
        //TODO: Fix hardcode
        dashboard = new DashboardLayout(this.getWidth()-300, this.getHeight(), io, this);
        editStudent = new StudentManagerLayout(this.getWidth()-300, this.getHeight());
        seatGen = new SeatingGenLayout(this.getWidth()-300, this.getHeight());
        tableLayout = new TableLayout(this.getWidth()-300, this.getHeight());

        this.addTab("Dashboard", dashboard);
        this.addTab("Student Manager", editStudent);
        this.addTab("Seating Generator", seatGen);
        this.addTab("Table Display", tableLayout);

        // Disable other tabs until project is set up/loaded
        this.setEnabledAt(1, false);
        this.setEnabledAt(2, false);
        this.setEnabledAt(3, false);
    }

    // Enable other tabs when project is set up/loaded
    public void enableTabs(){
        this.setEnabledAt(1, true);
        this.setEnabledAt(2, true);
        this.setEnabledAt(3, true);
    }
}
