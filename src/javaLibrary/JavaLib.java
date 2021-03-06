package javaLibrary;

import java.util.List;
import main.LibraryClassDecl;
import main.LibraryMethodDecl;

public class JavaLib {

	public static void getLibDeclarations(
		List<LibraryClassDecl> libraryClassList,
		List<LibraryMethodDecl> libraryMethList)
	{
		// Add predefined library classes/methods
		//
		LibraryClassDecl libraryClassDecl = new LibraryClassDecl(null, "StringBuilder");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl(null, "CharSequence");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("java.sql", "Connection");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("java.sql", "Statement");
		libraryClassDecl.addField("java.sql", "Connection", "connection");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("java.sql", "PreparedStatement", "java.sql", "Statement");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("java.sql", "CallableStatement", "java.sql", "PreparedStatement");
		libraryClassList.add(libraryClassDecl);

		libraryClassDecl = new LibraryClassDecl("java.sql", "ResultSet");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("javax.servlet.http", "Cookie");
		libraryClassDecl.addField(null, "String", "name");
		libraryClassDecl.addField(null, "String", "value");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("java.util", "Enumeration");
		libraryClassDecl.addField(null, "Object", "element");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("java.util", "Map");
		libraryClassDecl.addField(null, "Object", "key");
		libraryClassDecl.addField(null, "Object", "value");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("java.util", "HashMap", "java.util", "Map");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("java.util", "List");
		libraryClassDecl.addField(null, "Object", "element");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("java.util", "ArrayList", "java.util", "List");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("javax.servlet.http", "HttpServletRequest");
		libraryClassDecl.alwaysUnsafe = true;
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("javax.servlet.http", "HttpServletResponse");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("org.owasp.benchmark.helpers", "SeparateClassRequest");
		libraryClassDecl.addField("javax.servlet.http", "HttpServletRequest", "request");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("org.owasp.benchmark.helpers", "ThingInterface");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("org.springframework.jdbc.core", "JdbcTemplate");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("org.owasp.benchmark.helpers", "DatabaseHelper");
		libraryClassDecl.addField("org.springframework.jdbc.core", "JdbcTemplate", "JDBCtemplate");
		libraryClassList.add(libraryClassDecl);
		
		libraryClassDecl = new LibraryClassDecl("org.apache.commons.codec.binary", "Base64");
		libraryClassList.add(libraryClassDecl);

		LibraryMethodDecl libraryMethDecl = new LibraryMethodDecl(null, "String", "String");
		libraryMethDecl.isConstructor = true;
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.methodArgs.add("Object[]");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN, 
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				"0");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl(null, "String", "concat");
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN, 
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				"0 & " + SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl(null, "String", "split");
		libraryMethDecl.retTypeName = "String[]";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String[]"),
				"0");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl(null, "String", "substring");
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl(null, "String", "substring");
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl(null, "String", "getBytes");
		libraryMethDecl.retTypeName = "Object[]";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("Object[]"),
				SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl(null, "StringBuilder", "StringBuilder");
		libraryMethDecl.isConstructor = true;
		libraryMethDecl.retTypeName = "StringBuilder";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("StringBuilder"),
				SpecialAction.SAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl(null, "StringBuilder", "StringBuilder");
		libraryMethDecl.isConstructor = true;
		libraryMethDecl.retTypeName = "StringBuilder";
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("StringBuilder"),
				SpecialAction.SAFE);
		libraryMethList.add(libraryMethDecl);

		libraryMethDecl = new LibraryMethodDecl(null, "StringBuilder", "StringBuilder");
		libraryMethDecl.isConstructor = true;
		libraryMethDecl.retTypeName = "StringBuilder";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("StringBuilder"),
				"0");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl(null, "StringBuilder", "StringBuilder");
		libraryMethDecl.isConstructor = true;
		libraryMethDecl.retTypeName = "StringBuilder";
		libraryMethDecl.methodArgs.add("CharSequence");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN, 
				SpecialAction.ASSIGN_NEW_OBJECT("StringBuilder"),
				"0");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl(null, "StringBuilder", "append");
		libraryMethDecl.retTypeName = "StringBuilder";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.THIS, 
				SpecialAction.SET_SAFE, 
				"0 & " + SpecialAction.THIS);
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl(null, "StringBuilder", "append");
		libraryMethDecl.retTypeName = "StringBuilder";
		libraryMethDecl.methodArgs.add("CharSequence");
		libraryMethDecl.addSpecialAction(
				SpecialAction.THIS, 
				SpecialAction.SET_SAFE, 
				"0 & " + SpecialAction.THIS);
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl(null, "StringBuilder", "toString");
		libraryMethDecl.retTypeName = "StringBuilder";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.apache.commons.codec.binary", "Base64", "encodeBase64");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.methodArgs.add("Object[]");
		libraryMethDecl.retTypeName = "Object[]";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("Object[]"),
				"0");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.apache.commons.codec.binary", "Base64", "decodeBase64");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.methodArgs.add("Object[]");
		libraryMethDecl.retTypeName = "Object[]";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("Object[]"),
				"0");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.owasp.benchmark.helpers", "DatabaseHelper", "getSqlConnection");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.retTypePackage = "java.sql";
		libraryMethDecl.retTypeName = "Connection";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.sql.Connection"),
				SpecialAction.SAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.owasp.benchmark.helpers", "DatabaseHelper", "getSqlStatement");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.retTypePackage = "java.sql";
		libraryMethDecl.retTypeName = "Statement";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.sql.Statement"),
				SpecialAction.SAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.springframework.jdbc.core", "JdbcTemplate", "execute");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.springframework.jdbc.core", "JdbcTemplate", "queryForRowSet");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.springframework.jdbc.core", "JdbcTemplate", "queryForLong");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.springframework.jdbc.core", "JdbcTemplate", "queryForMap");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.springframework.jdbc.core", "JdbcTemplate", "query");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("org.springframework.jdbc.core.RowMapper");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.springframework.jdbc.core", "JdbcTemplate", "batchUpdate");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.springframework.jdbc.core", "JdbcTemplate", "queryForInt");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.springframework.jdbc.core", "JdbcTemplate", "queryForObject");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("Object[]");
		libraryMethDecl.methodArgs.add("Object");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.springframework.jdbc.core", "JdbcTemplate", "queryForList");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.owasp.benchmark.helpers", "ThingFactory", "createThing");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.retTypePackage = "org.owasp.benchmark.helpers";
		libraryMethDecl.retTypeName = "ThingInterface";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("org.owasp.benchmark.helpers.ThingInterface"),
				SpecialAction.SAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.owasp.benchmark.helpers", "ThingInterface", "doSomething");
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				"0");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "DriverManager", "getConnection");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.retTypePackage = "java.sql";
		libraryMethDecl.retTypeName = "Connection";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.sql.Connection"),
				SpecialAction.SAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Connection", "prepareCall");
		libraryMethDecl.retTypePackage = "java.sql";
		libraryMethDecl.retTypeName = "CallableStatement";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.sql.CallableStatement"),
				"0");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".connection",
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Connection", "prepareCall");
		libraryMethDecl.retTypePackage = "java.sql";
		libraryMethDecl.retTypeName = "CallableStatement";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.sql.CallableStatement"),
				"0");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".connection",
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Connection", "prepareCall");
		libraryMethDecl.retTypePackage = "java.sql";
		libraryMethDecl.retTypeName = "CallableStatement";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.sql.CallableStatement"),
				"0");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".connection",
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Connection", "prepareStatement");
		libraryMethDecl.retTypePackage = "java.sql";
		libraryMethDecl.retTypeName = "PreparedStatement";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.sql.PreparedStatement"),
				"0");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".connection",
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Connection", "prepareStatement");
		libraryMethDecl.retTypePackage = "java.sql";
		libraryMethDecl.retTypeName = "PreparedStatement";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.sql.PreparedStatement"),
				"0");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".connection",
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Connection", "prepareStatement");
		libraryMethDecl.retTypePackage = "java.sql";
		libraryMethDecl.retTypeName = "PreparedStatement";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("int[]");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.sql.PreparedStatement"),
				"0");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".connection",
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Connection", "prepareStatement");
		libraryMethDecl.retTypePackage = "java.sql";
		libraryMethDecl.retTypeName = "PreparedStatement";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.sql.PreparedStatement"),
				"0");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".connection",
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Connection", "prepareStatement");
		libraryMethDecl.retTypePackage = "java.sql";
		libraryMethDecl.retTypeName = "PreparedStatement";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.sql.PreparedStatement"),
				"0");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".connection",
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Connection", "prepareStatement");
		libraryMethDecl.retTypePackage = "java.sql";
		libraryMethDecl.retTypeName = "PreparedStatement";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("String[]");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.sql.PreparedStatement"),
				"0");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".connection",
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Connection", "createStatement");
		libraryMethDecl.retTypePackage = "java.sql";
		libraryMethDecl.retTypeName = "Statement";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.sql.Statement"),
				SpecialAction.SAFE);
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".connection",
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "execute");
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "execute");
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "execute");
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("int[]");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "execute");
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("String[]");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "executeBatch");
		libraryMethDecl.addSpecialAction(SpecialAction.THIS, SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
				
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "executeQuery");
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "executeQuery");
		libraryMethDecl.addSpecialAction(SpecialAction.THIS, SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "CallableStatement", "executeQuery");
		libraryMethDecl.addSpecialAction(SpecialAction.THIS, SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "executeUpdate");
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "executeUpdate");
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "executeUpdate");
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("int[]");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "executeUpdate");
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("String[]");
		libraryMethDecl.addSpecialAction("0", SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
				
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "getConnection");
		libraryMethDecl.retTypePackage = "java.sql";
		libraryMethDecl.retTypeName = "Connection";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS + ".connection");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "addBatch");
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.THIS, 
				SpecialAction.SET_SAFE, 
				"0 & " + SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "Statement", "escapeSQL");
		libraryMethDecl.isStatic = true;
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.SAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "PreparedStatement", "execute");
		libraryMethDecl.addSpecialAction(SpecialAction.THIS, SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "PreparedStatement", "executeQuery");
		libraryMethDecl.addSpecialAction(SpecialAction.THIS, SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "PreparedStatement", "executeUpdate");
		libraryMethDecl.addSpecialAction(SpecialAction.THIS, SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "PreparedStatement", "execute");
		libraryMethDecl.addSpecialAction(SpecialAction.THIS, SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "PreparedStatement", "executeQuery");
		libraryMethDecl.addSpecialAction(SpecialAction.THIS, SpecialAction.CRITICAL_OUTPUT);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.sql", "PreparedStatement", "setString");
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.THIS, 
				SpecialAction.SET_SAFE, 
				"1 & " + SpecialAction.THIS);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("javax.servlet.http", "HttpServletRequest", "getParameter");
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.UNSAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("javax.servlet.http", "HttpServletRequest", "getParameterMap");
		libraryMethDecl.retTypePackage = "java.util";
		libraryMethDecl.retTypeName = "Map";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.util.Map"),
				SpecialAction.UNSAFE);
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".key",
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.UNSAFE);
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".value",
				SpecialAction.ASSIGN_NEW_OBJECT("String[]"),
				SpecialAction.UNSAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("javax.servlet.http", "HttpServletRequest", "getParameterNames");
		libraryMethDecl.retTypePackage = "java.util";
		libraryMethDecl.retTypeName = "Enumeration";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.util.Enumeration"),
				SpecialAction.UNSAFE);
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".element",
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.UNSAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("javax.servlet.http", "HttpServletRequest", "getParameterValues");
		libraryMethDecl.retTypeName = "String[]";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String[]"),
				SpecialAction.UNSAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("javax.servlet.http", "HttpServletRequest", "getHeader");
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.UNSAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("javax.servlet.http", "HttpServletRequest", "getHeaders");
		libraryMethDecl.retTypePackage = "java.util";
		libraryMethDecl.retTypeName = "Enumeration";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.util.Enumeration"),
				SpecialAction.UNSAFE);
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".element",
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.UNSAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("javax.servlet.http", "HttpServletRequest", "getQueryString");
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.UNSAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("javax.servlet.http", "HttpServletRequest", "getCookies");
		libraryMethDecl.retTypePackage = "javax.servlet.http";
		libraryMethDecl.retTypeName = "Cookie[]";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("javax.servlet.http.Cookie[]"),
				SpecialAction.UNSAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.owasp.benchmark.helpers", "SeparateClassRequest", "SeparateClassRequest");
		libraryMethDecl.isConstructor = true;
		libraryMethDecl.retTypePackage = "org.owasp.benchmark.helpers";
		libraryMethDecl.retTypeName = "SeparateClassRequest";
		libraryMethDecl.methodArgs.add("javax.servlet.http.HttpServletRequest");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("org.owasp.benchmark.helpers.SeparateClassRequest"),
				SpecialAction.SAFE);
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".request",
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				"0");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.owasp.benchmark.helpers", "SeparateClassRequest", "getTheParameter");
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.THIS + ".request");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("org.owasp.benchmark.helpers", "SeparateClassRequest", "getTheValue");
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.SAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("javax.servlet.http", "Cookie", "getName");
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.THIS + ".name");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("javax.servlet.http", "Cookie", "getValue");
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.THIS + ".value");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("javax.servlet.http", "Cookie", "Cookie");
		libraryMethDecl.isConstructor = true;
		libraryMethDecl.retTypePackage = "javax.servlet.http";
		libraryMethDecl.retTypeName = "Cookie";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("javax.servlet.http.Cookie"),
				SpecialAction.SAFE);
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".name",
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				"0");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".value",
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				"1");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.util", "Enumeration", "nextElement");
		libraryMethDecl.retTypeName = "Object";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS + ".element");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.util", "HashMap", "HashMap");
		libraryMethDecl.isConstructor = true;
		libraryMethDecl.retTypePackage = "java.util";
		libraryMethDecl.retTypeName = "HashMap";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.util.HashMap"),
				SpecialAction.SAFE);
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".key",
				SpecialAction.ASSIGN_NEW_OBJECT("Object"),
				SpecialAction.SAFE);
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".value",
				SpecialAction.ASSIGN_NEW_OBJECT("Object"),
				SpecialAction.SAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.util", "Map", "put");
		libraryMethDecl.retTypeName = "Object";
		libraryMethDecl.methodArgs.add("Object");
		libraryMethDecl.methodArgs.add("Object");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS + ".value");
		libraryMethDecl.addSpecialAction(
				SpecialAction.THIS + ".key",
				SpecialAction.ASSIGN_NEW_OBJECT("0|Object"),
				SpecialAction.THIS + ".key & 0");
		libraryMethDecl.addSpecialAction(
				SpecialAction.THIS + ".value",
				SpecialAction.ASSIGN_NEW_OBJECT("1|Object"),
				SpecialAction.THIS + ".value & 1");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.util", "Map", "get");
		libraryMethDecl.retTypeName = "Object";
		libraryMethDecl.methodArgs.add("Object");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS + ".value");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.util", "Map", "clear");
		libraryMethDecl.addSpecialAction(
				SpecialAction.THIS,
				SpecialAction.SET_SAFE,
				SpecialAction.SAFE);
		libraryMethDecl.addSpecialAction(
				SpecialAction.THIS + ".key",
				SpecialAction.ASSIGN_NEW_OBJECT("Object"),
				SpecialAction.SAFE);
		libraryMethDecl.addSpecialAction(
				SpecialAction.THIS + ".value",
				SpecialAction.ASSIGN_NEW_OBJECT("Object"),
				SpecialAction.SAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.util", "ArrayList", "ArrayList");
		libraryMethDecl.isConstructor = true;
		libraryMethDecl.retTypePackage = "java.util";
		libraryMethDecl.retTypeName = "ArrayList";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("java.util.ArrayList"),
				SpecialAction.SAFE);
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN + ".element",
				SpecialAction.ASSIGN_NEW_OBJECT("Object"),
				SpecialAction.SAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.util", "List", "add");
		libraryMethDecl.methodArgs.add("Object");
		libraryMethDecl.addSpecialAction(
				SpecialAction.THIS + ".element",
				SpecialAction.ASSIGN_NEW_OBJECT("0|Object"),
				SpecialAction.THIS + ".element & 0");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.util", "List", "get");
		libraryMethDecl.retTypeName = "Object";
		libraryMethDecl.methodArgs.add("int");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_EXISTING_OBJECT,
				SpecialAction.THIS + ".element");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.util", "List", "clear");
		libraryMethDecl.addSpecialAction(
				SpecialAction.THIS,
				SpecialAction.SET_SAFE,
				SpecialAction.SAFE);
		libraryMethDecl.addSpecialAction(
				SpecialAction.THIS + ".element",
				SpecialAction.ASSIGN_NEW_OBJECT("Object"),
				SpecialAction.SAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("javax.servlet.http", "HttpServletResponse", "getHeader");
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.UNSAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.net", "URLDecoder", "decode");
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.methodArgs.add("String");
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				"0");
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.util", "Scanner", "next");
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.UNSAFE);
		libraryMethList.add(libraryMethDecl);
		
		libraryMethDecl = new LibraryMethodDecl("java.util", "Scanner", "nextLine");
		libraryMethDecl.retTypeName = "String";
		libraryMethDecl.addSpecialAction(
				SpecialAction.RETURN,
				SpecialAction.ASSIGN_NEW_OBJECT("String"),
				SpecialAction.UNSAFE);
		libraryMethList.add(libraryMethDecl);
	}

}
