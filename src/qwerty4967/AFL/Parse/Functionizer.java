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
		// this is the core of turning tokens into function calls
		// you could say it 'functionizes'
		// huh. What a fitting name
		
		// find any parenthetical groups and violently rip them from the container
		if( !GroupManager.findGroups(container))
		{
			return false;
		}
		
		// While the container is bleeding out, figure out which groups are function parameters and respond accordingly.
		
		if(!functionizeFunctions(container))
		{
			return false;
		}
		
		
		// functionize the remaining groups while we wait for the container to take it's last breath.
		if( !GroupManager.functionizeGroups(container))
		{
			return false;
		}
		
		// unfortunately it just won't die,  so we may as well put its organs back in
		if(!OperationManager.functionizeOperators(container))
		{
			return false;
		}
		
		GroupManager.stitchGroups(container);
		return true;
	}
	
	private static boolean functionizeFunctions(Container c)
	{
		for(int i=0; i<c.getSize();i++)
		{
			Token t = (Token)c.getChild(i);
			if(t.getType()==TokenType.function)
			{
				Shell.error("Functions are not currently supported.",t.getStatementNumber());
				return false;
			}
			
		}
		return true; 
	}
	
	
}
