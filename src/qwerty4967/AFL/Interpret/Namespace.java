package qwerty4967.AFL.Interpret;

import java.util.ArrayList;
import qwerty4967.AFL.*;
import qwerty4967.AFL.Function.*;
import qwerty4967.AFL.Lang.*;


public class Namespace 
{
	// MAN, I'm properly starting on the code that runs the code...
	// well, only kind of
	// but hey, it's in the Interpret package, that's got to count for something
	
	private static ArrayList<Function> functions = new ArrayList<Function>();
	
	public static boolean addFunction(Function toAdd)
	{
		String name = toAdd.getName();
		
		// first check that the function's name isn't restricted.
		for(String s : Lang.CONTROL_FUNCTIONS)
		{
			if(name.equals(s))
			{
				// hopefully whoever has to actually deal with it shouts an error message.
				return false;
			}
		}
		
		// then check that it doesn't match any other functions.
		for(Function f: functions)
		{
			if(f.equals(toAdd))
			{
				return false;
			}
		}
		
		
		functions.add(toAdd);
		return true;
	}
	
	public static Function getFunction(String name, int parameters)
	{
		for(Function f: functions)
		{
			if(f.getName().equals(name))
			{
				if(f.getParameters()==parameters)
				{
					return f;
				}
			}
			
		}
		
		return null;
	}
	
	public static boolean removeFunction(Function toRemove)
	{
		String name = toRemove.getName();
		int parameters = toRemove.getParameters();
		return removeFunction(name, parameters);
	}
	
	public static boolean removeFunction(String name, int parameters)
	{
		Function f = getFunction(name, parameters);
		if( f==null)
		{
			return false;
		}
		
		functions.remove(f);
		return true;
	}
	
	public static void clearFunctions()
	{
		functions.clear();
	}
}
