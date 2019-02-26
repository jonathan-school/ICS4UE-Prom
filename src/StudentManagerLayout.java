/**
 * StudentManagerLayout.java
 * Version 1.0;
 * @author Bao, Xu
 * Febuary 16, 2019
 * Layout to see and edit students
 **/

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

//TODO: Add button to generate test student data

public class StudentManagerLayout extends CustomPanel{
    private int x, y;
    private CustomPanel[] frames = new CustomPanel[2];
    private FileIOManager io;
    private DashboardLayout dashboard;

    private StudentChart chart;
    private FriendSelector likesSelector;
    private DietSelector dietSelector;
    private JTextField nameField, numField;

    private ArrayList<Student> students;
    private CustomButton editStudentBtn, deleteStudentBtn;

    private boolean editingMode = false;

    public StudentManagerLayout(int x, int y, FileIOManager io, DashboardLayout dashboardLayout){
        super(x, y, "Student Manager", "Add and modify students");
        this.x = x;
        this.y = y;
       // this.setLayout(new OverlayLayout(this));
        this.io = io;
        this.dashboard = dashboardLayout;

        addFrame1();
        addFrame2();
        showFrame(0);
    }

    public void loadStudents(){
        this.students = io.loadStudents();
        chart.loadStudents(students);
    }

    public void changeSelected(boolean state){
        editStudentBtn.setEnabled(state);
        deleteStudentBtn.setEnabled(state);
    }

    // Default Student Display
    private void addFrame1(){
        frames[0] = new CustomPanel();
        frames[0].setLayout(new BoxLayout(frames[0], BoxLayout.PAGE_AXIS));

        CustomPanel row1 = new CustomPanel();
        row1.setLayout(new BoxLayout(row1, BoxLayout.LINE_AXIS));
            DynamicLabel header = new DynamicLabel("Current Students", x, y/30, Color.BLACK);
            CustomPanel buttonRow = new CustomPanel();
            buttonRow.setLayout(new FlowLayout(FlowLayout.RIGHT));
            CustomButton saveBtn = new CustomButton("Save", 2, x, y/40);
            saveBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    io.saveStudents(students);
                }
            });
            editStudentBtn = new CustomButton("Edit Student", 2, x, y/40);
            editStudentBtn.setEnabled(false);
            editStudentBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setEditorMode(1);
                    showFrame(1);
                }
            });
            deleteStudentBtn = new CustomButton("Delete Student", 2, x, y/40);
            deleteStudentBtn.setEnabled(false);
            deleteStudentBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    students.remove(chart.getStudent(students));
                    chart.deleteStudent();
                    dashboard.updateDashboard(students);
                }
            });
            CustomButton newStudentBtn = new CustomButton("New Student", 2, x, y/40);
            newStudentBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setEditorMode(0);
                    showFrame(1);
                }
            });
        buttonRow.add(saveBtn);
        buttonRow.add(editStudentBtn);
        buttonRow.add(deleteStudentBtn);
        buttonRow.add(newStudentBtn);
        row1.add(header);
        row1.add(Box.createHorizontalGlue());
        row1.add(buttonRow);

        chart = new StudentChart(x/10*9,y/5*4, this);

        frames[0].add(row1);
        frames[0].add(chart);

        this.add(frames[0], BorderLayout.CENTER);
    }

    // New student display
    private void addFrame2(){
        frames[1] = new CustomPanel();
        frames[1].setLayout(new BoxLayout(frames[1], BoxLayout.PAGE_AXIS));
        // Initialize pane
        JPanel initPane = new JPanel();
        initPane.setBackground(Color.WHITE);
        initPane.setLayout(new BoxLayout(initPane, BoxLayout.PAGE_AXIS));
            CustomPanel row1 = new CustomPanel();
            CustomPanel namePanel = new CustomPanel();
            namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.PAGE_AXIS));
                DynamicLabel nameLabel = new DynamicLabel("Full Name", x, y/30, Color.BLACK);
                nameField = new JTextField(15);
                nameField.setPreferredSize(new Dimension(x, y/20));
            namePanel.add(nameLabel);
            namePanel.add(nameField);

            CustomPanel numberPanel = new CustomPanel();
            numberPanel.setLayout(new BoxLayout(numberPanel, BoxLayout.PAGE_AXIS));
                DynamicLabel numLabel = new DynamicLabel("Student Number", x, y/30, Color.BLACK);
                numField = new JTextField(15);
                numField.setPreferredSize(new Dimension(x,y/20));
                numField.addKeyListener(new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        if (numField.getText().length() >= 9 ) // limit textfield to 3 characters
                            e.consume();
                    }
                });
            namePanel.add(numLabel);
            namePanel.add(numField);
            row1.add(namePanel);
            row1.add(numberPanel);

            CustomPanel row2 = new CustomPanel();
            dietSelector = new DietSelector(x/4, y/3);
            likesSelector = new FriendSelector(x/4, y/3, this);
            row2.add(dietSelector);
            row2.add(likesSelector);
        // Error Jlabel
        CustomPanel row4 = new CustomPanel();
        DynamicLabel errorLabel = new DynamicLabel("Placeholder text. I hope Mr.Mangat gives me a good mark.", x/2, y/20, Color.RED);
        row4.add(errorLabel);
        errorLabel.setText("");
        initPane.add(row1);
        initPane.add(row2);
        initPane.add(row4);

        CustomPanel row5 = new CustomPanel();
        // Navigation buttons
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                nameField.setText("");
                numField.setText("");
                dietSelector.clear();
                likesSelector.clear();
                errorLabel.setText("");
                showFrame(0);
            }
        });
        //Creates a student
        JButton saveBtn = new JButton("Next");
        saveBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String name = nameField.getText();
                String studentNumber = numField.getText();
                ArrayList<String> dietaryRestrictions = dietSelector.getDiet();
                ArrayList<String> friends = likesSelector.getFriends();

                errorLabel.setText("");
                boolean inputVerified = true;
                if(name.isEmpty()){
                    errorLabel.setText(errorLabel.getText() + " Name field is empty.");
                    inputVerified = false;
                }
                if(!name.contains(" ")){
                    errorLabel.setText(errorLabel.getText() + " Last name is required.");
                    inputVerified = false;
                }
                if(name.matches(".*\\d+.*")){
                    errorLabel.setText(errorLabel.getText() + " Numbers are in name.");
                    inputVerified = false;
                }
                if(studentNumber.isEmpty()){
                    errorLabel.setText(errorLabel.getText() + " Student number field empty.");
                    inputVerified = false;
                } else if(studentNumber.length() < 9 || !studentNumber.matches("[0-9]+")){
                    errorLabel.setText(errorLabel.getText() + " Improper student number.");
                    inputVerified = false;
                }
                if(inputVerified){
                    Student newStudent = new Student(name, studentNumber, dietaryRestrictions, friends);
                    if(editingMode){
                        System.out.println("ignore");
                        int index = students.indexOf(chart.getStudent(students));
                        students.set(index, newStudent);
                        chart.updateStudent(newStudent);
                        //System.out.println("saved edited student");
                    } else {
                        students.add(newStudent);
                        chart.loadStudents(students);
                        dashboard.updateDashboard(students);
                    }

                    nameField.setText("");
                    numField.setText("");
                    dietSelector.clear();
                    likesSelector.clear();
                    errorLabel.setText("");
                    showFrame(0);
                }
            }
        });
        row5.add(cancelBtn);
        row5.add(saveBtn);
        frames[1].add(initPane);
        frames[1].add(row5);

        this.add(frames[1], BorderLayout.CENTER);
    }

    public void setEditorMode(int x){
        likesSelector.loadOptions(students);
        // New Student
        if(x == 0){
            editingMode = false;
            numField.setEditable(true);
            super.changeHeader("New Student", "Create a new student.");
        } else { // Edit student
            //TODO: Uncheck all the add friend options when they are loaded
            editingMode = true;
            super.changeHeader("Edit Student", "Edit an existing student.");
            System.out.println(students);
            Student selectedStudent = chart.getStudent(students);
            //System.out.println(selectedStudent);
            numField.setText(selectedStudent.getStudentNumber());
            numField.setEditable(false);
            nameField.setText(selectedStudent.getName());
            dietSelector.setDiet(selectedStudent.getDietaryRestrictions());
            likesSelector.setLikes(selectedStudent.getFriendStudentNumbers());
            //System.out.println("editing panel styled");
        }
    }

    private void showFrame(int x) {
        for (int i = 0; i < frames.length; i++) {
            frames[i].setVisible(false);
        }

        if(x == 0){
            super.changeHeader("Student Manager", "Add and modify students here.");
        }
        frames[x].setVisible(true);
    }
}
