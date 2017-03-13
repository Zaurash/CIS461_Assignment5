import java.util.*;
import java.io.*;

public class Generator{
	
	int next_temp = 0;
	
	public static void codegen(Program prog){
		try{
			//Create output file
			PrintWriter writer = new PrintWriter("output.c");
			
			//Generate code for classes
			writer.println("Classes: ");
			
			for(int cl = 0; cl < prog.class_list.size(); cl++){
				writer.println(prog.class_list.get(cl).sig.name);
			}
			
						
			
			writer.close();
		}
		
		catch(IOException e){

		}
		
	}
	
}