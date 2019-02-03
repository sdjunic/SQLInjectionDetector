package main.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import java_cup.assoc;
import main.LibraryClassDecl;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.Insets;
import java.awt.event.ActionEvent;

public class PnlAddLibClass extends JPanel {
	private LibraryClassDecl currentClass;
	
	private JTextField tfNewClassPackage;
	private JTextField tfNewClassName;
	private JTextField tfSuperClassPackage;
	private JTextField tfSuperClassName;
	private JTextField tfFieldType;
	private JTextField tfFieldName;
	
	private JCheckBox chckbxIsAlwaysUnsafe;
	
	private JButton btnDiscard;
	private JButton btnAddField;
	
	private JTextArea textArea;

	private boolean checkClassDeclaration()
	{
		if (tfNewClassName.getText().trim().isEmpty())
		{
			textArea.setText("The name of the new class can't be empty!");
			return false;
		}
		
		currentClass = new LibraryClassDecl(
				tfNewClassPackage.getText(),
				tfNewClassName.getText(),
				tfSuperClassPackage.getText(),
				tfSuperClassName.getText());
		
		currentClass.alwaysUnsafe = chckbxIsAlwaysUnsafe.isSelected();
		
		textArea.setText(currentClass.toString());
		
		return true;
	}
	
	private void enableClassDeclarationFields(boolean enable)
	{
		tfNewClassPackage.setEnabled(enable);
		tfNewClassName.setEnabled(enable);
		tfSuperClassPackage.setEnabled(enable);
		tfSuperClassName.setEnabled(enable);
		chckbxIsAlwaysUnsafe.setEnabled(enable);
	}
	
	private void enableAddFields(boolean enable)
	{
		tfFieldType.setEnabled(enable);
		tfFieldName.setEditable(enable);
		
		btnAddField.setEnabled(enable);
	}
	
	private void resetAllFields()
	{
		tfNewClassPackage.setText("");
		tfNewClassName.setText("");
		tfSuperClassPackage.setText("");
		tfSuperClassName.setText("");
		chckbxIsAlwaysUnsafe.setSelected(false);
		
		tfFieldName.setText("");
		tfFieldType.setText("");
	}
	
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
		
		JButton btnAddClass = new JButton("Continue");
		btnAddClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (btnAddClass.getText().trim().equals("Continue"))
				{
					if (checkClassDeclaration())
					{
						assert currentClass != null;
						enableClassDeclarationFields(false);
						enableAddFields(true);
						btnAddClass.setText("Save class");
						btnDiscard.setVisible(true);
					}
				}
				else
				{
					assert currentClass != null;
					assert btnAddClass.getText().trim().equals("Save class");
					btnAddClass.setText("Continue");
					enableClassDeclarationFields(true);
					enableAddFields(false);
					resetAllFields();
					btnDiscard.setVisible(false);
					
					main.Main.libraryClassList.add(currentClass);
					currentClass = null;
				}
			}
		});
		btnAddClass.setBounds(798, 16, 102, 23);
		add(btnAddClass);
		
		btnDiscard = new JButton("Discard");
		btnDiscard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				assert currentClass != null;
				assert btnAddClass.getText().trim().equals("Save class");
				btnAddClass.setText("Continue");
				enableClassDeclarationFields(true);
				enableAddFields(false);
				currentClass = null;
				resetAllFields();
				btnDiscard.setVisible(false);
			}
		});
		btnDiscard.setBounds(798, 45, 102, 23);
		btnDiscard.setVisible(false);
		add(btnDiscard);
		
		btnAddField = new JButton("Add field");
		btnAddField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				assert currentClass != null;
				if (tfFieldType.getText().trim().isEmpty())
				{
					textArea.setText("The type of the field can't be empty!");
					return;
				}
				if (tfFieldName.getText().trim().isEmpty())
				{
					
					textArea.setText("The name of the field can't be empty!");
					return;
				}
				
				String typePackage = null;
				String typeName = tfFieldType.getText().trim();
				
				if (typeName.contains("."))
				{
					typePackage = typeName.substring(0, typeName.lastIndexOf("."));
					typeName = typeName.substring(typeName.lastIndexOf(".") + 1);
				}
				
				currentClass.addField(typePackage, typeName, tfFieldName.getText().trim());
				
				textArea.setText(currentClass.toString());
			}
		});
		btnAddField.setBounds(798, 240, 102, 23);
		add(btnAddField);
		
		textArea = new JTextArea();
		textArea.setBounds(0, 274, 910, 315);
		textArea.setMargin(new Insets(10,10,10,10));
		add(textArea);
		
		chckbxIsAlwaysUnsafe = new JCheckBox("is always unsafe");
		chckbxIsAlwaysUnsafe.setBounds(108, 13, 195, 23);
		add(chckbxIsAlwaysUnsafe);

		enableAddFields(false);
	}
}
