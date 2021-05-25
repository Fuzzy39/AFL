package qwerty4967.Ziker4;

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
	}
}
