package symbol.object;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import Parse.ParseData;

public class TypeReference {
	
	public Type type = null;
	
	private static List<TypeReference> unknownTypes = new LinkedList<TypeReference>();
	
	public TypeReference(Type type) {
		this.type = type;
		if (type != null && type instanceof UnknownType) {
			unknownTypes.add(this);
		}
	}
	
	public void addArrayLevel(int arrayLevel) {
		if (arrayLevel <= 0) return;
		if (type instanceof ArrayType) {
			ArrayType at = (ArrayType)type;
			type = new ArrayType(new TypeReference(type), at.getArrayLevel() + arrayLevel);
		} else {
			type = new ArrayType(new TypeReference(type), arrayLevel);
		}
	}
	
	public static void resolveUnknownTypes() {
		Iterator<TypeReference> it = unknownTypes.iterator();
		while (it.hasNext()) {
			TypeReference typeRef = it.next();

			if (typeRef.type instanceof ArrayType)
			{
				typeRef = ((ArrayType)typeRef.type).getType();
			}
			assert typeRef.type instanceof UnknownType;
			UnknownType unknownType = (UnknownType)typeRef.type;
			Obj obj = ParseData.findName(unknownType.getFullName(), unknownType.getImportedObjects(), 
					unknownType.getImportedScopes(), unknownType.getScope());
			if (obj != null && obj instanceof Type) {
				typeRef.type = (Type)obj;
				it.remove();
			}
		}
	}
	
	public static String getAllUnknownTypes() {
		StringBuilder sb = new StringBuilder();
		HashSet<String> unknownTypesHash = new HashSet<String>();
		Iterator<TypeReference> it = unknownTypes.iterator();
		while (it.hasNext()) {
			TypeReference typeRef = it.next();
			if (!unknownTypesHash.contains(typeRef.type.toString())) {
				unknownTypesHash.add(typeRef.type.toString());
				sb.append(typeRef.type.toString() + "\r\n");
			}
		}
		return sb.toString();
	}
	
	public String getName() {
		return type.getName();
	}
	
	@Override
	public String toString() {
		return (type != null ? type.toString() : "");
	}
	
	public void print(StringBuilder sb, int tabNum) {
		type.print(sb, tabNum);
	}
	
	public boolean isRefType() {
		return type.isRefType();
	}
	
	public boolean isValueType() {
		return type.isValueType();
	}

}
