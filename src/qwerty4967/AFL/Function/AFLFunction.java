package qwerty4967.AFL.Function;

import java.util.ArrayList;
import qwerty4967.AFL.ParseTree.*;

public class AFLFunction implements qwerty4967.AFL.ParseTree.HasChildren
{
		
		// It is also any bock of independent code, which for all intents and purposes is a void function with no perameters.
		
		private ArrayList<Element> children=new ArrayList<Element>();
		private final String name;
		private final int perameters; // how many perameters the functionalGroup has.
		// TODO figure out how perameters work better.
		/**
		 * 
		 * @param name
		 */
		public AFLFunction(String name)
		{
			this(name, 0);
		}	
		
		public AFLFunction(String name, int perameters)
		{
			this.name=name;
			this.perameters=perameters;
		}
		
		
		/**
		 * adds a child to the group. returns ID.
		 */
		public int addChild( Element child)
		{
			// this never fails, I proclaimed proudly without any testing.
			// whatever.
			
			// some serious work needs to be done for this to make sense
			//TODO fix this	

			
			children.add(child);
			return children.size()-1;
		}
		
		public void addChild(int index, Element child)
		{
			// really very simple
			// having this method earlier would definitely reduce some awkwardness.
			children.add(index,child);
			
			updateChildrenIDs();
		}
		
		@Override
		/**
		 * A chonky method.
		 */
		public String toString() 
		{
			// Why have a giant convoluted to string when you can have a much simpler and more elegant one
			// it's Recursive, if you count calling other container's to strings, which in turn call even more.
			// it's nice and simple, is what it is.
			
			String tree="";
			for(int i = 0; i<children.size(); i++)
			{
				String child = children.get(i).toString();
				String[] childsChilds = child.split("\n");
				for(int j = 0; j<childsChilds.length;j++)
				{
					tree+="\n    "+childsChilds[j];
				}
				
			}
			
			
			
			return "\n\nAFLFunction:\n" 
					+ "name: " + name
					+ "\nperameters:" + perameters 
					+ "\n\nChildren:" +  tree ;
		}

		
		
		/**
		 * removes a specific child.
		 */
		public void removeChild(Element child)
		{
			if(child instanceof Container)
			{
				Container container = (Container)child;
				removeChildrenOfChild(container);
			}
			
			children.remove(child);
			updateChildrenIDs();
			
		}
		
		/**
		 * helps with removing children, I think?
		 * @param container
		 */
		// because they aren't going to remove themselves, though they do add themselves.
		private void removeChildrenOfChild(Container container)
		{
			for(int i=0; i<container.getSize(); i++)
			{
				
				if(container.getChild(i) instanceof Container)
				{
					removeChildrenOfChild((Container)container.getChild(i));
				}
				
			}
			updateChildrenIDs();
		}
		/**
		 * yeah, same stuff
		 */
		public void removeChild(int ID)
		{
			
			removeChild(children.get(ID));
		}
		
		/**
		 * I have a hunch that this javadoc isn't working quite right, but
		 * meh.
		 * gets a child from its id.
		 */
		public Element getChild(int ID)
		{
			return children.get(ID);
		}
		
		// returns -1 on failure.
		/**
		 * same thing, but with an object.
		 */
		public int getChildID(Element child)
		{
			updateChildrenIDs();
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
		 * gets the size of the group
		 */
		public int getSize()
		{
			return children.size();
		}
		
		/**
		 * removes all children from the group
		 */
		public void removeAllChildren()
		{
			
			children= new ArrayList<Element>();
		}
		
		

		/**
		 * @return the name
		 */
		public String getName()
		{
			return name;
		}


		/**
		 * @return the perameters
		 */
		public int getPerameters() 
		{
			return perameters;
		}
		
		
		protected void updateChildrenIDs()
		{
			int j = 0;
			for(Element i : children)
			{
				i.setID(j,this);
				j++;
			}
		}
}
