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
	
	public ElementContainer( FunctionalGroup group)
	{
		super(group);
	}
	
	public ElementContainer (FunctionalGroup group, ElementContainer parent) throws Exception
	{
		
		
		super(group, parent);

	}
	

	public int addChild( FunctionalElement child)
	{
		// this never fails, I proclaimed proudly without any testing.
		// whatever.
		
		children.add(child);
		return children.size()-1;
	}
	
	

	public void removeChild(FunctionalElement child)
	{
		if(child instanceof ElementContainer)
		{
			ElementContainer container = (ElementContainer)child;
			
		}
		
		children.remove(child);
		
		
	}
	
	
	public void removeChild(int ID)
	{
		
		removeChild(children.get(ID));
	}
	
	public FunctionalElement getChild(int ID)
	{
		return children.get(ID);
	}
	
	// returns -1 on failure.
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
	
	
	public int getSize()
	{
		return children.size();
	}
	
	public void removeAllChildren()
	{
		
		children= new ArrayList<FunctionalElement>();
	}

	@Override
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
