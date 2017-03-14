package edu.arizona.ece.memsim.implementations.core;

import edu.arizona.ece.memsim.model.CacheController;
import edu.arizona.ece.memsim.model.Memory;

import java.util.Random;

public class Program{
	
	private static CacheController L1, L2;
	
	private static Memory mem;
	
	public static void main(String[] args) throws InterruptedException{
		Run();
	}
	
	public static void Run() throws InterruptedException{
		// Currently Block Sizes Must Be EQUAL Among all Cache Level(s) and Memory
		mem = new Memory(536870912,64, 200);// 512MB, 64B Block, 200 Cycle Access
		L2 = new CacheController(2, 131072, 64, 16, 20, mem);// 128KB, 64B Block, 16-Way Associative, 20 Cycle Access
		L1 = new CacheController(1, 8192,64, 0, 1, L2);// 8KB, 64B Block, Fully Associative, 1 Cycle Access
		
		try {
			SequentialAccess();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void RandomAccess(){
		// TODO: Implement
	}
	
	public void StrideAccess(){
		// TODO: Implement
	}
	
	public static void SequentialAccess() throws Exception{
		System.out.println("Running Sequential Memory Access");
		Random rand = new Random();
		for(int i = 0; i < 134217728; i++){
			Integer rw = rand.nextInt(2);
			if(rw == 0){// Read
				L1.get(i);
			}else if(rw == 1){
				L1.put(i, (byte)rand.nextInt(Byte.MAX_VALUE + 1));
			}else{
				throw new Exception("Random Gave Value other than 0 or 1");
			}
		}
		// TODO: PRINT STATS
	}
}
