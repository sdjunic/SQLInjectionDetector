package main;

import java.nio.file.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

import symbol.Table;
import symbol.object.TypeReference;
import symbol.object.UnknownType;
import symbol.object.Class;
import symbol.object.MethParam;
import symbol.object.Method;
import symbol.object.Modifiers;
import symbol.object.Modifiers.Modifier;
import symbol.object.Obj;
import symbol.object.PrimitiveType;
import Parse.*;
import execution.TaskExecutor;
import object.values.MethodValuesHolder;

import java.io.*;

public class Main {
	
	private static List<LibraryMethodDecl> libraryMethList; 

	public static PrintStream infoPS = System.out; /* null; */
	public static boolean useCheck = true; /* false */
	
	public static void main(String args[]) throws Exception {
		
		libraryMethList = new LinkedList<LibraryMethodDecl>();
		ProgramParser.setCriticalMethList(libraryMethList);
		ClassContentParser.setCriticalMethList(libraryMethList);
		
		LibraryMethodDecl libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "executeQuery");
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.specialArguments.add(new SpecialArg(0, SpecialArg.TYPE_CRITICAL_OUTPUT));
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "executeUpdate");
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.specialArguments.add(new SpecialArg(0, SpecialArg.TYPE_CRITICAL_OUTPUT));
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "executeUpdate");
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.specialArguments.add(new SpecialArg(0, SpecialArg.TYPE_CRITICAL_OUTPUT));
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "escapeSQL");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.retType = "String";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.specialArguments.add(new SpecialArg(SpecialArg.INDEX_RETURN_OBJ, SpecialArg.TYPE_SAFE_ARG));
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("javax.servlet.http", "HttpServletRequest", "getParameter");
		libraryMethDecl.retType = "String";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.specialArguments.add(new SpecialArg(SpecialArg.INDEX_RETURN_OBJ, SpecialArg.TYPE_UNSAFE_ARG));
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("javax.servlet.http", "HttpServletResponse", "getParameter");
		libraryMethDecl.retType = "String";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.specialArguments.add(new SpecialArg(SpecialArg.INDEX_RETURN_OBJ, SpecialArg.TYPE_UNSAFE_ARG));
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.util", "Scanner", "next");
		libraryMethDecl.retType = "String";
		libraryMethDecl.specialArguments.add(new SpecialArg(SpecialArg.INDEX_RETURN_OBJ, SpecialArg.TYPE_UNSAFE_ARG));
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.util", "Scanner", "nextLine");
		libraryMethDecl.retType = "String";
		libraryMethDecl.specialArguments.add(new SpecialArg(SpecialArg.INDEX_RETURN_OBJ, SpecialArg.TYPE_UNSAFE_ARG));
		libraryMethList.add(libraryMethDecl);
		
		if (args.length > 1 && (args[0].equals("-test") || args[1].equals("-test"))) {
			int vulnerableTests = 0;
			int safeTests = 0;
			
			int vulnerableCorrect = 0;
			int safeCorrect = 0;
			
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
			            	   String analyzeFromMain = eElement.getElementsByTagName("analyzeFromMain").item(0).getTextContent();
			            	   String initArgumentsSafe = eElement.getElementsByTagName("initArgumentsSafe").item(0).getTextContent();
			            	   
			            	   if (testName == null || vulnerability == null || analyzeFromMain == null || initArgumentsSafe == null) {
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
			            		   testProjectForSQLInjection(testFolder, analyzeFromMain.equals("false"), initArgumentsSafe.equals("true"));
			            		   
			            		   if (vulnerability.equals("false")) {
			            			   safeCorrect++;
			            		   } else {
			            			   System.out.println("Test " + testName + " failed!");
			            		   }
			            	   } catch (main.exception.SQLInjection e) {
			            		   if (vulnerability.equals("true")) {
			            			   vulnerableCorrect++;
			            		   } else {
			            			   System.out.println("Test " + testName + " failed!");
			            		   }
			            	   } catch (Exception e) {
			            		   System.out.println("Error in test " + testName);
			            		   //e.printStackTrace();
			            		   continue;
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
			
			System.out.println("True positive: " + vulnerableCorrect + "/" + vulnerableTests);
			System.out.println("False positive: " + (safeTests - safeCorrect) + "/" + safeTests);
		} else {
			/* GUI MODE */
			SQLIDetectorGUI window = new SQLIDetectorGUI(libraryMethList);
			window.show();
		}
	}
	
	public static void testProjectForSQLInjection(File projectRoot, boolean startFromRiskyMethods, boolean initialArgumentsSafe) throws Exception{				
		Table.makeNewTable();
		Method.methCallStack = new Stack<Method>();
		Parse.ParseData.riskyMethods = new HashSet<Method>();
		Parse.ParseData.mainMethods =new HashSet<Method>();
		
		for (LibraryMethodDecl decl : libraryMethList) {
			Table.setScope(Table.universe());
			Obj packageObj = Table.find(decl.packageName);
			symbol.object.Package currentPackage = null;
			if (packageObj != null && packageObj instanceof symbol.object.Package) {
				currentPackage = (symbol.object.Package)packageObj;
				Table.setScope(currentPackage.getScope());
			} else {
				currentPackage = new symbol.object.Package(decl.packageName);
				Table.insert(currentPackage);
				Table.openScope(currentPackage);		
				currentPackage.setScope(Table.currentScope());
			}
			
			Obj classObj = Table.find(decl.className);
			Class currentClass = null;
			if (classObj != null && classObj instanceof symbol.object.Class) {
				currentClass = (symbol.object.Class)classObj;
				Table.setScope(currentClass.getScope());
			} else {
				currentClass = new symbol.object.Class(decl.className, null);
				Table.insert(currentClass);
				Table.openScope(currentClass);		
				currentClass.setScope(Table.currentScope());
			}
			
			Obj methodObj = Table.find(decl.methodName);
			Method currentMethod = null;
			if (methodObj != null && methodObj instanceof symbol.object.Method) {
				continue;
			} else {
				currentMethod = new symbol.object.Method(decl.methodName, decl.className.equals(decl.methodName));
				int i = 0;
				for (String arg : decl.methodArgs) {
					Obj obj = Table.find(arg);
					symbol.object.Type type = null;
					if (obj != null || obj instanceof PrimitiveType || obj instanceof Class)
						type = (symbol.object.Type)obj;
					else {
						type = new UnknownType(arg, null, null, Table.currentScope());
					}
					currentMethod.addFormalParam(new MethParam(new TypeReference(type), "temp" + ++i));
				}
				currentMethod.complFormalParamAdding();
				
				if (decl.retType != null) {
					Obj obj = Table.find(decl.retType);
					symbol.object.Type retType = null;
					if (obj != null || obj instanceof PrimitiveType || obj instanceof Class)
						retType = (symbol.object.Type)obj;
					else {
						retType = new UnknownType(decl.retType, null, null, Table.currentScope());
					}
					currentMethod.setRetType(new TypeReference(retType));
				}
				if (decl.isStatic) {
					currentMethod.setModifiers(new Modifiers(Modifier.STATIC));
				}
				currentMethod.setDefined(false);
				currentMethod.setSpecialArguments(decl.specialArguments);
				Table.insert(currentMethod);
				Table.openScope(currentMethod);		
				currentMethod.setScope(Table.currentScope());
			}
		}
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
			
				if (startFromRiskyMethods) infoPS.println("\r\nRisky methods:\r\n");
				else infoPS.println("\r\nMain methods:\r\n");
			}
			
			{
				Iterator<Method> it = null;
				if (startFromRiskyMethods) it = ParseData.riskyMethods.iterator();
				else it = ParseData.mainMethods.iterator();
				
				while (it.hasNext()) {
					Method m = it.next();
					Method.methCallStack.push(m);
					if (infoPS != null) 
					{
						infoPS.println("\r\n----------------------------------------------------------------------------------------");
						infoPS.println(" -START METHOD NAME: " + m.getName() + "  -file: " + m.getMethodDefFilePath());
					}
					
					try {
						TaskExecutor.execute(m, initialArgumentsSafe);
						
						// For testing purposes:
						// Good test shouldn't have problem with null pointers or with infinite recursion.
						// It's OK to kill some tasks because of that, but we should always have at least one task,
						// which executes to the end. We can't throw exception here, since in realistic usage, 
						// execution from risky methods can hit null pointer exception on all tasks.
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
			StringBuilder sb = new StringBuilder();
			Table.print(sb);
			if (infoPS != null) infoPS.println(sb);
			
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
