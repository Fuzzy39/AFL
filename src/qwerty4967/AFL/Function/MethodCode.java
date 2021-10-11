package qwerty4967.AFL.Function;
import qwerty4967.AFL.ParseTree.*;

@FunctionalInterface
public interface MethodCode 
{
	public Token call(Token[] parameters);
}
