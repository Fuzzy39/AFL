package qwerty4967.Ziker4;

import java.util.ArrayList;

public class FunctionalGroup implements HasChildren
{
	// A Functional Group is a function made of Ziker code.
	// It is also any bock of independent code, which for all intents and purposes is a void function with no perameters.
	private ArrayList<FunctionalElement> allChildren = new ArrayList<FunctionalElement>(); // includes children of children, etc.
	private ArrayList<FunctionalElement> children=new ArrayList<FunctionalElement>();
	private final String name;
	private final Type returnType;
	private final ArrayList<Type> perameters;
	
	public FunctionalGroup(String name)
	{
		this(name, Type.none, new ArrayList<Type>());
	}	
	
	public FunctionalGroup(String name, Type returnType, ArrayList<Type> perameters)
	{
		this.name=name;
		this.returnType=returnType;
		this.perameters=perameters;
	}
	

	public int addChild( FunctionalElement child)
	{
		// this never fails, I proclaimed proudly without any testing.
		// whatever.
		
		children.add(child);
		allChildren.add(child);
		return children.size()-1;
	}
	
	@Override
	public String toString() {
		return "FunctionalGroup [allChildren=" + allChildren + ", children=" + children + ", name=" + name
				+ ", returnType=" + returnType + ", perameters=" + perameters + "]";
	}

	public void removeChild(FunctionalElement child)
	{
		if(child instanceof ElementContainer)
		{
			ElementContainer container = (ElementContainer)child;
			removeChildrenOfChild(container);
		}
		
		allChildren.remove(child);
		children.remove(child);
		
		
	}
	
	// because they aren't going to remove themselves, though they do add themselves.
	private void removeChildrenOfChild(ElementContainer container)
	{
		for(int i=0; i<container.getSize(); i++)
		{
			
			if(container.getChild(i) instanceof ElementContainer)
			{
				removeChildrenOfChild((ElementContainer)container.getChild(i));
			}
			allChildren.remove(container.getChild(i));	
		}
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
	
	public boolean add( FunctionalElement toAdd)
	{
		// adds to all children.
		allChildren.add(toAdd);
		return true;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the returnType
	 */
	public Type getReturnType() {
		return returnType;
	}

	/**
	 * @return the perameters
	 */
	public ArrayList<Type> getPerameters() {
		return perameters;
	}
	
	
	
}
