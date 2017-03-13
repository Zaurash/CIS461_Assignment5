import java.util.*;

public class Method{
	
	public String name;
	public LinkedList<Method.Argument> formals;
	public String ret_type;
	LinkedList<Stmt> stmt_block;
	
	public Method(){
		
	}
	
	public Method(String n, LinkedList<Method.Argument> args, String r, LinkedList<Stmt> bl){
		this.name = n;
		this.formals = args;
		this.ret_type = r;
		this.stmt_block = bl;
	}
	
	public static class Argument extends Method{
		
		public String name;
		public String type;
		
		public Argument(){
			
		}
		
		public Argument(String n, String t){
			this.name = n;
			this.type = t;
		}
		
	}
}