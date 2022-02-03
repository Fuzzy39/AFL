package qwerty4967.AFL.Lang;

import java.util.ArrayList;



public class Lang 
{

	// Contains all of the INNERMOST SECRETS
		// nice!
		// but in reality this just holds a bunch of common definitions for ziker.
		// effectively a struct.
		
		// various things in the lang package are hardcoded in various ways, so while
		// it seems like you should be able to just add stuff to these and expect it to work,
		// that won't always be the case.
		
		public static final String[] TYPES= {"num","string","char","bool","array","type"};
		
		public static final String[] BOOLS= {"true","false"}; // wow
		
		public static final int MAXIMUM_DEPTH =500; //  wanted the int limit, but that gave me errors...
		
		//public static final String[] CONTROL_FUNCTIONS = {"if","else","while","=","return","end"};

		// Names of reserved functions, that cannot be used by an AFL user.
		// note that for is reserved, despite not being a control function (at the time of writing).
		public static final String[] CONTROL_FUNCTIONS= {"=","if","while","return","continue","break","function","end","for"};
		
		// this list doesn't include semicolons, because those are dealt with stupidly.
		public static final char[] ESCAPED_CHARS = {'n','\'','\"','\\','#'};
		
		// I wasn't sure whether it would make more sense to make a series of enums or just do a two dimensional array, so
		// feel free to laugh at me, future self, if this was a bad decision 
		
}
