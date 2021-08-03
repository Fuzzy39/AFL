package qwerty4967.Ziker4.FunctionStructures;

public class Node extends ElementContainer 
{
	// this class is just the  container. it only exists as a contrast to Statement.
	// it is nothing
	// at all.
	
	
	
	/**
	 * 
	 * @param group
	 */
	public Node( FunctionalGroup group)
	{
		super(group);
		name="Node";
	}
	
	/**
	 * 
	 * @param group
	 * @param parent
	 * @throws Exception
	 */
	public Node(FunctionalGroup group, ElementContainer parent) throws Exception
	{
		super(group, parent);
		name="Node";
	}
	
	public ElementContainer copy()
	{
		Node n;
		try 
		{
			n = new Node(this.getGroup(),this.getParent());
		} 
		catch (Exception e) 
		{
			
			e.printStackTrace();
			return null;
		}
		int size = this.getSize();
		for(int i = 0; i<size; i++)
		{
			n.addChild(this.getChild(i).copy());
		}
		
		return n;
	
	}
}
