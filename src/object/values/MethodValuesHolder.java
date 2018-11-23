package object.values;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

	// Deep copy this and all parent MethodValuesHolder maps, with all objects
	// and its references. 
	// NOTE: Result list is ordered starting from this, to parent MVH maps.
	//
	public List<MethodValuesHolder> deepCopy()
	{
		HashMap<ObjValue, ObjValue> copyMap = new HashMap<>();
		List<MethodValuesHolder> copyValuesHolderList = new LinkedList<MethodValuesHolder>();
		
		// Copy all objects and make capyMap table, which is mapping between
		// previous and new object. Also populate new ValuesHolder map, with
		// references to new objects that we are creating in this method.
		//
		MethodValuesHolder currentMethVHMap = this;
		while(currentMethVHMap != null)
		{
			MethodValuesHolder currentCopyMethVHMap = new MethodValuesHolder(null);
			if (!copyValuesHolderList.isEmpty())
			{
				copyValuesHolderList.get(copyValuesHolderList.size() - 1).setParentValuesHolder(currentCopyMethVHMap);
			}
			copyValuesHolderList.add(currentCopyMethVHMap);
			
			for (Entry<String, ObjValue> var : currentMethVHMap.values.entrySet())
			{
				ObjValue prevObj = var.getValue();
				ObjValue newObj = prevObj.copy();
				currentCopyMethVHMap.values.put(var.getKey(), newObj);
				if (prevObj instanceof ClassValue)
				{
					((ClassValue)prevObj).getFields().copyAllObjects(copyMap);
				}
			}
		}
		
		// Update field references for all new objects of type ClassValue, using the
		// previously created mapping in copyMap.
		//
		for (ObjValue newValue : copyMap.values())
		{
			if (newValue instanceof ClassValue)
			{
				((ClassValue)newValue).getFields().updateReferences(copyMap);
			}
		}
		
		return copyValuesHolderList;
	}

}
