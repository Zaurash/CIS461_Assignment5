import java.util.*;

public class CLS_Rec{
		
	private LinkedList<CLS> record;
		
	public CLS_Rec(){
		record = new LinkedList<CLS>();
	}
	
	public LinkedList<CLS> insert(CLS x){
		record.add(x);
	}
	
}