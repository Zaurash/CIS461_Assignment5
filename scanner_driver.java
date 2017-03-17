import java.io.*;
import java.util.*;
import java.lang.*;


public class scanner_driver {
  static public void main(String argv[]) {    
    /* Start the parser */
	System.out.println("Beginning parse ...");
    try {
      parser p = new parser(new Lexer(new FileReader(argv[0])));
      Program result = (Program)p.parse().value;
	  System.out.println("Parsing completed with no errors");
	  LinkedList class_names = new LinkedList<String>();
	  LinkedList super_names = new LinkedList<String>();
	  LinkedList visited = new LinkedList<String>();
	  
	  for(int i = 0; i < result.class_list.size(); i++){
		  CLS cur_class = result.class_list.get(i);
		  String c_name = cur_class.sig.name;
		  String c_super = cur_class.sig.super_class;
		  
		  HashMap cl_vars = new HashMap<String, String>();	
		  Set<String> cvar_names = new HashSet<String>();
		  
		  
		  //Get hashmap of class arguments
		  for(int j = 0; j < cur_class.sig.formals.size(); j++){
			  cl_vars.put(cur_class.sig.formals.get(j).name, cur_class.sig.formals.get(j).type);
			  cvar_names.add(cur_class.sig.formals.get(j).name);
		  }	  
		  
		  
		  //Get variables defined by class
		  for(int v = 0; v < cur_class.bd.statements.size(); v++){
			  String tt;
			  if(Stmt.AssignStmt.class.isInstance(cur_class.bd.statements.get(v))){
				  Stmt.AssignStmt zt = (Stmt.AssignStmt)cur_class.bd.statements.get(v);
				  if(!(zt.l_expr.type.equals("Ident"))){
					  System.err.println("ERROR: left side of expression isn't an identifier");
					  System.exit(1);
				  }
				  
				  if(zt.r_expr.type.equals("Ident")){
					  if(!(cvar_names.contains(zt.r_expr.name))){
						  System.err.println("ERROR: Identifier " + zt.r_expr.name + " is undefined in class " + cur_class.sig.name);
						  System.exit(1);
					  }
					  String gg = (String)zt.r_expr.name;
					  
					  tt = (String)cl_vars.get(gg);
				  }
				  else{
					  String gg = (String)zt.r_expr.type;
					  tt = gg;
				  }
				  cvar_names.add(zt.l_expr.name);
				  cl_vars.put(zt.l_expr.name, tt);
				  zt.l_expr.type = tt;
				  zt.r_expr.type = tt;
			  }
		  }
		  			  
		  
		  //Checking if methods are redefined in current class
		  if(cur_class.bd.methods != null){
			  for(int j = 0; j < cur_class.bd.methods.size(); j++){
				  Method cm = cur_class.bd.methods.get(j);

				  if(cur_class.bd.methods.contains(cm.name)){
		      		System.err.println("ERROR: Method " + cm.name + " defined twice in class " + c_name);
		    			System.exit(1);
				  }
				  
		  	
			  }
		  }

		  		  
		  //Checking if the same class is redefined
		  if(class_names.contains(c_name)){
    		System.err.println("ERROR: Class " + c_name + " defined twice ");
  			System.exit(1);
		  }
		  class_names.add(c_name);
		  super_names.add(c_super);
		  
		  
		  
		  //Checking if Method is overridden
		  CLS sup_class = CLS.get_class(result, cur_class.sig.super_class);
		  LinkedList<Method> cur_methods = cur_class.bd.methods;
		  
		  if(sup_class.bd.methods != null){
			  int ssize = sup_class.bd.methods.size();
			  for(int cmeth = 0; cmeth < cur_methods.size(); cmeth++){
				  Method cmt = cur_class.bd.methods.get(cmeth);
				  for(int smeth = 0; smeth < ssize; smeth++){
					  					  
					  if(cur_methods.get(cmeth).name.equals(sup_class.bd.methods.get(smeth).name)){

						  //Check # of args
						 if(cur_methods.get(cmeth).formals.size() != sup_class.bd.methods.get(smeth).formals.size()){
							 System.err.println("ERROR: Overridden method " + cur_methods.get(cmeth).name + " has an incorrect number of arguments");
							 System.exit(1);
						  }
						  
						  //Check argument types
						  for(int bb = 0; bb < cur_methods.get(cmeth).formals.size(); bb++){
							  //call fn that compares 2 arg types
							  if(!CLS.issuper(result, cmt.formals.get(bb).type, sup_class.bd.methods.get(smeth).formals.get(bb).type)){
								  System.err.println("ERROR: Argument " + cmt.formals.get(bb).type + " is not a superclass of " + sup_class.bd.methods.get(smeth).formals.get(bb).type);
								  System.exit(1);
							  }
						  }
					
						  //Check if return type is subtype of superclass' argument
						  if(!CLS.issuper(result, sup_class.bd.methods.get(smeth).ret_type, cmt.ret_type)){
							  System.err.println("ERROR: Return type " + cmt.ret_type + " is not a subclass of " + sup_class.bd.methods.get(smeth).ret_type);
						  }
						  
					  }
				  }
  
			  }
		  }
		  
		  
		  
		  		  
		  
		  //Check if each method returns the type it is supposed to return
		  for(int j = 0; j < cur_methods.size(); j ++){
			  Method cur_meth = cur_methods.get(j);
			  String ret_tp = cur_meth.ret_type;
			  
			  //Get hashmap of method arguments
			  HashMap<String, String> m_vars = new HashMap<String, String>();
			  Set<String> mvar_names = new HashSet<String>();
			  for (int ll = 0; ll < cur_meth.formals.size(); ll++){
				  m_vars.put(cur_meth.formals.get(ll).name, cur_meth.formals.get(ll).type);
				  mvar_names.add(cur_meth.formals.get(ll).name);
			  }
			  
			  for(int k = 0; k < cur_meth.stmt_block.size(); k++){
				  Stmt cs = cur_meth.stmt_block.get(k);
				  
				  //check return type for return statement
				  if(Stmt.RetStatement.class.isInstance(cs)){
					  Stmt.RetStatement cst = (Stmt.RetStatement)cs;
					  String temp_type = cst.ret_type;
					  
					  if(temp_type.equals("Ident")){
						  	String r_name = cst.ret_expr.name;
							
						  if(m_vars.get(r_name) == null){
	  						  System.err.println("ERROR: Method " + cur_meth.name + " is supposed to return " + ret_tp + " but instead returns " + r_name + " of unkown type");
							  System.exit(1);
						  }
							
							if(!(m_vars.get(r_name).equals(ret_tp))){
	  						  System.err.println("ERROR: Method " + cur_meth.name + " is supposed to return " + ret_tp + " but instead returns " + m_vars.get(r_name));
	  						  System.exit(1);
							}
					  }
					  else{	
						  //here
						  if(Expr.BinOp.class.isInstance(cst.ret_expr)){
							  Expr.BinOp temp1 = (Expr.BinOp) cst.ret_expr;
							  temp_type = temp1.ty;
						  }
					 
					  	if(!(temp_type.equals(ret_tp))){
							System.err.println("ERROR: Method " + cur_meth.name + " is supposed to return " + ret_tp + " but instead returns " + temp_type);
						  	System.exit(1);
					  }
				  	}
				  }
				  
				  //check return type for if statement
				  if(Stmt.IfStmt.class.isInstance(cur_meth.stmt_block.get(k))){
					  Stmt.IfStmt cst = (Stmt.IfStmt)cs;
					  for(int l = 0; l < cst.ifstmts.size(); l++){
						  for(int t = 0; t < cst.ifstmts.get(l).stmts.size(); t++){
							  if(Stmt.RetStatement.class.isInstance(cst.ifstmts.get(l).stmts.get(t))){
								  Stmt.RetStatement cr = (Stmt.RetStatement)cst.ifstmts.get(l).stmts.get(t);

								  String temp_type = cr.ret_type;
					  
								  if(temp_type.equals("Ident")){
									  	String r_name = cr.ret_expr.name;
							
									  if(m_vars.get(r_name) == null){
				  						  System.err.println("ERROR: Method " + cur_meth.name + " is supposed to return " + ret_tp + " but instead returns " + r_name + " of unkown type in one of its if/elif/else blocks");
										  System.exit(1);
									  }
							
										if(!(m_vars.get(r_name).equals(ret_tp))){
				  						  System.err.println("ERROR: Method " + cur_meth.name + " is supposed to return " + ret_tp + " but instead returns " + m_vars.get(r_name) + " in one of its if/elif/else blocks");
				  						  System.exit(1);
										}
								  }
								  else{	
					 
								  	if(!(temp_type.equals(ret_tp))){
										System.err.println("ERROR: Method " + cur_meth.name + " is supposed to return " + ret_tp + " but instead returns " + temp_type + " in one of its if/elif/else blocks");
									  	System.exit(1);
								  }
							  	}
						  	}

						  }
					  }
				  }
				  
				  //check return type for while statement
				  if(Stmt.WhileStmt.class.isInstance(cur_meth.stmt_block.get(k))){
					  Stmt.WhileStmt cst = (Stmt.WhileStmt)cs;
					  for(int l = 0; l < cst.stmts.size(); l++){

						  if(Stmt.RetStatement.class.isInstance(cst.stmts.get(l))){
							  Stmt.RetStatement cr = (Stmt.RetStatement)cst.stmts.get(l);

							  String temp_type = cr.ret_type;
				  
							  if(temp_type.equals("Ident")){
								  	String r_name = cr.ret_expr.name;
						
								  if(m_vars.get(r_name) == null){
			  						  System.err.println("ERROR: Method " + cur_meth.name + " is supposed to return " + ret_tp + " but instead returns " + r_name + " of unkown type in while block");
									  System.exit(1);
								  }
						
									if(!(m_vars.get(r_name).equals(ret_tp))){
			  						  System.err.println("ERROR: Method " + cur_meth.name + " is supposed to return " + ret_tp + " but instead returns " + m_vars.get(r_name) + " in while block");
			  						  System.exit(1);
									}
							  }
							  else{	
				 
							  	if(!(temp_type.equals(ret_tp))){
									System.err.println("ERROR: Method " + cur_meth.name + " is supposed to return " + ret_tp + " but instead returns " + temp_type + " in while block");
								  	System.exit(1);
							  }
						  	}
					  	}
						
					  }
				  	
				  }
				  
				  
				  
				  //check if variables are assigned before used
				  for(int z = 0; z < cur_meth.stmt_block.size(); z++){
					  Stmt cur_meth_stmt = cur_meth.stmt_block.get(z);
					  Set<String> cpvar_names =  new HashSet<String>();
					  Set<String> zzvar_names = new HashSet<String>();
					  HashMap<String, String> cpcl_vars = new HashMap<String,String>();
					  HashMap<String, String> firstcl_vars = new HashMap<String, String>();
					  
					  
					  if(Stmt.WhileStmt.class.isInstance(cur_meth_stmt)){
						  continue;
					  }
					  
					  //Typechecks if statements
					  // if(Stmt.IfStmt.class.isInstance(cur_meth_stmt)){
// 						  Stmt.IfStmt cur_if_stmt = (Stmt.IfStmt)cur_meth_stmt;
// 						  //test
//
// 						  for(int y = 0; y < cur_if_stmt.ifstmts.size(); y++){
// 							  for(int x = 0; x < cur_if_stmt.ifstmts.get(y).stmts.size(); x++){
// 								  if(Stmt.AssignStmt.class.isInstance(cur_if_stmt.ifstmts.get(y).stmts.get(x))){
// 									  Stmt.AssignStmt cur_a = (Stmt.AssignStmt)cur_if_stmt.ifstmts.get(y).stmts.get(x);
// 									  if(!(cur_a.l_expr.type.equals("Ident"))){
// 										  System.err.println("ERROR: " + cur_a.l_expr.name + " is not an identifier ");
// 										  System.exit(1);
// 									  }
//
// 										  String hh = cur_a.l_expr.name;
// 										  String gg = cur_a.r_expr.type;
//
//
// 										  if(cur_a.r_expr.type.equals("Ident")){
//
// 										  if ((cvar_names.contains(cur_a.r_expr.name))){
// 											  gg = (String)cl_vars.get(cur_a.r_expr.name);
// 										  }
//
// 										  else if((zzvar_names.contains(cur_a.r_expr.name))){
// 											  gg = (String)firstcl_vars.get(cur_a.r_expr.name);
// 										  }
// 										  else{
// 											  System.err.println("ERROR: Variable " + cur_a.r_expr.name + " has not been initialized ");
// 											  System.exit(1);
// 										  }
//
// 									  }
//
// 									  if(firstcl_vars.get(cur_a.l_expr.name) != null){
// 										  if(!(firstcl_vars.get(cur_a.l_expr.name).equals(gg))){
// 											  System.out.println(firstcl_vars);
// 											  System.err.println("ERROR: Variable " + hh + " of type " + firstcl_vars.get(cur_a.l_expr.name) + " cannot be assigned the value " + cur_a.r_expr.name + " of type " + gg);
// 											  System.exit(1);
// 										  }
// 									  }
//
// 									  firstcl_vars.put(hh, gg);
// 									  zzvar_names.add(cur_a.l_expr.name);
//
// 								  }
// 								  //might need to cp above code again
// 							  }
// 							  break;
//
// 						  }
//
// 						  //end test
// 						  for(int y = 0; y < cur_if_stmt.ifstmts.size(); y++){
// 							  for(int x = 0; x < cur_if_stmt.ifstmts.get(y).stmts.size(); x++){
// 								  cpvar_names = cvar_names;
// 								  cpcl_vars = cl_vars;
//
//
// 								  if(Stmt.AssignStmt.class.isInstance(cur_if_stmt.ifstmts.get(y).stmts.get(x))){
// 									  Stmt.AssignStmt cur_a = (Stmt.AssignStmt)cur_if_stmt.ifstmts.get(y).stmts.get(x);
// 									  if(!(cur_a.l_expr.type.equals("Ident"))){
// 										  System.err.println("ERROR: " + cur_a.l_expr.name + " is not an identifier ");
// 										  System.exit(1);
// 									  }
//
// 										  String hh = cur_a.l_expr.name;
// 										  String gg = cur_a.r_expr.type;
//
//
// 										  if(cur_a.r_expr.type.equals("Ident")){
// 										  if (!(cpvar_names.contains(cur_a.r_expr.name))){
// 											  System.err.println("ERROR: Variable " + cur_a.r_expr.name + " has not been initialized ");
// 											  System.exit(1);
// 										  }
// 										  gg = cpcl_vars.get(cur_a.r_expr.name);
// 									  }
//
// 									  if(cpcl_vars.get(cur_a.l_expr.name) != null){
// 										  if(!(cpcl_vars.get(cur_a.l_expr.name).equals(gg))){
// 											  System.out.println(cpcl_vars);
// 											  System.err.println("ERROR: Variable " + hh + " of type " + cpcl_vars.get(cur_a.l_expr.name) + " cannot be assigned the value " + cur_a.r_expr.name + " of type " + gg);
// 											  System.exit(1);
// 										  }
// 									  }
//
// 									  cpcl_vars.put(hh, gg);
// 									  cpvar_names.add(cur_a.l_expr.name);
//
// 								  }
// 								  //might need to cp above code again
//
// 							  }
// 							  //System.out.println(" zzvar " + zzvar_names + " cpvar " + cpvar_names);
// 							  if(!(zzvar_names.equals(cpvar_names))){
// 								  System.err.println("ERROR: not all variables initialized on all paths in method " + cur_meth.name);
// 								  System.exit(1);
// 						  }
// 						  }
// 					  }
					  
					  //if Constructor (CStatement)
					  if(Stmt.CStatement.class.isInstance(cur_meth_stmt)){
						  Stmt.CStatement cur_cons_stmt = (Stmt.CStatement)cur_meth_stmt;
						  for(int y = 0; y < cur_cons_stmt.args.size(); y++){
							  if (cur_cons_stmt.args.get(y).type == "Ident"){
								  if(!(cvar_names.contains(cur_cons_stmt.args.get(y).name))){
									  if(!(mvar_names.contains(cur_cons_stmt.args.get(y).name))){
										  System.out.println("mvars" + m_vars);
										  System.err.println("ERROR: Argument " + cur_cons_stmt.args.get(y).name + " has not been initialized");
										  System.exit(1);
									  }

								  }
							  }
						  }
					  }
					  
					  //if AssignStatement
					  if(Stmt.AssignStmt.class.isInstance(cur_meth_stmt)){
					  						  Stmt.AssignStmt cur_assign_stmt = (Stmt.AssignStmt)cur_meth_stmt;
					  						  String temp = cur_assign_stmt.l_expr.type;
					  						  String temptype = cur_assign_stmt.r_expr.type;

					  						  if(cur_assign_stmt.r_expr.type == "Ident"){
					  							  if(cvar_names.contains(cur_assign_stmt.r_expr.name)){
					  								  temptype = (String)cl_vars.get(cur_assign_stmt.r_expr.name);
					  								  }

					  							  else if(mvar_names.contains(cur_assign_stmt.r_expr.name)){
					  								  temptype = (String)m_vars.get(cur_assign_stmt.r_expr.name);
					  							  }

					  							  else{
					  								  System.err.println("ERROR: Argument " + cur_assign_stmt.r_expr.name + " has not been initialized");
					  								  System.exit(1);
					  							  }

					  							  }
					  							  else{
					  								  temptype = cur_assign_stmt.r_expr.type;
					  							  }

							  					if(cvar_names.contains(cur_assign_stmt.l_expr)){
							  						temp = (String)cl_vars.get(cur_assign_stmt.l_expr);
	  					  							if(temp != temptype){
	  					  								  System.err.println("ERROR: L Expression " + cur_assign_stmt.l_expr.name + " cannot be assigned a type " + temptype);
	  					  								  System.exit(1);
	  					  							}
							  					}
							  					else if(mvar_names.contains(cur_assign_stmt.l_expr)){
													
							  						temp = (String)m_vars.get(cur_assign_stmt.l_expr);
	  					  							if(temp != temptype){
	  					  								  System.err.println("ERROR: L Expression " + cur_assign_stmt.l_expr.name + " cannot be assigned a type " + temptype);
	  					  								  System.exit(1);
	  					  							}
							  					}
												
												else{
	  					  						  m_vars.put(cur_assign_stmt.l_expr.name, temptype);
												  mvar_names.add(cur_assign_stmt.l_expr.name);
													
												}

					  }
					  
					  //if Method Statement
					  if(Stmt.MethodStatement.class.isInstance(cur_meth_stmt)){
  						  Stmt.MethodStatement cur_method_stmt = (Stmt.MethodStatement)cur_meth_stmt;
						  for(int arg = 0; arg < cur_method_stmt.args.size(); arg++){
							  if(cur_method_stmt.args.get(arg).type == "Ident"){
								  if(!(cvar_names.contains(cur_method_stmt.args.get(arg).name))){
									  if(!(mvar_names.contains(cur_method_stmt.args.get(arg).name))){
		  								  System.err.println("ERROR: Method call of " + cur_method_stmt.method_name + " uses an unkown identifier " + cur_method_stmt.args.get(arg).name + " as an argument");
		  								  System.exit(1);
									  }
								  }
							  }
						  }
						  //Check well typedness of method calls
							  //gets class of receiver object
						  	 String rclass = cur_method_stmt.r_expr.type;
							 //System.out.println("expr name " + cur_method_stmt.r_expr.name + " expr type " + cur_method_stmt.r_expr.type);
						  	 if(cur_method_stmt.r_expr.type == "Ident"){
								 if(cvar_names.contains(cur_method_stmt.r_expr.name)){
									 rclass = (String)cl_vars.get(cur_method_stmt.r_expr.name);
								 }
								 else if(mvar_names.contains(cur_method_stmt.r_expr.name)){
									 rclass = (String)m_vars.get(cur_method_stmt.r_expr.name);
									 
								 }
								 else{
  								  System.err.println("ERROR: Method call of " + cur_method_stmt.method_name + " uses an unkown identifier " + cur_method_stmt.r_expr.name + " as a receiver object");
  								  System.exit(1);
								 }
						  	 }
							 
							if(!class_names.contains(rclass)){
								  System.err.println("ERROR: Method call of " + cur_method_stmt.method_name + " uses an identifier " + cur_method_stmt.r_expr.name + " of an unknown class as a receiver object");
								  System.exit(1);
							}
							
   						  	for(int c = 0; c < result.class_list.size(); c++){
								CLS thclass = result.class_list.get(c);
								if(thclass.sig.name == rclass){
									for(int m = 0; m < thclass.bd.methods.size(); m++){
										if(thclass.bd.methods.get(m).name.equals(rclass)){
											//compare args
											if(thclass.bd.methods.get(m).formals.size() != cur_method_stmt.args.size()){
												System.err.println("ERROR: Incorrect number of arguments for method " + cur_method_stmt.method_name);
												System.exit(1);
											}
										}
									}
								}

							 
							 
						  }
						  
					  }
					  
					  
				  }
				  


				  //finish loop of method statements
			  }
		  }
		  
		  
		  //Check fields and get names and types
		  HashMap cur_fields = new HashMap<String, String>();
		  LinkedList<Stmt> cl_stmts = cur_class.bd.statements;
		  if(cl_stmts != null){  
			  for(int j = 0; j < cl_stmts.size(); j++){
				  if(Stmt.AssignStmt.class.isInstance(cl_stmts.get(j))){
					  Stmt.AssignStmt c_stmt = (Stmt.AssignStmt)cl_stmts.get(j);
					  if(cur_fields.get(c_stmt.l_expr.name) == null){
						  if(c_stmt.r_expr.type == "Ident"){
							  if(cl_vars.get(c_stmt.r_expr.name) == null){
								  System.err.println("ERROR field " + c_stmt.l_expr.name + " in class " + c_name + " initialized to unknown type");
								  System.exit(1);
							  }
							  else{
								  cur_fields.put(c_stmt.l_expr.name, cl_vars.get(c_stmt.l_expr.name));					  
							  }
						  }
						  
						  else{
							  cur_fields.put(c_stmt.l_expr.name, c_stmt.r_expr.type);
						  }
					  }
				  }
			  }
			  
			  //Checking if subclasses initialize fields of superclass
			  for(int j = 0; j < result.class_list.size(); j++){
				  if(result.class_list.get(j).sig.super_class.equals(cur_class.sig.name)){
					  
					  CLS q_class = result.class_list.get(j);
					  
					  HashMap<String, String> temp_fields = cur_fields;
					  HashMap<String, String> temp_vars = new HashMap<String, String>();
					  
					  for(int l = 0; l < q_class.sig.formals.size(); l++){
						  
						  temp_vars.put(q_class.sig.formals.get(l).name, q_class.sig.formals.get(l).type);
					  }
					  
					  for(int l = 0; l < q_class.bd.statements.size(); l++){
						  
						  if(Stmt.AssignStmt.class.isInstance(q_class.bd.statements.get(l))){
							  Stmt.AssignStmt t_stmt = (Stmt.AssignStmt)q_class.bd.statements.get(l);
							  if(temp_fields.containsKey(t_stmt.l_expr.name)){
								  if(temp_fields.get(t_stmt.l_expr.name) == null){									  
								  	if(t_stmt.r_expr.type == "Ident"){
									  System.err.println("ERROR field " + t_stmt.l_expr.name + " in class " + q_class.sig.name + " initialized to unknown type");
									  System.exit(1);
								  	}
								  else{
									  
									  temp_fields.put(t_stmt.l_expr.name, temp_vars.get(t_stmt.l_expr.name));
								  }
							  	}

							  }
							  if(temp_fields.get(t_stmt.l_expr.name)!= null){
								  if(CLS.issuper(result, temp_fields.get(t_stmt.l_expr.name), t_stmt.r_expr.type)){
									  temp_fields.remove(t_stmt.l_expr.name);
								  }
							  }

						  }
					  }
					  if(!(temp_fields.isEmpty())){
						  System.err.println("ERROR not all fields from class " + c_name + " initialized properly in subclass " + q_class.sig.name);
						  System.exit(1);
					  }
				  }					
			  }		  		  	
		  }
		  
		  //Checking class arguments NYI (Not sure if we need to)
		 /* for (int fi = 0; fi < cur_class.sig.formals.size(); fi++){
			  CLS temp = CLS.get_class(result, cur_class.sig.formals.get(fi).type);
			  if(temp.sig != null){
				  temp = CLS.get_class(result, temp.sig.super_class);
			  }
			  
			  System.out.println(cur_class.sig.formals.get(fi).type);
		  }	 */ 
		  
		  
	  }
	  
	  
	  // Tests to see if superclasses are existing classes 
	  for(int i = 0; i < result.class_list.size(); i++){
		  String temp = result.class_list.get(i).sig.super_class;
		  if(!class_names.contains(temp)){
  		  	System.err.println("ERROR: super class " + temp + " does not exist ");
			System.exit(1);
		  }
		  // Check if there are any loops in the class hierarchy
		  else{
			  CLS.loop_check(result, visited, temp);
		  }
	  } 
	  
	  // Loops through the list of statements in the file
	  for(int i = 0; i < result.statement_list.size(); i++){
		  Stmt temp = result.statement_list.get(i);
		  
		  // Tests to see if constructor calls have existing classes
		  if (Stmt.CStatement.class.isInstance(result.statement_list.get(i))){
			  Stmt.CStatement nt = (Stmt.CStatement)result.statement_list.get(i);
				  if (CLS.check_super(result, nt.constructor_name)){
				  }
				  else {
					  System.err.println("ERROR: called constructor " + nt.constructor_name + " which calls a class that does not exist");
					  System.exit(1);
				  }
			 
		  }

	  }
	  
	  Generator.codegen(result);

	  
    } catch (Exception e) {
      /* do cleanup here -- possibly rethrow e */
      e.printStackTrace();
    }
  }
}