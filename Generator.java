import java.util.*;
import java.io.*;

public class Generator{
	
	int next_tmp = 0;
		
	public String get_tmp(){
		next_tmp++;
		return "tmp_" + next_tmp + ";";
	}
	
	public static String declare(Stmt.AssignStmt d, String t){
		
	  if(d.l_expr.type.equals("Ident")){
		  if(!(d.r_expr.type.equals("Ident"))){
			  d.l_expr.type = d.r_expr.type;
		  }
	  }
	  
	  String o = d.l_expr.type + " " + t;
	  return o;
	  
	}
	
	public static void codegen(Program prog){
		try{
			//Create output file
			PrintWriter writer = new PrintWriter("output.c");
			
		    Generator gen = new Generator();
			
			//Generate generic code
			writer.println("#include <stdio.h>");
			writer.println("#include \"builtins/runtime/Builtins.h\"\n");
			writer.println("void generated_main();\n");
			writer.println("int main(int argc, char** argv) {");
			writer.println("	printf(\"=== BEGINNING EXECUTION ===\\n\");");
			writer.println("	generated_main();");
			writer.println("	printf(\"=== FINISHED EXECUTION === \\n\");");
			writer.println("}\n");
			writer.println("void generated_main() {\n");				
			
			
			//Generate code for classes
			writer.println("Classes: ");
			
			for(int cl = 0; cl < prog.class_list.size(); cl++){
				writer.println(prog.class_list.get(cl).sig.name);
			}
			
			writer.println("");		
			
			
			//Generate code for statements
			Stmt.codegen(prog.statement_list, writer, gen);
			

			//End of file
			writer.println("}");
			writer.close();
		}
		
		catch(IOException e){

		}
		
	}
	
}