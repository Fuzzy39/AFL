package qwerty4967.AFL;
import java.util.Scanner;

import qwerty4967.AFL.Function.*;
import qwerty4967.AFL.Parse.*;
import qwerty4967.AFL.ParseTree.*;


public class Shell 
{
	// Here is a comment, in the mildly vain hope that I might remember to put something more meaningful here.
	// I've got to say, I'm excited to get started on something new.
	// Hopefully it won't take months... (4 months later: HAH)
	// Started 4/30/21, for Computer Science II final project.
	// Hopefully there aren't any requirements in there that will be a challenge... 
	// I can shoe-horn some stuff in if need-be.
	
	
	

	
	protected static int debugLevel = 0; // ranges from 0 to max, inclusive, determines the prevalence of debug messages.
	public static final int MAX_DEBUG_LEVEL=4; // may as well make it public, it's final.
	/*
	 * As a general Guideline for which debug level to use:
	 * 1 : Only the highest level information. Only extensively used in Parser.parse.
	 * 2 : Basic information about a process.
	 * 3 : Details about a process
	 * 4 : information about a sub-process.
	 */
	
	private static Scanner sc= new Scanner( System.in ); // Setup the Scanner for gathering user input. 
	
	

	protected static String getUserInput() 
	{
	
		
		// and now the actual stuff.
		
		String toReturn="";
		while(true)
		{
			//Grab the input
			String currentLine = Shell.in();
			toReturn+=currentLine.strip();
			
			if(checkString(currentLine))
			{
				// input is continuing.
				continue;
			}
			
			break;
		}
			//input is done.
		out("Input Recieved: \""+toReturn+"\"", 1);
		return toReturn;
			
	
	}
	
	
	/**
	 * Sets debug level to a valid value.
	 * @param debugLevel 
	 * @return returns true if input was valid.
	 */
	public static boolean setDebugLevel( int debugLevel)
	{
		// I've just noticed that I've almost strategically been avoiding using an else.
		// I guess it adds clutter, so that's good I guess?
		// maybe it just makes things more confusing... who knows? somebody, probably.
		if( debugLevel > MAX_DEBUG_LEVEL || debugLevel < 0 )
		{
			return false;
		}
		
		Shell.debugLevel=debugLevel;
		return true;
		
	}
	
	/**
	 * 
	 * @return debug level
	 */
	public static int getDebugLevel ()
	{
		return debugLevel;
	}
	
	
	/*
	 * checks if a string ends with a semicolon, among other things.
	 */
	private static boolean checkString(String string)
	{
		// Checks the string to see if it's last non-whitespace character is a semicolon.
		// This is done to check whether the user is done typing their code.
		
		string=string.strip(); // removing whitespace, because there's no such thing as black and white, only grey.
		// that joke was so bad it didn't make any sense
		// well, here it stays, to eventually be read by my future self.
		// hopefully this finds you well.
		// and yes, I know I'm an idiot and that surely you could do this better in a million ways.
		// learning is great!
		// uh, did I get sidetracked?
		// whatever, moving on.
		
		// Note from slightly future self:
		// the joke is still really bad
		// and I am trying to do this better
		// hopefully it works
		// best of luck
		
		// a couple days later: 
		// you doof, this thing still calls itself ziker...
		// pah, I'll do it myself.
		
		// Note from Half a year future self, revisiting this method for (hopefully) the final time:
		// joke still isn't good.
		// The project is going well, at least.
		
		if(string.length()==0)
		{
			//doesn't really matter.
			out("No Input.", 3);
			return true;
			
		}
		
		out("Line Recieved: \""+string+"\"",2);
		
		// yeah okay, this used to cut it.
		if( string.charAt(string.length()-1)==';')
		{
			if(string.length()>1)
			{
				if(string.charAt(string.length()-2)!='\\')
				{
					out("Input Continuing...", 3);
					return true;
				}
			}
		}
		
		out("End of Input.", 3);
		return false;
		
	}
	
	
	public static String in()
	{
		System.out.print(">:");
		return sc.nextLine();
	}
	
	
	
	/**
	 *  The shell handles all user interaction, so the out method has a variety of ways of printing to console.
	 * @param toOut string to output
	 * @return returns true.
	 */
	public static boolean out( String toOut )
	{
		out(toOut, "AFL");
		return true;
	}
	
	
	
	// the origin is the location the message came from. 
	//If you're using this particular method the origin is probably going to be "Ziker 4",
	//but maybe not, who knows! certainly not me.
	
	// I'm keeping that comment for posterity, but yeah
	// If you are reading this, Mr. Bower, you might remember the pile of gibberish I handed in as a final
	// last year, which addressed itself as ziker 4
	// This is still the same project.
	// I mean it doesn't bear much resemblance at all, only this class and a couple methods are very similar...
	// And most everything got at least slightly touched if not completely destroyed or rewritten.
	/**
	 *  The shell handles all user interaction, so the out method has a variety of ways of printing to console.
	 * @param toOut string to output
	 * @param origin the sender of the message. typically 'Ziker 4'
	 * @return returns true.
	 */
	public static boolean out( String toOut, String origin )
	{
		out( toOut, origin, 0 );
		return true;
	}
	
	
	/**
	 *  The shell handles all user interaction, so the out method has a variety of ways of printing to console.
	 * @param toOut string to output
	 * @param origin the sender of the message. typically 'Ziker 4'
	 * @return returns true.
	 */
	public static boolean out( String toOut, int DebugLevel )
	{
		return out( toOut, "AFL", DebugLevel );		
	}
	
	
	/**
	 * The debug level controls whether the message is shown. Typically, it won't be if the debug level is greater than 1.
	 * debug is set by a special command, where 0 is off and the higher it is the more messages are shown
 	 * I'm not sure what the max is at the time of writing... maybe 3?
	 * @param toOut information to output to user
	 * @param origin the sender of the message. typically 'Ziker 4'
	 * @param debugLevel the required debug level for the message to be displayed to the user
	 * @return returns true if the message was displayed
	 */
	
	
	public static boolean out( String toOut, String origin, int debugLevel ) 
	{
		
		
		// you might argue that 0 should be a final like MAX_DEBUG_LEVEL, and you might be right. but it isn't.
		// check that debugLevel is valid, though I guess it doesn't really matter.
		if( Shell.debugLevel > MAX_DEBUG_LEVEL || Shell.debugLevel < 0 )
		{
			throw new IllegalArgumentException
			(
					origin + " attempted to send a message \"" + toOut + "\" with invalid debug level " 
					+ Shell.debugLevel + ". Expected range is 0-" + MAX_DEBUG_LEVEL + "."
			);
		}
		
		// if the debugLevel of the message is higher than the shell's, we don't show the message.
		if( Shell.debugLevel < debugLevel )
		{
			return false;
		}
		
		String[] out = toOut.split("\n");
		for(String s: out)
		{
			System.out.println( origin + ": " + s );
		}
		
		return true;
	}
	
	
	
	
	// I don't know if this will be used, but may as well include it now.
	/**
	 * The debug level controls whether the message is shown. Typically, it won't be if the debug level is greater than 1.
	 * debug is set by a special command, where 0 is off and the higher it is the more messages are shown
 	 * I'm not sure what the max is at the time of writing... maybe 3?
	 * @param toOut information to output to user
	 * @param origin the sender of the message. typically 'Ziker 4'
	 * @param debugLevel the required debug level for the message to be displayed to the user
	 * @param line the line number the message came from/
	 * @return returns true if the message was displayed
	 */
	public static void error( String toOut, int line )
	{
		if(line ==-2)
		{
			System.out.println( "AFL: Error on [Native Code]: "+toOut);
			return;
		}
		System.out.println( "AFL: Error on line "+line+": "+toOut);
		
	}
	
	public static void errorAt(String name, int line)
	{
		System.out.println("AFL:    on line "+line+" of "+name);
	}
}
