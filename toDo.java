package projectSCD;

import java.awt.*;
import javax.swing.*;
import java.text.*;
import java.util.*;

public class toDo {

    private JFrame frame;
    private JTextField taskField;
    private JTextField dateField;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private ArrayList<Task> tasks;
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
        frame.setBounds(100, 100, 600, 430);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setLayout(null);

        JLabel title = new JLabel("Add Task & Due Date");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(30, 20, 300, 25);
        frame.add(title);

        taskField = new JTextField();
        taskField.setBounds(30, 60, 200, 30);
        taskField.setToolTipText("Enter task name");
        frame.add(taskField);

        dateField = new JTextField();
        dateField.setBounds(240, 60, 120, 30);
        dateField.setToolTipText("Enter due date like: 24 June");
        frame.add(dateField);

        JButton addButton = new JButton("Add Task");
        addButton.setBounds(380, 60, 120, 30);
        frame.add(addButton);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBounds(30, 110, 530, 150);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        frame.add(scrollPane);

        JButton completeButton = new JButton("Mark as Done");
        completeButton.setBounds(30, 280, 130, 30);
        frame.add(completeButton);

        JButton deleteButton = new JButton("Delete Task");
        deleteButton.setBounds(170, 280, 130, 30);
        frame.add(deleteButton);

        JButton replayButton = new JButton("Replay / Exit");
        replayButton.setBounds(310, 280, 130, 30);
        frame.add(replayButton);

        String[] sortOptions = { "Order Added", "Due Date Left" };
        sortSelector = new JComboBox<>(sortOptions);
        sortSelector.setBounds(450, 280, 110, 30);
        frame.add(sortSelector);

        sortSelector.addActionListener(e -> {
            updateTaskList();
        });

        addButton.addActionListener(e -> {
            String taskTitle = taskField.getText().trim();
            String dateStr = dateField.getText().trim();

            if (taskTitle.isEmpty() || dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill both fields.");
                return;
            }

            Date dueDate;

            try {
                String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                String fullDateStr = dateStr + " " + currentYear;
                dueDate = inputDateFormat.parse(fullDateStr);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(frame, "Use date format like '24 June'");
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

        completeButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();

            if (index >= 0) {
                Task selected = getDisplayedTasks().get(index);

                if (!selected.title.startsWith("[Done] ")) {
                    selected.title = "[Done] " + selected.title;
                    updateTaskList();
                } else {
                    JOptionPane.showMessageDialog(frame, "Task already marked as done.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Select a task to mark as done.");
            }
        });

        deleteButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();

            if (index >= 0) {
                Task selected = getDisplayedTasks().get(index);
                tasks.remove(selected);
                updateTaskList();
            } else {
                JOptionPane.showMessageDialog(frame, "Select a task to delete.");
            }
        });

        replayButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(frame, "Do you want to restart or exit?", "Replay/Exit", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                tasks.clear();
                taskListModel.clear();
                taskField.setText("");
                dateField.setText("");
            } else {
                frame.dispose();
            }
        });
    }

    private List<Task> getDisplayedTasks() {
        List<Task> displayList = new ArrayList<>(tasks);
        String selectedSort = (String) sortSelector.getSelectedItem();

        if (selectedSort.equals("Due Date Left")) {
            displayList.sort((t1, t2) -> {
                boolean t1Done = t1.title.startsWith("[Done] ");
                boolean t2Done = t2.title.startsWith("[Done] ");

                if (t1Done && !t2Done) {
                    return 1;
                }

                if (!t1Done && t2Done) {
                    return -1;
                }

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

            String rem;

            if (daysLeft >= 0) {
                rem = daysLeft + " days left";
            } else {
                rem = "Overdue";
            }

            return title + " (Due: " + format.format(dueDate) + ", " + rem + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Task)) {
                return false;
            }

            Task o = (Task) obj;
            return title.equalsIgnoreCase(o.title) && dueDate.equals(o.dueDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title.toLowerCase(), dueDate);
        }
    }
}
