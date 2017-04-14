<<<<<<< HEAD
package edu.arizona.ece.memsim.implementations.CP;

import java.util.Random;

import edu.arizona.ece.memsim.model.CacheController;
import edu.arizona.ece.memsim.model.CacheStatistics;
import edu.arizona.ece.memsim.model.Memory;

//public class CP extends Program {
public class CP  {	
	
	protected static CacheController L1, L2;
	protected static Memory mem;
	/*public CP(CacheController L1,CacheController L2,Memory mem){//constructor
		L1 = L1; 
	}
	*/
	public static void main(String[] args) throws InterruptedException{
		Run();
		System.exit(0);
	}
	public Memory getmem(){
		return mem;
	}
	public CacheController getCache1(){
		return L1;
	}
	public CacheController getCache2(){
		return L2;
	}
	
	
	public static void Run() throws InterruptedException{
		try {
			Reset();// you no longer need to reset as long as the prefetching algorith reset it and prefetched the values 
			BasicSequentialAccess(); 

		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	public static void Reset() throws Exception{
		mem = null;
		//mem = new Memory(134217728, 64, 200);// 128MB, 64B Block, 200 Cycle Access
		mem = new Memory(131072, 64, 200);// 128KB, 64B Block, 200 Cycle Access //************************** For debugging
		L2 = null;//Cache controller to keep it consistent for the prefetching on L1 cache 
		L2 = new CacheController(2, 131072, 64, 16, 20, mem);// 128KB, 64B Block, 16-Way Associative, 20 Cycle Access
		L1 = null; // fully asocciative is 1
		L1 = new ProgramPatternController(1, 8192, 64, 1, 1, mem);// 8KB, 64B Block, Fully associative, 1 Cycle Access
	}	



	//Verifying Hits for perfect memory pattern
	public static void BasicSequentialAccess() throws Exception{
		System.out.println("Running Basic Sequential Memory Access");
		Random rand = new Random();
		/*
		for(int i = 0; i < 16777216; i++){
			L1.put(i, (byte)rand.nextInt(Byte.MAX_VALUE + 1));
			L1.get(i);
		}
		*/
		//For debugging on small memory incialiazed
		for(int i = 0; i < 131072; i++){
			L1.put(i, (byte)rand.nextInt(Byte.MAX_VALUE + 1));
			L1.get(i);
		}
		printStats("L1", L1.getCacheStats());
		printStats("L2", L2.getCacheStats());
		printStats("M1", mem.getMemoryStats());
	}
		
	public static void RandomAccess() throws Exception{
		System.out.println("Running Random Memory Access");
		Random rand = new Random();
		for(int i = 0; i < 16777216; i++){
			Integer rw = rand.nextInt(2);
			Integer pos = rand.nextInt(16777217);
			if(rw == 0){// Read
				L1.get(pos);
			}else if(rw == 1){
				L1.put(pos, (byte)rand.nextInt(Byte.MAX_VALUE + 1));
			}else{
				throw new Exception("Random Gave Value other than 0 or 1");
			}
		}
		printStats("L1", L1.getCacheStats());
		printStats("L2", L2.getCacheStats());
		printStats("M1", mem.getMemoryStats());
	}
	
	public void StrideAccess(){
		// TODO: Implement
	}
	
	public static void SequentialAccess() throws Exception{
		System.out.println("Running Sequential Memory Access");
		Random rand = new Random();
		for(int i = 0; i < 16777216; i++){
			Integer rw = rand.nextInt(2);
			if(rw == 0){// Read
				L1.get(i);
			}else if(rw == 1){
				L1.put(i, (byte)rand.nextInt(Byte.MAX_VALUE + 1));
			}else{
				throw new Exception("Random Gave Value other than 0 or 1");
			}
		}
		printStats("L1", L1.getCacheStats());
		printStats("L2", L2.getCacheStats());
		printStats("M1", mem.getMemoryStats());
	}

	
	protected static void printStats(String prefix, CacheStatistics stats){
		System.out.println(prefix + ".ACCESSES\t\t" + stats.ACCESS);
		System.out.println(prefix + ".READ_TOTAL\t\t" + (stats.READ_HIT + stats.READ_MISS));
		System.out.println(prefix + ".READ_HITS\t\t" + stats.READ_HIT);
		System.out.println(prefix + ".READ_MISS\t\t" + stats.READ_MISS);
		System.out.println(prefix + ".BLOCKREAD_TOTAL\t" + (stats.BLOCKREAD_HIT + stats.BLOCKREAD_MISS));
		System.out.println(prefix + ".BLOCKREAD_HITS\t" + stats.BLOCKREAD_HIT);
		System.out.println(prefix + ".BLOCKREAD_MISSES\t" + stats.BLOCKREAD_MISS);
		System.out.println(prefix + ".WRITE_TOTAL\t\t" + (stats.WRITE_HIT + stats.WRITE_MISS));
		System.out.println(prefix + ".WRITE_HIT\t\t" + stats.WRITE_HIT);
		System.out.println(prefix + ".WRITE_MISS\t\t" + stats.WRITE_MISS);
		System.out.println(prefix + ".BLOCKWRITE_TOTAL\t" + (stats.BLOCKWRITE_HIT + stats.BLOCKWRITE_MISS));
		System.out.println(prefix + ".BLOCKWRITE_HITS\t" + stats.BLOCKWRITE_HIT);
		System.out.println(prefix + ".BLOCKWRITE_MISSES\t" + stats.BLOCKWRITE_MISS);
		System.out.println(prefix + ".REPLACEMENTS\t\t" + stats.REPLACEMENT);
		System.out.println(prefix + ".INVALIDATES\t\t" + stats.INVALIDATE);
	}

=======
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
	

>>>>>>> 31af15d37264bbf9c1482d94513f2246d1e8c7a2
}