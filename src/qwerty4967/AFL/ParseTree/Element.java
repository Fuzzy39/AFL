package qwerty4967.AFL.ParseTree;

import qwerty4967.AFL.Function.AFLFunction;

public abstract class Element 
{
	// this is the second class that I wrote, which is true.
	// The class when I wrote that sentence doesn't bare a ton of resemblance to what it is now
	// used to be called FunctionalElement, for example.
	
	// An Element is the most basic thing in a parse tree.
	// Parse trees are made of elements, with a function at their root
	// Elements know what Function they are part of,
	// as well as their direct parent, and their index in their parent's list of children
	// If I've done my job right, at least.
		

	
	private HasChildren parent;
	
	// I'm not sure if this is necessary, but I'll put it in because it can only make things easier.
	// As it turns out, very helpful.
	protected int ID;
	
	
	public Element(HasChildren parent)
	{
		this.parent=parent;
		this.ID=parent.addChild(this);
	}

	
	/** 
	 * get the id
	 * @return
	 */
	public int getID()
	{
		assert this==parent.getChild(ID);
		return ID;
	}
	
	/**
	 * do the thing
	 * @return
	 */
	public HasChildren getParent()
	{
		return parent;
	}
	
	/**
	 * this is really standard
	 * @return
	 */
	public AFLFunction getFunction()
	{
		HasChildren root = getRoot();
		if(root instanceof AFLFunction)
		{
			return (AFLFunction)root;
		}
		return null;
	}
	
	public Group getGroup()
	{
		HasChildren root = getRoot();
		if(root instanceof Group)
		{
			return (Group)root;
		}
		return null;
	}
	
	public HasChildren getRoot()
	{
		HasChildren p = parent;
		while(p instanceof Container)
		{
			p=((Container)p).getParent();
		}
		return p;
	}
	
	
	/**
	 * add to a new parent.
	 * @param newParent
	 * @return
	 */
	protected boolean moveTo( HasChildren newParent, int id)
	{
		// this is only false during an element's initialization.
		if(parent!=null)
		{
			parent.removeChild(this);
		}
		//newParent.addChild(id, this);
		
		this.ID=id;
		this.parent=newParent;
		return true;
	}
	
	
	
	/**
	 * remove from parent, delete the object more or less
	 */
	protected void remove()
	{
		// delete the object, basically.
		
	
		this.parent=null;
		this.ID=-1;
	
		
	}
	
	

	@Override
	/**
	 * standard
	 * 
	 */
	public String toString() 
	{
		// if this were to list it's parents, then it would never end, so that's bad, and won't happen.
		return "Element [ID=" + ID + "]";
	}
	
	// this is to expand visibility to AFLFunctions, for when they need to re-sync their children's ID's.
	// seems weird, but I can't think of a more reasonable solution...
	public boolean setID(int newID, HasChildren parent)
	{
		if(this.parent==parent)
		{
			ID=newID;
			return true;
		}
		return false;
	}

}
