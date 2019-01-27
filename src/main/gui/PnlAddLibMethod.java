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
import javax.swing.border.LineBorder;
import java.awt.Color;

public class PnlAddLibMethod extends JPanel {
	private JTextField tfMethodName;
	private JTextField tfReturnType;
	private JTextField tfClass;
	private JTextField tfPackage;
	private JTextField tfLeftCriticalOutput;
	private JTextField tfArgumentType;
	private JTextField tfLeft;
	private JTextField tfExpression;

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
		
		JCheckBox chckbxIsStatic = new JCheckBox("is static");
		chckbxIsStatic.setBounds(332, 41, 97, 23);
		add(chckbxIsStatic);
		
		JCheckBox chckbxIsConstructor = new JCheckBox("is constructor");
		chckbxIsConstructor.setBounds(332, 16, 113, 23);
		add(chckbxIsConstructor);
		
		JLabel lblMethodFunctionality = new JLabel("Method functionalities");
		lblMethodFunctionality.setBounds(20, 165, 127, 14);
		add(lblMethodFunctionality);
		
		JComboBox cbSpecialActionType = new JComboBox();
		cbSpecialActionType.setBounds(64, 188, 166, 20);
		add(cbSpecialActionType);
		
		JLabel lblType = new JLabel("Type");
		lblType.setBounds(30, 190, 77, 14);
		add(lblType);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(0, 210, 871, 44);
		add(panel_3);
		panel_3.setLayout(null);
		
		tfLeft = new JTextField();
		tfLeft.setColumns(10);
		tfLeft.setBounds(30, 11, 156, 20);
		panel_3.add(tfLeft);
		
		JLabel lblAssignOp = new JLabel(":=");
		lblAssignOp.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAssignOp.setBounds(196, 14, 40, 14);
		panel_3.add(lblAssignOp);
		
		tfExpression = new JTextField();
		tfExpression.setColumns(10);
		tfExpression.setBounds(219, 11, 540, 20);
		panel_3.add(tfExpression);
		
		JButton btnAddAssignmentAction = new JButton("Add");
		btnAddAssignmentAction.setBounds(772, 10, 89, 23);
		panel_3.add(btnAddAssignmentAction);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(0, 210, 385, 43);
		add(panel_1);
		
		tfLeftCriticalOutput = new JTextField();
		tfLeftCriticalOutput.setColumns(10);
		tfLeftCriticalOutput.setBounds(30, 11, 156, 20);
		panel_1.add(tfLeftCriticalOutput);
		
		JLabel lblIsCriticalOutput = new JLabel("is critical output");
		lblIsCriticalOutput.setBounds(196, 14, 120, 14);
		panel_1.add(lblIsCriticalOutput);
		
		JButton btnAddCriticalOutput = new JButton("Add");
		btnAddCriticalOutput.setBounds(282, 10, 89, 23);
		panel_1.add(btnAddCriticalOutput);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(-1, 254, 910, 2);
		add(separator_1);
		
		JButton btnAddMethod = new JButton("Finish");
		btnAddMethod.setBounds(806, 16, 89, 23);
		add(btnAddMethod);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(0, 254, 910, 335);
		add(textArea);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_2.setBounds(332, 70, 391, 64);
		add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblArguments = new JLabel("Arguments");
		lblArguments.setBounds(10, 10, 127, 14);
		panel_2.add(lblArguments);
		
		JLabel lblType_1 = new JLabel("Type");
		lblType_1.setBounds(20, 33, 52, 14);
		panel_2.add(lblType_1);
		
		tfArgumentType = new JTextField();
		tfArgumentType.setColumns(10);
		tfArgumentType.setBounds(56, 31, 226, 20);
		panel_2.add(tfArgumentType);
		
		JButton btnAddArgument = new JButton("Add");
		btnAddArgument.setBounds(292, 29, 89, 23);
		panel_2.add(btnAddArgument);

	}

}
