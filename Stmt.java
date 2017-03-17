import java.util.*;
import java.io.*;

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
			this.ifstmts.add(ifbranch("if", e, stmts));
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
		String type = "base_if";
		Expr condition = new Expr();
		LinkedList<Stmt> stmts = new LinkedList<Stmt>();
		
		public IFBRANCH(String t, Expr condition, LinkedList<Stmt> stmts){
			this.type = t;
			this.condition = condition;
			this.stmts = stmts;
		}
	}
	
	public static IFBRANCH ifbranch(String t, Expr condition, LinkedList<Stmt> stmts){
		return new IFBRANCH(t, condition, stmts);
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
	
	
	//Statement codegen
	public static void codegen(LinkedList<Stmt> l, PrintWriter writer, Generator gen){
		for(int st = 0; st < l.size(); st++){
			
		  //If Statements
  		  if (Stmt.IfStmt.class.isInstance(l.get(st))){
  			  Stmt.IfStmt ts = (Stmt.IfStmt)l.get(st);	
			  
			  //For each if branch
			  for(int b = 0; b < ts.ifstmts.size(); b++){
				  //Print type of branch and call codegen on list of statements for current branch
				  if(ts.ifstmts.get(b).type != "else"){
					  writer.println(ts.ifstmts.get(b).type + " () " + "{\n");				  	
				  }
				  else{
					  writer.println(ts.ifstmts.get(b).type + "{\n");				  					  	  
				  }
				  
				  Stmt.codegen(ts.ifstmts.get(b).stmts, writer, gen);
				  writer.println("}\n");
			  }
  		  }  		  	
  			
		  
		  //Assignment Statements
	  	  if (Stmt.AssignStmt.class.isInstance(l.get(st))){
		  	  Stmt.AssignStmt ts = (Stmt.AssignStmt)l.get(st);
			  
			  String tmp = gen.get_tmp();
			  
			  if(gen.tmp_list.containsKey(ts.l_expr.name)){
				  if(gen.tmp_list.containsKey(ts.r_expr.name)){
					  writer.println(gen.tmp_list.get(ts.l_expr.name) + " = " + gen.tmp_list.get(ts.r_expr.name) + ";\n");
				  }
				  else{
					  writer.println(gen.tmp_list.get(ts.l_expr.name) + " = " + ts.r_expr.name + ";\n");				  	
				  }
			  }
			  
			  else{
				  
				  if(gen.tmp_list.containsKey(ts.r_expr.name)){
					  writer.println(Generator.declare(ts,tmp));
					  writer.println(tmp + " = " + gen.tmp_list.get(ts.r_expr.name) + ";\n");
					  gen.tmp_list.put(ts.l_expr.name, gen.tmp_list.get(ts.r_expr.name));					  
					  						  
				  }
				  else{
					  writer.println(Generator.declare(ts,tmp));
					  writer.println(tmp + " = " + ts.r_expr.name + ";\n");
					  gen.tmp_list.put(ts.l_expr.name, tmp);
					  					  
				  }
			  }				  

	  	  }
		  
		  
		  //While Statements
  		  if (Stmt.WhileStmt.class.isInstance(l.get(st))){
  			  Stmt.WhileStmt ts = (Stmt.WhileStmt)l.get(st);
			  
			  writer.println("while(" + ")" + "{");
			  Stmt.codegen(ts.stmts, writer, gen);
			  writer.println("}\n");
			  				  
  		  }
		  
		  
		  //Return Statement
  		  if (Stmt.RetStatement.class.isInstance(l.get(st))){
  			  Stmt.RetStatement ts = (Stmt.RetStatement)l.get(st);
			  
			  writer.println("return " + ";\n");
			  				  
  		  }  
		  
		  
		  //Constructor Call
  		  if (Stmt.CStatement.class.isInstance(l.get(st))){
  			  Stmt.CStatement ts = (Stmt.CStatement)l.get(st);
			  
			  writer.print(ts.constructor_name);
			  if(ts.args.isEmpty()){
				  writer.print("();");
			  }
			  else{
				  if(gen.tmp_list.containsKey(ts.args.get(0).name)){
					  writer.print("(" + gen.tmp_list.get(ts.args.get(0).name));				  	
				  }
				  else{
					  writer.print("(" + ts.args.get(0).name);				  	
				  }
				  
				  for(int a = 1; a < ts.args.size(); a++){
					  if(gen.tmp_list.containsKey(ts.args.get(a).name)){
						  writer.print(", " + gen.tmp_list.get(ts.args.get(a).name));				  	
					  }
					  else{
						  writer.print(", " + ts.args.get(a).name);				  	
					  }
				  }
				  writer.println(");");
			  }	
			  		  					  
  		  }      
		
		
		  //Method Call
  		  if (Stmt.MethodStatement.class.isInstance(l.get(st))){
  			  Stmt.MethodStatement ts = (Stmt.MethodStatement)l.get(st);
			  
			  writer.print(ts.r_expr.name + "." + ts.method_name);	
			  if(ts.args.isEmpty()){
				  writer.print("();");
			  }
			  else{
				  if(gen.tmp_list.containsKey(ts.args.get(0).name)){
					  writer.print("(" + gen.tmp_list.get(ts.args.get(0).name));				  	
				  }
				  else{
					  writer.print("(" + ts.args.get(0).name);				  	
				  }
				  
				  for(int a = 1; a < ts.args.size(); a++){
					  if(gen.tmp_list.containsKey(ts.args.get(a).name)){
						  writer.print(", " + gen.tmp_list.get(ts.args.get(a).name));				  	
					  }
					  else{
						  writer.print(", " + ts.args.get(a).name);				  	
					  }
				  }
				  writer.println(");");
			  }				  
  		  }  
  
		
		}
	}
	
}