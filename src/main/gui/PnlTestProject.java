package main.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PnlTestProject extends JPanel {
	private JTextField tfProjectPath;

	/**
	 * Create the panel.
	 */
	public PnlTestProject() {
		setLayout(null);
		
		JLabel lblProjectPath = new JLabel("Project path:");
		lblProjectPath.setBounds(20, 20, 75, 14);
		add(lblProjectPath);
		
		tfProjectPath = new JTextField();
		tfProjectPath.setBounds(105, 17, 311, 20);
		add(tfProjectPath);
		tfProjectPath.setColumns(10);
		
		JLabel lblStartAnalysisFrom = new JLabel("Start analysis from ");
		lblStartAnalysisFrom.setBounds(20, 47, 131, 14);
		add(lblStartAnalysisFrom);
		
		JComboBox cbStartFrom = new JComboBox();
		cbStartFrom.setBounds(141, 44, 69, 20);
		add(cbStartFrom);
		
		JLabel lblMethods = new JLabel("method(s)");
		lblMethods.setBounds(220, 47, 93, 14);
		add(lblMethods);
		
		JLabel lblStartMethodArguments = new JLabel("Start method arguments safety:");
		lblStartMethodArguments.setBounds(20, 74, 190, 14);
		add(lblStartMethodArguments);
		
		JRadioButton rdbtnSafe = new JRadioButton("safe");
		rdbtnSafe.setBounds(216, 70, 55, 23);
		add(rdbtnSafe);
		
		JRadioButton rdbtnUnsafe = new JRadioButton("unsafe");
		rdbtnUnsafe.setBounds(268, 70, 69, 23);
		add(rdbtnUnsafe);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 105, 910, 2);
		add(separator);
		
		JButton btnTestSqlInjection = new JButton("Test SQL Injection vulnerability");
		btnTestSqlInjection.setBounds(691, 70, 209, 23);
		add(btnTestSqlInjection);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(0, 106, 910, 483);
		add(textArea);

	}
}
