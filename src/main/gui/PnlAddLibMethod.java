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
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;

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
		
		textField = new JTextField();
		textField.setBounds(115, 92, 166, 20);
		add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(115, 117, 166, 20);
		add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(115, 67, 166, 20);
		add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(115, 42, 166, 20);
		add(textField_3);
		
		JCheckBox chckbxIsStatic = new JCheckBox("is static");
		chckbxIsStatic.setBounds(332, 41, 97, 23);
		add(chckbxIsStatic);
		
		JCheckBox chckbxIsConstructor = new JCheckBox("is constructor");
		chckbxIsConstructor.setBounds(332, 16, 113, 23);
		add(chckbxIsConstructor);
		
		JLabel lblMethodFunctionality = new JLabel("Method functionalities");
		lblMethodFunctionality.setBounds(20, 165, 127, 14);
		add(lblMethodFunctionality);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(64, 188, 166, 20);
		add(comboBox);
		
		JLabel lblType = new JLabel("Type");
		lblType.setBounds(30, 190, 77, 14);
		add(lblType);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(0, 210, 871, 44);
		add(panel_3);
		panel_3.setLayout(null);
		
		textField_8 = new JTextField();
		textField_8.setColumns(10);
		textField_8.setBounds(30, 11, 156, 20);
		panel_3.add(textField_8);
		
		JLabel label_1 = new JLabel(":=");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_1.setBounds(196, 14, 40, 14);
		panel_3.add(label_1);
		
		textField_9 = new JTextField();
		textField_9.setColumns(10);
		textField_9.setBounds(219, 11, 540, 20);
		panel_3.add(textField_9);
		
		JButton button_1 = new JButton("Add");
		button_1.setBounds(772, 10, 89, 23);
		panel_3.add(button_1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(0, 210, 385, 43);
		add(panel_1);
		
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(30, 11, 156, 20);
		panel_1.add(textField_6);
		
		JLabel lblIsCriticalOutput = new JLabel("is critical output");
		lblIsCriticalOutput.setBounds(196, 14, 120, 14);
		panel_1.add(lblIsCriticalOutput);
		
		JButton button = new JButton("Add");
		button.setBounds(282, 10, 89, 23);
		panel_1.add(button);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(-1, 254, 910, 2);
		add(separator_1);
		
		JButton btnFinish = new JButton("Finish");
		btnFinish.setBounds(806, 16, 89, 23);
		add(btnFinish);
		
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
		
		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(56, 31, 226, 20);
		panel_2.add(textField_7);
		
		JButton btnAdd_1 = new JButton("Add");
		btnAdd_1.setBounds(292, 29, 89, 23);
		panel_2.add(btnAdd_1);

	}

}
