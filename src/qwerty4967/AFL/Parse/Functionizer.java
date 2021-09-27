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
			Shell.out("Functionizing Statement:\n"+s, 3);
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
		GroupManager.reset();
		boolean toReturn = functionize(c, false);
		
		// time to scrunch the container
		if(toReturn)
		{
			// this is ugly...
			AFLFunction main =(AFLFunction)s.getParent();
			
			Statement s2 = new Statement(s.getStatementNumber(), main);
			s2.addChild(c.getChild(0));
			main.removeChild(s);
			main.moveChild(s2.getStatementNumber()-1, s2);
		}
		
		return toReturn;
		
		
	}
	
	protected static boolean functionize(Container container, boolean mode)
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
		
		GroupManager.stitchGroups(container);
		
		// unfortunately it just won't die,  so we may as well put its organs back in
		if(!OperationManager.functionizeOperators(container, mode))
		{
			return false;
		}
		
		
		return true;
	}
	
	private static boolean functionizeFunctions(Container c)
	{
		// look for any functions...
		for(int i=0; i<c.getSize();i++)
		{
			Token t = (Token)c.getChild(i);
			if(t.getType()==TokenType.function)
			{
				Token pointer = (Token)c.getChild(i+1);
				if(!(pointer.getType() == TokenType.groupPointer))
				{
					// if this ever happens we've got a big problem
					return false;
				}
				int pointerValue = Integer.parseInt(pointer.getData());
				Group group = GroupManager.getGroup(pointerValue);
				if(!createFunction(t,group))
				{
					return false;
				}
				continue;
			}
			
		}
		return true; 
	}
	
	private static boolean createFunction(Token function, Group parameters)
	{
		Container parent = (Container)function.getParent();
		Container param = (Container)parameters.getChild(0);
		
		int functionLocation  = function.getID();
		
		//start by deleting the group pointer.
		// that line of code is a bit of a mess.
		// oh well.
		parent.removeChild(parent.getChild(functionLocation+1));
		// grab the function we are to be functionizing.
		String functionName=function.getData();
		parent.removeChild(function); // poof!
		
		FunctionCall newFunction = new FunctionCall(functionName, parent);
		parent.addChild(functionLocation, newFunction);
		
		//now it's time to add the parameters.
		if(!functionize(param, true))
		{
			return false;
		}
		
		for(int i = 0; i<param.getSize(); )
		{
			Element e = param.getChild(i);
			newFunction.addChild(e);
		}
		return true;
		
	}
}
