import java.util.*;

public class Expr{
	
	Expr loc;
	String name = "foo";
	int val;
	String type = "Obj";
	
	public Expr(){
		this.name = "temp";
	}
	
	public Expr(String n){
		this.name = n;
	}
	
	public Expr(String n, String t){
		this.name = n;
		this.type = t;
	}
	
	public Expr(int val, String t){
		this.name = new Integer(val).toString();
		this.val = val;
		this.type = t;
	}
	
	public Expr(Expr l, String n, String t){
		this.loc = l;
		this.name = n;
		this.type = t;
	}
	
	// Conversion from expr to int
	public static class IntConst extends Expr{
		public int name;
		public String type = "Int";
		
		public IntConst(int i){
			this.name = i;
		}
	}
	
	public static IntConst intconst(int i){
		return new IntConst(i);
	}
	
	// Conversion from expr to string
	public static class StringConst extends Expr{
		public String name;
		public String type = "String"; 
		
		public StringConst(String s){
			this.name = s;
		}
	}
	
	public static StringConst stringconst(String s){
		return new StringConst(s);
	}
	
	public static class BinOp extends Expr{
		public int e1, e2;
		public String op;
		public int res;
		
		public BinOp(String op, Expr a, Expr b){

			if(op == "PLUS"){
				this.op = "PLUS";
			}
			
			if(op == "MINUS"){
				this.op = "MINUS";
			}
			
			if(op == "TIMES"){
				this.op = "TIMES";
			}
			
			if(op == "DIVIDE"){
				this.op = "DIVIDE";
			}
			
			this.e1 = e1;
			this.e2 = e2;
		}
	}
	
	public static BinOp binop(String op, Expr a, Expr b){
		return new BinOp(op, a, b);
	}
	
	public static class Constructor extends Expr{
		String c_name;
		LinkedList<Expr> args;
		
		public Constructor(String s, LinkedList<Expr>ar){
			this.c_name = s;
			this.args = ar;
		}
	}
	
	public static Constructor constructor(String n, LinkedList<Expr> ar){
		return new Constructor(n, ar);
	}	
	
	public static class MethodCall extends Expr{
		Expr r_expr;
		String method_name;
		LinkedList<Expr> args;
		
		public MethodCall(Expr r, String s, LinkedList<Expr>ar){
			this.r_expr = r;
			this.method_name = s;
			this.args = ar;
		}
	}
	
	public static MethodCall methodcall(Expr r, String n, LinkedList<Expr> ar){
		return new MethodCall(r, n, ar);
	}
	
}