package qwerty4967.Ziker4;

import java.util.ArrayList;

public class StatementData extends DataElement 
{
	// yeah, pretty boring.
	// A statementData contains a string and an int.
	// Explained in the statementDataData enum
	
	
	public StatementData(FunctionalGroup group) throws Exception 
	{
		
		super(group);
		dataTypes=new ArrayList<Object>();
		dataTypes.add( Integer.valueOf(0));
		dataTypes.add( new String());
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


	

}
