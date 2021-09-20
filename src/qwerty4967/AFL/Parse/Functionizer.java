package qwerty4967.AFL.Parse;

import qwerty4967.AFL.Function.*;
import qwerty4967.AFL.*;
import qwerty4967.AFL.Lang.*;
import qwerty4967.AFL.ParseTree.*;

public class Functionizer 
{
 
	protected static AFLFunction functionize( AFLFunction main )
	{
		// should just be able to treat each statement as a separate entity
		for(int i = 0; i<main.getSize(); i++)
		{
			
			Statement s = (Statement)main.getChild(i);
			if(!functionizeStatement(s))
			{
				return null;
			}
			
		}
		
		return main;
	}
	
	private static boolean functionizeStatement(Statement s)
	{
		
		Container c = (Container)s.getChild(0);
		return functionize(c);
	}
	
	protected static boolean functionize(Container container)
	{
		// this, you see. is the function of functions.
		// this is the second most important method of AFL.Parse.
		// While it is true that Parser.parse() is the controller for parsing,
		// this is the core of turning tokens into
		return false;
	}
	
	
}
