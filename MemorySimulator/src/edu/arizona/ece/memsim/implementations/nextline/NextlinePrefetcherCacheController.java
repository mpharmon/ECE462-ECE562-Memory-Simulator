package edu.arizona.ece.memsim.implementations.nextline;

import edu.arizona.ece.memsim.model.CacheController;
import edu.arizona.ece.memsim.model.Memory;
import edu.arizona.ece.memsim.model.MemoryBlock;
import edu.arizona.ece.memsim.model.MemoryResult;

public class NextlinePrefetcherCacheController extends CacheController {
	
	public NextlinePrefetcherCacheController(Integer level, Integer tSize, Integer bSize, Integer assoc, Integer aTime,
			CacheController pCache) throws InterruptedException {
		super(level, tSize, bSize, assoc, aTime, pCache);
	}
	
	public NextlinePrefetcherCacheController(Integer level, Integer tSize, Integer bSize, Integer assoc, Integer aTime,
			Memory pMemory) throws InterruptedException {
		super(level, tSize, bSize, assoc, aTime, pMemory);
	}
	
	@Override
	public MemoryResult get(Integer eAddress) throws Exception{
		if(DEBUG_LEVEL >= 1)System.out.println("\nL" + cacheLevel + " CacheController.get(" + eAddress + ")");
		
		// Prevent Element Access if ChildCache(s) is/are Present
		if(childCaches.size() > 0)throw new IllegalAccessException("Can Not Call put if Child Caches Are Present");
		
		// Validity Checks
		if(eAddress == null)throw new NullPointerException("address Can Not Be Null");
		if(eAddress < 0)throw new IllegalArgumentException("address Must Be Greater Than Zero");
		
		// Create 
		MemoryResult returnValue = new MemoryResult();
		
		returnValue.addMemoryElement(cache.get(eAddress));
		
		// Next Line Pre-Fetch
		cache.get(eAddress + 1);

		if(DEBUG_LEVEL >= 2)System.out.println("...Returning " + returnValue);
		
		cacheStats.ACCESS++;
		
		return returnValue;
	}

	/**
	 * Gets a MemoryBlock from Cache, and loads it from a Parent Cache or Parent Memory If Necessary
	 * 
	 * @param bAddress Address of the MemoryBlock desired
	 * @return MemoryBlock
	 * @throws Exception 
	 */
	public MemoryBlock getBlock(Integer bAddress) throws Exception {
		if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + " CacheController.getBlock(" + bAddress + ")");
		
		// Prevent Block Access if Child Cache(s) is/are not Present
		if(childCaches.size() == 0)throw new IllegalAccessException("Can Not Call getBlock if Child Caches Are Not Present");
		
		// Validity Checks
		if(bAddress == null)throw new NullPointerException("address Can Not Be Null");
		if(bAddress < 0)throw new IllegalArgumentException("address Must Be Greater Than Zero");
		
		MemoryBlock returnValue = cache.getBlock(bAddress, blockSize);
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Returning " + returnValue);
		
		cacheStats.ACCESS++;
		
		return returnValue;
	}
	
	/**
	 * Puts a Value into Memory Element in This Level of Cache
	 * 
	 * @param eAddress Address of the data being written
	 * @param bite Byte of the data being written
	 * @throws Exception 
	 */
	public void put(Integer eAddress, Byte bite) throws Exception {
		if(DEBUG_LEVEL >= 1)System.out.println("\nL" + cacheLevel + " CacheController.put(" + eAddress + ", " + bite +")");
		
		//Prevent Element Access if ChildCache(s) is/are Present
		if(childCaches.size() > 0)throw new IllegalAccessException("Can Not Call put if Child Caches Are Present");
		
		// Validity Checks
		if(eAddress == null)throw new NullPointerException("address Can Not Be Null");
		if(eAddress < 0)throw new IllegalArgumentException("address Must Be Zero or Greater");
		
		if(bite == null)throw new NullPointerException("var Can Not Be Null");
		
		cache.put(eAddress, bite);
		
		// Next Line Pre-Fetch
		cache.get(eAddress + 1);
		
		cacheStats.ACCESS++;
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Finished");
	}
	
	/**
	 * Puts a MemoryBlock Into This Level of Cache
	 * 
	 * @param block MemoryBlock to be written
	 * @throws Exception 
	 */
	public void putBlock(MemoryBlock block) throws Exception{
		if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + " CacheController.putBlock(" + block.getBlockAddress() + ")");
		
		// Prevent Block Access if Child Cache(s) is/are not Present
		if(childCaches.size() == 0)throw new IllegalAccessException("Can Not Call getBlock if Child Caches Are Not Present");
			
		// Validity Checking
		if(block == null)throw new NullPointerException("block Can Not Be NULL");
		
		cache.putBlock(block);
		
		cacheStats.ACCESS++;
		//cacheStats.BLOCKWRITE++;
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Finished");
	}
	
}
