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
	
	// This class define the elements that hold data.
	// the format of the data is restricted by subclasses.
	
	
	protected ArrayList<Object> data = new ArrayList<Object>();
	protected ArrayList<Object> dataTypes; // the expected types for the data.
	// Data elements contain data, duh. they do not have children.
	/**
	 * constructors don't really require descriptions?
	 * @param group
	 * @param parent
	 * @throws Exception
	 */
	public DataElement(FunctionalGroup group, ElementContainer parent) throws Exception
	{
		super(group,parent);
	}
	
	/**
	 * 
	 * @param group
	 * @throws Exception
	 */
	public DataElement(FunctionalGroup group) throws Exception
	{
		super(group);
	}
	
	/**
	 * Sets data. Only does so if the data is in the appropriate format.
	 * @param data
	 * @return returns true if data was correctly set.
	 * @throws Exception
	 */
	public boolean setData(ArrayList<Object> data) throws Exception
	{
		if(data.size()!=dataTypes.size())
		{
			
			throw new Exception("Data type sizes incorrect");
		}
		
		for(int i = 0; i<data.size(); i++)
		{
			
			if(data.get(i).getClass() != this.dataTypes.get(i).getClass())
			{
				throw new Exception("Data types incorrect, exprected "+this.dataTypes.get(i).getClass()+", got "+data.get(i).getClass());
			}
		}
		
		this.data=data;
		return true;
		
	}
	/**
	 * 
	 * @param index
	 * @return
	 */
	public Object getData( int index )
	{
		return data.get(index);
	}

	@Override
	/**
	 * 
	 */
	public String toString() {
		return "DataElement [data=" + data + "]";
	}
	
	
	
	
}
