package qwerty4967.AFL.ParseTree;
import qwerty4967.AFL.Function.*;

public class FunctionCall extends Container
{
	private String functionName;
	
	public FunctionCall( String functionName, HasChildren parent)
	{
		super(parent);
		this.functionName = functionName;
		this.name="Function "+functionName;
	}
	
	
	public String getFunctionName()
	{
		return functionName;
	}
	
	
	
}
