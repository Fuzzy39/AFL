package qwerty4967.AFL.ParseTree;
import qwerty4967.AFL.Function.*;

public class FunctionCall extends Container
{
	private String functionName;
	
	public FunctionCall( String functionName, AFLFunction function)
	{
		super(function);
		this.functionName = functionName;
		this.name="Function "+functionName;
	}
	
	public FunctionCall( String functionName, AFLFunction function, Container parent)
	{
		super(function, parent);
		this.functionName = functionName;
		this.name="Function "+functionName;
	}
	
	public String getFunctionName()
	{
		return functionName;
	}
	
	
	
}
