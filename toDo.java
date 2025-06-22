package projectSCD;

import java.awt.*;
import javax.swing.*;

public class toDo {

	private JFrame frame;
	private JTextField taskField;
	private DefaultListModel<String> taskListModel;
	private JList<String> taskList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					toDo window = new toDo();
					window.frame.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public toDo() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Mini Task Reminder - SCD");
		frame.setBounds(100, 100, 400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);

		JLabel title = new JLabel("Add a Task");
		title.setFont(new Font("Segoe UI", Font.BOLD, 16));
		title.setBounds(30, 20, 200, 25);
		frame.add(title);

		taskField = new JTextField();
		taskField.setBounds(30, 60, 200, 30);
		frame.add(taskField);

		JButton addButton = new JButton("Add");
		addButton.setBounds(240, 60, 80, 30);
		frame.add(addButton);

		taskListModel = new DefaultListModel<>();
		taskList = new JList<>(taskListModel);
		JScrollPane scrollPane = new JScrollPane(taskList);
		scrollPane.setBounds(30, 110, 290, 120);
		frame.add(scrollPane);

		// Add task event
		addButton.addActionListener(e -> {
			String task = taskField.getText().trim();
			if (!task.isEmpty()) {
				taskListModel.addElement(task);
				taskField.setText("");
			} else {
				JOptionPane.showMessageDialog(frame, "Task can't be empty.");
			}
		});
	}
}
