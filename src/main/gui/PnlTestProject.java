package main.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import main.Main;

import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;

public class PnlTestProject extends JPanel {
	private JTextField tfProjectPath;
	
	private JRadioButton rdbtnSafe;
	private JRadioButton rdbtnUnsafe;
	
	private JComboBox cbStartFrom;
	
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
		
		cbStartFrom = new JComboBox();
		cbStartFrom.setModel(new DefaultComboBoxModel(new String[] {"risky", "main", "all"}));
		cbStartFrom.setSelectedIndex(0);
		cbStartFrom.setBounds(141, 44, 69, 20);
		add(cbStartFrom);
		
		JLabel lblMethods = new JLabel("method(s)");
		lblMethods.setBounds(220, 47, 93, 14);
		add(lblMethods);
		
		JLabel lblStartMethodArguments = new JLabel("Start method arguments safety:");
		lblStartMethodArguments.setBounds(20, 74, 190, 14);
		add(lblStartMethodArguments);
		
		ButtonGroup bgInitialArgSafe = new ButtonGroup();
		
		rdbtnSafe = new JRadioButton("safe");
		rdbtnSafe.setBounds(216, 70, 55, 23);
		add(rdbtnSafe);
		bgInitialArgSafe.add(rdbtnSafe);
		
		rdbtnUnsafe = new JRadioButton("unsafe");
		rdbtnUnsafe.setBounds(268, 70, 69, 23);
		add(rdbtnUnsafe);
		bgInitialArgSafe.add(rdbtnUnsafe);

		rdbtnSafe.setSelected(true);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 105, 910, 2);
		add(separator);
		
		JTextArea textAreaOutput = new JTextArea();
		textAreaOutput.setBounds(0, 106, 910, 483);
		textAreaOutput.setMargin(new Insets(10, 10, 10, 10));
		add(textAreaOutput);
		
		JButton btnTestSqlInjection = new JButton("Test SQL Injection vulnerability");
		btnTestSqlInjection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File testsRoot = new File(tfProjectPath.getText().trim());
				if (!testsRoot.exists()) {
					textAreaOutput.setText("The project path doesn't exist!");
					return;
				}
				
				try {
					main.Main.testProjectForSQLInjection(testsRoot, ((String)cbStartFrom.getSelectedItem()).toLowerCase(), rdbtnSafe.isSelected());
					textAreaOutput.setText("This code isn't vulnerable to SQL injection.");			
				} catch (Exception ex) {
					if (ex instanceof main.exception.SQLInjection) {
						textAreaOutput.setText(ex.getMessage());
					} else {
						textAreaOutput.setText("An error occurred!\r\nThe test probably contains features that aren't supported in this version.");
						ex.printStackTrace();
					}
				}
			}
		});
		btnTestSqlInjection.setBounds(691, 70, 209, 23);
		add(btnTestSqlInjection);
	}
}
