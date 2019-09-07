package object.values;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import symbol.object.Method;

public class MethodValuesHolder extends ValuesHolder {

	private MethodValuesHolder parentValuesHolder = null;
	
	// Used to detect identical method calls on stack.
	// Used to determine and terminate recursion cycle. 
	//
	private Method method = null;
	private byte[] methodInputMVH_hash = null;
	
	public MethodValuesHolder(MethodValuesHolder parentValuesHolder, Method method) {
		this(parentValuesHolder, method, null);
	}
	
	public MethodValuesHolder(MethodValuesHolder parentValuesHolder, Method method, byte[] methodInputMVH_hash) {
		super();
		this.parentValuesHolder = parentValuesHolder;
		
		this.method = method;
		this.methodInputMVH_hash = methodInputMVH_hash;
	}

	@Override
	public ObjValue get(String name) {
		if (name == null) return null;
		if (name.endsWith("[]"))
		{
			ObjValue array = values.get(name.replaceAll("\\[\\]", ""));
			if (array == null) return null;
			assert array instanceof ArrayValue;
			return ((ArrayValue)array).element;
		}
		else
		{
			return values.get(name);
		}
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
	
	// Should be called only once, after adding all actual arguments to this MVH map,
	// to hash input values.
	public void saveInputMVH_hash() throws NoSuchAlgorithmException
	{
		assert (this.methodInputMVH_hash == null);
		// Get string representation of this MVH map.
		StringBuilder sb = new StringBuilder();
		this.print(sb, "", true);
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
		this.methodInputMVH_hash = hash;
	}

	// Used to check does this MVH completes the recursion cycle.
	// If returns true, we shouldn't execute this method.
	public boolean checkForRecursionCycle()
	{
		MethodValuesHolder prevCallMVH = this.parentValuesHolder;
		while(prevCallMVH != null)
		{
			if (this.method == prevCallMVH.method 
					&& Arrays.equals(this.methodInputMVH_hash, prevCallMVH.methodInputMVH_hash))
			{
				return true;
			}
			prevCallMVH = prevCallMVH.parentValuesHolder;
		}
		return false;
	}
	
	public byte[] hash() throws NoSuchAlgorithmException
	{
		// Get string representation of this and all parent Method VH maps.
		StringBuilder sb = new StringBuilder();
		MethodValuesHolder mvh = this;
		
		// We must check all parent MVH maps, because two tasks with different maps
		// can call the method which don't see the difference between these
		// tasks (for example static method, with no arguments). 
		// In such case, if we hash only current MVH maps at the end of such method
		// we would get the same values and we would reduce non-equal tasks.
		// 
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
		// original and new object. Also populate new ValuesHolder map, with
		// references to new objects that we are created in this method.
		//
		MethodValuesHolder currentMethVHMap = this;
		MethodValuesHolder resultCopyVHMap = null;
		MethodValuesHolder prevCopyMethVHMap = null;
		while(currentMethVHMap != null)
		{
			MethodValuesHolder currentCopyMethVHMap = new MethodValuesHolder(
															null, 
															currentMethVHMap.method, 
															currentMethVHMap.methodInputMVH_hash);
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
		return resultCopyVHMap;
	}

}
