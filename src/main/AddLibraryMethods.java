package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JRadioButton;

public class AddLibraryMethods extends JFrame {
	private LibraryMethodDecl method = new LibraryMethodDecl();
	
	private JPanel contentPane;
	private JTextField tbPackage;
	private JTextField tbClass;
	private JTextField tbMethod;
	private JTextField tbRetType;
	private JCheckBox chckbxIsStatic;

	private JLabel lblMethodsign;
	private JLabel lblError;
	private JFrame thisObj = this;
	private JTextField tbArgumentType;
	
	private JRadioButton rdbtnRetSafe;
	private JRadioButton rdbtnRetUnsafe;
	private JRadioButton rdbtnRetUnknown;
	
	private JRadioButton rdbtnThisSafe;
	private JRadioButton rdbtnThisUnsafe;
	private JRadioButton rdbtnThisUnknown;
	private JRadioButton rdbtnThisCritical;
	private JPanel panelThisObj;
	
	private JRadioButton rdbtnArgSafe;
	private JRadioButton rdbtnArgUnsafe;
	private JRadioButton rdbtnArgUnknown;
	private JRadioButton rdbtnArgCritical;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddLibraryMethods frame = new AddLibraryMethods(null, null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AddLibraryMethods(List<LibraryMethodDecl> libraryMethList, JFrame parent) {
		setResizable(false);
		setTitle("Add critical method");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				if (parent != null) {
					parent.setEnabled(true);
					parent.setVisible(true);
				}
			}
		});
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 379, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				thisObj.dispose();
			}
		});
		btnClose.setBounds(288, 420, 75, 23);
		contentPane.add(btnClose);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				lblError.setText("");
			}
		});
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String packName = tbPackage.getText().trim();
				if (packName.isEmpty()) {
					packName = "_defaultPackage";
				}
				method.packageName = packName;
				
				String className = tbClass.getText().trim();
				if (className.isEmpty()) {
					lblError.setText("Enter class name!");
					return;
				}
				method.className = className;
				
				String methName = tbMethod.getText().trim();
				if (methName.isEmpty()){
					lblError.setText("Enter method name!");
					return;
				}
				method.methodName = methName;
				
				String retType = tbRetType.getText().trim();
//				if (retType.isEmpty() || retType.toLowerCase().equals("void")){
//					retType = null;
//				} else {
//					if (rdbtnRetSafe.isSelected()) {
//						method.specialActions.add(new SpecialAction(SpecialArg.INDEX_RETURN_OBJ, SpecialArg.TYPE_SAFE_ARG));
//					} else if (rdbtnRetUnsafe.isSelected()) {
//						method.specialActions.add(new SpecialAction(SpecialArg.INDEX_RETURN_OBJ, SpecialArg.TYPE_UNSAFE_ARG));
//					}
//				}
//				method.retType = retType;
//				
//				method.isStatic = chckbxIsStatic.isSelected();
//				if (!method.isStatic) {
//					if (rdbtnThisCritical.isSelected()) {
//						method.specialActions.add(new SpecialAction(SpecialArg.INDEX_THIS_OBJ, SpecialArg.TYPE_CRITICAL_OUTPUT));
//					} else if (rdbtnThisSafe.isSelected()) {
//						method.specialActions.add(new SpecialAction(SpecialArg.INDEX_THIS_OBJ, SpecialArg.TYPE_SAFE_ARG));
//					} else if (rdbtnThisUnsafe.isSelected()) {
//						method.specialActions.add(new SpecialAction(SpecialArg.INDEX_THIS_OBJ, SpecialArg.TYPE_UNSAFE_ARG));
//					}
//				}
				
				if (libraryMethList != null) libraryMethList.add(method);
				
				thisObj.dispose();
			}
		});
		btnAdd.setBounds(203, 420, 75, 23);
		contentPane.add(btnAdd);
		
		lblMethodsign = new JLabel("void noName()");
		lblMethodsign.setBounds(10, 399, 381, 14);
		lblMethodsign.setEnabled(false);
		contentPane.add(lblMethodsign);
		
		lblError = new JLabel("");
		lblError.setBounds(10, 424, 197, 14);
		lblError.setEnabled(false);
		contentPane.add(lblError);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.GRAY));
		panel_1.setBounds(10, 11, 353, 255);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblPackage = new JLabel("Package");
		lblPackage.setBounds(10, 11, 77, 14);
		panel_1.add(lblPackage);
		
		tbPackage = new JTextField();
		tbPackage.setBounds(97, 8, 246, 20);
		panel_1.add(tbPackage);
		tbPackage.setColumns(10);
		
		JLabel lblClass = new JLabel("Class");
		lblClass.setBounds(10, 32, 77, 14);
		panel_1.add(lblClass);
		
		JLabel lblMethod = new JLabel("Method");
		lblMethod.setBounds(10, 57, 77, 14);
		panel_1.add(lblMethod);
		
		chckbxIsStatic = new JCheckBox("Is static");
		chckbxIsStatic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Component c : panelThisObj.getComponents()) {
					c.setEnabled(!chckbxIsStatic.isSelected());
				}
				rdbtnThisUnknown.setSelected(true);
			}
		});
		chckbxIsStatic.setBounds(10, 162, 81, 23);
		panel_1.add(chckbxIsStatic);
		chckbxIsStatic.setHorizontalTextPosition(SwingConstants.LEFT);
		
		tbClass = new JTextField();
		tbClass.setBounds(97, 32, 246, 20);
		panel_1.add(tbClass);
		tbClass.setColumns(10);
		
		tbMethod = new JTextField();
		tbMethod.setBounds(97, 57, 246, 20);
		panel_1.add(tbMethod);
		tbMethod.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				method.methodName = tbMethod.getText().trim();
				lblMethodsign.setText(method.getMethSign());
			}
		});
		tbMethod.setColumns(10);
		
		JPanel panelRetObj = new JPanel();
		panelRetObj.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panelRetObj.setBounds(10, 82, 333, 77);
		panel_1.add(panelRetObj);
		panelRetObj.setLayout(null);
		
		JLabel lblReturnValue = new JLabel("Return object");
		lblReturnValue.setBounds(5, 5, 84, 14);
		panelRetObj.add(lblReturnValue);
		
		JLabel lblReturnType = new JLabel("Type");
		lblReturnType.setBounds(12, 27, 77, 14);
		panelRetObj.add(lblReturnType);
		
		tbRetType = new JTextField();
		tbRetType.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (tbRetType.getText().trim().isEmpty() || tbRetType.getText().trim().equals("void")) {
					rdbtnRetSafe.setEnabled(false);
					rdbtnRetUnsafe.setEnabled(false);
					rdbtnRetUnknown.setEnabled(false);
					rdbtnRetUnknown.setSelected(true);
				} else {
					rdbtnRetSafe.setEnabled(true);
					rdbtnRetUnsafe.setEnabled(true);
					rdbtnRetUnknown.setEnabled(true);
				}
				method.retTypeName = tbRetType.getText().trim();
				lblMethodsign.setText(method.getMethSign());
			}
		});
		tbRetType.setBounds(67, 24, 249, 20);
		panelRetObj.add(tbRetType);
		tbRetType.setColumns(10);
		
		rdbtnRetSafe = new JRadioButton("safe");
		rdbtnRetSafe.setBounds(67, 48, 77, 23);
		rdbtnRetSafe.setEnabled(false);
		panelRetObj.add(rdbtnRetSafe);
		
		rdbtnRetUnsafe = new JRadioButton("unsafe");
		rdbtnRetUnsafe.setBounds(147, 48, 77, 23);
		rdbtnRetUnsafe.setEnabled(false);
		panelRetObj.add(rdbtnRetUnsafe);
		
		rdbtnRetUnknown = new JRadioButton("unknown");
		rdbtnRetUnknown.setBounds(226, 48, 77, 23);
		rdbtnRetUnknown .setEnabled(false);
		panelRetObj.add(rdbtnRetUnknown);
		rdbtnRetUnknown.setSelected(true);
		
		ButtonGroup retObjSafetyGroup = new ButtonGroup();
		retObjSafetyGroup.add(rdbtnRetSafe);
		retObjSafetyGroup.add(rdbtnRetUnsafe);
		retObjSafetyGroup.add(rdbtnRetUnknown);
		
		JLabel lblSafety = new JLabel("Safety");
		lblSafety.setBounds(12, 52, 77, 14);
		panelRetObj.add(lblSafety);
		
		panelThisObj = new JPanel();
		panelThisObj.setLayout(null);
		panelThisObj.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panelThisObj.setBounds(10, 186, 333, 58);
		panel_1.add(panelThisObj);
		
		JLabel lblThisObject = new JLabel("This object");
		lblThisObject.setBounds(5, 5, 71, 14);
		panelThisObj.add(lblThisObject);
		
		rdbtnThisSafe = new JRadioButton("safe");
		rdbtnThisSafe.setBounds(85, 24, 77, 23);
		panelThisObj.add(rdbtnThisSafe);
		
		rdbtnThisUnsafe = new JRadioButton("unsafe");
		rdbtnThisUnsafe.setBounds(165, 24, 77, 23);
		panelThisObj.add(rdbtnThisUnsafe);
		
		rdbtnThisUnknown = new JRadioButton("unknown");
		rdbtnThisUnknown.setSelected(true);
		rdbtnThisUnknown.setBounds(244, 24, 77, 23);
		panelThisObj.add(rdbtnThisUnknown);
		
		rdbtnThisCritical = new JRadioButton("critical");
		rdbtnThisCritical.setBounds(5, 24, 77, 23);
		panelThisObj.add(rdbtnThisCritical);
		
		ButtonGroup thisObjSafetyGroup = new ButtonGroup();
		thisObjSafetyGroup.add(rdbtnThisSafe);
		thisObjSafetyGroup.add(rdbtnThisUnsafe);
		thisObjSafetyGroup.add(rdbtnThisUnknown);
		thisObjSafetyGroup.add(rdbtnThisCritical);
		
		JPanel panelArg = new JPanel();
		panelArg.setLayout(null);
		panelArg.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panelArg.setBounds(10, 277, 353, 111);
		contentPane.add(panelArg);
		
		JLabel lblNewArgument = new JLabel("New argument");
		lblNewArgument.setBounds(5, 5, 84, 14);
		panelArg.add(lblNewArgument);
		
		JLabel label_1 = new JLabel("Type");
		label_1.setBounds(12, 27, 77, 14);
		panelArg.add(label_1);
		
		tbArgumentType = new JTextField();
		tbArgumentType.setColumns(10);
		tbArgumentType.setBounds(67, 24, 276, 20);
		panelArg.add(tbArgumentType);
		
		rdbtnArgSafe = new JRadioButton("safe");
		rdbtnArgSafe.setBounds(89, 48, 77, 23);
		panelArg.add(rdbtnArgSafe);
		
		rdbtnArgUnsafe = new JRadioButton("unsafe");
		rdbtnArgUnsafe.setBounds(169, 48, 77, 23);
		panelArg.add(rdbtnArgUnsafe);
		
		rdbtnArgUnknown = new JRadioButton("unknown");
		rdbtnArgUnknown.setSelected(true);
		rdbtnArgUnknown.setBounds(248, 48, 77, 23);
		panelArg.add(rdbtnArgUnknown);
		
		rdbtnArgCritical = new JRadioButton("critical");
		rdbtnArgCritical.setBounds(12, 48, 77, 23);
		panelArg.add(rdbtnArgCritical);
		
		ButtonGroup argSafetyGroup = new ButtonGroup();
		argSafetyGroup.add(rdbtnArgSafe);
		argSafetyGroup.add(rdbtnArgUnsafe);
		argSafetyGroup.add(rdbtnArgUnknown);
		argSafetyGroup.add(rdbtnArgCritical);
		
		JButton btnAddArgument = new JButton("Add argument");
		btnAddArgument.setBounds(221, 78, 122, 23);
		panelArg.add(btnAddArgument);
		btnAddArgument.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				lblError.setText("");
			}
		});
		
		btnAddArgument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String argType = tbArgumentType.getText().trim();
				
//				if (!argType.isEmpty()) {
//					if (rdbtnArgCritical.isSelected()){
//						method.specialArguments.add(new SpecialArg(method.methodArgs.size(), SpecialArg.TYPE_CRITICAL_OUTPUT));
//					} else if (rdbtnArgSafe.isSelected()) {
//						method.specialArguments.add(new SpecialArg(method.methodArgs.size(), SpecialArg.TYPE_SAFE_ARG));
//					} else if (rdbtnArgUnsafe.isSelected()) {
//						method.specialArguments.add(new SpecialArg(method.methodArgs.size(), SpecialArg.TYPE_UNSAFE_ARG));
//					}
//					method.methodArgs.add(argType);
//				} else {
//					lblError.setText("Enter argument type!");
//					return;
//				}
				
				rdbtnArgUnknown.setSelected(true);
				tbArgumentType.setText("");
				lblMethodsign.setText(method.getMethSign());
			}
		});
	}
}
