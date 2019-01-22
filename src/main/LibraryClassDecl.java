package main;

import java.util.LinkedList;
import java.util.List;

public class LibraryClassDecl {
	
	public class LibField
	{
		public String typePackage;
		public String typeName;
		public String name;
		
		public LibField(String typePackage, String typeName, String name)
		{
			this.typePackage = typePackage;
			this.typeName = typeName;
			this.name = name;
		}
	}
	
	public String packageName;
	public String className;
	public String superClassPackageName;
	public String superClass;
	public boolean alwaysUnsafe = false;
	
	public List<LibField> fields;

	public LibraryClassDecl(String packageName, String className) {
		this(packageName, className, null, "Object");
	}
	
	public LibraryClassDecl(String packageName, String className, String superClassPackageName, String superClass) {
		this.packageName = packageName;
		this.className = className;
		this.superClassPackageName = superClassPackageName;
		this.superClass = superClass;
		this.fields = new LinkedList<>();
	}
	
	public void addField(String typePackage, String typeName, String name)
	{
		this.fields.add(new LibraryClassDecl.LibField(typePackage, typeName, name));
	}

}
