package qwerty4967.Ziker4;

import java.util.ArrayList;

public class StatementData extends DataElement 
{
	// yeah, pretty boring.
	// A statementData contains a string and an int.
	
	
	/**
	 * 
	 * @param group
	 * @throws Exception
	 */
	public StatementData(FunctionalGroup group) throws Exception 
	{
		
		super(group);
		dataTypes=new ArrayList<Object>();
		dataTypes.add( Integer.valueOf(0)); // statement number
		dataTypes.add( new String()); // statement
	}
	/**
	 * @param group
	 * @param parent
	 * @throws Exception
	 */
	public StatementData(FunctionalGroup group, ElementContainer parent) throws Exception 
	{
		super(group, parent);
		dataTypes=new ArrayList<Object>();
		dataTypes.add( Integer.valueOf(0));
		dataTypes.add( new String());
	}
	
	/**
	 * standard toString
	 */
	@Override
	public String toString() {
		return "StatementData [data=" + data + "]";
	}
	


	

}
