package main;

import java.util.LinkedList;
import java.util.List;

public class LibraryClassDecl {
	
	class Field
	{
		public String type;
		public String name;
		
		public Field(String type, String name)
		{
			this.type = type;
			this.name = name;
		}
	}
	
	public String packageName;
	public String className;
	public String superClass;
	
	public List<Field> fields;
	
	public LibraryClassDecl()
	{
		this.packageName = null;
		this.className = null;
		this.superClass = null;
		this.fields = new LinkedList<>();
	}

	public LibraryClassDecl(String packageName, String className, String superClass) {
		this.packageName = packageName;
		this.className = className;
		this.superClass = superClass;
	}
	
	public void addField(String type, String name)
	{
		this.fields.add(new LibraryClassDecl.Field(type, name));
	}

}
