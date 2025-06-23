package projectCalculator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class calculator {

    private JFrame frame;
    private JTextField taskField;
    private JTextField dateField;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private ArrayList<String> tasks; // raw data for internal handling

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                calculator window = new calculator();
                window.frame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Something went wrong: " + e.getMessage());
            }
        });
    }

    public calculator() {
        initialize();
    }

    private void initialize() {
        tasks = new ArrayList<>();
        frame = new JFrame("Task Reminder App");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setLayout(null);

        JLabel title = new JLabel("Add New Task");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBounds(30, 20, 200, 30);
        frame.add(title);

        taskField = new JTextField();
        taskField.setBounds(30, 60, 200, 30);
        taskField.setToolTipText("Enter task title");
        frame.add(taskField);

        dateField = new JTextField();
        dateField.setBounds(240, 60, 120, 30);
        dateField.setToolTipText("Enter due date (e.g., 21 June)");
        frame.add(dateField);

        JButton addButton = new JButton("Add Task");
        addButton.setBounds(380, 60, 100, 30);
        frame.add(addButton);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBounds(30, 110, 450, 160);
        frame.add(scrollPane);

        JButton completeButton = new JButton("Mark as Done");
        completeButton.setBounds(30, 280, 130, 30);
        frame.add(completeButton);

        JButton deleteButton = new JButton("Delete Task");
        deleteButton.setBounds(170, 280, 120, 30);
        frame.add(deleteButton);

        JButton replayButton = new JButton("Replay/Exit");
        replayButton.setBounds(300, 280, 130, 30);
        frame.add(replayButton);

        // Add Task Button Action
        addButton.addActionListener(e -> {
            String task = taskField.getText().trim();
            String date = dateField.getText().trim();
            if (task.isEmpty() || date.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill both task and date.");
            } else {
                String fullTask = task + " (Due: " + date + ")";
                tasks.add(fullTask);
                taskListModel.addElement(fullTask);
                taskField.setText("");
                dateField.setText("");
            }
        });

        // Mark as Done Button Action
        completeButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index >= 0) {
                String doneTask = "[Done] " + taskListModel.get(index);
                taskListModel.set(index, doneTask);
            } else {
                JOptionPane.showMessageDialog(frame, "Select a task to mark as done.");
            }
        });

        // Delete Task Button Action
        deleteButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index >= 0) {
                taskListModel.remove(index);
                tasks.remove(index);
            } else {
                JOptionPane.showMessageDialog(frame, "Select a task to delete.");
            }
        });

        // Replay Option
        replayButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(frame, "Do you want to restart or exit?", "Replay/Exit", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                taskListModel.clear();
                tasks.clear();
                taskField.setText("");
                dateField.setText("");
            } else {
                frame.dispose();
            }
        });
    }
}
