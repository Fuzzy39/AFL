package qwerty4967.Ziker4.FunctionStructures;

import java.util.ArrayList;

import qwerty4967.Ziker4.Lang;


public class TokenData extends DataElement 
{
	
	// yeah, pretty boring.
	// very useful in Parser.tokenPass and Parser.finalPass though.
	
	/**
	 * 
	 * @param group
	 * @throws Exception
	 */
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
	
	/**
	 * standard tostring
	 */
	@Override
	public String toString() {
		try 
		{
		return "TokenData [\"" + data.get(0)+"\", "+Lang.tokenTypes.values()[(int)(data.get(1))]+ "]";
		}
		catch(Exception e)
		{
			return "Invalid TokenData";
		}
	}

	public FunctionalElement copy()
	{
		TokenData d;
		try 
		{
			d= new TokenData(this.getGroup(),this.getParent());
			d.setData(this.getData());
			return d;
		} 
		catch (Exception e) 
		{
			
			e.printStackTrace();
			return null;
		}
		
		
		
		
	}
		

	
}
