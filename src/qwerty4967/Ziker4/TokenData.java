package qwerty4967.Ziker4;

import java.util.ArrayList;

public class TokenData extends DataElement 
{
	
	// yeah, pretty boring.
	// very useful in Parser.tokenPass and Parser.finalPass though.
	
	
	public TokenData(FunctionalGroup group) throws Exception 
	{
		
		super(group);
		dataTypes=new ArrayList<Object>();
		dataTypes.add( new String()); // data
		dataTypes.add( Integer.valueOf(0) ); // tokenType
	}
	/**
	 * @param group
	 * @param parent
	 * @throws Exception
	 */
	public TokenData(FunctionalGroup group, ElementContainer parent) throws Exception 
	{
		super(group, parent);
		dataTypes=new ArrayList<Object>();
		dataTypes.add( new String());
		dataTypes.add( Integer.valueOf(0) );
	}


		

	
}
