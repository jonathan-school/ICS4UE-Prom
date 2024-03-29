/**
 * TableChart.java
 * Version 1.0;
 * @author Bao, Xu
 * Febuary 20, 2019
 * Layout for tables of students to show
 **/
// GUI & Graphics imports
import java.awt.Color;
import java.awt.Dimension;
// Table Imports
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
// Utils
import java.util.ArrayList;

public class TableChart extends Chart {
    // Class variables
    private static JTable table;
    private static DefaultTableModel model;
    private int x, y;

    // Constructor
    TableChart(int x, int y){
        super(x,y);
        this.x = x;
        this.y = y;
        String[] columnNames = {};
        Object[][] emptyRow = {};
        // Makes the model for the chart
        model = new DefaultTableModel(emptyRow, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells unEditable
                return false;
            }
        };

        // adds the chart to the pane
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(x/2, y/2));
        table.setFillsViewportHeight(true);
        DynamicLabel placeholder = new DynamicLabel("placeholder", x/10, y/20, Color.BLACK);
        table.setFont(placeholder.getFont());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(y/20);
        table.setShowGrid(false);
        table.getTableHeader().setBackground(Color.decode("#BDA7D4"));
        table.getTableHeader().setFont(placeholder.getFont());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(x, y));
        scrollPane.getHorizontalScrollBar().setPreferredSize(
                new Dimension(Integer.MAX_VALUE, x/60));
        scrollPane.getHorizontalScrollBar().setBackground(Color.WHITE);

        this.add(scrollPane);
    }// End of constructor
/**-----------------------------METHODS---------------------------**/

    /**
     * loadTable
     * Loads up a chart from generated tables
     * @param tableSize, integer representing size of table
     * @param tables, ArrayList of Tables for the project
     * @return String, student's number
     */
    public void loadTable(ArrayList<Table> tables, int tableSize) {
        model.setColumnCount(0);
        model.setRowCount(0);
        for (int i = 0; i < tables.size(); i++){
            model.addColumn("Table " + (i + 1));
        }
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(x/5);
        }

        int col = tables.size();
        int row = tableSize;

        Object[][] data = new Object[row][col];
        // Gets info to fill table
        for (int j = 0; j < col; j++) {
            for (int k = 0; k < tables.get(j).getStudents().size(); k++){
                Student currentStudent = tables.get(j).getStudents().get(k);
                data[k][j] = currentStudent.getName();
            }
        }
        for (int l = 0; l < row; l++){
            model.addRow(data[l]);
        }
    }

}

