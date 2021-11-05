package qwerty4967.AFL.ParseTree;
import qwerty4967.AFL.Function.*;

public class FunctionCall extends Container
{
	private String functionName;
	
	public FunctionCall( String functionName)
	{
		super();
		this.functionName = functionName;
		this.name="Function "+functionName;
	}
	
	public FunctionCall( String functionName, HasChildren parent)
	{
		super(parent);
		this.functionName = functionName;
		this.name="Function "+functionName;
	}
	
	public FunctionCall copy()
	{
		FunctionCall toReturn = new FunctionCall(getFunctionName());
		for(int i = 0; i<this.getSize(); i++)
		{
			Element child = getChild(i);
			toReturn.addChild(child.copy());
		}
		return toReturn;
	}
	
	public String getFunctionName()
	{
		return functionName;
	}
	
	
	
}
