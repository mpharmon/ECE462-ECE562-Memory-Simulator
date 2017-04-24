package edu.arizona.ece.memsim.implementations.CP;

//import java.util.Random;

import edu.arizona.ece.memsim.model.CacheController;
import edu.arizona.ece.memsim.model.Memory;
//import edu.arizona.ece.memsim.implementations.core.Program;
import edu.arizona.ece.memsim.implementations.core.ProgramSmall;
//import edu.arizona.ece.memsim.implementations.nextline.NextlinePrefetcherCacheController;


public class CP extends ProgramSmall {
//public class CP  {	
	
	//protected static CacheController L1, L2;
	//protected static Memory mem;
	//private static Integer BlockSize ;
	//private static Integer TotalMemSize;
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
			StrideAccess(true, 128);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static void Reset() throws Exception{
		System.out.println("\n+---------------+");
		System.out.println("| Running Reset (Override) |");
		System.out.println("+---------------+\n");
		
		mem = null;
		mem = new Memory(16384, 200);// 16KB, 200 Cycle Access
		L2 = null;
		L2 = new CacheController(2, 4096, 128, 16, 20, mem);// 4KB, 128B Block, 16-Way Associative, 20 Cycle Access
		L1 = null;
		L1 = new ProgramPatternController(1, 1024, 32, 0, 1, L2, mem.getSize());// 1KB, 32B Block, Fully Associative, 1 Cycle Access
	}
	
	
	
	
	/*
	public static void Run() throws InterruptedException{
		try {
			Reset();//
			BasicSequentialAccess(); 
			
			Reset();
			StrideAccess();
			Reset();// 
			SequentialAccess(); 
			Reset();
			ScatterAccessLight();
			Reset();
			ScatterAccessMedium();
			Reset();
			ScatterAccessHard();
			Reset();
			RandomAccess();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	*/


  // Memory Access Pattern Organized by Difficulty, The Easiest one is the first one 
	//Verifying Hits for perfect memory pattern
	
	/*
	public static void BasicSequentialAccess() throws Exception{
		System.out.println("Running Perfect Sequential Memory Access");
		//For debugging perfect if you prefetech the next block always
		for(int i = 0; i < TotalMemSize; i++){
			//L1.get(i);
		}
		printStats("L1", L1.getCacheStats());
		printStats("L2", L2.getCacheStats());
		printStats("M1", mem.getMemoryStats());
	}
	
	public static void StrideAccess() throws Exception{
		System.out.println("Running Stride Memory Access Pattern");
		Random rand = new Random();
		for(int i = 0; i < TotalMemSize ; i++){
			Integer rw = rand.nextInt(2);
			if(rw == 0){// Read
				L1.get(i);
			}else if(rw == 1){
				
			}else{
				throw new Exception("Random Gave Value other than 0 or 1");
			}
		}
		printStats("L1", L1.getCacheStats());
		printStats("L2", L2.getCacheStats());
		printStats("M1", mem.getMemoryStats());
	}
			
	public static void SequentialAccess() throws NullPointerException, IllegalArgumentException, IllegalAccessException{
		System.out.println("Running Sequential Memory Access Pattern");
		Random rand = new Random();
		Integer Sequence = 0;
		for(int i = 0; i < TotalMemSize ; i++){
			Integer pos = rand.nextInt(i+1)+i; // get the first index from the 0 to the reference index plus de base index to make it sequential 
			Sequence = rand.nextInt((TotalMemSize/BlockSize)) + pos; // how many iterations of the sequencial access pattern 
			if(Sequence > TotalMemSize){ // got out of bounds, therefore we need to reach the top
				Sequence =  TotalMemSize - 1;
			}
			while (pos < Sequence){
				L1.get(pos);
				pos = pos+1;
			}
			i = i+pos-1; // make the new reference index a bigger index 
		}
		printStats("L1", L1.getCacheStats());
		printStats("L2", L2.getCacheStats());
		printStats("M1", mem.getMemoryStats());
	}
	
	public static void ScatterAccessLight() throws NullPointerException, IllegalArgumentException, IllegalAccessException {
		System.out.println("Easy Scatter Memory Access Pattern");
		Random rand = new Random();
		for(int i = 0; i < TotalMemSize ; i++){
			Integer pos = rand.nextInt(TotalMemSize); // any random value within the whole memory 
			Integer length = rand.nextInt((TotalMemSize/BlockSize));
			i = i+length; // add the number of acces to  the total 
			length = length + pos;
			while (pos < length){
				L1.get(pos);
				pos = pos+1;
			}
		}
		printStats("L1", L1.getCacheStats());
		printStats("L2", L2.getCacheStats());
		printStats("M1", mem.getMemoryStats());
	}
	
	
	public static void ScatterAccessMedium() throws NullPointerException, IllegalArgumentException, IllegalAccessException {
		System.out.println("Medium Scatter Memory Access Pattern");
		Random rand = new Random();
		for(int i = 0; i < TotalMemSize ; i++){
			Integer pos = rand.nextInt(TotalMemSize); // any random value within the whole memory 
			Integer length = rand.nextInt(15); // just 10 to make it hard because either you have one block or two in the 
			i = i+length; // add the number of acces to  the total 
			length = length + pos;
			while (pos < length){
				L1.get(pos);
				pos = pos+1;
			}
		}
		printStats("L1", L1.getCacheStats());
		printStats("L2", L2.getCacheStats());
		printStats("M1", mem.getMemoryStats());
	}
	
	public static void ScatterAccessHard() throws NullPointerException, IllegalArgumentException, IllegalAccessException {
		System.out.println("HardCore Scatter Memory Access Pattern");
		Random rand = new Random();
		for(int i = 0; i < TotalMemSize ; i++){
			Integer pos = rand.nextInt(TotalMemSize); // any random value within the whole memory 
			Integer length = rand.nextInt(10); // just 10 to make it hard because either you have one block or two in the 
			i = i+length; // add the number of acces to  the total 
			length = length + pos;
			while (pos < length){
				L1.get(pos);
				pos = pos+1;
			}
		}
		printStats("L1", L1.getCacheStats());
		printStats("L2", L2.getCacheStats());
		printStats("M1", mem.getMemoryStats());
	}
	
	public static void RandomAccess() throws Exception{
		System.out.println("Running Random Memory Access");
		Random rand = new Random();
		for(int i = 0; i < TotalMemSize ; i++){
				Integer pos = rand.nextInt(TotalMemSize);
				L1.get(pos);
			}
		printStats("L1", L1.getCacheStats());
		printStats("L2", L2.getCacheStats());
		printStats("M1", mem.getMemoryStats());
	}
	
	*/
	/*
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
	*/
}
