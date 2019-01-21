package main;

import javax.swing.*;

import java.awt.event.ItemListener;
import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Insets;

public class SQLIDetectorGUI {

	private List<LibraryMethodDecl> criticalMethList;	
	private JFrame frmSqlInjectionVulnerability;
	private JTextField tbProjectPath;
	private ButtonGroup analysisFromGroup = new ButtonGroup(), initArgSafeGroup = new ButtonGroup();
	private JLabel lblRiskyMethodArguments;
	private JTextArea textAreaOutput;

	/**
	 * Create the application.
	 */
	public SQLIDetectorGUI(List<LibraryMethodDecl> criticalMethList) {
		this.criticalMethList = criticalMethList;
	}

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	public void show() {
		frmSqlInjectionVulnerability = new JFrame();
		frmSqlInjectionVulnerability.setResizable(false);
		frmSqlInjectionVulnerability.setTitle("SQL injection vulnerability detector");
		frmSqlInjectionVulnerability.setBounds(100, 100, 389, 403);
		frmSqlInjectionVulnerability.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSqlInjectionVulnerability.getContentPane().setLayout(null);
		
		textAreaOutput = new JTextArea();
		textAreaOutput.setLineWrap(true);
		textAreaOutput.setMargin(new Insets(5,5,5,5));
		textAreaOutput.setEditable(false);
		textAreaOutput.setWrapStyleWord(true);
		
		JScrollPane scroll = new JScrollPane(textAreaOutput);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(10, 157, 361, 172);
		frmSqlInjectionVulnerability.getContentPane().add(scroll);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnClose.setBounds(282, 340, 89, 23);
		frmSqlInjectionVulnerability.getContentPane().add(btnClose);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.GRAY));
		panel.setBounds(10, 11, 361, 133);
		frmSqlInjectionVulnerability.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblProjectPath = new JLabel("Project path:");
		lblProjectPath.setBounds(10, 14, 72, 14);
		panel.add(lblProjectPath);
		
		tbProjectPath = new JTextField();
		tbProjectPath.setBounds(92, 11, 250, 20);
		panel.add(tbProjectPath);
		tbProjectPath.setColumns(10);
		
		JLabel lblStartAnalysis = new JLabel("Start analysis from:");
		lblStartAnalysis.setBounds(10, 39, 166, 14);
		panel.add(lblStartAnalysis);
		
		lblRiskyMethodArguments = new JLabel("Risky methods arguments:");
		lblRiskyMethodArguments.setBounds(10, 64, 166, 14);
		panel.add(lblRiskyMethodArguments);
		lblRiskyMethodArguments.setEnabled(false);
		
		JRadioButton rdbtnSafe = new JRadioButton("safe");
		rdbtnSafe.setBounds(182, 60, 68, 23);
		panel.add(rdbtnSafe);
		rdbtnSafe.setSelected(true);
		rdbtnSafe.setEnabled(false);
		
		initArgSafeGroup.add(rdbtnSafe);
		
		JRadioButton rdbtnMain = new JRadioButton("Main");
		rdbtnMain.setBounds(182, 38, 68, 23);
		panel.add(rdbtnMain);
		rdbtnMain.setSelected(true);
		
		analysisFromGroup.add(rdbtnMain);
		
		JRadioButton rdbtnRiskyMethods = new JRadioButton("Risky methods");
		rdbtnRiskyMethods.setBounds(250, 38, 109, 23);
		panel.add(rdbtnRiskyMethods);
		rdbtnRiskyMethods.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				boolean riskyMethMode = rdbtnRiskyMethods.isSelected();
				
				lblRiskyMethodArguments.setEnabled(riskyMethMode);
				Enumeration<AbstractButton> btns = initArgSafeGroup.getElements();
				while (btns.hasMoreElements()) {
					AbstractButton btn = btns.nextElement();
					btn.setEnabled(riskyMethMode);
				}
			}
		});
		analysisFromGroup.add(rdbtnRiskyMethods);
		
		JRadioButton rdbtnUnsafe = new JRadioButton("unsafe");
		rdbtnUnsafe.setBounds(250, 60, 109, 23);
		panel.add(rdbtnUnsafe);
		rdbtnUnsafe.setEnabled(false);
		initArgSafeGroup.add(rdbtnUnsafe);
		
		JButton btnAddCriticalMethods = new JButton("Add library methods");
		btnAddCriticalMethods.setBounds(10, 96, 165, 23);
		panel.add(btnAddCriticalMethods);
		
		JButton btnTestVulnerability = new JButton("Test SQLI vulnerability");
		frmSqlInjectionVulnerability.getRootPane().setDefaultButton(btnTestVulnerability);
		btnTestVulnerability.setBounds(184, 96, 165, 23);
		panel.add(btnTestVulnerability);
		btnTestVulnerability.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String projectFolderPath = tbProjectPath.getText().trim();
				if (projectFolderPath.isEmpty()) {
					textAreaOutput.setText("Enter project path!");
					return;
				}
				File projectRoot = new File(projectFolderPath);
				if (!projectRoot.exists()) {
					textAreaOutput.setText("Project path doesn't exist!");
					return;
				}
				boolean startFromRiskyMethods = rdbtnRiskyMethods.isSelected();
				boolean initialArgumentsSafe = rdbtnSafe.isSelected();
				try {
					Main.testProjectForSQLInjection(projectRoot, startFromRiskyMethods ? "risky" : "main", initialArgumentsSafe);
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
		btnAddCriticalMethods.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSqlInjectionVulnerability.setEnabled(false);
				AddLibraryMethods addCritMethDialog = new AddLibraryMethods(criticalMethList, frmSqlInjectionVulnerability);
				addCritMethDialog.setVisible(true);
			}
		});
		
		frmSqlInjectionVulnerability.setVisible(true);
	}
}
