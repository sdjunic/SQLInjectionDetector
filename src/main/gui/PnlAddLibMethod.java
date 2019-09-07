package main.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.border.LineBorder;

import javaLibrary.SpecialAction;
import main.LibraryMethodDecl;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PnlAddLibMethod extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final String SA_CO = "Critical output";
	private final String SA_ASSIGN_EXISTING = "Assign existing object";
	private final String SA_ASSIGN_NEW = "Assign new object";
	private final String SA_SET_SAFETY = "Set safety value";
	
	private final String SPECIAL_ACTION_TYPE[] = { SA_CO, SA_ASSIGN_EXISTING, SA_ASSIGN_NEW, SA_SET_SAFETY };
	
	private LibraryMethodDecl currentMethod = null;
	
	private JLabel lblAssignOp;
	
	private JTextField tfMethodName;
	private JTextField tfReturnType;
	private JTextField tfClass;
	private JTextField tfPackage;
	private JTextField tfLeftCriticalOutput;
	private JTextField tfArgumentType;
	private JTextField tfLeft;
	private JTextField tfExpression;
	
	private JPanel pnlAddArguments;
	private JPanel pnlAddAssignmentAction;
	private JPanel pnlAddCriticalOutput;
	private JPanel pnlAddNewAssignmentAction;
	
	private JCheckBox chckbxIsStatic;
	private JCheckBox chckbxIsConstructor; 
	
	private JComboBox cbSpecialActionType;
	private JButton btnDiscard;
	
	private JTextArea textArea;
	private JTextField tfLeftAssignNewOp;
	private JTextField tfAssignNewRetType;
	private JTextField tfExpressionAssignNew;

	private boolean checkMethodDeclaration()
	{
		if (tfClass.getText().trim().isEmpty())
		{
			textArea.setText("The class of the new method can't be empty!");
			return false;
		}
		
		if (tfMethodName.getText().trim().isEmpty())
		{
			textArea.setText("The name of the new method can't be empty!");
			return false;
		}
		
		currentMethod = new LibraryMethodDecl(tfPackage.getText(), tfClass.getText(), tfMethodName.getText());
		currentMethod.isStatic = chckbxIsStatic.isSelected();
		currentMethod.isConstructor = chckbxIsConstructor.isSelected();
		
		String retType = tfReturnType.getText().trim();
		if (!(retType.isEmpty() || retType.equals("void")))
		{
			String retPackageName = null;
			String retTypeName = retType;
			
			if (retType.contains("."))
			{
				retPackageName = retType.substring(0, retType.lastIndexOf("."));
				retTypeName = retType.substring(retType.lastIndexOf(".") + 1);
			}
			currentMethod.retTypePackage = retPackageName;
			currentMethod.retTypeName = retTypeName;
		}
		
		textArea.setText(currentMethod.toString());
		
		return true;
	}
	
	private void enableMethodDeclarationFields(boolean enable)
	{
		tfPackage.setEnabled(enable);
		tfClass.setEnabled(enable);
		tfMethodName.setEnabled(enable);
		tfReturnType.setEnabled(enable);
		chckbxIsConstructor.setEnabled(enable);
		chckbxIsStatic.setEnabled(enable);
	}
	
	private void addArgument()
	{
		assert currentMethod != null;
		if (tfArgumentType.getText().trim().isEmpty())
		{
			textArea.setText("Argument type can't be empty!");
			return;
		}
		
		currentMethod.methodArgs.add(tfArgumentType.getText().trim());
		
		tfArgumentType.setText("");
		textArea.setText(currentMethod.toString());
	}
	
	private void addAssignmentAction()
	{
		assert currentMethod != null;
		if (tfLeft.getText().trim().isEmpty())
		{
			textArea.setText("Left side of operator " + lblAssignOp.getText() + " can't be empty!");
			return;
		}
		else if (tfExpression.getText().trim().isEmpty())
		{
			textArea.setText("Right side of operator " + lblAssignOp.getText() + " can't be empty!");
			return;
		}
		
		currentMethod.addSpecialAction(tfLeft.getText().trim(), lblAssignOp.getText(), tfExpression.getText().trim());
		
		tfLeft.setText("");
		tfExpression.setText("");
		textArea.setText(currentMethod.toString());
	}
	
	private void addNewAssignmentAction()
	{
		assert currentMethod != null;
		if (tfLeftAssignNewOp.getText().trim().isEmpty())
		{
			textArea.setText("Left side of operator :n()= can't be empty!");
			return;
		}
		else if (tfExpressionAssignNew.getText().trim().isEmpty())
		{
			textArea.setText("Right side of operator :n()= can't be empty!");
			return;
		}
		else if (tfAssignNewRetType.getText().trim().isEmpty())
		{
			textArea.setText("Return type for operator :n()= can't be empty!");
			return;
		}
		
		currentMethod.addSpecialAction(
				tfLeftAssignNewOp.getText().trim(), 
				SpecialAction.ASSIGN_NEW_OBJECT(tfAssignNewRetType.getText().trim()),
				tfExpressionAssignNew.getText().trim());
		
		tfLeftAssignNewOp.setText("");
		tfAssignNewRetType.setText("");
		tfExpressionAssignNew.setText("");
		textArea.setText(currentMethod.toString());
	}
	
	private void addCriticalOutput()
	{
		assert currentMethod != null;
		if (tfLeftCriticalOutput.getText().trim().isEmpty())
		{
			textArea.setText("Left side of 'is critical output' can't be empty!");
			return;
		}
		
		currentMethod.addSpecialAction(tfLeftCriticalOutput.getText().trim(), SpecialAction.CRITICAL_OUTPUT);
		
		tfLeftCriticalOutput.setText("");
		textArea.setText(currentMethod.toString());
	}
	
	void setPanelEnabled(JPanel panel, Boolean isEnabled) {
	    panel.setEnabled(isEnabled);

	    Component[] components = panel.getComponents();

	    for(int i = 0; i < components.length; i++) {
	        if(components[i].getClass().getName() == "javax.swing.JPanel") {
	            setPanelEnabled((JPanel) components[i], isEnabled);
	        }

	        components[i].setEnabled(isEnabled);
	    }
	}
	
	private void enableAddingArgumentsAndSpecialActions(boolean enable)
	{
		setPanelEnabled(pnlAddArguments, enable);
		setPanelEnabled(pnlAddCriticalOutput, enable);
		setPanelEnabled(pnlAddAssignmentAction, enable);
		setPanelEnabled(pnlAddNewAssignmentAction, enable);
		cbSpecialActionType.setEnabled(enable);
	}
	
	private void showSpecialActionPanel(JPanel pnl)
	{
		pnlAddCriticalOutput.setVisible(false);
		pnlAddAssignmentAction.setVisible(false);
		pnlAddNewAssignmentAction.setVisible(false);
		
		if (pnl != null)
		{
			pnl.setVisible(true);
		}
	}
	
	private void hideSpecialActionPanels()
	{
		showSpecialActionPanel((JPanel)null);
	}
	
	private void showSpecialActionPanel(String specialAction)
	{
		if (specialAction == null)
		{
			hideSpecialActionPanels();
		}
		else if (specialAction.equals(SA_CO))
		{
			showSpecialActionPanel(pnlAddCriticalOutput);
		}
		else if (specialAction.equals(SA_ASSIGN_EXISTING))
		{
			lblAssignOp.setText(SpecialAction.ASSIGN_EXISTING_OBJECT);
			showSpecialActionPanel(pnlAddAssignmentAction);
		}
		else if (specialAction.equals(SA_SET_SAFETY))
		{
			lblAssignOp.setText(SpecialAction.SET_SAFE);
			showSpecialActionPanel(pnlAddAssignmentAction);
		}
		else if (specialAction.equals(SA_ASSIGN_NEW))
		{
			showSpecialActionPanel(pnlAddNewAssignmentAction);
		}
		else
		{
			assert false;
		}
	}
	
	private void resetAllFields()
	{
		tfClass.setText("");
		tfPackage.setText("");
		tfMethodName.setText("");
		tfReturnType.setText("");
		tfLeftCriticalOutput.setText("");
		tfArgumentType.setText("");
		tfLeft.setText("");
		tfExpression.setText("");
		chckbxIsStatic.setSelected(false);
		chckbxIsConstructor.setSelected(false);
		textArea.setText("");
		cbSpecialActionType.setSelectedIndex(-1);
	}
	
	/**
	 * Create the panel.
	 */
	public PnlAddLibMethod() {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("New method\r\n");
		lblNewLabel.setBounds(20, 20, 77, 14);
		add(lblNewLabel);
		
		JLabel lblPackage = new JLabel("Package");
		lblPackage.setBounds(30, 45, 77, 14);
		add(lblPackage);
		
		JLabel lblClass = new JLabel("Class");
		lblClass.setBounds(30, 70, 77, 14);
		add(lblClass);
		
		JLabel lblMet = new JLabel("Method name");
		lblMet.setBounds(30, 95, 77, 14);
		add(lblMet);
		
		JLabel lblReturn = new JLabel("Return type");
		lblReturn.setBounds(30, 120, 77, 14);
		add(lblReturn);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 148, 910, 2);
		add(separator);
		
		tfMethodName = new JTextField();
		tfMethodName.setBounds(115, 92, 166, 20);
		add(tfMethodName);
		tfMethodName.setColumns(10);
		
		tfReturnType = new JTextField();
		tfReturnType.setColumns(10);
		tfReturnType.setBounds(115, 117, 166, 20);
		add(tfReturnType);
		
		tfClass = new JTextField();
		tfClass.setColumns(10);
		tfClass.setBounds(115, 67, 166, 20);
		add(tfClass);
		
		tfPackage = new JTextField();
		tfPackage.setColumns(10);
		tfPackage.setBounds(115, 42, 166, 20);
		add(tfPackage);
		
		chckbxIsStatic = new JCheckBox("is static");
		chckbxIsStatic.setBounds(332, 41, 97, 23);
		add(chckbxIsStatic);
		
		chckbxIsConstructor = new JCheckBox("is constructor");
		chckbxIsConstructor.setBounds(332, 16, 113, 23);
		add(chckbxIsConstructor);
		
		JLabel lblMethodFunctionality = new JLabel("Method functionalities");
		lblMethodFunctionality.setBounds(20, 165, 127, 14);
		add(lblMethodFunctionality);
		
		cbSpecialActionType = new JComboBox(SPECIAL_ACTION_TYPE);
		cbSpecialActionType.setSelectedIndex(-1);
		cbSpecialActionType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showSpecialActionPanel((String)cbSpecialActionType.getSelectedItem());
			}
		});
		cbSpecialActionType.setBounds(64, 188, 166, 20);
		add(cbSpecialActionType);
		
		JLabel lblType = new JLabel("Type");
		lblType.setBounds(30, 190, 77, 14);
		add(lblType);
		
		pnlAddAssignmentAction = new JPanel();
		pnlAddAssignmentAction.setBounds(0, 210, 871, 43);
		add(pnlAddAssignmentAction);
		pnlAddAssignmentAction.setLayout(null);
		
		tfLeft = new JTextField();
		tfLeft.setColumns(10);
		tfLeft.setBounds(30, 11, 156, 20);
		pnlAddAssignmentAction.add(tfLeft);
		
		lblAssignOp = new JLabel(":=");
		lblAssignOp.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAssignOp.setBounds(196, 13, 40, 14);
		pnlAddAssignmentAction.add(lblAssignOp);
		
		tfExpression = new JTextField();
		tfExpression.setColumns(10);
		tfExpression.setBounds(219, 11, 540, 20);
		pnlAddAssignmentAction.add(tfExpression);
		
		JButton btnAddAssignmentAction = new JButton("Add");
		btnAddAssignmentAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addAssignmentAction();
			}
		});
		btnAddAssignmentAction.setBounds(773, 10, 89, 23);
		pnlAddAssignmentAction.add(btnAddAssignmentAction);
		
		pnlAddCriticalOutput = new JPanel();
		pnlAddCriticalOutput.setLayout(null);
		pnlAddCriticalOutput.setBounds(0, 210, 400, 43);
		add(pnlAddCriticalOutput);
		
		tfLeftCriticalOutput = new JTextField();
		tfLeftCriticalOutput.setColumns(10);
		tfLeftCriticalOutput.setBounds(30, 11, 156, 20);
		pnlAddCriticalOutput.add(tfLeftCriticalOutput);
		
		JLabel lblIsCriticalOutput = new JLabel("is critical output");
		lblIsCriticalOutput.setBounds(196, 14, 120, 14);
		pnlAddCriticalOutput.add(lblIsCriticalOutput);
		
		JButton btnAddCriticalOutput = new JButton("Add");
		btnAddCriticalOutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addCriticalOutput();
			}
		});
		btnAddCriticalOutput.setBounds(301, 10, 89, 23);
		pnlAddCriticalOutput.add(btnAddCriticalOutput);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(-1, 254, 910, 2);
		add(separator_1);
		
		JButton btnAddMethod = new JButton("Continue");
		btnAddMethod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (btnAddMethod.getText().equals("Continue"))
				{
					if(checkMethodDeclaration())
					{
						assert currentMethod != null;
						btnAddMethod.setText("Save method");
						enableMethodDeclarationFields(false);
						enableAddingArgumentsAndSpecialActions(true);
						btnDiscard.setVisible(true);
					}
				}
				else
				{
					assert currentMethod != null;
					assert btnAddMethod.getText().equals("Save method");
					enableMethodDeclarationFields(true);
					enableAddingArgumentsAndSpecialActions(false);
					resetAllFields();
					btnAddMethod.setText("Continue");
					btnDiscard.setVisible(false);
					
					main.Main.libraryMethList.add(currentMethod);
					currentMethod = null;
				}
			}
		});
		btnAddMethod.setBounds(782, 16, 113, 23);
		add(btnAddMethod);
		
		btnDiscard = new JButton("Discard");
		btnDiscard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				assert btnAddMethod.getText().equals("Save method");
				enableMethodDeclarationFields(true);
				enableAddingArgumentsAndSpecialActions(false);
				resetAllFields();
				btnAddMethod.setText("Continue");
				btnDiscard.setVisible(false);
			}
		});
		btnDiscard.setBounds(782, 48, 113, 23);
		btnDiscard.setVisible(false);
		add(btnDiscard);
		
		textArea = new JTextArea();
		textArea.setBounds(0, 254, 910, 335);
		textArea.setMargin(new Insets(10,10,10,10));
		add(textArea);
		
		pnlAddArguments = new JPanel();
		pnlAddArguments.setBorder(new LineBorder(Color.LIGHT_GRAY));
		pnlAddArguments.setBounds(332, 70, 391, 64);
		add(pnlAddArguments);
		pnlAddArguments.setLayout(null);
		
		JLabel lblArguments = new JLabel("Arguments");
		lblArguments.setBounds(10, 10, 127, 14);
		pnlAddArguments.add(lblArguments);
		
		JLabel lblType_1 = new JLabel("Type");
		lblType_1.setBounds(20, 33, 52, 14);
		pnlAddArguments.add(lblType_1);
		
		tfArgumentType = new JTextField();
		tfArgumentType.setColumns(10);
		tfArgumentType.setBounds(56, 31, 226, 20);
		pnlAddArguments.add(tfArgumentType);
		
		JButton btnAddArgument = new JButton("Add");
		btnAddArgument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addArgument();
			}
		});
		btnAddArgument.setBounds(292, 29, 89, 23);
		pnlAddArguments.add(btnAddArgument);
		
		pnlAddNewAssignmentAction = new JPanel();
		pnlAddNewAssignmentAction.setBounds(0, 210, 871, 43);
		add(pnlAddNewAssignmentAction);
		pnlAddNewAssignmentAction.setLayout(null);
		
		tfLeftAssignNewOp = new JTextField();
		tfLeftAssignNewOp.setBounds(29, 11, 156, 20);
		tfLeftAssignNewOp.setColumns(10);
		pnlAddNewAssignmentAction.add(tfLeftAssignNewOp);
		
		JLabel lblnew = new JLabel(":n(");
		lblnew.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblnew.setBounds(194, 13, 61, 14);
		pnlAddNewAssignmentAction.add(lblnew);
		
		tfAssignNewRetType = new JTextField();
		tfAssignNewRetType.setColumns(10);
		tfAssignNewRetType.setBounds(214, 11, 124, 20);
		pnlAddNewAssignmentAction.add(tfAssignNewRetType);
		
		JLabel label = new JLabel(")=");
		label.setFont(new Font("Tahoma", Font.BOLD, 12));
		label.setBounds(341, 13, 25, 14);
		pnlAddNewAssignmentAction.add(label);
		
		tfExpressionAssignNew = new JTextField();
		tfExpressionAssignNew.setColumns(10);
		tfExpressionAssignNew.setBounds(366, 11, 393, 20);
		pnlAddNewAssignmentAction.add(tfExpressionAssignNew);
		
		JButton btnAddNewAssignmentAction = new JButton("Add");
		btnAddNewAssignmentAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addNewAssignmentAction();
			}
		});
		btnAddNewAssignmentAction.setBounds(773, 10, 89, 23);
		pnlAddNewAssignmentAction.add(btnAddNewAssignmentAction);

		enableAddingArgumentsAndSpecialActions(false);
		hideSpecialActionPanels();
	}
}
