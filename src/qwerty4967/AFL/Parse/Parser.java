package qwerty4967.AFL.Parse;

import java.util.ArrayList;

import qwerty4967.AFL.Function.*;
import qwerty4967.AFL.ParseTree.*;
import qwerty4967.AFL.*;

public class Parser 
{

	//Okay, this is the second time I'm writing this class so let's hope I do it well.
	
	public static AFLFunction parse(String code)
	{
		
		// we need to turn a string of text into a tree that is functionally a list of function calls,
		// at least ideally.
		
		//this function in particular does that, yes, but the code here is just a bunch of function calls 
		// one after another
		// the complex logic is left for later.
		
		Shell.out("Begining Parsing...", 1);
		
		// Here we're just splitting all of the code by ';' to determine statements.
		// I know that this presents a problem with ; not being able to be part of strings,
		// but I'm okay with that, I think. Is it a problem? I guess, but it's logically consistent to me
		// even if it's not great...
		// also it would probably take more energy to solve than I care to attempt, at least at the moment.
		Shell.out("Creating Statement Array..", 2);
		ArrayList<String> statements = seperateStatements(code);
		Shell.out("Statement Array Created:\n"+statements, 1);
		
		// TODO everything.
		return null;
		// Template
		/*
		Shell.out("Calling Example Function.",3);
		main = Example.example(main);
		if(main == null)
		{	
			Shell.out("example errored out... returning",2);
			return;
		}
		Shell.out("Results of example:"+main+"\n",1);
		*/
		
		
		
	}
	
	private static ArrayList<String> seperateStatements(String code)
	{
		// this is pretty simple, we are basically just splitting a string by ';' with some trimming.
		//
		
		ArrayList<String> toReturn = new ArrayList<String>();
		
		// why waste effort?
		if(!code.contains(";"))
		{
			Shell.out("Code contained 1 statement.", 2);
			toReturn.add(code);
			return toReturn;
		}
		
		
		String[] tokens = code.split(";");
		
		// we need to remove any and all whitespace from our precious new baby statements.
		for(int i = 0; i<tokens.length;i++)
		{
			String token = tokens[i].strip();
			
			// if a token is just whitespace it's probably easier to toss it now.
			if(token.equalsIgnoreCase(""))
			{
				continue;
			}
			
			toReturn.add( token);
		}
		
		Shell.out("Code contained "+toReturn.size()+" statements.",2);
		return toReturn;
		
		
		
	}
}
