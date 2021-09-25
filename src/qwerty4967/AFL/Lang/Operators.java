package qwerty4967.AFL.Lang;

import java.util.ArrayList;

public class Operators 
{
	public static final String[][] SORTED = 
	{
		// Highest priority at the top.
		{"(",")" }, // organize (these operators are special)
		{"*","/","%"}, // multiply and divide	
		{"+","-"}, // add and subtract.
		{"==","!=",">=","<=",">","<"}, // compare
		{"="} // assign
		
	};
		
	public static enum PRIORITY_GROUP
	{
		organize,
		multiply,
		add, 
		compare,
		assign
		
		
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
	
	public static String[] getGroup(PRIORITY_GROUP pg)
	{
		return SORTED[pg.ordinal()];
	}
}
