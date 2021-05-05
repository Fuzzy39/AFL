package qwerty4967.Ziker4;

import java.util.ArrayList;

public abstract class DataElement extends FunctionalElement
{
	
	// Wee, a whole new class
	// again
	// it's still abstract
	// this is the last one though
	// of course a file structure doesn't have a sequence to you, so that means nothing.
	// anyways...
	
	private ArrayList<Object> data = new ArrayList<Object>();
	protected ArrayList<Object> dataTypes; // the expected types for the data.
	// Data elements contain data, duh. they do not have children.
	
	public DataElement(FunctionalGroup group, ElementContainer parent) throws Exception
	{
		super(group,parent);
	}
	
	public DataElement(FunctionalGroup group) throws Exception
	{
		super(group);
	}
	
	public boolean setData(ArrayList<Object> data)
	{
		if(data.size()!=dataTypes.size())
		{
			return false;
		}
		
		for(int i = 0; i<data.size(); i++)
		{
			
			if(data.get(i).getClass() != this.dataTypes.get(i).getClass())
			{
				return false;
			}
		}
		
		this.data=data;
		return true;
		
	}
	
	public Object getData( int index )
	{
		return data.get(index);
	}
	
	
}
