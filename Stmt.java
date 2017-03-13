import java.util.*;

public class Stmt{
		
	public Stmt(){
		
	}
		
	public static class WhileStmt extends Stmt{
		public Expr condition;
		public LinkedList<Stmt> stmts;
		
		public WhileStmt(Expr e, LinkedList<Stmt> stmts){
			this.condition = e;
			this.stmts = stmts;
		}
	}
	
	public static WhileStmt whilestmt(Expr condition, LinkedList<Stmt> stmts){
		return new WhileStmt(condition, stmts);
	}	
	
	public static class IfStmt extends Stmt{
		public LinkedList<IFBRANCH> ifstmts = new LinkedList<IFBRANCH>();;
		
		public IfStmt(Expr e, LinkedList<Stmt> stmts, LinkedList<IFBRANCH> elifst, IFBRANCH elsest){
			this.ifstmts.add(ifbranch(e, stmts));
			this.ifstmts.addAll(elifst);
			this.ifstmts.add(elsest);
		}
	
	}
	
	public static IfStmt ifstmt(Expr e, LinkedList<Stmt> stmts, LinkedList<IFBRANCH> elifst, IFBRANCH elsest){
		return new IfStmt(e, stmts, elifst, elsest);
	}
	
	public static class AssignStmt extends Stmt{
		public Expr l_expr;
		public Expr r_expr;
		
		public AssignStmt(){
		}
		
		public AssignStmt(Expr l, Expr r){
			this.l_expr = l;
			this.r_expr = r;
		}
	}
	
	public static AssignStmt assignstmt(Expr l, Expr r){
		return new AssignStmt(l,r);
	} 
	
	public static class IFBRANCH extends Stmt{
		Expr condition = new Expr();
		LinkedList<Stmt> stmts = new LinkedList<Stmt>();
		
		public IFBRANCH(Expr condition, LinkedList<Stmt> stmts){
			this.condition = condition;
			this.stmts = stmts;
		}
	}
	
	public static IFBRANCH ifbranch(Expr condition, LinkedList<Stmt> stmts){
		return new IFBRANCH(condition, stmts);
	}
	
	//Constructor Statement
	public static class CStatement extends Stmt{
		String constructor_name;
		LinkedList<Expr> args;
		
		public CStatement(Expr.Constructor s){
			this.constructor_name = s.c_name;
			this.args = s.args;
		}
	}
	
	public static CStatement cstatement(Expr.Constructor c){
		return new CStatement(c);
	}
	
	//Method Statement
	public static class MethodStatement extends Stmt{
		Expr r_expr;
		String method_name;
		LinkedList<Expr> args;
		
		public MethodStatement(Expr.MethodCall s){
			this.r_expr = s.r_expr;
			this.method_name = s.method_name;
			this.args = s.args;
		}
	}
	
	public static MethodStatement methodstatement(Expr.MethodCall c){
		return new MethodStatement(c);
	}
	
	//Return Statement
	public static class RetStatement extends Stmt{
		Expr ret_expr;
		String ret_type;
		
		public RetStatement(Expr r){
			this.ret_expr = r;
			this.ret_type = r.type;
		}
	}
	
	public static RetStatement retstatement(Expr r){
		return new RetStatement(r);
	}
	
}