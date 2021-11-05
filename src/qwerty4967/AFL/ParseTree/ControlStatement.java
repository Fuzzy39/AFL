package qwerty4967.AFL.ParseTree;

import java.util.ArrayList;

import qwerty4967.AFL.Shell;
import qwerty4967.AFL.Function.AFLFunction;

public class ControlStatement extends Statement 
{
	// this thing is a bit of a wacky fusion of a Statement and a FunctionCall
	
	private String function;
	private Group parameters;
	
	// take a statement, husk its corpse, etc.
	public ControlStatement(int num, Statement from)
	{
		super(num, from.getParent());
		
		// do some prep.
		HasChildren parent = from.getParent();
		this.name="ControlStatement";
		
		parameters= new Group();
		
		if(from instanceof ControlStatement)
		{
			// who are you trying to trick here?
			// begone, fool!
			System.out.println("ATTEMPTED TO CREATE CONTROL STATEMENT FROM CONTROLSTATEMENT: "+from);
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
		
		for(int i = 0; i<function.getSize();)
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
		String p = getParameterString();
		this.name +=" "+this.function+":\n"+p;
	}
	
	
	private String getParameterString()
	{
		// this is some kind of tumor.
		
		String toReturn = "    Parameters:";
		for(int i=0; i<parameters.getSize(); i++)
		{
			String child =parameters.getChild(i).toString();
			String[] childsChilds = child.split("\n");
			for(int j = 0; j<childsChilds.length;j++)
			{
				toReturn+="\n        "+childsChilds[j];
			}
		}
		return toReturn;
	}
	
	public ControlStatement copy()
	{
		Shell.out("CANNOT COPY CONTROLSTATEMENT (due to laziness, rather than any good reason)");
		System.exit(-1);
		return null;
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
