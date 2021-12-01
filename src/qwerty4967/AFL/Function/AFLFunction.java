package qwerty4967.AFL.Function;

import java.util.ArrayList;

import qwerty4967.AFL.Shell;
import qwerty4967.AFL.Interpret.Interpreter;
import qwerty4967.AFL.Interpret.Namespace;
import qwerty4967.AFL.Interpret.Variable;
import qwerty4967.AFL.Lang.Lang;
import qwerty4967.AFL.Lang.TokenType;
import qwerty4967.AFL.ParseTree.*;

public class AFLFunction extends Function implements qwerty4967.AFL.ParseTree.HasChildren 
{
		
		// It is also any bock of independent code, which for all intents and purposes is a void function with no perameters.
		private static int nestedCalls = 0; // to prevent calamity (stack overflows)
		private ArrayList<Element> children=new ArrayList<Element>();
		
		public AFLFunction(String name, int parameters)
		{
			super(name,parameters);
		}
		
		public Token call(Token[] parameters)
		{
			nestedCalls++;
			if(nestedCalls>=Lang.MAXIMUM_DEPTH)
			{
				// UH OH! Stack Overflow!
				Shell.error("Stack overflow on function '"+this.getName()+"'", 0);
				return new Token("Error", TokenType.error);
			}
			// we need to make the parameters available to the AFL Code.
			// this means putting it in an array.
			createParameterArray(parameters);
			Token toReturn = Interpreter.interpret(this, parameters);
			nestedCalls--;
			return toReturn;
		}
		
		private void createParameterArray(Token[] parameters)
		{
			// first step, create the token that refers to the array.
			Token arrayPointer =Namespace.newArray();
			// next create the variable that will provide access to the array.
			Token variable = new Token("params", TokenType.variable);
			Namespace.setVariable(variable, arrayPointer, this);
			
			
			ArrayList<Token> params = new ArrayList<Token>();
			// copy over the params.
			for( Token t : parameters)
			{
				params.add(t);
			}
			
			Namespace.setArray(arrayPointer, params);
			
			
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

			
			addChild(children.size(),child);
			return children.size()-1;
		}
		
		public void addChild(int index, Element child)
		{
			// really very simple
			// having this method earlier would definitely reduce some awkwardness.
			child.moveTo(this,index);
			if(!children.contains(child))
			{
				children.add(index,child);
			}
			else
			{
				// would it be better to throw an exception here? yes.
				System.out.println("DONT MOVE A CHILD USING addChild! USE moveChild INSTEAD!");
				System.exit(-1);
			}
			
			updateChildrenIDs();
		}
		
		public void moveChild(int index, Element child)
		{
			if(!children.contains(child))
			{
				System.out.println("CANT MOVE A CHILD I DONT HAVE!");
				System.exit(-1);
			}
			
			children.remove(child);
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
					+ "name: " + getName()
					+ "\nperameters:" + getParameters()
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
