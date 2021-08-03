package qwerty4967.Ziker4.FunctionStructures;

public class Statement extends ElementContainer
{
	// this class is just the  container. it only exists as a contrast to Statement.
	// it is nothing
	// at all.
	// ... I may have made some questionable descisions regarding the design of this at the start.
	// but at first it made sense, so...
	// learning is happening, maybe.
	// this is much better than Ziker 3 anyways.
	// if Mr. Bower is reading this ( do you go through every class? I guess so)
	// Ziker 3 was a thing I tried to show you at the start of last school year
	// I know you're very busy and ziker 3 was terrible anyways, so...
	// whatever.
	// was there a point I was going to make?
	// no?
	// hmm
	// why do I never use multi-line comments?
	// I don't know.
	
	
	// ANYWAYS, statements are just the same as nodes.
	// just a seperate type
	// except they have line numbers too.
	
	private int statementNumber;
	
	/**
	 * 
	 * @param num
	 * @param group
	 */
	public Statement( int num, FunctionalGroup group)
	{
		super(group);
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
	public Statement(int num, FunctionalGroup group, ElementContainer parent) throws Exception
	{
		super(group, parent);
		statementNumber=num;
		name="Statement";
	}
	
	/**
	 * 
	 * @return statementNumber
	 */
	public int getStatementNumber()
	{
		return statementNumber;
	}
	
	
	public ElementContainer copy()
	{
		Statement s;
		try 
		{
			s = new Statement(this.getStatementNumber(),this.getGroup(),this.getParent());
		} 
		catch (Exception e) 
		{
			
			e.printStackTrace();
			return null;
		}
		int size = this.getSize();
		for(int i = 0; i<size; i++)
		{
			s.addChild(this.getChild(i).copy());
		}
		
		return s;
	
	}
	
	
}
