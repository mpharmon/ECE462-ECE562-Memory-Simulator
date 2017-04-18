package edu.arizona.ece.memsim.implementations.stream;

import edu.arizona.ece.memsim.implementations.core.Program;
import edu.arizona.ece.memsim.model.Memory;

public class StreamProgram extends Program {

	public static void main(String[] args) throws InterruptedException{
		Run();
	}
	
	protected static void Reset() throws Exception{
		mem = null;
		mem = new Memory(134217728, 200);// 128MB, 64B Block, 200 Cycle Access
		L1 = null;
		L1 = new StreamPrefetcherCacheController(1, 8192, 64, 0, 1, mem);// 8KB, 64B Block, Fully Associative, 1 Cycle Access
	}
}
