package edu.arizona.ece.memsim.implementations.stream;

import edu.arizona.ece.memsim.implementations.core.Program;
import edu.arizona.ece.memsim.implementations.core.ProgramSmall;
import edu.arizona.ece.memsim.model.CacheController;
import edu.arizona.ece.memsim.model.Memory;

public class StreamProgram extends ProgramSmall {

	public static void main(String[] args) throws InterruptedException{
		Run();
	}
	
	public static void Run() throws InterruptedException{
		System.out.println("\nStream Prefetcher ");
		try {
			Reset();
			SequentialAccess();
			Reset();
			RandomAccess();
			Reset();
			StrideAccess(true, 128);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static void Reset() throws Exception{
		System.out.println("\n+---------------+");
		System.out.println("| Running Reset |");
		System.out.println("+---------------+\n");
		
		mem = null;
		mem = new Memory(16384, 200);// 16KB, 200 Cycle Access
		L2 = null;
		L2 = new CacheController(2, 4096, 128, 16, 20, mem);// 4KB, 128B Block, 16-Way Associative, 20 Cycle Access
		L1 = null;
		L1 = new StreamPrefetcherCacheController(1, 1024, 32, 0, 1, L2);// 1KB, 32B Block, Fully Associative, 1 Cycle Access
	}
}
