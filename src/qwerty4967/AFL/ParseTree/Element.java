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
		

	private AFLFunction function;
	private Container parent;
	
	// I'm not sure if this is necessary, but I'll put it in because it can only make things easier.
	protected int ID;
	
	// now that I think about it I wonder if java already has a tree thing like I'm making?
	// meh, probably not, mine is kind of weird.
	/**
	 * this throws an exception.
	 * it is the cauase of many problems
	 * it is impossible for this to actually cause an exception
	 * but yet it throws it.
	 * @param function
	 * @param parent
	 * @throws Exception
	 */
	public Element( AFLFunction function, Container parent)
	{
		
		if( !function.add(this))
		{
			assert false; 
		}
		
		this.function=function;
		this.ID = -1;
		int id=parent.addChild(this);
		
		if( id == -1) // adding child failed
		{
			// it might be possible for it to be impossible for adding this stuff to fail, I'm not sure.
			assert false;
		}
		
		this.ID=id;
		this.parent=parent;
		
	}
	
	/**
	 * 
	 * @param function
	 */
	public Element( AFLFunction function)
	{
		// in this case the element has no container as a parent, and is directly part of the function.
		int id = function.addChild(this);
		
	
		
		this.function=function;
		this.ID=id;
		this.parent=null;
		
	}

	
	/** 
	 * get the id
	 * @return
	 */
	public int getID()
	{
		if(parent!=null)
		{	
			assert this==parent.getChild(ID);
		}
		else
		{
			assert this==function.getChild(ID);
		}
		
		return ID;
	}
	
	/**
	 * do the thing
	 * @return
	 */
	public Container getParent()
	{
		return parent;
	}
	
	/**
	 * this is really standard
	 * @return
	 */
	public AFLFunction getFunction()
	{
		return function;
	}
	
	/**
	 * add to a new parent.
	 * @param newParent
	 * @return
	 */
	protected boolean addTo( Container newParent, int id)
	{
		
		
		// now, it's actually usefull to check for the EXACT same object here,
		// it needs to be the same, not just a copy or something.
		// why? it doesn't have to be...
		// no real reason, but I know that an element should never travel between functions.
		
		if(!newParent.getFunction().equals(this.getFunction()))
		{
			return false;
		}
		
		if(newParent != parent)
		{
			if(parent!= null)
			{
				parent.removeChild(this);
			}
			else
			{
				// okay, we are moving from root to, presumably, a new container.
				if(this.ID!=-1)
				{
					function.removeChild(this);
				}
			}
		}
		
		this.ID=id;
		this.parent=newParent;
		return true;
	}
	
	/**
	 * remove from parent, delete the object more or less
	 */
	public void remove()
	{
		// delete the object, basically.
		if(parent!=null)
		{
			parent.removeChild(this);
			this.parent=null;
		}
		
		function.removeChild(this);
		this.ID=-1;
		function=null;
		
	}
	
	public void removeFromParent()
	{
		if(parent!=null)
		{
			parent.removeChild(this);
			this.parent=null;
		}
	}

	@Override
	/**
	 * standard
	 */
	public String toString() {
		return "FunctionalElement [function=" + function + ", parent=" + parent + ", ID=" + ID + "]";
	}
	
	public void setID(int newID, Object o )
	{
		if(o instanceof HasChildren)
		{	
			ID=newID;
		}
	}


}
