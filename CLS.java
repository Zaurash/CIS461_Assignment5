import java.util.*;
import java.io.*;

public class CLS{
		
	public CLS.Signature sig;
	public CLS.Body bd;
	
	public CLS(){
		this.sig = null;
		this.bd = null;
	}
	
	public CLS(Signature s, Body b){
		this.sig = s;
		this.bd = b;
	}
		
		public static Boolean check_super(Program res, String s){
	  	  for(int inc = 0; inc < res.class_list.size(); inc++){
	  		  String temp = res.class_list.get(inc).sig.name;
	  				  if(s.equals(temp)){
					return true;	  				  	
	  		  }
	  	  }
		  return false;
		}
		
		public static Boolean issuper(Program res, String sup, String sub){
  	  	  for(int inc = 0; inc < res.class_list.size(); inc++){
			  if(sub.equals(res.class_list.get(inc).sig.name)){
				  if(sub.equals(sup)){
					  return true;
				  }
				  if(res.class_list.get(inc).sig.super_class.equals(sup)){
					  return true;
				  }
				  else{
					  if(res.class_list.get(inc).sig.super_class.equals("Obj")){
						  return false;
					  }
					  issuper(res, sup, res.class_list.get(inc).sig.super_class);
				  }
			  }
  	  	  }
		  return false;
		}
		
		public static CLS get_class(Program res, String s){
			for(int inc = 0; inc < res.class_list.size(); inc++){
				if(res.class_list.get(inc).sig.name.equals(s)){
					return res.class_list.get(inc);
				}
			}
			return new CLS();
		}
		
		public static int loop_check(Program res, LinkedList visit, String s){			
		  if(s == "Obj"){
			  return 0;
		  }
		  
  	  	  for(int inc = 0; inc < res.class_list.size(); inc++){
			  Signature temp = res.class_list.get(inc).sig;
			  
			  if(s.equals(temp.name)){

				  if(visit.contains(s)){
					  System.err.println("ERROR: Loop Detected in Class Hierarchy involving " + s);
					  System.exit(1);
				  }
				visit.add(s);

				loop_check(res, visit, temp.super_class);
			  }
		  }
		return 0;
		}
		
		public static class Signature extends CLS{
			public String name;
			public LinkedList<Method.Argument> formals;
			public String super_class;
			
			public Signature(String n, String s){
				this.name = n;
				this.formals = new LinkedList<Method.Argument>();
				this.super_class = s;
			}
			
			public Signature(String n, LinkedList<Method.Argument> f, String s){
				this.name = n;
				this.formals = f;
				this.super_class = s;
			}
		}
		
		public static Signature signature(String n, String s){
			return new Signature(n,s);
		}
		
		public static Signature signature(String n, LinkedList<Method.Argument> f, String s){
			return new Signature(n, f, s);
		}
		
		public static class Body extends CLS{
			public LinkedList<Method> methods;
			public LinkedList<Stmt> statements;
			
			public Body(LinkedList<Stmt> s, LinkedList<Method> m){
				this.statements = s;
				this.methods = m;
			}
			
			public Body(){
				this.statements = new LinkedList<Stmt>();
				this.methods = new LinkedList<Method>();
			}
			
			
		}
		
		public static Body body(LinkedList<Stmt> s, LinkedList<Method> m){
			return new Body(s, m);
		}
		
		public static void codegen(LinkedList<CLS> l, PrintWriter writer, Generator gen){
			
			for(int i = 0; i < l.size(); i++){
				CLS cur_class = l.get(i);
				String c_name = cur_class.sig.name;
				if(!(c_name == "Obj" || c_name == "String" || c_name == "Int" || c_name == "Nothing")){
					writer.println("\n\nstruct class_" + c_name + "_struct;");
					writer.println("typedef struct class_" + c_name + "_struct* class_" + c_name);
					writer.println("\ntypedef struct obj_" + c_name + "_struct {");
					writer.println("  class_" + c_name + " clazz;");
					for(int j = 0; j < cur_class.sig.formals.size(); j++){
		  	  	  		writer.println("  obj_" + cur_class.sig.formals.get(j).type + " " + cur_class.sig.formals.get(j).name + ";");
									
					}			
					
					writer.println();
					writer.println("} * obj_" + c_name + ";\n");
					writer.println("struct class_" + c_name + "_struct  the_class_" + c_name + "_struct;\n");	
					writer.println("struct class_" + c_name + "_struct {");
					writer.println("  /* Method table */");
					writer.print("  obj_" + c_name + " (*constructor) (");
					if(cur_class.sig.formals.isEmpty()){
						writer.print("void");
					}
					else{
						writer.print("obj_" + cur_class.sig.formals.get(0).type);
					
						for(int j = 1; j < cur_class.sig.formals.size(); j++){
							writer.print(", obj_" + cur_class.sig.formals.get(j).type);
						}
					}					
					writer.print(");\n");
					
					//Inherited Methods from Obj (Allow overriding later if time allows)
					writer.println("  obj_String (*STRING) (obj_Obj);");
					writer.println("  obj_Obj (*PRINT) (obj_Obj);");
					writer.println("  obj_Boolean (*EQUALS) (obj_Obj, obj_Obj);");
					
					//Introduced Methods
					for(int j = 0; j < cur_class.bd.methods.size(); j++){
						Method m = cur_class.bd.methods.get(j);

						writer.print("  obj_" + m.ret_type + " (*" + m.name + ")" + " (");
						if(m.formals.isEmpty()){
							writer.print("void");
						}
						else{
							writer.print("obj_" + m.formals.get(0).type);
							for(int s = 1; s < m.formals.size(); s++){
								writer.print(", obj_" + m.formals.get(s).type);
							}
						}
						writer.println(");");
						writer.println("};");
					}
					
					writer.println("\nextern class_" + c_name + " the_class_" + c_name);
					
					
					//Constructor
					writer.print("\nobj_" + c_name + " new_" + c_name + "(");
					if(cur_class.sig.formals.isEmpty()){
						writer.print("void");
					}
					else{
						writer.print("obj_" + cur_class.sig.formals.get(0).type + " " + cur_class.sig.formals.get(0).name);
					
						for(int j = 1; j < cur_class.sig.formals.size(); j++){
							writer.print(", obj_" + cur_class.sig.formals.get(j).type + " " + cur_class.sig.formals.get(j).name);
						}	
					}				
					writer.println(") {");
					writer.println("  obj_" + c_name + " new_thing = (obj_)" + c_name);
					writer.println("   malloc(sizeof(struct obj_)" + c_name + "_struct));");
					writer.println("  new_thing->clazz = the_class_" + c_name + ";");
					if(!(cur_class.sig.formals.isEmpty())){
						for(int j = 0; j < cur_class.sig.formals.size(); j++){
							writer.println("  new_thing->" + cur_class.sig.formals.get(j).name + " = " + cur_class.sig.formals.get(j).name + ";");
						}
					}
					
					writer.println("  return new_thing;");
					writer.println("}");
					
					//Created Methods
					
										
										
										

				}
				
				
							
			}
			
		}
		


}