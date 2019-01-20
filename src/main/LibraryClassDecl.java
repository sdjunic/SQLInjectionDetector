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
	public String superClass;
	
	public List<LibField> fields;

	public LibraryClassDecl(String packageName, String className, String superClass) {
		this.packageName = packageName;
		this.className = className;
		this.superClass = superClass;
		this.fields = new LinkedList<>();
	}
	
	public void addField(String typePackage, String typeName, String name)
	{
		this.fields.add(new LibraryClassDecl.LibField(typePackage, typeName, name));
	}

}
