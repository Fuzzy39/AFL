package qwerty4967.AFL.Lang;

public enum TokenType 
{
	variable, // first 4 are 'raw text'
	function,
	type,
	bool,
	number,
	string,
	character,
	operator,
	// used during parsing
	groupPointer,
	// used during interpreting
	arrayPointer,
	error, 
	voidToken // void's a keyword, oof.
}
