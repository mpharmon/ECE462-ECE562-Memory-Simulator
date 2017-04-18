package edu.arizona.ece.memsim.implementations.core;

import edu.arizona.ece.memsim.model.CacheController;
import edu.arizona.ece.memsim.model.CacheStatistics;
import edu.arizona.ece.memsim.model.Memory;

import java.util.Random;

public class ProgramSmall{
	
	protected static CacheController L1, L2;
	
	protected static Memory mem;
	
	public static void main(String[] args) throws InterruptedException{
		Run();
	}
	
	public static void Run() throws InterruptedException{
		try {
			Reset();
			SequentialAccess();
			Reset();
			RandomAccess();
			Reset();
			StrideAccess(2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void RandomAccess() throws Exception{
		System.out.println("\n+------------------------------+");
		System.out.println("| Running Random Memory Access |");
		System.out.println("+------------------------------+\n");
		
		Random rand = new Random();
		
		for(int i = 0; i < 8192; i++){
			Integer rw = rand.nextInt(2);
			Integer pos = rand.nextInt(8192);
			
			if(rw == 0){// Read
				L1.get(pos);
			}else if(rw == 1){
				L1.put(pos, (byte)rand.nextInt(Byte.MAX_VALUE + 1));
			}else{
				throw new Exception("Random Gave Value other than 0 or 1");
			}
		}
		
		// Print Statistics
		printStats("L1", L1.getCacheStats());
		printStats("L2", L2.getCacheStats());
		printStats("M1", mem.getMemoryStats());
	}
	
	public static void StrideAccess(Integer strideSize) throws Exception{
		System.out.println("\n+------------------------------+");
		System.out.println("| Running Stride Memory Access |");
		System.out.println("+------------------------------+\n");
		
		Random rand = new Random();
		
		for(int i = 0 ; i < 8192 / strideSize; i++){
			Integer startLocation = rand.nextInt(8192 - strideSize - 1);
			
			for(int j = 0; j < strideSize; j++){
				Integer rw = rand.nextInt(2);
				
				if(rw == 0){// Read
					L1.get(startLocation + i);
				}else if(rw == 1){// Write
					L1.put(startLocation + i, (byte)rand.nextInt(Byte.MAX_VALUE + 1));
				}else{
					throw new Exception("Random Gave Value other than 0 or 1");
				}
			}
		}
		
		// Print Statistics
		printStats("L1", L1.getCacheStats());
		printStats("L2", L2.getCacheStats());
		printStats("M1", mem.getMemoryStats());
	}
	
	public static void SequentialAccess() throws Exception{
		System.out.println("\n+----------------------------------+");
		System.out.println("| Running Sequential Memory Access |");
		System.out.println("+----------------------------------+");
		
		Random rand = new Random();
		
		for(int i = 0; i < 8192; i++){
			Integer rw = rand.nextInt(2);
			
			if(rw == 0){// Read
				L1.get(i);
			}else if(rw == 1){
				L1.put(i, (byte)rand.nextInt(Byte.MAX_VALUE + 1));
			}else{
				throw new Exception("Random Gave Value other than 0 or 1");
			}
		}
		
		// Print Statistics
		printStats("L1", L1.getCacheStats());
		printStats("L2", L2.getCacheStats());
		printStats("M1", mem.getMemoryStats());
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
		L1 = new CacheController(1, 1024, 32, 0, 1, L2);// 1KB, 32B Block, Fully Associative, 1 Cycle Access
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
}
