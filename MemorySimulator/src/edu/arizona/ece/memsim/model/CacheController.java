package edu.arizona.ece.memsim.model;

import java.util.ArrayList;
import java.util.Iterator;

import edu.arizona.ece.memsim.Interfaces.CacheCallBack;
import edu.arizona.ece.memsim.Interfaces.WriteInvalidateListener;

/**
 * Models L1-Ln Cache Memory
 * 
 * @author Mike Harmon (mpharmo@email.arizona.edu)
 *
 */
public class CacheController implements CacheCallBack, WriteInvalidateListener{
	
	/**
	 * Level of Debug Output
	 *	 0 - None (No Debug Output Will Be Generated)
	 * 	 1 - Low (Function Call Notifications Only)
	 * 	 2 - Low-Medium (Low plus Function Return Notifications)
	 *   3 - Medium (Low-Medium plus ???)
	 *   4 - Medium-High (Medium plus ???)
	 *   5 - High (Full Debug Output Will Be Generated)
	 */
	protected static Integer DEBUG_LEVEL = 0;
	
	/**
	 * Cache Level (1-n)
	 */
	protected Integer cacheLevel;
	
	/**
	 * Block Size (Number of Elements)
	 */
	protected Integer blockSize;
	
	/**
	 * Access Time of the Cache
	 */
	protected Integer accessTime;
	
	/**
	 * The Cache that is One Level Above this Level of Cache, This is Mutually Exclusive 
	 * with parentMemory with parentMemory having priority.
	 */
	protected CacheController parentCache;
	
	/**
	 * The Memory that is One Level Above this Level of Cache, This is Mutually Exclusive 
	 * with parentCache with parentMemory having priority.
	 */
	protected Memory parentMemory;
	
	protected Cache cache;
	
	/**
	 * Cache Statistics for the Level of Cache
	 */
	protected CacheStatistics cacheStats;
	
	/**
	 * All of the Caches that are One Level Below this Cache (for Cache Coherency)
	 */
	protected ArrayList<CacheController> childCaches;
	
	/**
	 * Controls If Cache Coherency Messages are Forward to Child Cache(s)
	 */
	protected boolean cacheCoherencyEnabled;
	
	/**
	 * Creates a Model CacheController with a Parent CacheController
	 * 
	 * @param level Cache Level (1-n)
	 * @param tSize Total Size of the Cache
	 * @param bSize Block Size of the Cache
	 * @param assoc Associativity of the Cache
	 * @param aTime Access Time of the Cache
	 * @param pCache Parent Cache (for 1 to n-1 Caches)
	 * @throws InterruptedException 
	 * @throws NullPointerException If pCache is NULL
	 */
	public CacheController(Integer level, Integer tSize, Integer bSize, Integer assoc, Integer aTime, CacheController pCache) throws InterruptedException{
		this(level, tSize, bSize, assoc, aTime);
		
		if(DEBUG_LEVEL >= 1)System.out.println("CacheController(" + level + ", " + tSize + ", " + bSize + ", " + assoc + ", " + aTime + ", Cache)");
		
		if(pCache == null)throw new NullPointerException("pCache Can Not Be Null");
		
		parentCache = pCache;
		
		// Register For Cache Coherency
		parentCache.registerChildCache(this);
	}
	
	/**
	 * Creates a Model Cache with a Parent Memory
	 * 
	 * @param level Cache Level (1-n)
	 * @param tSize Total Size of the Cache
	 * @param bSize Block Size of the Cache
	 * @param assoc Associativity of the Cache
	 * @param aTime Access Time of the Cache
	 * @param pMemory Parent Memory (for top level Cache)
	 * @throws InterruptedException 
	 */
	public CacheController(Integer level, Integer tSize, Integer bSize, Integer assoc, Integer aTime, Memory pMemory) throws InterruptedException{
		this(level, tSize, bSize, assoc, aTime);
		
		if(DEBUG_LEVEL >= 1)System.out.println("CacheController(" + level + ", " + tSize + ", " + bSize + ", " + assoc + ", " + aTime + ", Memory)");
		
		if(pMemory == null)throw new NullPointerException("pMemory Can Not Be Null");
		
		parentMemory = pMemory;
		
		// Register For Cache Coherency
		parentMemory.registerChildCache(this);
	}
	
	/**
	 * All Inheritance of this Class MUST Call this Constructor in the Custom Constructor
	 * 
	 * @param level Cache Level (1-n)
	 * @param tSize Total Size of the Cache
	 * @param bSize Block Size of the Cache
	 * @param assoc Associativity of the Cache
	 * @param aTime Access Time of the Cache
	 * @throws InterruptedException 
	 */
	protected CacheController(Integer level, Integer tSize, Integer bSize, Integer assoc, Integer aTime) throws InterruptedException{
		if(DEBUG_LEVEL >= 1)System.out.println("\nCacheController(" + level + ", " + tSize + ", " + bSize + ", " + assoc + ", " + aTime + ")");
		
		if(tSize == null)throw new NullPointerException("tSize Can Not Be Null");
		if(tSize < 1)throw new IllegalArgumentException("tSize Must Be Positive");
		
		if(bSize == null)throw new NullPointerException("bSize Can Not Be Null");
		if(bSize < 1)throw new IllegalArgumentException("bSize Must Be Positive");
		
		if(assoc == null)throw new NullPointerException("associativity Can Not Be Null");
		if(assoc < 0)throw new IllegalArgumentException("Associativity Must Be Positive");
		
		if(aTime == null)throw new NullPointerException("aTime Can Not Be Null");
		if(aTime < 1)throw new IllegalArgumentException("Access Time Must Be Positive");
		
		cache = new Cache(tSize, bSize, assoc, this, level);
		
		cacheLevel = level;
		accessTime = aTime;
		cacheCoherencyEnabled = false;
		blockSize = bSize;
		
		if(tSize % bSize != 0)throw new ArrayIndexOutOfBoundsException("Block Size Not Aligned with Total Size");
		
		// Initialize Child Cache Array
		childCaches = new ArrayList<>();
		
		cacheStats = new CacheStatistics();
		
		if(DEBUG_LEVEL >= 2)System.out.println("CacheController()...Finished");
	}
	
	/**
	 * Returns a {@link MemoryResult} that Wraps a {@link MemoryElement}
	 * 
	 * For Backwards Compatibility; Use get(Boolean trackStats, Integer eAddress)
	 * 
	 * @deprecated
	 * 
	 * @param eAddress Address of the Element Requested
	 * @return {@link MemoryResult} Containing the {@link MemoryElement} Refrenced By eAddress
	 * @throws Exception
	 */
	//public MemoryResult get(Integer eAddress) throws Exception{
	//	return get(true, eAddress);
	//}
	
	/**
	 * Returns a {@link MemoryResult} that Wraps a {@link MemoryElement}
	 * 
	 * @param eAddress Address of the Memory Locator Desired
	 * @return MemoryResult 
	 * @throws Exception 
	 */
	public MemoryResult get(Boolean trackStats, Integer eAddress) throws Exception{
		if(DEBUG_LEVEL >= 1)System.out.println("\nL" + cacheLevel + "-CacheController.get(" + eAddress + ")");
		
		// Prevent Element Access if ChildCache(s) is/are Present
		if(childCaches.size() > 0)throw new IllegalAccessException("Can Not Call put if Child Caches Are Present");
		
		// Validity Checks
		if(eAddress == null)throw new NullPointerException("address Can Not Be Null");
		if(eAddress < 0)throw new IllegalArgumentException("address Must Be Greater Than Zero");
		
		// Create 
		MemoryResult returnValue = new MemoryResult();
		
		returnValue.addMemoryElement(cache.get(trackStats, eAddress));
		
		if(DEBUG_LEVEL >= 2)System.out.println("L" + cacheLevel + "-CacheController.get()...Finished");
		
		if(trackStats)cacheStats.ACCESS++;
		
		return returnValue;
	}
	
	/**
	 * Gets a MemoryBlock from Cache
	 * 
	 * For Backwards Compatibility; Use getBlock(Boolean trackStats, Integer bAddress, Integer size)
	 * 
	 * @deprecated
	 * 
	 * @param bAddress Address of the MemoryBlock desired
	 * @param size Number of Elements to Get
	 * @return {@link MemoryBlock} Starting at bAddress Containing size Number of Elements
	 * @throws Exception
	 */
	public MemoryBlock getBlock(Integer bAddress, Integer size) throws Exception{
		return getBlock(true, bAddress, size);
	}
	
	/**
	 * Gets a MemoryBlock from Cache
	 * 
	 * @param bAddress Address of the MemoryBlock desired
	 * @param size Number of Elements to Get
	 * @return MemoryBlock
	 * @throws Exception 
	 */
	public MemoryBlock getBlock(Boolean trackStats, Integer bAddress, Integer size) throws Exception {
		if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + "-CacheController.getBlock(" + bAddress + ")");
		
		// Prevent Block Access if Child Cache(s) is/are not Present
		if(childCaches.size() == 0)throw new IllegalAccessException("Can Not Call getBlock if Child Caches Are Not Present");
		
		// Validity Checks
		if(bAddress == null)throw new NullPointerException("address Can Not Be Null");
		if(bAddress < 0)throw new IllegalArgumentException("address Must Be Greater Than Zero");
		
		if(size == null)throw new NullPointerException("size Can Not Be Null");
		if(size <= 0)throw new IllegalArgumentException("size Must Be Greater Than Zero");
		
		MemoryBlock returnValue = cache.getBlock(trackStats, bAddress, size);
		
		if(DEBUG_LEVEL >= 2)System.out.println("L" + cacheLevel + "-CacheController.getBlock()...Finished");
		
		cacheStats.ACCESS++;
		
		return returnValue;
	}
	
	/**
	 * Puts a Value into a Memory Element in this Level of Cache
	 * 
	 * For Backwards Compatibilty, Uuse put(Boolean trackStats, Integer eAddress, Byte bite)
	 * 
	 * @deprecated
	 * 
	 * @param eAddress
	 * @param bite
	 * @throws Exception
	 */
	public void put(Integer eAddress, Byte bite) throws Exception{
		put(true, eAddress, bite);
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
		
		cacheStats.ACCESS++;
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Finished");
	}
	
	/**
	 * Puts a MemoryBlock Into This Level of Cache.
	 * 
	 * For Backwards Compatibility; USE putBlock(Boolean trackStats, MemoryBlock block)
	 * 
	 * @deprecated
	 * 
	 * @param block MemoryBlock to be written
	 * @throws Exception
	 */
	public void putBlock(MemoryBlock block) throws Exception{
		putBlock(true, block);
	}
	
	/**
	 * Puts a MemoryBlock Into This Level of Cache
	 * 
	 * @param trackStats Controls if Statistics are Tracked for this Request
	 * @param block MemoryBlock to be written
	 * @throws Exception 
	 */
	public void putBlock(Boolean trackStats, MemoryBlock block) throws Exception{
		if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + " CacheController.putBlock(" + block.getBlockAddress() + ")");
		
		// Prevent Block Access if Child Cache(s) is/are not Present
		if(childCaches.size() == 0)throw new IllegalAccessException("Can Not Call getBlock if Child Caches Are Not Present");
			
		// Validity Checking
		if(block == null)throw new NullPointerException("block Can Not Be NULL");
		
		cache.putBlock(trackStats,block);
		
		cacheStats.ACCESS++;
		
		if(DEBUG_LEVEL >= 2)System.out.println("CacheController.putBlock()...Finished");
	}
	
	@Override
	public boolean registerChildCache(CacheController newChild) {
		if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + "-CacheController.registerChildCache()");
		
		if(newChild == null)throw new NullPointerException("newChild Can Not Be Null");
		
		boolean returnValue = childCaches.add(newChild);
		
		if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + "-Cache.registerChildCache()...Finished");
		
		return returnValue;
	}

	@Override
	public boolean unRegisterChildCache(CacheController oldChild) {
		if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + "-CacheController.unRegisterChildCache(" + oldChild + ")");
		
		if(oldChild == null)throw new NullPointerException("oldChild Can Not Be Null");
		
		boolean returnValue = childCaches.remove(oldChild);
		
		if(DEBUG_LEVEL >= 2)System.out.println("L" + cacheLevel + "-CacheController.unRegisterChildCache()...Finished");
		
		return returnValue;
	}

	@Override
	public void onWriteMiss(Integer address) {
		if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + " CacheController.onWriteMiss(" + address + ")");
		
		if(cacheCoherencyEnabled){
			Iterator<CacheController> childCacheIterator = childCaches.iterator();
			
			// Distribute Write Miss Message to all Child Caches
			while(childCacheIterator.hasNext()){
				childCacheIterator.next().onWriteMiss(address);
			}
			
			cacheStats.INVALIDATE++;
			
			// TODO: Implement Cache Coherancy
		}
		
		if(DEBUG_LEVEL >= 2)System.out.println("L" + cacheLevel + "-CacheController.onWriteMiss()...Finished");
	}

	@Override
	public void onReadMiss(Integer address) {
		if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + "-CacheController.onReadMiss(" + address + ")");
		
		if(cacheCoherencyEnabled){
			Iterator<CacheController> childCacheIterator = childCaches.iterator();
			
			// Distribute Read Miss to all Child Caches
			while(childCacheIterator.hasNext()){
				childCacheIterator.next().onReadMiss(address);
			}
			
			// TODO: Increment Proper Statistics
			
			// TODO: Implement Cache Coherancy
		}
		
		if(DEBUG_LEVEL >= 2)System.out.println("L" + cacheLevel + "-CacheController.onReadMiss()...Finished");
	}

	@Override
	public void onWriteUpdate(Integer address) {
		if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + "-CacheController.onWriteUpdate(" + address + ")");
		
		if(cacheCoherencyEnabled){
			Iterator<CacheController> childCacheIterator = childCaches.iterator();
			
			while(childCacheIterator.hasNext()){
				childCacheIterator.next().onWriteUpdate(address);
			}
			
			cacheStats.INVALIDATE++;
			
			// TODO: Implement Cache Coherancy
		}
		
		if(DEBUG_LEVEL >= 2)System.out.println("L" + cacheLevel + "-CacheController.onWriteUpdate()...Finished");
	}
	
	/**
	 * Gets the CacheStatistics for this Level of Cache
	 * 
	 * @return {@link CacheStatistics} for this Cache
	 */
	public CacheStatistics getCacheStats(){
		return cacheStats;
	}
}
