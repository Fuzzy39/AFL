package qwerty4967.AFL.Lang;

import java.util.ArrayList;

public class Operators 
{
	public static final String[][] SORTED = 
	{
		{"(",")" }, // organize (these operators are special)
		{"="}, // assign
		{"==","!=",">=","<=",">","<"}, // compare
		{"+","-"}, // add and subtract.
		{"*","/","%"} // multiply and divide
		
	};
		
	public static enum PRIORITY_GROUPS
	{
		organize,
		assign,
		compare,
		add,
		multiply
		
		
	}
	
	public static ArrayList<String> list()
	{
		ArrayList<String> toReturn = new ArrayList<String>();
		for(String[] i: SORTED)
		{
			for(String j: i)
			{
				toReturn.add(j);
			}
		}
		return toReturn;
	}
}
