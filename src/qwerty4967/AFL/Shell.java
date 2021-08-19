package qwerty4967.AFL;
import java.util.Scanner;



public class Shell 
{
	// Here is a comment, in the mildly vain hope that I might remember to put something more meaningful here.
	// I've got to say, I'm excited to get started on something new.
	// Hopefully it won't take months... (4 months later: HAH)
	// Started 4/30/21, for Computer Science II final project.
	// Hopefully there aren't any requirements in there that will be a challenge... 
	// I can shoe-horn some stuff in if need-be.
	
	
	
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
	
	
	private static final int build = 329;
	
	private static int debugLevel = 4; // ranges from 0 to max, inclusive, determines the prevalence of debug messages.
	public static final int MAX_DEBUG_LEVEL=4; // may as well make it public, it's final.
	
	private static Scanner sc= new Scanner( System.in ); // Setup the Scanner for gathering user input. 
	
	
	/**
	 * I don't think I've done this to the main method before...
	 * Oh well, it's about what you expect
	 * If you want more detailed explanations of what things do and how they work, they might be in the code's comments
	 * only sometimes, of course.
	 * @param args does nothing, yet. 
	 */
	public static void main(String[] args) 
	{
		
		  
		
		// What to put here?
		// let's worry about that later...
		
		// TODO replace this with an AFL function in the very long term.
		out("AFL !WARNING: not functional in the slightest! | build: "+build+" | debug: "+debugLevel);
		out("");
		
		
		// and now the actual stuff.
		
		String inputBuffer="";
		while(true)
		{
			//Grab the input
			String currentLine = Shell.in();
			inputBuffer+=currentLine.strip();
			
			if(checkString(currentLine))
			{
				// input is continuing.
				continue;
			}
			
			//input is done.
			out("Input Recieved: \""+inputBuffer+"\"","Ziker 4", 1);
			
			
			
			// Attempt to parse the program:
			Parser.Parse(inputBuffer);
			
			// clear the input buffer
			inputBuffer="";
			
			//TODO You know, try running the program
			// something like Interpreter.run("_Main");
			
			
			
		}
		
		
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
		
		if(string.length()==0)
		{
			//doesn't really matter.
			out("No Input.","Ziker 4", 3);
			return true;
			
		}
		
		out("Line Recieved: \""+string+"\"","Ziker 4",2);
		
		if( string.charAt(string.length()-1)==';')
		{
			out("Input Continuing...","Ziker 4", 3);
			return true;
		}
		
		out("End of Input.","Ziker 4", 3);
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
		
		
		System.out.println( origin + ": " + toOut );
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
	public static boolean out( String toOut, String origin, int debugLevel, int line )
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
		if(Shell.debugLevel < debugLevel)
		{
			return false;
		}
		
		
		System.out.println( origin+", Line "+line+": "+toOut);
		return true;
	}
}