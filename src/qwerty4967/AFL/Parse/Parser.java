package qwerty4967.AFL.Parse;

import java.util.ArrayList;

import qwerty4967.AFL.Function.*;
import qwerty4967.AFL.Interpret.Namespace;
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
		
		// probably should make the AFLFunction everything will end up in
		Namespace.removeFunction("_main",0);
		AFLFunction main = new AFLFunction("_main",0,0);
		
		// Here we're just splitting all of the code by ';' to determine statements.
		// I know that this presents a problem with ; not being able to be part of strings,
		// but I'm okay with that, I think. Is it a problem? I guess, but it's logically consistent to me
		// even if it's not great...
		// also it would probably take more energy to solve than I care to attempt, at least at the moment.
		// Later: I fixed it. kinda. you can have strings with ; in them, if nothing else.
		Shell.out("Creating Statement Array..", 2);
		ArrayList<String> statements = seperateStatements(code);
		//statements = removeComments(statements);
		Shell.out("Statement Array Created:\n"+statements, 1);
		
		
		
		// next we identify and separate important tokens ( strings, numbers, operators, etc.) i
		Shell.out("Calling Tokenizer...",3);
		main = Tokenizer.tokenize(main, statements);
		if(main == null)
		{	
			Shell.out("Tokenizer errored out... returning",2);
			return null;
		}
		Shell.out("Results of Tokenizer:"+main+"\n",1);
		
		
		// and then we turn those tokens into a tree made entirely of function calls. 
		Shell.out("Calling Functionizer...",3);
		main = Functionizer.functionize(main);
		if(main == null)
		{	
			Shell.out("Functionizer errored out... returning",2);
			return null;
		}
		Shell.out("Results of Functionizer:"+main+"\n",1);
		
		// We take statements that contain control statements and interpret their meaning. 
		Shell.out("Calling Contextuallizer...",3);
		main = Contextualizer.contextualize(main);
		if(main == null)
		{	
			Shell.out("Contextualizer errored out... returning",2);
			return null;
		}
		Shell.out("Results of Contextuallizer:"+main+"\n",1);
		
		// probably add main to the thing
		Namespace.addFunction(main);
		return main;
		// Template
		/*
		Shell.out("Calling Example Function.",3);
		main = Example.example(main);
		if(main == null)
		{	
			Shell.out("example errored out... returning",2);
			return null;
		}
		Shell.out("Results of example:"+main+"\n",1);
		*/
		
		
		
	}
	
	private static ArrayList<String> seperateStatements(String code)
	{
		// this is pretty simple, we are basically just splitting a string by ';' with some trimming.
		//
		
		ArrayList<String> toReturn = new ArrayList<String>();
		code = removeComments(code);
		
		// why waste effort?
		if(!code.contains(";"))
		{
			Shell.out("Code contained 1 statement.", 2);
			toReturn.add(code);
			return toReturn;
		}
		
		
		String[] tokens = splitBySemicolon(code); //code.split(";");
		
		// we need to remove any and all whitespace from our precious new baby statements.
		for(int i = 0; i<tokens.length;i++)
		{
			String token = tokens[i];/*.strip();*/
			
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
	
	private static String[] splitBySemicolon(String s)
	{
		ArrayList<String> lines=new ArrayList<String>();
		int lastLine = -1; // the last char of the last line that was split.
		
		for(int i = 0; i<s.length();i++)
		{
			char c = s.charAt(i);
			if(c==';')
			{
				// check that there is no escape sequence.
				if(i-1>-1)
				{
					if(s.charAt(i-1)=='\\')
					{
						// just absolutely destroy that little \.
						s=s.substring(0, i-1)+s.substring(i,s.length());
						continue;
					}
				}
				
				// snip snip
				String line = s.substring(lastLine+1,i);
				lines.add(line);
				lastLine=i;
			}
		}
		
		// grab the straggler.
		String line = s.substring(lastLine+1,s.length());
		lines.add(line);
		
		// Oh, you can convert an arraylist to an array pretty easily.
		// good to know.
		// heh nope, apparently you can't cast an Object[] to a String[].
		// ugh
		
		String[] toReturn = new String[lines.size()];
		int i = 0;
		for(String a: lines)
		{
			toReturn[i]=a;
			i++;
		}
		return toReturn;
				
	}
	
	 public static String removeComments(String code)
	 {
		// Comments are started with a # and ended with a newline.
		 
		 String toReturn = "";
		 boolean isComment=false;
		 boolean possibleEscape=false;
		 
		 for(int i = 0; i<code.length();i++)
		 {
			 
			 char c = code.charAt(i);
			 
			 if(isComment)
			 {
				if(c=='\n')
				{
					toReturn+=c+" ";
					isComment=false;
				}
			 }
			 else
			 {
				 if(c=='\\')
				 {
					 possibleEscape=true;
					 toReturn+=c;
					 continue;
				 }
				 
				 if(c=='#')
				 {
					 if(possibleEscape)
					 {
						 possibleEscape=false;
						 toReturn+=c;
						 continue;
					 }
					 else
					 {
						 isComment=true;
					 }
				 }
				 else
				 {
					 toReturn+=c;
					 possibleEscape=false;
				 }
			 }
		 }
		
		
		return toReturn;
	}
}
