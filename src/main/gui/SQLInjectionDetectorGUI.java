package main.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SQLInjectionDetectorGUI extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SQLInjectionDetectorGUI frame = new SQLInjectionDetectorGUI();
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
	public SQLInjectionDetectorGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		initComponents();
	}
	                      
    private void initComponents() {
        jMenuBar1 = new javax.swing.JMenuBar();
        menuTestProject = new javax.swing.JMenu();
        menuTestProject.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent arg0) {
        		pnlAddLibClass.setVisible(false);
        		pnlAddLibMethod.setVisible(false);
        		pnlTestProject.setVisible(true);	
        	}
        });
        menuDefineLib = new javax.swing.JMenu();
        menuItemAddClass = new javax.swing.JMenuItem();
        menuItemAddClass.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		pnlTestProject.setVisible(false);
        		pnlAddLibMethod.setVisible(false);
        		pnlAddLibClass.setVisible(true);
        	}
        });
        menuItemAddMethod = new javax.swing.JMenuItem();
        menuItemAddMethod.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		pnlTestProject.setVisible(false);
        		pnlAddLibClass.setVisible(false);
        		pnlAddLibMethod.setVisible(true);
        	}
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        menuTestProject.setText("Test project");
        jMenuBar1.add(menuTestProject);

        menuDefineLib.setText("Define libraries");

        menuItemAddClass.setText("Add library class");
        menuDefineLib.add(menuItemAddClass);
        
        menuItemAddMethod.setText("Add library method");
        menuDefineLib.add(menuItemAddMethod);

        jMenuBar1.add(menuDefineLib);
        setJMenuBar(jMenuBar1);
        contentPane.setLayout(null);
        
        pnlTestProject = new PnlTestProject();
        pnlTestProject.setBounds(0, 0, 910, 589);
        pnlTestProject.setVisible(true);
        contentPane.add(pnlTestProject);
        
        pnlAddLibClass = new PnlAddLibClass();
        pnlAddLibClass.setBounds(0, 0, 910, 589);
        pnlAddLibClass.setVisible(false);
        contentPane.add(pnlAddLibClass);
        
        pnlAddLibMethod = new PnlAddLibMethod();
        pnlAddLibMethod.setBounds(0, 0, 910, 589);
        pnlAddLibMethod.setVisible(false);
        contentPane.add(pnlAddLibMethod);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGap(0, 900, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGap(0, 579, Short.MAX_VALUE)
        );
        getContentPane().setLayout(layout);

        pack();
    }                                  
    
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu menuDefineLib;
    private javax.swing.JMenuItem menuItemAddClass;
    private javax.swing.JMenuItem menuItemAddMethod;
    private javax.swing.JMenu menuTestProject;
    
    private JPanel pnlTestProject;
    private JPanel pnlAddLibClass;
    private JPanel pnlAddLibMethod;
}
