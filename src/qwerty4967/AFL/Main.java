package qwerty4967.AFL;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import qwerty4967.AFL.Parse.*;
import qwerty4967.AFL.Function.*;

public class Main 
{
	// Project Information
	// 4/30/21 Started
	// 5/1/21 Shell class probably finished, aside from errors
	// 5/7/21 Ready to begin work on pass 1
	// 5/11/21 Pass 1 done, with the exception of functions
	// 5/X/21 Began major refactor pass 1 would be pass 3.
	// 5/X/21 Stopped working on the project
	// 7/13/21 Started work on the project again, capable of identifing all token types. 
	// 7/1X/21 tokenPass, pass 1, complete.
	// 8/13/21 b 328 Decided to massively refactor the code. Renamed from Ziker 4 to AFL (Arguably Functional Language)
	// 9/27/21 b 385 All Ziker functionality restored. (parentheses, operations, functions)
	// 10/0X/21 b ??? finished AFL.Parse (control Statements functional)
	// 10/9/21 b 412 Began work on foundations for interpreter
	
	protected static final int BUILD = 421;
	private static boolean usesShell = true;
	private static ArrayList<Path> toExecute;
	
	public static void main(String[] args) 
	{
		
		if(!interpretArgs(args))
		{
			return;
		}
		
		if(!initializeFunctions())
		{
			Shell.out("Failed initializing.");
			return;
		}
		
		
		
		if(usesShell)
		{
			// TODO replace this with a JavaFunction in the long term.
			// maybe.
			Shell.out("NOTE: not yet functional! Build: "+BUILD+" | debug: "+Shell.getDebugLevel()+"\n");
			
			while(true)
			{
				execute(Shell.getUserInput());
			}
		}
		else
		{
			for(Path p: toExecute)
			{
				execute(getCodeFromFile(p));
			}
			return;
		}
		
	}
	
	private static boolean interpretArgs(String[] args)
	{
	
		// exillerating, I'm sure.
		String run = "run";
		String debug = "debug";
		
		
		for(int i = 0; i<args.length; i++)
		{
			String s = args[i];
			
			if(s.equals(run))
			{
				// run
				usesShell=false;
				
				if((i+1)>=args.length)
				{
					Shell.out("Please specify one or more file paths to execute from when using the 'run' argument.");
					return false;
				}
				
				if(!getFiles(i+1,args))
				{
					return false;
				}
				return true;
				
			}
			else if(s.equals(debug))
			{
				// debug
				if((i+1)>=args.length)
				{
					Shell.out("Please specify a debug level. valid ranges are 0-"+Shell.MAX_DEBUG_LEVEL+".");
					return false;
				}
				if(!setDebugLevel(args[i+1]))
				{
					Shell.out("Please specify a valid debug level. valid ranges are 0-"+Shell.MAX_DEBUG_LEVEL+".");
					return false;
				}
				i++;
				
			}
			else
			{
				Shell.out("Invalid argument '"+s+"'.");
				return false;
			}
		}
		return true;
	}
	
	private static boolean getFiles (int i, String[] args)
	{
		toExecute=new ArrayList<Path>();
		for(; i<args.length; i++)
		{
			// each of these Ought to be a file.
			Path p = Paths.get(args[i]);
			try
			{
				p.toRealPath();
			}
			catch (NoSuchFileException e) 
			{
				Shell.out("Couldn't find specified file: '"+p+"'.\nDid you type the path correctly?");
				return false;
		    
			} 
			catch (IOException e) 
			{
				Shell.out("There was some unspecified problem with file: '"+p+"'.\nHuh, that seems weird. Do you have read access to the file?");
				return false;
				
			}
			
			toExecute.add(p);
		}
		return true;
	}
	
	private static boolean setDebugLevel(String level)
	{
		// check for an int.
		int toSet;
		try
		{
			toSet =Integer.parseInt(level);
		
		}
		catch(Exception e)
		{
			return false;
		}
		
		if(toSet<0||toSet>Shell.MAX_DEBUG_LEVEL)
		{
			return false;
		}
		
		Shell.debugLevel=toSet;
		return true;
	}

	private static boolean initializeFunctions()
	{
		// this does nothing at the moment, but be sure that it will.
		return true;
		
		// example javaFunction.
		/*MethodCode mc = ((Token[] tokens) ->{System.out.println("pretend that this code does something!");return null;})	;
		JavaFunction jf = new JavaFunction("test",0,mc);
		jf.call(new Token[0]);*/
	}
	
	private static void execute(String code)
	{
		AFLFunction toExecute = Parser.parse(code);
		//Interpreter.interpret(toExecute);
	}
	
	private static String getCodeFromFile(Path p)
	{
		// Uh, so apparently a list is better than an arrayList
		// for, uh, reasons
		// that's what I've heard, apparently
		// and readAllLines returns a list, which is how I learned they exist
		// no clue what the real difference is
		// I don't understand java collections.
		List<String> lines= new ArrayList<String>();
		try 
		{
			lines = Files.readAllLines(p);
		} 
		catch (IOException e) 
		{
			
			// we already checked for these errors, so I'm fine with it just crashing.
			e.printStackTrace();
			System.exit(-1);
			
		}
		
		String toReturn = "";
		for(String s : lines)
		{
			toReturn+=s;
		}
		return toReturn;
		
	}
}
