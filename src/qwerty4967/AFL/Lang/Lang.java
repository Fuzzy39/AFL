package qwerty4967.AFL.Lang;

import java.util.ArrayList;

import qwerty4967.AFL.Function.ControlFunction;

public class Lang 
{

	// Contains all of the INNERMOST SECRETS
		// nice!
		// but in reality this just holds a bunch of common definitions for ziker.
		// effectively a struct.
		
		// various things in the lang package are hardcoded in various ways, so while
		// it seems like you should be able to just add stuff to these and expect it to work,
		// that won't always be the case.
		
		public static final String[] TYPES= {"num","string","char","bool"};
		
		public static final String[] BOOLS= {"true","false"}; // wow
		
		public static final int MAXIMUM_DEPTH =100; //  wanted the int limit, but that gave me errors...
		
		//public static final String[] CONTROL_FUNCTIONS = {"if","else","while","=","return","end"};
		// set by main
		public static ArrayList<ControlFunction> controlFunctions;
		
		// this list doesn't include semicolons, because those are dealt with stupidly.
		public static final char[] ESCAPED_CHARS = {'n','\'','\"','\\'};
		
		// I wasn't sure whether it would make more sense to make a series of enums or just do a two dimensional array, so
		// feel free to laugh at me, future self, if this was a bad decision 
		
}
