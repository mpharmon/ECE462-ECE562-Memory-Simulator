package edu.arizona.ece.memsim.implementations.CP;


import edu.arizona.ece.memsim.implementations.core.Program;
import edu.arizona.ece.memsim.model.CacheController;
import edu.arizona.ece.memsim.model.Memory;


public class CP extends Program {	
//public class CP{
	

	public static void main(String[] args) throws InterruptedException{
		//ProgramPatternController Prefetch = new ProgramPatternController(1, Integer tSize, Integer bSize,1, Integer aTime,Memory pMemory);
		
		try {
			Reset();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Run();
	}	
		
	public static void Reset() throws Exception{
		mem = null;
		mem = new Memory(134217728, 64, 200);// 128MB, 64B Block, 200 Cycle Access
		L2 = null;
		L2 = new ProgramPatternController(2, 131072, 64, 16, 20, mem);// 128KB, 64B Block, 16-Way Associative, 20 Cycle Access
		L1 = null;
		L1 = new ProgramPatternController(1, 8192, 64, 0, 1, mem);// 8KB, 64B Block, Fully Associative, 1 Cycle Access
		
	}	
	

}