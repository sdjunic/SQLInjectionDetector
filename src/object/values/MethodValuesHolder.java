package object.values;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map.Entry;

public class MethodValuesHolder extends ValuesHolder {

	private MethodValuesHolder parentValuesHolder = null;
	
	public MethodValuesHolder(MethodValuesHolder parentValuesHolder) {
		super();
		this.parentValuesHolder = parentValuesHolder;
	}

	@Override
	public ObjValue get(String name) {
		if (name == null) return null;
		ObjValue result = values.get(name);
		return result;
	}
	
	// Copy the whole values holder map recursively, and set all references 
	// between newly created objects to get the same relations as between original objects.
	//
	public MethodValuesHolder getParentValuesHolder() {
		return parentValuesHolder;
	}

	public void setParentValuesHolder(MethodValuesHolder parentValuesHolder) {
		this.parentValuesHolder = parentValuesHolder;
	}

	public byte[] hash() throws NoSuchAlgorithmException
	{
		// Get string representation of this and all parent Method VH maps
		StringBuilder sb = new StringBuilder();
		MethodValuesHolder mvh = this;
		while(mvh != null)
		{
			mvh.print(sb, "", true);
			sb.append("\n");
			mvh = mvh.parentValuesHolder;
		}
		
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
		return hash;
	}
	
	// Deep copy this and all parent MethodValuesHolder maps, with all objects
	// and its references. 
	//
	public MethodValuesHolder deepCopy()
	{
		HashMap<ObjValue, ObjValue> copyMap = new HashMap<>();
		
		// Copy all objects and make capyMap table, which is mapping between
		// previous and new object. Also populate new ValuesHolder map, with
		// references to new objects that we are creating in this method.
		//
		MethodValuesHolder currentMethVHMap = this;
		MethodValuesHolder resultCopyVHMap = null;
		MethodValuesHolder prevCopyMethVHMap = null;
		while(currentMethVHMap != null)
		{
			MethodValuesHolder currentCopyMethVHMap = new MethodValuesHolder(null);
			if (prevCopyMethVHMap != null)
			{
				prevCopyMethVHMap.setParentValuesHolder(currentCopyMethVHMap);
			}
			else
			{
				resultCopyVHMap = currentCopyMethVHMap;
			}
			
			for (Entry<String, ObjValue> var : currentMethVHMap.values.entrySet())
			{
				ObjValue originalObj = var.getValue();
				ObjValue copyObj = copyMap.get(originalObj); 
				if (copyObj == null)
				{
					copyObj = originalObj.shallowCopy();
					copyMap.put(originalObj, copyObj); 
					
					if (originalObj instanceof ClassValue)
					{
						assert (copyObj instanceof ClassValue);
						((ClassValue)copyObj).deepCopyFields(copyMap);
					}
				}
				assert copyObj != null;
				currentCopyMethVHMap.values.put(var.getKey(), copyObj);
			}
			
			prevCopyMethVHMap = currentCopyMethVHMap;
			currentMethVHMap = currentMethVHMap.parentValuesHolder;
		}
		
		assert (resultCopyVHMap != null);
		
//		// Update field references for all new objects of type ClassValue, using the
//		// previously created mapping in copyMap.
//		//
//		for (ObjValue newValue : copyMap.values())
//		{
//			if (newValue instanceof ClassValue)
//			{
//				((ClassValue)newValue).getFields().updateReferences(copyMap);
//			}
//		}
		
		return resultCopyVHMap;
	}

}
