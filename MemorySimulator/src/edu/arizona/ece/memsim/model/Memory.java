package edu.arizona.ece.memsim.model;

import java.util.ArrayList;
import java.util.Iterator;

import edu.arizona.ece.memsim.Interfaces.CacheCallBack;
import edu.arizona.ece.memsim.Interfaces.WriteInvalidateListener;

/**
 * Models the Main System Memory of a Computer
 * 
 * Data stored in Array memory[] is byte indexed
 * 
 * @author Mike Harmon (mpharmon@email.arizona.edu)
 *
 */
public class Memory implements CacheCallBack, WriteInvalidateListener {
	
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
	 * Dynamic Array to Track the Child Caches to this Cache, this is used in Cache Coherency Protocols
	 */
	protected ArrayList<CacheController> childCache;
	
	/**
	 * List Holding the MemoryBlocks Currently in this Memory
	 */
	protected MemoryElement[] memory;
	
	/**
	 * Total Size of this Memory
	 */
	protected Integer totalSize;
	
	/**
	 * Access Time of Memory
	 */
	protected Integer accessTime;
	
	/**
	 * All of the Caches that are One Level Below Memory
	 */
	protected ArrayList<CacheController> childCaches;
	
	/**
	 * Memory (Cache) Statistics for this Memory
	 */
	protected CacheStatistics cacheStats;

	/**
	 * Creates a Memory Representation
	 * 
	 * @param tSize Total Size of Memory (in Bytes)
	 * @param bSize Block Size (in Bytes)
	 * @param aTime Access Time (in clock cycle)
	 */
	public Memory(Integer tSize, Integer aTime){
		if(DEBUG_LEVEL >= 1)System.out.println("Memory(" + tSize +", " + aTime + ")");
		
		// Validity Checking
		if(tSize == null)throw new NullPointerException("tSize Can Not Be Null");
		if(tSize < 0)throw new IllegalArgumentException("tSize Must Be Greater Than Zero");
		
		if(aTime == null)throw new NullPointerException("aTime Can Not Be Null");
		if(aTime < 0)throw new IllegalArgumentException("aTime Must Be Greater Than Zero");
		
		totalSize = tSize;
		accessTime = aTime;
		
		memory = new MemoryElement[totalSize];
		
		childCaches = new ArrayList<CacheController>();
		
		cacheStats = new CacheStatistics();
		
		if(DEBUG_LEVEL >= 2)System.out.println("Memory()...Finished");
	}

	/**
	 * Gets a Block of Memory
	 * 
	 * @param address Address of the MemoryBlock Requested
	 * @return MemoryBlock
	 */
	public MemoryBlock getBlock(Integer bAddress, Integer size){
		if(DEBUG_LEVEL >= 1)System.out.println("Memory.getBlock(" + bAddress + ", " + size + ")");
		
		// Validation
		if(bAddress == null)throw new NullPointerException("bAddress Can Not Be Null");
		if(bAddress < 0)throw new ArrayIndexOutOfBoundsException("bAddress Must Be Positive");
		
		if(size == null)throw new NullPointerException("size Can Not Be Null");
		if(size < 1)throw new ArrayIndexOutOfBoundsException("size Must Be Greater Than Zero");
		
		cacheStats.ACCESS++;
		
		// MemoryBlock to Return
		MemoryBlock newMB = new MemoryBlock(size, bAddress);
		
		if(DEBUG_LEVEL >= 4)System.out.println("Memory.getBlock()...Starting at " + bAddress + " Going to " + (bAddress + size - 1));
		
		// Load Memory Block to Return
		for(int i = bAddress; i < bAddress + size; i++){
			if(memory[i] == null){
				if(DEBUG_LEVEL >= 5)System.out.println("Memory.getBlock()...memory[" + i + "] MISS, Creating MemoryBlock");
				memory[i] = new MemoryElement(i, (byte)0);
			}else{
				if(DEBUG_LEVEL >= 5)System.out.println("Memory.getBlock()...memory[" + i + "] HIT");
			}
			
			Integer offset = i % size;
			
			newMB.setElement(offset, memory[i].clone());
		}
		
		cacheStats.BLOCKREAD_HIT++;
		
		if(DEBUG_LEVEL >= 2)System.out.println("Memory.getBlock()...Finished");
		
		return newMB;
	}
	
	
	/**
	 * Updates a MemoryBlock in Memory
	 * 
	 * @param block The MemoryBlock being written
	 * @return boolean If the memory block was written true, otherwise false
	 */
	public void putBlock(MemoryBlock block){
		if(DEBUG_LEVEL >= 1)System.out.println("Memory.putBlock(" + block.getBlockAddress() + ")");
		
		// Validate
		if(block == null)throw new NullPointerException("block Can Not Be Null");
		if((block.getBlockAddress() + block.getSize() - 1) > totalSize)throw new IllegalArgumentException("block Can Not Be Greater Than total Size");
		
		// Write MemoryElements to Memory
		MemoryElementIterator meIterator = block.getIterator();
		while(meIterator.hasNext()){
			MemoryElement element = meIterator.next();
			
			if(DEBUG_LEVEL >= 4)System.out.println("Memory.putBlock()...Writing memory[" + element.getElementAddress() + "]");
			
			memory[element.getElementAddress()].setData(element.getData());
		}
		
		cacheStats.ACCESS++;
		cacheStats.BLOCKWRITE_HIT++;
		
		if(DEBUG_LEVEL >= 2)System.out.println("Memory.putBlock()...Finished");
	}

	@Override
	public boolean registerChildCache(CacheController cacheController) {
		if(DEBUG_LEVEL >= 1)System.out.println("Memory.registerChildCache(CacheController)");
		
		if(cacheController == null)throw new NullPointerException("cacheController Can Not Be Null");
		
		if(DEBUG_LEVEL >= 2)System.out.println("Memory.registerChildCache()...Finished");
		
		return childCaches.add(cacheController);
	}

	@Override
	public boolean unRegisterChildCache(CacheController cacheController) {
		if(DEBUG_LEVEL >= 1)System.out.println("Memory.unRegisterChildCache(CacheController)");
		if(cacheController == null)throw new NullPointerException("cacheController Can Not Be Null");
		
		if(DEBUG_LEVEL >= 2)System.out.println("Memory.unRegisterChildCache()...Finished");
		
		return childCaches.remove(cacheController);
	}

	@Override
	public void onWriteMiss(Integer address) {
		if(DEBUG_LEVEL >= 1)System.out.println("Memory.onWriteMiss(" + address + ")");
		Iterator<CacheController> childCacheIterator = childCaches.iterator();
		while(childCacheIterator.hasNext()){
			childCacheIterator.next().onWriteMiss(address);
		}
		// No Local Implementation Needed
		if(DEBUG_LEVEL >= 2)System.out.println("Memory.onWriteMiss()...Finished");
	}

	@Override
	public void onWriteUpdate(Integer address) {
		if(DEBUG_LEVEL >= 1)System.out.println("Memory.onWriteUpdate(" + address + ")");
		Iterator<CacheController> childCacheIterator = childCaches.iterator();
		while(childCacheIterator.hasNext()){
			childCacheIterator.next().onWriteUpdate(address);
		}
		// No Local Implementation Needed
		if(DEBUG_LEVEL >= 2)System.out.println("Memory.onWriteUpdate()...Finished");
	}

	@Override
	public void onReadMiss(Integer address) {
		if(DEBUG_LEVEL >= 1)System.out.println("Memory.onReadMiss(" + address + ")");
		Iterator<CacheController> childCacheIterator = childCaches.iterator();
		while(childCacheIterator.hasNext()){
			childCacheIterator.next().onReadMiss(address);
		}
		// No Local Implementation Needed
		if(DEBUG_LEVEL >= 2)System.out.println("Memory.onReadMiss()...Finished");
	}
	
	/**
	 * @return {@link CacheStatistics} of this Memory
	 */
	public CacheStatistics getMemoryStats(){
		return cacheStats;
	}
}
