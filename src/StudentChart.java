import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class StudentChart extends Chart {
    private static JTable table;
    private static DefaultTableModel model;
    private int selectedIndex = -1;

    private StudentManagerLayout studentManager;

    public StudentChart(int x, int y, StudentManagerLayout manager){
        super(x,y);
        this.studentManager = manager;

        String[] columnNames = {"Student Number",
                "First",
                "Last",
                "Likes",
                "Dietary Restrictions"};

        Object[][] emptyRow = {};

        model = new DefaultTableModel(emptyRow, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells unEditable
                return false;
            }
        };

        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(x, y));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting())
                    return;
                selectedIndex = selectionModel.getAnchorSelectionIndex();
                studentManager.changeSelected(true);
                System.out.println(selectedIndex);
            }
        });

        table.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(x, y));
        this.add(scrollPane);
    }

    public void loadStudents(ArrayList<Student> students) {
        Object[][] data = new Object[students.size()][5];
        for (int i = 0; i < students.size(); i++) {
            String[] token = students.get(i).getName().split(" ");
            data[i][0] = students.get(i).getStudentNumber();
            data[i][1] = token[0];
            data[i][2] = token[1];
            data[i][3] = students.get(i).getFriendStudentNumbers();
            data[i][4] = students.get(i).getDietaryRestrictions();
            if(!existsInTable(data[i])){
                model.addRow(data[i]);
            }
        }
    }

    public void deleteStudent(){
        model.removeRow(table.getSelectedRow());
        studentManager.changeSelected(false);
    }

    public Student getStudent(){
        int row = table.getSelectedRow();
        String studentNumber = (String)model.getValueAt(row, 0);
        String name = model.getValueAt(row, 1) + " " + model.getValueAt(row, 2);
        ArrayList<String> friends = (ArrayList<String>)model.getValueAt(row, 3);
        ArrayList<String> diet = (ArrayList<String>)model.getValueAt(row, 4);

        Student selectedStudent = new Student(name, studentNumber, diet, friends);
        return selectedStudent;
    }

    private boolean existsInTable(Object[] entry) {

        // Get row and column count
        int rowCount = table.getRowCount();
        int colCount = table.getColumnCount();
        //TODO: Compare only student numbers instead of everything

        // Get Current Table Entry
        String curEntry = "";
        for (Object o : entry) {
            String e = o.toString();
            curEntry = curEntry + " " + e;
        }

        // Check against all entries
        for (int i = 0; i < rowCount; i++) {
            String rowEntry = "";
            for (int j = 0; j < colCount; j++)
                rowEntry = rowEntry + " " + table.getValueAt(i, j).toString();
            if (rowEntry.equalsIgnoreCase(curEntry)) {
                return true;
            }
        }
        return false;
    }
}