package edu.arizona.ece.memsim.implementations.markov;

import edu.arizona.ece.memsim.model.CacheController;
import edu.arizona.ece.memsim.model.Memory;
import edu.arizona.ece.memsim.model.MemoryResult;

public class MarkovPrefetcherCacheController extends CacheController {
	
	public MarkovPrefetcherCacheController(Integer level, Integer tSize, Integer bSize, Integer assoc, Integer aTime,
			CacheController pCache, Integer memSize) throws InterruptedException {
		super(level, tSize, bSize, assoc, aTime, pCache);
		HistoryTable.initialize(memSize/cache.getBlockSize());
	}
	
	public MarkovPrefetcherCacheController(Integer level, Integer tSize, Integer bSize, Integer assoc, Integer aTime,
			Memory pMemory, Integer memSize) throws InterruptedException {
		super(level, tSize, bSize, assoc, aTime, pMemory);
		HistoryTable.initialize(memSize/cache.getBlockSize());
	}

	public MemoryResult get(Boolean trackStats, Integer eAddress) throws Exception{
		if(DEBUG_LEVEL >= 1)System.out.println("\nL" + cacheLevel + " CacheController.get(" + eAddress + ")");
		
		// Prevent Element Access if ChildCache(s) is/are Present
		if(childCaches.size() > 0)throw new IllegalAccessException("Can Not Call put if Child Caches Are Present");
		
		// Validity Checks
		if(eAddress == null)throw new NullPointerException("address Can Not Be Null");
		if(eAddress < 0)throw new IllegalArgumentException("address Must Be Greater Than Zero");
		
		// Create 
		MemoryResult returnValue = new MemoryResult();
		
		returnValue.addMemoryElement(cache.get(true, eAddress));
		
		cache.get(false, 
				  HistoryTable.getNextCandidate(eAddress/cache.getBlockSize(),
				  cache.getBlockSize()));
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Returning " + returnValue);
		
		if(trackStats)cacheStats.ACCESS++;
		
		return returnValue;
	}
	
	/**
	 * Puts a Value into Memory Element in This Level of Cache
	 * 
	 * @param eAddress Address of the data being written
	 * @param bite Byte of the data being written
	 * @throws Exception 
	 */
	public void put(Boolean trackStats, Integer eAddress, Byte bite) throws Exception {
		if(DEBUG_LEVEL >= 1)System.out.println("\nL" + cacheLevel + " CacheController.put(" + eAddress + ", " + bite +")");
		
		//Prevent Element Access if ChildCache(s) is/are Present
		if(childCaches.size() > 0)throw new IllegalAccessException("Can Not Call put if Child Caches Are Present");
		
		// Validity Checks
		if(eAddress == null)throw new NullPointerException("address Can Not Be Null");
		if(eAddress < 0)throw new IllegalArgumentException("address Must Be Zero or Greater");
		
		if(bite == null)throw new NullPointerException("var Can Not Be Null");
		
		cache.put(trackStats, eAddress, bite);
		
		cache.get(false, 
				  HistoryTable.getNextCandidate(eAddress/cache.getBlockSize(),
				  cache.getBlockSize()));
		
		if(trackStats)cacheStats.ACCESS++;
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Finished");
	}
}
