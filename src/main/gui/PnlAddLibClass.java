package main.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;

public class PnlAddLibClass extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;

	/**
	 * Create the panel.
	 */
	public PnlAddLibClass() {
		setLayout(null);
		
		JLabel lblBaba = new JLabel("New Class");
		lblBaba.setBounds(20, 20, 64, 14);
		add(lblBaba);
		
		JLabel lblPackage = new JLabel("Package");
		lblPackage.setBounds(30, 45, 64, 14);
		add(lblPackage);
		
		JLabel lblClassName = new JLabel("Class name");
		lblClassName.setBounds(30, 70, 75, 14);
		add(lblClassName);
		
		JLabel lblSuperClass = new JLabel("Super Class");
		lblSuperClass.setBounds(20, 95, 75, 14);
		add(lblSuperClass);
		
		JLabel label_2 = new JLabel("Package");
		label_2.setBounds(30, 120, 64, 14);
		add(label_2);
		
		JLabel lblClassName_1 = new JLabel("Class name");
		lblClassName_1.setBounds(30, 145, 75, 14);
		add(lblClassName_1);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 176, 910, 2);
		add(separator);
		
		textField = new JTextField();
		textField.setBounds(108, 43, 195, 20);
		add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(108, 70, 195, 20);
		add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(108, 117, 195, 20);
		add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(108, 144, 195, 20);
		add(textField_3);
		
		JLabel lblFields = new JLabel("Fields");
		lblFields.setBounds(20, 189, 64, 14);
		add(lblFields);
		
		JLabel lblType = new JLabel("Type");
		lblType.setBounds(30, 214, 64, 14);
		add(lblType);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(108, 214, 195, 20);
		add(textField_4);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(108, 241, 195, 20);
		add(textField_5);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(30, 244, 64, 14);
		add(lblName);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 272, 910, 2);
		add(separator_1);
		
		JButton btnAddClass = new JButton("Finish");
		btnAddClass.setBounds(798, 16, 102, 23);
		add(btnAddClass);
		
		JButton btnAddField = new JButton("Add field");
		btnAddField.setBounds(798, 240, 102, 23);
		add(btnAddField);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(0, 274, 910, 315);
		add(textArea);
		
		JCheckBox chckbxIsAlwaysUnsafe = new JCheckBox("is always unsafe");
		chckbxIsAlwaysUnsafe.setBounds(108, 13, 195, 23);
		add(chckbxIsAlwaysUnsafe);

	}
}
