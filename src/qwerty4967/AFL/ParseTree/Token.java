package qwerty4967.AFL.ParseTree;

import qwerty4967.AFL.Function.AFLFunction;
import qwerty4967.AFL.Lang.*;

public class Token extends Element 
{
	// There's not a single comment here, I figure I ought to fix that.
	// tokens are pretty simple. they are elements that have a string, the token, and also contain
	// the type of the token.
	
	private String data;
	private TokenType type;
	

	public Token(String data, TokenType type, HasChildren parent) 
	{
		super(parent);
		this.data = data;
		this.type = type;
	}


	public String getData() {
		return data;
	}

	public void setData(String data) 
	{
		this.data = data;
	}

	public TokenType getType() 
	{
		return type;
	}

	public void setType(TokenType type) 
	{
		this.type = type;
	}

	@Override
	public String toString() 
	{
		return "Token [ ID:"+this.ID+" | \""+data  + "\", " + type + "]";
	}

}
