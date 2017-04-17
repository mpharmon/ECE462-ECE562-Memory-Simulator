package edu.arizona.ece.memsim.implementations.CP;

import java.util.Random;

import edu.arizona.ece.memsim.model.CacheController;
import edu.arizona.ece.memsim.model.CacheStatistics;
import edu.arizona.ece.memsim.model.Memory;

//public class CP extends Program {
public class CP  {	
	
	protected static CacheController L1, L2;
	protected static Memory mem;
	private static Integer BlockSize ;
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
			//Reset();//
			//BasicSequentialAccess(); 
			//Reset();
			//StrideAccess();
			//Reset();// 
			//SequentialAccess(); 
			//Reset();
			//ScatterAccessLight();
			//Reset();
			//ScatterAccessMedium();
			//Reset();
			//ScatterAccessHard();
			Reset();
			RandomAccess();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	public static void Reset() throws Exception{
		BlockSize = 64; // Coherency block size must be the same 
		mem = null;
		//mem = new Memory(134217728, 64, 200);// 128MB, 64B Block, 200 Cycle Access
		mem = new Memory(131072, BlockSize, 200);// 128KB, 64B Block, 200 Cycle Access //************************** For debugging
		L2 = null;//Cache controller to keep it consistent for the prefetching on L1 cache 
		L2 = new CacheController(2, 131072, BlockSize, 16, 20, mem);// 128KB, 64B Block, 16-Way Associative, 20 Cycle Access		
		L1 = null; // fully asocciative is 1
		L1 = new ProgramPatternController(1, 8192, BlockSize, 1, 1, mem);// 8KB, 64B Block, Fully associative, 1 Cycle Access

	}	



	//Verifying Hits for perfect memory pattern
	public static void BasicSequentialAccess() throws Exception{
		System.out.println("Running Perfect Sequential Memory Access");
		//For debugging perfect if you prefetech the next block always
		for(int i = 0; i < mem.getMemorySize(); i++){
			L1.get(i);
		}
		printStats("L1", L1.getCacheStats());
		printStats("L2", L2.getCacheStats());
		printStats("M1", mem.getMemoryStats());
	}
		
	public static void SequentialAccess() throws NullPointerException, IllegalArgumentException, IllegalAccessException{
		System.out.println("Running Sequential Memory Access Pattern");
		Random rand = new Random();
		Integer Sequence = 0;
		for(int i = 0; i < mem.getMemorySize() ; i++){
			Integer pos = rand.nextInt(i+1)+i; // get the first index from the 0 to the reference index plus de base index to make it sequential 
			Sequence = rand.nextInt((mem.getMemorySize()/BlockSize)) + pos; // how many iterations of the sequencial access pattern 
			if(Sequence > mem.getMemorySize()){ // got out of bounds, therefore we need to reach the top
				Sequence =  mem.getMemorySize()-1;
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
	
	public static void StrideAccess() throws Exception{
		System.out.println("Running Stride Memory Access Pattern");
		Random rand = new Random();
		for(int i = 0; i < mem.getMemorySize() ; i++){
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
	
	public static void ScatterAccessLight() throws NullPointerException, IllegalArgumentException, IllegalAccessException {
		System.out.println("Easy Scatter Memory Access Pattern");
		Random rand = new Random();
		for(int i = 0; i < mem.getMemorySize() ; i++){
			Integer pos = rand.nextInt(mem.getMemorySize()); // any random value within the whole memory 
			Integer length = rand.nextInt((mem.getMemorySize()/BlockSize));
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
	//Light
	
	public static void ScatterAccessMedium() throws NullPointerException, IllegalArgumentException, IllegalAccessException {
		System.out.println("Medium Scatter Memory Access Pattern");
		Random rand = new Random();
		for(int i = 0; i < mem.getMemorySize() ; i++){
			Integer pos = rand.nextInt(mem.getMemorySize()); // any random value within the whole memory 
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
		for(int i = 0; i < mem.getMemorySize() ; i++){
			Integer pos = rand.nextInt(mem.getMemorySize()); // any random value within the whole memory 
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
		for(int i = 0; i < mem.getMemorySize() ; i++){
				Integer pos = rand.nextInt(mem.getMemorySize());
				L1.get(pos);
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
	
}
