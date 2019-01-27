package main.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;

public class PnlAddLibClass extends JPanel {
	private JTextField tfNewClassPackage;
	private JTextField tfNewClassName;
	private JTextField tfSuperClassPackage;
	private JTextField tfSuperClassName;
	private JTextField tfFieldType;
	private JTextField tfFieldName;

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
		
		tfNewClassPackage = new JTextField();
		tfNewClassPackage.setBounds(108, 43, 195, 20);
		add(tfNewClassPackage);
		tfNewClassPackage.setColumns(10);
		
		tfNewClassName = new JTextField();
		tfNewClassName.setColumns(10);
		tfNewClassName.setBounds(108, 70, 195, 20);
		add(tfNewClassName);
		
		tfSuperClassPackage = new JTextField();
		tfSuperClassPackage.setColumns(10);
		tfSuperClassPackage.setBounds(108, 117, 195, 20);
		add(tfSuperClassPackage);
		
		tfSuperClassName = new JTextField();
		tfSuperClassName.setColumns(10);
		tfSuperClassName.setBounds(108, 144, 195, 20);
		add(tfSuperClassName);
		
		JLabel lblFields = new JLabel("Fields");
		lblFields.setBounds(20, 189, 64, 14);
		add(lblFields);
		
		JLabel lblType = new JLabel("Type");
		lblType.setBounds(30, 214, 64, 14);
		add(lblType);
		
		tfFieldType = new JTextField();
		tfFieldType.setColumns(10);
		tfFieldType.setBounds(108, 214, 195, 20);
		add(tfFieldType);
		
		tfFieldName = new JTextField();
		tfFieldName.setColumns(10);
		tfFieldName.setBounds(108, 241, 195, 20);
		add(tfFieldName);
		
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
