package qwerty4967.AFL.Function;
import qwerty4967.AFL.Interpret.*;
import qwerty4967.AFL.ParseTree.*;

public abstract class Function 
{
	// ah, the finery of a pure white, uncluttered file.
	// you know how expensive it is to make a new file, after all.
	
	// of course we are about to utterly ruin that, but so it goes.
	// such a shame...
	
	private final String NAME;
	private final int PARAMS; // the amount of parameters the function has.
	
	public Function(String name, int parameters)
	{
		NAME=name;
		PARAMS=parameters;
	}
	
	public String getName()
	{
		return NAME;
	}
	
	public int getParameters()
	{
		return PARAMS;
	}
	
	public boolean addToNamespace()
	{
		return Namespace.addFunction(this);
	}
	
	public abstract Token call( Token[] Tokens);

	@Override
	public String toString() 
	{
		return "Function [name=" + NAME + ", parameters=" + PARAMS + "]";
	}


	// mmm, auto generated
	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Function other = (Function) obj;
		if (NAME == null) 
		{
			if (other.NAME != null)
				return false;
		} else if (!NAME.equals(other.NAME))
			return false;
		if (PARAMS != other.PARAMS)
			return false;
		return true;
	}
	
	
	
	
}
