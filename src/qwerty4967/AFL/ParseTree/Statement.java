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
	
	
	@Override
	public int addChild( Element toAdd)
	{
		// Statements can only have the one child.
		// it's the rules, kid.
		// I think that joke was so bad it can't even be classified as such.
		// welp.
		if(this.getSize()!=0)
		{
			// I should probably throw an exception, that would be the polite thing to do
			// or maybe this should be an assertion?
			System.out.println("ATTEMPTED TO GIVE A STATEMENT MORE THAN ONE CHILD");
			System.exit(-1);
			
		}
		
		return super.addChild(toAdd);
		
		
	}
	
	
	
	
	
}
