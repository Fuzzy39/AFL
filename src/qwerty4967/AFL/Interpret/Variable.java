package qwerty4967.AFL.Interpret;

import qwerty4967.AFL.ParseTree.*;
import qwerty4967.AFL.Function.*;
public class Variable 
{
	// Shock of shocks!
	// A class out side of ParseTree or Function that 
	// doesn't just have static methods?
	// I know, right?
	
	// this class ought to be pretty simple
	// a name, value, and scope.
	// alright?
	
	private String name;
	private Token value;
	private AFLFunction scope;

	public Variable(String name, Token value, AFLFunction scope)
	{
		this.name=name;
		this.value=value;
		this.scope=scope;
		if(scope==null||scope.getName().equals("_main") )
		{
			this.scope=null;
		}
	}
	
	public Variable(String name, Token value)
	{
		this(name, value, null);
	}

	public Token getValue() {
		return value;
	}

	public void setValue(Token value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public AFLFunction getScope() {
		return scope;
	}

	@Override
	public String toString() {
		if(scope==null)
		{
			// derp derp derp.
			// this is silly, but whatever.
			return "Variable [name=" + name + ", value=" + value + ", scope=null]";
		}
		return "Variable [name=" + name + ", value=" + value + ", scope=" + scope.toShortString() + "]";
	}


	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Variable other = (Variable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		
		// okay...
		// yeah, if either scope is null they are equal.
		// this will obviously be a problem down the road for somebody, and that will be me writting namespaces in about thirty seconds.
		// oh well.
		if (!(scope == null || other.scope==null)) 
		{
			if (!(scope==other.scope ))
			{
				return false;
	
			}
		}
		return true;
	}
	
	public boolean isInScope(AFLFunction toCheck)
	{
		if(scope==toCheck)
		{
			return true;
		}
		if(toCheck==null)
		{
			return true;
		}
		
		return false;
	}
	
}
