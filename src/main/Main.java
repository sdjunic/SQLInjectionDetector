package main;

import java.nio.file.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

import symbol.Table;
import symbol.object.TypeReference;
import symbol.object.Class;
import symbol.object.Method;
import Parse.*;
import execution.TaskExecutor;
import javaLibrary.JavaLib;

import java.io.*;

public class Main {
	
	public static List<LibraryClassDecl> libraryClassList;
	public static List<LibraryMethodDecl> libraryMethList; 

	public static PrintStream infoPS = System.out; /* Set to null to skip printing info. */
	public static boolean useCheck = true; /* Set to false to ignore DbgCheckStatement. */
	
	public static void main(String args[]) throws Exception {
		
		libraryClassList = new LinkedList<LibraryClassDecl>();
		libraryMethList = new LinkedList<LibraryMethodDecl>();
		
		JavaLib.getLibDeclarations(libraryClassList, libraryMethList);
		
		List<LibraryMethodDecl> criticalMethodList = new LinkedList<LibraryMethodDecl>();
		for(LibraryMethodDecl libMethDecl : libraryMethList)
		{
			if (libMethDecl.isCriticalOutput())
			{
				criticalMethodList.add(libMethDecl);
			}
		}
		ProgramParser.setCriticalMethList(criticalMethodList);
		ClassContentParser.setCriticalMethList(criticalMethodList);
		
		if (args.length > 1 && (args[0].equals("-test") || args[1].equals("-test"))) {
			int vulnerableTests = 0;
			int safeTests = 0;
			
			int vulnerableCorrect = 0;
			int safeCorrect = 0;
			
			List<String> failedTests = new LinkedList<>();
			
			/* TEST MODE */
			String testsFolderPath = null;
			if (args[0].equals("-test")) {
				testsFolderPath = args[1];
			} else {
				testsFolderPath = args[0];
			}
			File testsRoot = new File(testsFolderPath);
			if (!testsRoot.exists() || !testsRoot.isDirectory()) {
				System.out.println("The test folder path isn't valid!");
				return;
			} else {
				File[] listOfFiles = testsRoot.listFiles();
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].getName().endsWith(".xml")) {
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						Document doc = dBuilder.parse(listOfFiles[i]);
						doc.getDocumentElement().normalize();
						NodeList nList = doc.getElementsByTagName("test-metadata");
				        for (int temp = 0; temp < nList.getLength(); temp++) {
				        	Node nNode = nList.item(temp);;
				            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				               Element eElement = (Element) nNode;
				               String testName = eElement.getElementsByTagName("testName").item(0).getTextContent();
			            	   String vulnerability = eElement.getElementsByTagName("vulnerability").item(0).getTextContent();
			            	   String analyzeFromMethod = eElement.getElementsByTagName("analyzeFromMethod").item(0).getTextContent();
			            	   String initArgumentsSafe = eElement.getElementsByTagName("initArgumentsSafe").item(0).getTextContent();
			            	   
			            	   if (testName == null || vulnerability == null || analyzeFromMethod == null || initArgumentsSafe == null) {
			            		   System.out.println("Error in test xml file " + listOfFiles[i].getName());
			            		   continue;
			            	   }
			            	   
			            	   File testFolder = new File(testsFolderPath + "\\" + testName);
			            	   if (!testFolder.exists() && !testName.endsWith(".java")) {
			            		   testName = testName.concat(".java");
			            		   testFolder = new File(testsFolderPath + "\\" + testName);
				            	   if (!testFolder.exists()) {
				            		   System.out.println("Error in test xml file " + listOfFiles[i].getName());
				            		   continue;
				            	   }
			            	   }
			            	   
			            	   
			            	   try {
			            		   testProjectForSQLInjection(testFolder, analyzeFromMethod, initArgumentsSafe.equals("true"));
			            		   
			            		   if (vulnerability.equals("false")) {
			            			   safeCorrect++;
			            		   } else {
			            			   failedTests.add(testName);
			            			   System.out.println("Test " + testName + " failed!");
			            			   System.out.println();
			            		   }
			            	   } catch (main.exception.SQLInjection e) {
			            		   if (vulnerability.equals("true")) {
			            			   vulnerableCorrect++;
			            		   } else {
			            			   failedTests.add(testName);
			            			   System.out.println("Test " + testName + " failed!");
			            			   System.out.println();
			            		   }
			            	   } catch (Exception e) {
			            		   System.err.println("Error in test " + testName);
			            		   e.printStackTrace();
			            		   System.exit(0);
			            	   }
			            	   
			            	   if (vulnerability.equals("true")) 
			            	   {
			            		   vulnerableTests++;
			            	   }
			            	   else 
			            	   {
			            		   safeTests++;
			            	   }
				            }
				        }
					}
				}
			}
			
			if (!failedTests.isEmpty())
			{
				System.out.println("FAILED TESTS:");
				for (String s : failedTests)
				{
					System.out.println(s);
				}
				System.out.println();
			}
			
			System.out.println("True positive: " + vulnerableCorrect + "/" + vulnerableTests);
			System.out.println("False positive: " + (safeTests - safeCorrect) + "/" + safeTests);
		} else {
			/* GUI MODE */
			main.gui.SQLInjectionDetectorGUI window = new main.gui.SQLInjectionDetectorGUI();
			window.setVisible(true);
		}
	}
	
	public static void testProjectForSQLInjection(File projectRoot, String startFrom, boolean initialArgumentsSafe) throws Exception{				
		Table.makeNewTable(libraryClassList, libraryMethList);
		Parse.ParseData.allProjectMethods = new HashSet<Method>();
		Parse.ParseData.riskyMethods = new HashSet<Method>();
		Parse.ParseData.mainMethods = new HashSet<Method>();
		
		Table.setScope(Table.universe());
		
		if (infoPS != null) {
			infoPS.println("\r\n----------------------------------------------------------------------------------------");
			infoPS.println("------------------------------- NEW SQLI TESTING STARTED -------------------------------");
			infoPS.println("----------------------------------------------------------------------------------------");
 			infoPS.println("---------------- Project root: " + projectRoot + " ----------------");
			infoPS.println("----------------------------------------------------------------------------------------\r\n");
			infoPS.println(Paths.get(".").toAbsolutePath().normalize().toString());
		}
		
		String tempFolderName = "temp_" + String.valueOf(new Random().nextInt(1000000));
		String tempFolderPath = Paths.get(".").toAbsolutePath().normalize().toString() + "\\" + tempFolderName;
		File tempFolder = new File(tempFolderPath);
		tempFolder.mkdir();

		try {
			ProgramParser.setTempFolderPath(tempFolderPath);
			ClassContentParser.setTempFolderPath(tempFolderPath);
			
			parseAllFilesInFolder(projectRoot);
			
			TypeReference.resolveUnknownTypes();
			Class.extendAllClasses();
			
			StringBuilder sb = new StringBuilder();
			Table.print(sb);
			if (infoPS != null) {
				infoPS.println(sb);
				infoPS.println("\r\n---UNKNOWN TYPES TABLE---\r\n" + TypeReference.getAllUnknownTypes() + "-END UNKNOWN TYPES TABLE-\r\n");
			
				if (startFrom.equals("risky")) infoPS.println("Analyzing from risky methods!");
				else if (startFrom.equals("main")) infoPS.println("Analyzing from main methods!");
				else if (startFrom.equals("all")) infoPS.println("Analyzing from all methods!");
				else assert false;
			}
			
			{
				Iterator<Method> it = null;
				if (startFrom.equals("risky")) it = ParseData.riskyMethods.iterator();
				else if (startFrom.equals("main")) it = ParseData.mainMethods.iterator();
				else if (startFrom.equals("all")) it = ParseData.allProjectMethods.iterator();
				else assert false;
				
				while (it.hasNext()) {
					Method m = it.next();
					if (infoPS != null) 
					{
						infoPS.println("\r\n----------------------------------------------------------------------------------------");
						infoPS.println(" -START METHOD NAME: " + m.getName() + "  -file: " + m.getMethodDefFilePath());
					}
					
					try {
						TaskExecutor.execute(m, initialArgumentsSafe);
						
						// Only for testing purposes:
						// A good test, executed from the Main method, shouldn't have a problem with null pointers
						// or with infinite recursion. It's OK to kill some tasks because of that, but we should always
						// have at least one task that executes to the end. We can't throw an exception here, 
						// since in regular usage, execution from risky methods can hit null pointer exception on all tasks.
						//
						assert (TaskExecutor.finalTaskCount > 0);
						
					} finally {
						StringBuilder sb2 = new StringBuilder();
						sb2.append("\r\n\r\n--------- METHOD BODY ---------\r\n\r\n");
						m.getBody().print(sb2, "");
						sb2.append("\r\n------- END METHOD BODY -------\r\n");
						if (infoPS != null) infoPS.println(sb2);
					}
				}
			}
			
		} finally {
//			// Print symbol table after execution
//			StringBuilder sb = new StringBuilder();
//			Table.print(sb);
//			if (infoPS != null) infoPS.println(sb);
			
			for(File file: tempFolder.listFiles()) file.delete();
			tempFolder.delete();
		}
	}
	
	private static void parseAllFilesInFolder(File folder) {
		if (folder.isDirectory()) {
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				parseAllFilesInFolder(listOfFiles[i]);
			}
		} else {
			if (!folder.getName().endsWith(".java")) return;
			try { 
				Reader fr = new BufferedReader(new FileReader(folder));
				lex.Lexer l = new lex.Lexer(fr, 5);

				ProgramParser g = new ProgramParser(l);
				g.setErrorPS(System.err);
				// g.setInfoPS(System.out);
				g.parse();
				
				fr.close();
				if (infoPS != null) infoPS.println("Parsiranje fajla '" + folder.getName() +(g.errorDetected ? "' NIJE" : "'")+ " uspjesno zavrseno!");
				if (infoPS != null) infoPS.println("Broj leksickih gresaka: " + l.numErrors());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
