package qwerty4967.AFL.ParseTree;

import qwerty4967.AFL.Function.*;

public class Statement extends Container 
{
	// Statements can only have one child, and can only have root as a parent.
	
	private int statementNumber;
	
	/**
	 * 
	 * @param num
	 * @param group
	 */
	public Statement( int num, AFLFunction function)
	{
		super(function);
		statementNumber=num;
		name="Statement";
	}
	
	/**
	 * 
	 * @param num
	 * @param group
	 * @param parent
	 * @throws Exception
	 */
	public Statement(int num, AFLFunction function, Container parent)
	{
		super(function, parent);
		System.out.println("ATTEMPTED TO ADD A STATEMENT TO A CONTAINER");
		System.exit(-1);
		
	}
	
	/**
	 * 
	 * @return statementNumber
	 */
	public int getStatementNumber()
	{
		return statementNumber;
	}
	
	
	
	
	
	
}
