package qwerty4967.Ziker4;

import java.util.ArrayList;

public abstract class ElementContainer extends FunctionalElement implements HasChildren
{
	// Time to get down to buisness
	// of the very abstract.
	// I've maybe slightly been dreading this, but only because I keep putting it off.
	// functional group was more complicated than this, and very similar.
	// introduce this object, which is a copy of functional group with most of it stripped out.
	// wow.
	private ArrayList<FunctionalElement> children=new ArrayList<FunctionalElement>();
	
	/**
	 * 
	 * @param group
	 */
	public ElementContainer( FunctionalGroup group)
	{
		super(group);
	}
	
	/**
	 * 
	 * @param group
	 * @param parent
	 * @throws Exception
	 */
	public ElementContainer (FunctionalGroup group, ElementContainer parent) throws Exception
	{
		
		
		super(group, parent);

	}
	
	/**
	 * adds a child to the element.
	 * returns the id of the child.
	 */
	public int addChild( FunctionalElement child)
	{
		// this never fails, I proclaimed proudly without any testing.
		// whatever.
		
		children.add(child);
		return children.size()-1;
	}
	
	
	/**
	 * removes a child 
	 */
	public void removeChild(FunctionalElement child)
	{
		if(child instanceof ElementContainer)
		{
			ElementContainer container = (ElementContainer)child;
			
		}
		
		children.remove(child);
		
		
	}
	
	/**
	 * why isn't the javadoc working?
	 */
	public void removeChild(int ID)
	{
		
		removeChild(children.get(ID));
	}
	
	public FunctionalElement getChild(int ID)
	{
		return children.get(ID);
	}
	
	// returns -1 on failure.
	/**
	 * gets id of child
	 * returns -1 on failure.
	 */
	public int getChildID(FunctionalElement child)
	{
		
		for (int i = 0; i < children.size(); i++) 
		{
			if( children.get(i) == child)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/** 
	 * gets size
	 */
	public int getSize()
	{
		return children.size();
	}
	
	/**
	 * removes all children
	 */
	public void removeAllChildren()
	{
		
		children= new ArrayList<FunctionalElement>();
	}

	@Override
	/**
	 * toString, pretty standard, ish.
	 */
	public String toString() 
	{
		String tree="";
		for(int i = 0; i<children.size(); i++)
		{
			tree+="\n    "+children.get(i);
		}
		
		return "ElementContainer:    " + tree;
	}
	
	
	
}
