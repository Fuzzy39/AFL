package qwerty4967.Ziker4;

// The old version of the class is included in the code as a testiment to not planning.
// planning is important. you should do it before anything complicated
// and everything is complicated.
/*public class TreeClimber 
{

	// climbs programs.
	
	private boolean climbsStatements; // does this treeclimber climb Statements, or ignore them?
	private FunctionalGroup program; // the tree to climb.
	private int depth; // how deep have we gotten... ( far too deep, I suspect.)
	private int[] indexes;
	private boolean isDone;
	
	public TreeClimber(FunctionalGroup program, boolean climbsStatements)
	{
		this.climbsStatements=climbsStatements;
		this.program=program;
		
		depth=0;
		indexes = new int[Lang.MAXIMUM_DEPTH];
		for(int i =0; i<indexes.length; i++)
		{
			indexes[i]=0; // for, uh, reasons...
			// I don't understand them.
		}
		//indexes[0]=-1; // for reasons...
		isDone = false;
		
		
	}
	
	public DataElement nextData()
	{
		// I hope that I never need to change this method.
		// If I do need to change it it is far better to rewrite the whole thing.
		// what a mess.
		
		// start by getting our current object.
		for(int i=0; i<5; i++)
		{
			System.out.println("indexes ["+i+"]: "+indexes[i]);
		}
		
		if(indexes[0]>=program.getSize())
		{
			isDone=true;
			return null;
		}
		
		FunctionalElement current = program.getChild(indexes[0]);
		
		
		// try to figure out what current was last time.
		for(int i=0;current instanceof ElementContainer;i++ )
		{
			
			
			current = ((ElementContainer)current).getChild(indexes[i]);
			
		}
		
	
		// the previous code will always return an element.
		current=current.getParent();
		System.out.println("starting Current: "+current);
	
		// what does this loop do?
		// it is looping until it finds a suitible container.
	    for(;indexes[0]<program.getSize(); indexes[depth]++)
		{
			// okay I'm pretty confused....
			// we need to get the parent of the child, find the next element and...
			// check if it's a data or not
			// if not 
		
			System.out.println("depth: "+depth+" index count: "+indexes[depth]);
			System.out.println("Current: "+current);
			if( depth> 0)
			{
				// we went to the end, found nothing...
				if(indexes[depth]>=((ElementContainer)current).getSize())
				{
					System.out.println("Exiting container - reached the end of container size: "+((ElementContainer)current).getSize());
					indexes[depth]=0;
					depth--;
					
					continue;
				}
				
				
				
				FunctionalElement potentialNext = ((ElementContainer)current).getChild(indexes[depth]);
	
				// current is the parrent.
				if(potentialNext  instanceof Node || ( potentialNext instanceof Statement & climbsStatements )   )
				{
					System.out.println("found a container.");
					current = potentialNext;
					depth++;
					continue;
				}
				
				System.out.println("found a data.");
				indexes[depth]++;
				// we found something!
				return (DataElement) potentialNext;
				// probably...
			
			}
			// depth is zero.
			
			// so what now?
			// same thing!
			FunctionalElement potentialNext = program.getChild(indexes[depth]);
			
			// current is the parrent.
			if(potentialNext  instanceof Node || ( potentialNext instanceof Statement & climbsStatements )   )
			{
				System.out.println("found a container in root.");
				current = potentialNext;
				depth++;
				continue;
			}
			
			System.out.println("found data in root.");
			// we found something!
			indexes[depth]++;
			return (DataElement) potentialNext;
			
		}
		
		isDone=true;
		return null;
	}
	
	
	
	public boolean hasNext()
	{
		return !isDone;
	}
	
	
}*/


public class TreeClimber 
{

	// climbs programs.
	
	private boolean climbsStatements; // does this treeclimber climb Statements, or ignore them?
	private FunctionalGroup program; // the tree to climb.
	private FunctionalElement lastElement;
	
	
	public TreeClimber(FunctionalGroup program, boolean climbsStatements)
	{
		this.climbsStatements=climbsStatements;
		this.program=program;
		this.lastElement=null;
	}
	
	public DataElement nextData()
	{
		// my brain is empty, but not without ideas.
		// so, lets try them
		// and watch them fail horribly...
		// whatever.
		
		// the first step is to determine the element we are to check for.
		// then we need to check if it is valid.
		// if it is not valid there are various responses required.
		FunctionalElement toReturn=null;
		while(!(toReturn instanceof DataElement))
		{
			lastElement=getNext(lastElement, false);
			toReturn=lastElement;
			if(lastElement==null)
			{
				
				return null;
			}
			
		}
		return (DataElement) lastElement;
		
		
	}
	
	private FunctionalElement getNext( FunctionalElement prev, boolean ignoreContainers)
	{	

		// this method just rudely grabs what the next element is, regardless of what it is.
		// it doesn't check if it is data.
		
		
		
		if(prev==null)
		{
			// we are starting here, there was no last element.
			
			// no program, no elements
			if(program.getSize()==0)
			{
				return null;
			}
			
			
			// otherwise we just take the first element.
			Shell.out("taking first element","Group Monkey",3);
			return program.getChild(0);
			
		}
		
		
		// first check if the previous element was a container.
		Shell.out("Last Element: "+prev,"Group Monkey",3);
		
		if(prev instanceof Node || (prev instanceof Statement & climbsStatements))
		{
			if( ((ElementContainer)prev).getSize() !=0)
			{
				if(! ignoreContainers)
				{
					Shell.out("It is a container.","Group Monkey",3);
					return ((ElementContainer)prev).getChild(0);
				}
			}
		}
				
		
		// in this case, there is a last, and we need to determine the next thing.
		// this should be one line of code, but it isnt.
		// so...
		// yeah.
		
		// what can happen in these conditions?
		// well...
		// first, the last could either be attached to root, or not.
		// then, the last could be the last of it's container or root.
		
		if(prev.getParent()==null)
		{
			// prev is attached to root.
			// now keep in mind that we don't care whether what we return is a container or not.
			int id =prev.getID();
			
			
			if(id+1<program.getSize())
			{
				Shell.out("Got element in root...","Group Monkey",3);
				return program.getChild(id+1);
			}
			
			// we have reached the end of the program.
			Shell.out("End of FunctionalGroup reached.","Group Monkey",3);
			return null;
		}
		
		// in this case, the  last element was part of a container.
		
		// what can happen here?
		// well, we can either reach the end of the container, or not.
		
		int id =prev.getID();
		
		if(id+1<prev.getParent().getSize())
		{
			Shell.out("Got element in a container.","Group Monkey",3);
			return prev.getParent().getChild(id+1);
		}
		
		// we have reached the end of the container.
		// what now?
		Shell.out("End of Container reached.","Group Monkey",3);
		return getNext(prev.getParent(),true);
	}
	
}
