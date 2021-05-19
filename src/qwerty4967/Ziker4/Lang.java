package qwerty4967.Ziker4;

public class Lang 
{
	// Contains all of the INNERMOST SECRETS
	// nice!
	// but in reality this just holds a bunch of common definitions for ziker.
	// effectively a struct.
	
	public static final String[] CONTROL_KEYWORDS= {"if","while","else","function","end"};
	
	public static final int MAXIMUM_DEPTH =10000; //  wanted the int limit, but that gave me errors...
	
	public enum tokenTypes
	{
		variable,
		function,
		keyword,
		number,
		string,
		character,
		bool,
		operator,
		prototext // this is a dangerous one - only used while parsing, it should never be encountered in runtime.
	}
	
	public static final String[] OPERATORS = 
		{"==","!=",">=","<=",">","<","=","+","-","*","/","%","[","]","(",")","," };
}
