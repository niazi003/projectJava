package projectSCD;

import java.awt.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
public class toDo {
    private JFrame frame;
    private JTextField taskInput;
    private JTextField dateInput;
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private JComboBox<String> sortBox;
    private ArrayList<Task> allTasks;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
    private static final String PLACEHOLDER_TASK = "Enter task name";
    private static final String PLACEHOLDER_DATE = "e.g., 24 June";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                toDo window = new toDo();
                window.frame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        });
    }
    public toDo() {
        initialize();
    }
    private void initialize() {
        allTasks = new ArrayList<>();

        frame = new JFrame("Task Reminder App");
        frame.setBounds(100, 100, 600, 430);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(SystemColor.inactiveCaptionBorder);

        JLabel heading = new JLabel("Add Task and Due Date");
        heading.setFont(new Font("Arial", Font.BOLD, 18));
        heading.setBounds(30, 20, 300, 25);
        frame.getContentPane().add(heading);

        taskInput = new JTextField();
        taskInput.setBounds(30, 60, 200, 30);
        addPlaceholderBehavior(taskInput, PLACEHOLDER_TASK);
        frame.getContentPane().add(taskInput);

        dateInput = new JTextField();
        dateInput.setBounds(240, 60, 120, 30);
        addPlaceholderBehavior(dateInput, PLACEHOLDER_DATE);
        frame.getContentPane().add(dateInput);

        JButton addButton = new JButton("Add Task");
        addButton.setBackground(SystemColor.inactiveCaption);
        addButton.setBounds(380, 60, 120, 30);
        frame.getContentPane().add(addButton);

        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        JScrollPane scroll = new JScrollPane(taskList);
        scroll.setBounds(30, 110, 530, 150);
        scroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        frame.getContentPane().add(scroll);

        JButton doneButton = new JButton("Mark Done");
        doneButton.setBackground(SystemColor.menu);
        doneButton.setBounds(30, 280, 130, 30);
        frame.getContentPane().add(doneButton);

        JButton deleteButton = new JButton("Delete Task");
        deleteButton.setBackground(SystemColor.menu);
        deleteButton.setBounds(170, 280, 130, 30);
        frame.getContentPane().add(deleteButton);

        JButton resetButton = new JButton("Replay / Exit");
        resetButton.setBackground(new Color(255, 153, 102));
        resetButton.setBounds(310, 280, 130, 30);
        frame.getContentPane().add(resetButton);

        sortBox = new JComboBox<>(new String[] { "Order Added", "Due Date Left" });
        sortBox.setBackground(SystemColor.menu);
        sortBox.setBounds(450, 280, 110, 30);
        frame.getContentPane().add(sortBox);

        addButton.addActionListener(e -> addTask());
        doneButton.addActionListener(e -> markTaskDone());
        deleteButton.addActionListener(e -> deleteTask());
        resetButton.addActionListener(e -> replayOrExit());
        sortBox.addActionListener(e -> updateTaskList());
    }
    private void addTask() {
        String title = taskInput.getText().trim();
        String dateStr = dateInput.getText().trim();
        if (!isInputValid(title, dateStr)) {
            JOptionPane.showMessageDialog(frame, "Please fill both fields.");
            return;
        }
        Date dueDate;
        try {
            dueDate = parseDueDate(dateStr);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(frame, "Date should be like: 24 June");
            return;
        }
        Task newTask = new Task(title, dueDate);

        if (allTasks.contains(newTask)) {
            JOptionPane.showMessageDialog(frame, "Task already exists.");
        } else {
            allTasks.add(newTask);
            updateTaskList();
            resetInputFields();
        }
    }
    private boolean isInputValid(String title, String dateStr) {
        return !(title.isEmpty() || title.equals(PLACEHOLDER_TASK) || dateStr.isEmpty() || dateStr.equals(PLACEHOLDER_DATE));
    }
    private Date parseDueDate(String dateStr) throws ParseException {
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        return formatter.parse(dateStr + " " + year);
    }
    private void resetInputFields() {
        taskInput.setText("Enter task name");
        taskInput.setForeground(Color.GRAY);
        dateInput.setText("e.g., 24 June");
        dateInput.setForeground(Color.GRAY);
    }
    private void addPlaceholderBehavior(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }
    private void markTaskDone() {
        int index = taskList.getSelectedIndex();
        if (index >= 0) {
            Task selected = getSortedTasks().get(index);
            if (!selected.title.startsWith("[Done] ")) {
                selected.title = "[Done] " + selected.title;
                updateTaskList();
            } else {
                JOptionPane.showMessageDialog(frame, "Task already marked as done.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a task.");
        }
    }
    private void deleteTask() {
        int index = taskList.getSelectedIndex();
        if (index >= 0) {
            Task selected = getSortedTasks().get(index);
            allTasks.remove(selected);
            updateTaskList();
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a task.");
        }
    }
    private void replayOrExit() {
        int choice = JOptionPane.showConfirmDialog(frame, "Restart the app?", "Replay / Exit", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            resetInputFields();
            listModel.clear();
            allTasks.clear();
        } else {
            frame.dispose();
        }
    }
    private void updateTaskList() {
        listModel.clear();
        for (Task t : getSortedTasks()) {
            listModel.addElement(t.toString());
        }
    }
    private ArrayList<Task> getSortedTasks() {
        ArrayList<Task> sorted = new ArrayList<>(allTasks);
        if ("Due Date Left".equals(sortBox.getSelectedItem())) {
            sorted.sort(DUE_DATE_COMPARATOR);
        }
        return sorted;
    }
    private static final Comparator<Task> DUE_DATE_COMPARATOR = (a, b) -> {
        boolean aDone = a.title.startsWith("[Done] ");
        boolean bDone = b.title.startsWith("[Done] ");
        if (aDone && !bDone) return 1;
        if (!aDone && bDone) return -1;
        return a.dueDate.compareTo(b.dueDate);
    };
    private static class Task {
        String title;
        Date dueDate;
        Task(String title, Date dueDate) {
            this.title = title;
            this.dueDate = dueDate;
        }
        public String toString() {
            long diff = dueDate.getTime() - new Date().getTime();
            long days = diff / (1000L * 60 * 60 * 24);
            String status = (days >= 0) ? days + " days left" : "Overdue";
            return title + " (Due: " + formatter.format(dueDate) + ", " + status + ")";
        }
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Task)) return false;
            Task t = (Task) obj;
            return title.equalsIgnoreCase(t.title) && dueDate.equals(t.dueDate);
        }
        public int hashCode() {
            return Objects.hash(title.toLowerCase(), dueDate);
        }
    }
}
