package edu.arizona.ece.memsim.implementations.core;

import edu.arizona.ece.memsim.model.CacheController;
import edu.arizona.ece.memsim.model.CacheStatistics;
import edu.arizona.ece.memsim.model.CacheStatisticsAnalysis;
import edu.arizona.ece.memsim.model.Memory;

import java.util.ArrayList;
import java.util.Random;

public class ReportTestsProject{
	
	protected static CacheController L1, L2;
	
	protected static Memory mem;
	
	public static void main(String[] args) throws InterruptedException{
		Run();
	}
	
	public static void Run() throws InterruptedException{
		try {
			SequentialAccess(1000);
			RandomAccess(1000);
			StrideAccess(1000, 32);
			LinearAccess(1000, true, 32);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void RandomAccess(Integer times) throws Exception{
		Random rand = new Random();
		
		ArrayList<CacheStatistics> L1StatsArray = new ArrayList<CacheStatistics>();
		ArrayList<CacheStatistics> L2StatsArray = new ArrayList<CacheStatistics>();
		ArrayList<CacheStatistics> M1StatsArray = new ArrayList<CacheStatistics>();
		
		System.out.println("\n+----------------------------------+");
		System.out.println("| Running Random Memory Access " + times + " |");
		System.out.println("+----------------------------------+\n");
		
		// Run Test 'k' Times
		for(int k = 0; k < times; k++){
			Reset(false);
			
			for(int i = 0; i < 8192; i++){
				Integer rw = rand.nextInt(2);
				Integer pos = rand.nextInt(8192);
			
				if(rw == 0){// Read
					L1.get(true, pos);
				}else if(rw == 1){
					L1.put(true, pos, (byte)rand.nextInt(Byte.MAX_VALUE + 1));
				}else{
					throw new Exception("Random Gave Value other than 0 or 1");
				}
			}
			L1StatsArray.add(L1.getCacheStats());
			L2StatsArray.add(L2.getCacheStats());
			M1StatsArray.add(mem.getMemoryStats());
		}
		
		CacheStatisticsAnalysis L1Stats = CacheStatisticsAnalysis.AnalyzeArrayList(L1StatsArray);
		CacheStatisticsAnalysis L2Stats = CacheStatisticsAnalysis.AnalyzeArrayList(L2StatsArray);
		CacheStatisticsAnalysis M1Stats = CacheStatisticsAnalysis.AnalyzeArrayList(M1StatsArray);
		
		L1Stats.print("L1");
		L2Stats.print("L2");
		M1Stats.print("M1");
	}
	
	/**
	 * Performs Linear Access (Similar to Stride but with random selected blocks [and possibly randomly selected stride size)
	 * 
	 * @param times Number of Times the Test is Run
	 * @param randStride TRUE Indicates Random Stride Size, FALSE Indicates Fixed Stride Size
	 * @param strideSize If randStride is TRUE, this is the Largest Stride Possible; if randStride is FALSE this is the Stride Size
	 * @throws Exception
	 */
	public static void LinearAccess(Integer times, Boolean randStride, Integer strideSize) throws Exception{
		Random rand = new Random();
		
		ArrayList<CacheStatistics> L1StatsArray = new ArrayList<CacheStatistics>();
		ArrayList<CacheStatistics> L2StatsArray = new ArrayList<CacheStatistics>();
		ArrayList<CacheStatistics> M1StatsArray = new ArrayList<CacheStatistics>();
		
		System.out.println("\n+----------------------------------+");
		System.out.println("| Running Linear Memory Access " + times + " |");
		System.out.println("+----------------------------------+\n");
		
		// Run Simulation 'k' Times
		for(int k = 0; k < times; k++){
			Reset(false);
			
			for(int i = 0 ; i < 8192 - strideSize; i++){
				Integer startLocation = rand.nextInt(8192 - strideSize - 1);
				
				Integer sS = new Integer(0);
				
				if(randStride){
					sS = rand.nextInt(strideSize);
				}else{
					sS = strideSize;
				}
			
				for(int j = 0; j < sS; j++){
					Integer rw = rand.nextInt(2);
				
					if(rw == 0){// Read
						L1.get(true, (startLocation + i));
					}else if(rw == 1){// Write
						L1.put(true, (startLocation + i), (byte)rand.nextInt(Byte.MAX_VALUE + 1));
					}else{
						throw new Exception("Random Gave Value other than 0 or 1");
					}
				}
			}
			
			L1StatsArray.add(L1.getCacheStats());
			L2StatsArray.add(L2.getCacheStats());
			M1StatsArray.add(mem.getMemoryStats());
		}
		
		CacheStatisticsAnalysis L1Stats = CacheStatisticsAnalysis.AnalyzeArrayList(L1StatsArray);
		CacheStatisticsAnalysis L2Stats = CacheStatisticsAnalysis.AnalyzeArrayList(L2StatsArray);
		CacheStatisticsAnalysis M1Stats = CacheStatisticsAnalysis.AnalyzeArrayList(M1StatsArray);
		
		L1Stats.print("L1");
		L2Stats.print("L2");
		M1Stats.print("M1");
	}
	
	
	/**
	 * Performs Stride Access
	 * 
	 * @param times
	 * @param randStride
	 * @param strideSize
	 * @throws Exception
	 */
	public static void StrideAccess(Integer times, Integer strideSize, Integer numStride) throws Exception{
		
		
		Random rand = new Random();
		
		ArrayList<CacheStatistics> L1StatsArray = new ArrayList<CacheStatistics>();
		ArrayList<CacheStatistics> L2StatsArray = new ArrayList<CacheStatistics>();
		ArrayList<CacheStatistics> M1StatsArray = new ArrayList<CacheStatistics>();
		
		System.out.println("\n+----------------------------------+");
		System.out.println("| Running Stride Memory Access " + times + " |");
		System.out.println("+----------------------------------+\n");
		
		// Run Simulation 'k' Times
		for(int k = 0; k < times; k++){
			Reset(false);
			
			for(int i = 0; i < numStride; i++){
				for(int j = 0; j < numStrideElements; j++){
					Integer rw = rand.nextInt(2);
					
					if(rw == 0){// Read
						L1.get(true, (i * j + j));
					}else if(rw == 1){// Write
						L1.put(true, (i * j + j), (byte)rand.nextInt(Byte.MAX_VALUE + 1));
					}else{
						throw new Exception("Random Gave Value other than 0 or 1");
					}
				}
			}
			
			L1StatsArray.add(L1.getCacheStats());
			L2StatsArray.add(L2.getCacheStats());
			M1StatsArray.add(mem.getMemoryStats());
		}
		
		CacheStatisticsAnalysis L1Stats = CacheStatisticsAnalysis.AnalyzeArrayList(L1StatsArray);
		CacheStatisticsAnalysis L2Stats = CacheStatisticsAnalysis.AnalyzeArrayList(L2StatsArray);
		CacheStatisticsAnalysis M1Stats = CacheStatisticsAnalysis.AnalyzeArrayList(M1StatsArray);
		
		L1Stats.print("L1");
		L2Stats.print("L2");
		M1Stats.print("M1");
	}
	
	public static void SequentialAccess(Integer times) throws Exception{
		Random rand = new Random();
		
		ArrayList<CacheStatistics> L1StatsArray = new ArrayList<CacheStatistics>();
		ArrayList<CacheStatistics> L2StatsArray = new ArrayList<CacheStatistics>();
		ArrayList<CacheStatistics> M1StatsArray = new ArrayList<CacheStatistics>();
		
		System.out.println("\n+--------------------------------------+");
		System.out.println("| Running Sequential Memory Access " + times + " |");
		System.out.println("+--------------------------------------+");
		
		// Run Simulation 'k' Times
		for(int k = 0; k < times; k++){
			Reset(false);
			
			for(int i = 0; i < 8192; i++){
				Integer rw = rand.nextInt(2);
		
				if(rw == 0){// Read
					L1.get(true, i);
				}else if(rw == 1){
					L1.put(true, i, (byte)rand.nextInt(Byte.MAX_VALUE + 1));
				}else{
					throw new Exception("Random Gave Value other than 0 or 1");
				}
			}
			
			L1StatsArray.add(L1.getCacheStats());
			L2StatsArray.add(L2.getCacheStats());
			M1StatsArray.add(mem.getMemoryStats());
		}
		
		CacheStatisticsAnalysis L1Stats = CacheStatisticsAnalysis.AnalyzeArrayList(L1StatsArray);
		CacheStatisticsAnalysis L2Stats = CacheStatisticsAnalysis.AnalyzeArrayList(L2StatsArray);
		CacheStatisticsAnalysis M1Stats = CacheStatisticsAnalysis.AnalyzeArrayList(M1StatsArray);
		
		L1Stats.print("L1");
		L2Stats.print("L2");
		M1Stats.print("M1");
	}
	
	protected static void Reset(Boolean display) throws Exception{
		
		if(display){
			System.out.println("\n+---------------+");
			System.out.println("| Running Reset |");
			System.out.println("+---------------+\n");
		}
		
		mem = null;
		mem = new Memory(16384, 200);// 16KB, 200 Cycle Access
		L2 = null;
		L2 = new CacheController(2, 4096, 128, 16, 20, mem);// 4KB, 128B Block, 16-Way Associative, 20 Cycle Access
		L1 = null;
		L1 = new CacheController(1, 1024, 32, 0, 1, L2);// 1KB, 32B Block, Fully Associative, 1 Cycle Access
	}
}
