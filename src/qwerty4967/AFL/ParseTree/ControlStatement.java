package qwerty4967.AFL.ParseTree;

import java.util.ArrayList;

import qwerty4967.AFL.Function.AFLFunction;

public class ControlStatement extends Container 
{
	// this thing is a bit of a wacky fusion of a Statement and a FunctionCall
	
	private String function;
	private Group parameters;
	
	// take a statement, husk it's corpse, etc.
	public ControlStatement( Statement from)
	{
		super(from.getParent());
		
		// do some prep.
		HasChildren parent = from.getParent();
		this.name="ControlStatement";
		
		parameters= new Group();
		
		// check that the parent is a legal entity, and not some ne'er-do-well.
		if( (!(parent instanceof ControlStatement)) & (! (parent instanceof AFLFunction)))
		{
			System.out.println("CONTROL STATEMENT HAS INVALID PARENT!");
			System.exit(-1);
		}
		
		
		// now, attempt to grab the children!
		if(!(from.getChild(0) instanceof FunctionCall))
		{
			System.out.println("CONTROL STATEMENT ATTEMPTED TO EXIST DESPITE INVALID STATEMENT: "+from);
			System.exit(-1);
		}
		
		// yes, child... come closer...
		FunctionCall function = (FunctionCall)from.getChild(0);
		this.function = function.getFunctionName();
		
		for(int i = 0; i<function.getSize(); i++)
		{
			parameters.addChild(function.getChild(i));
		}
		
		// the children have been snatched!
		
		// now, kill the parent and take it's place!
		int id= from.getID();
		parent.removeChild(from);
		parent.moveChild(id, this);
		
		// that probably works
		// this makes sense, yes.
		this.name +=" "+this.function+"\n    Parameters: "+parameters;
	}
	
	public String getFunctionName() 
	{
		return function;
	}
	
	public Group getParameters()
	{
		return parameters;
	}
	
	
}
