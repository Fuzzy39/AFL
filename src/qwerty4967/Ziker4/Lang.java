
package qwerty4967.Ziker4;

public class Lang 
{
	// Contains all of the INNERMOST SECRETS
	// nice!
	// but in reality this just holds a bunch of common definitions for ziker.
	// effectively a struct.
	
	public static final String[] KEYWORDS= {"if","while","else","function","end"};
	
	public static final String[] BOOLS= {"true","false"}; // wow
	
	public static final int MAXIMUM_DEPTH =10000; //  wanted the int limit, but that gave me errors...
	
	public static enum tokenTypes
	{
		variable,
		function,
		keyword,
		number,
		string,
		character,
		bool,
		operator,
		prototext, // this is a dangerous one - only used while parsing, it should never be encountered in runtime.
		operation, // huh? you say? do not fret. - operations are similar to operators, but fully defined, and not just symbols
		groupPointer // also only used while parsing, specifically '(' and ')'s.
	}
	
	// I wasn't sure whether it would make more sense to make a series of enums or just do a two dimensional array, so
	// feel free to laugh at me, future self, if this was a bad decision 
	public static final String[][] OPERATORS = 
	{
		{"(",")" }, // organize (these operators are special)
		{"="}, // assign
		{"==","!=",">=","<=",">","<"}, // compare
		{"+","-"}, // add and subtract.
		{"*","/","%"} // multiply and divide
		
		
		
		
	};
	
	public static enum OPERATION_PRIORITY_GROUPS
	{
		organize,
		assign,
		compare,
		add,
		multiply
		
		
	}
	
}	

	
	
