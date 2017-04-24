package edu.arizona.ece.memsim.implementations.core;

import edu.arizona.ece.memsim.implementations.nextline.NextlinePrefetcherCacheController;
import edu.arizona.ece.memsim.implementations.stream.StreamPrefetcherCacheController;
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
			//SequentialAccess(100);
			//RandomAccess(100);
			StrideAccess(100, 128, 32);
			//LinearAccess(100, true, 128);
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
			ResetStream(false);
			
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
			ResetBase(false);
			
			Integer rS = new Integer(0);
			
			if(randStride){
				rS = strideSize / 2;
			}else{
				rS = strideSize;
			}
			
			for(int i = 0 ; i < 8192 / rS; i++){
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
						L1.get(true, (startLocation + j));
					}else if(rw == 1){// Write
						L1.put(true, (startLocation + j), (byte)rand.nextInt(Byte.MAX_VALUE + 1));
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
	 * @param times Number of Times to Run the Simulation
	 * @param strideSize Size of the Stride to Access
	 * @param numStride Number of Strides
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
			ResetBase(false);
			
			Integer startAddress = rand.nextInt(8193 - (strideSize * numStride));
			Integer endAddress = startAddress + (strideSize * numStride) - 1;
			
			for(int i = startAddress; i < startAddress + strideSize; i++){// Columns
				for(int j = i; j <= endAddress; j += strideSize){// Elements
					Integer rw = rand.nextInt(2);
					
					if(rw == 0){// Read
						L1.get(true, j);
					}else if(rw == 1){// Write
						L1.put(true, j, (byte)rand.nextInt(Byte.MAX_VALUE + 1));
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
			ResetStream(false);
			
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
	
	protected static void ResetBase(Boolean display) throws Exception{
		
		if(display){
			System.out.println("\n+--------------------+");
			System.out.println("| Running Base Reset |");
			System.out.println("+--------------------+\n");
		}
		
		mem = null;
		mem = new Memory(16384, 200);// 16KB, 200 Cycle Access
		L2 = null;
		L2 = new CacheController(2, 4096, 128, 16, 20, mem);// 4KB, 128B Block, 16-Way Associative, 20 Cycle Access
		L1 = null;
		L1 = new CacheController(1, 1024, 32, 0, 1, L2);// 1KB, 32B Block, Fully Associative, 1 Cycle Access
	}
	
	protected static void ResetNextLine(Boolean display) throws Exception{
		
		if(display){
			System.out.println("\n+------------------------+");
			System.out.println("| Running Nextline Reset |");
			System.out.println("+------------------------+\n");
		}
		
		mem = null;
		mem = new Memory(16384, 200);// 16KB, 200 Cycle Access
		L2 = null;
		L2 = new CacheController(2, 4096, 128, 16, 20, mem);// 4KB, 128B Block, 16-Way Associative, 20 Cycle Access
		L1 = null;
		L1 = new NextlinePrefetcherCacheController(1, 1024, 32, 0, 1, L2);// 1KB, 32B Block, Fully Associative, 1 Cycle Access
	}
	
	protected static void ResetStream(Boolean display) throws Exception{
		
		if(display){
			System.out.println("\n+----------------------+");
			System.out.println("| Running Stream Reset |");
			System.out.println("+---------------------+\n");
		}
		
		mem = null;
		mem = new Memory(16384, 200);// 16KB, 200 Cycle Access
		L2 = null;
		L2 = new CacheController(2, 4096, 128, 16, 20, mem);// 4KB, 128B Block, 16-Way Associative, 20 Cycle Access
		L1 = null;
		L1 = new StreamPrefetcherCacheController(1, 1024, 32, 0, 1, L2);// 1KB, 32B Block, Fully Associative, 1 Cycle Access
	}
}
