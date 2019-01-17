package main;

import java.util.LinkedList;
import java.util.List;

public class LibraryClassDecl {
	
	public class LibField
	{
		public String type;
		public String name;
		
		public LibField(String type, String name)
		{
			this.type = type;
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
	
	public void addField(String type, String name)
	{
		this.fields.add(new LibraryClassDecl.LibField(type, name));
	}

}
