package projectSCD;

import java.awt.*;
import javax.swing.*;
import java.text.*;
import java.util.*;
import java.util.List;

public class toDo {

    private JFrame frame;
    private JTextField taskField;
    private JTextField dateField;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private List<Task> tasks;
    private JComboBox<String> sortSelector;

    private static final SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                toDo window = new toDo();
                window.frame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Something went wrong: " + e.getMessage());
            }
        });
    }

    public toDo() {
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
        dateField.setToolTipText("Enter due date (e.g., 24 June)");
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

        String[] sortOptions = { "Order Added", "Due Date Left" };
        sortSelector = new JComboBox<>(sortOptions);
        sortSelector.setBounds(440, 280, 120, 30);
        frame.add(sortSelector);
        sortSelector.addActionListener(e -> updateTaskList());

        // Add Task Action
        addButton.addActionListener(e -> {
            String taskTitle = taskField.getText().trim();
            String dateStr = dateField.getText().trim();

            if (taskTitle.isEmpty() || dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill both task and date.");
                return;
            }

            Date dueDate;
            try {
                String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                String fullDateStr = dateStr + " " + currentYear;
                dueDate = inputDateFormat.parse(fullDateStr);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date format. Use format like '24 June'.");
                return;
            }

            Task newTask = new Task(taskTitle, dueDate);
            if (tasks.contains(newTask)) {
                JOptionPane.showMessageDialog(frame, "Task already exists.");
                return;
            }

            tasks.add(newTask);
            updateTaskList();

            taskField.setText("");
            dateField.setText("");
        });

        // Complete Task Action
        completeButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index >= 0) {
                Task selectedTask = getDisplayedTasks().get(index);
                if (!selectedTask.title.startsWith("[Done] ")) {
                    selectedTask.title = "[Done] " + selectedTask.title;
                    updateTaskList();
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Select a task to mark as done.");
            }
        });

        // Delete Task Action
        deleteButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index >= 0) {
                Task selectedTask = getDisplayedTasks().get(index);
                tasks.remove(selectedTask);
                updateTaskList();
            } else {
                JOptionPane.showMessageDialog(frame, "Select a task to delete.");
            }
        });

        // Replay / Exit
        replayButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(frame,
                    "Do you want to restart or exit?", "Replay/Exit",
                    JOptionPane.YES_NO_OPTION);
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

    // Sort and display tasks
    private List<Task> getDisplayedTasks() {
        List<Task> displayList = new ArrayList<>(tasks);
        String selectedSort = (String) sortSelector.getSelectedItem();

        if ("Due Date Left".equals(selectedSort)) {
            displayList.sort((t1, t2) -> {
                boolean t1Done = t1.title.startsWith("[Done] ");
                boolean t2Done = t2.title.startsWith("[Done] ");
                if (t1Done && !t2Done) return 1;
                if (!t1Done && t2Done) return -1;
                return t1.dueDate.compareTo(t2.dueDate);
            });
        }

        return displayList;
    }

    private void updateTaskList() {
        taskListModel.clear();
        for (Task task : getDisplayedTasks()) {
            taskListModel.addElement(task.toString());
        }
    }

    // Inner class for Task
    private static class Task {
        String title;
        Date dueDate;
        private static final SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

        Task(String title, Date dueDate) {
            this.title = title;
            this.dueDate = dueDate;
        }

        @Override
        public String toString() {
            long diff = dueDate.getTime() - new Date().getTime();
            long daysLeft = diff / (1000L * 60 * 60 * 24);
            String rem = daysLeft >= 0 ? daysLeft + " days left" : "Overdue";
            return title + " (Due: " + format.format(dueDate) + ", " + rem + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Task)) return false;
            Task o = (Task) obj;
            return title.equalsIgnoreCase(o.title) && dueDate.equals(o.dueDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title.toLowerCase(), dueDate);
        }
    }
}
