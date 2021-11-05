package qwerty4967.AFL.ParseTree;

import qwerty4967.AFL.Function.*;

public class Statement extends Container 
{
	// Statements can only have one child, and can only have root as a parent.
	
	private int statementNumber;
	
	public Statement(int num)
	{
		super();	
		statementNumber=num;
		name="Statement (Line: "+num+")";
	}
	/**
	 * 
	 * @param num
	 * @param group
	 */
	public Statement( int num, HasChildren parent)
	{
		super(parent);
		
		statementNumber=num;
		name="Statement (Line: "+num+")";
		
		//statements can only have ControlStatements and AFL Functions as parents
		// check that the parent is a legal entity, and not some ne'er-do-well.
		if( (!(parent instanceof ControlStatement)) & (! (parent instanceof AFLFunction)))
		{
			System.out.println("CONTROL STATEMENT HAS INVALID PARENT!");
			System.exit(-1);
		}
		

	}
	
	public Statement copy()
	{
		Statement toReturn = new Statement(statementNumber);
		for(int i = 0; i<this.getSize(); i++)
		{
			Element child = getChild(i);
			toReturn.addChild(child.copy());
		}
		return toReturn;
	}
	
	/**
	 * 
	 * @return statementNumber
	 */
	@Override
	public int getStatementNumber()
	{
		return statementNumber;
	}
		
	
	public void setStatementNumber(int statementNumber) 
	{
		this.statementNumber = statementNumber;
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
			if(!(this instanceof ControlStatement ))
			{
				System.out.println("ATTEMPTED TO GIVE A STATEMENT MORE THAN ONE CHILD");
				System.exit(-1);
			}
		}
		
		return super.addChild(toAdd);
		
		
	}
	
	
	
	
	
}
