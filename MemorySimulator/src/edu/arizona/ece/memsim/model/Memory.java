package edu.arizona.ece.memsim.model;

import java.util.ArrayList;
import java.util.Iterator;

import edu.arizona.ece.memsim.Interfaces.CacheCallBack;
import edu.arizona.ece.memsim.Interfaces.WriteInvalidateListener;

/**
 * Models the Main System Memory of a Computer
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
	protected MemoryBlock[] memory;
	
	/**
	 * Block Size of this Memory
	 */
	protected Integer blockSize;
	
	/**
	 * Access Time of Memory
	 */
	protected Integer accessTime;
	
	/**
	 * All of the Caches that are One Level Below Memory
	 */
	protected ArrayList<CacheController> childCaches;
	
	protected CacheStatistics cacheStats;

	/**
	 * Creates a Memory Representation
	 * 
	 * @param tSize Total Size of Memory (in Bytes)
	 * @param bSize Block Size (in Bytes)
	 * @param aTime Access Time (in clock cycle)
	 */
	public Memory(Integer tSize, Integer bSize, Integer aTime){
		if(DEBUG_LEVEL >= 1)System.out.println("Memory(" + tSize + ", " + bSize +", " + aTime + ")");
		
		// Validity Checking
		if(tSize == null)throw new NullPointerException("tSize Can Not Be Null");
		if(tSize < 0)throw new IllegalArgumentException("tSize Must Be Greater Than Zero");
		
		if(bSize == null)throw new NullPointerException("tSize Can Not Be Null");
		if(bSize < 0)throw new IllegalArgumentException("bSize Must Be Greater Than Zero");
		
		if(aTime == null)throw new NullPointerException("aTime Can Not Be Null");
		if(aTime < 0)throw new IllegalArgumentException("aTime Must Be Greater Than Zero");
		
		blockSize = bSize;
		accessTime = aTime;
		
		// Check blockSize vs. totalSize
		if(tSize % blockSize != 0)throw new ArrayIndexOutOfBoundsException("Block Size Not Aligned with Total Size");
		
		Integer numBlocks = tSize / bSize;
		if(DEBUG_LEVEL >= 3)System.out.println("Creating memory[" + numBlocks + "]");
		memory = new MemoryBlock[numBlocks];
		
		childCaches = new ArrayList<CacheController>();
		
		cacheStats = new CacheStatistics();
		
		if(DEBUG_LEVEL >= 2)System.out.println("Memory() Finished");
	}
	
	/**
	 * Gets a Block of Memory
	 * 
	 * @param address Address of the MemoryBlock Requested
	 * @return MemoryBlock
	 */
	public MemoryBlock getBlock(Integer address){
		if(DEBUG_LEVEL >= 1)System.out.println("Memory.getBlock(" + address + ")");
		if(address == null)throw new NullPointerException("bAddress Can Not Be Null");
		if(address < 0)throw new ArrayIndexOutOfBoundsException("bAddress Must Be Positive");
		
		Integer mAddress = address / blockSize;
		
		if(memory[mAddress] == null){
			if(DEBUG_LEVEL >= 3)System.out.println("...memory[" + mAddress + "] MISS, Creating MemoryBlock");
			
			MemoryBlock newMB = new MemoryBlock(blockSize, address);
			
			for(int i = 0; i < blockSize; i++){
				if(DEBUG_LEVEL >= 4)System.out.println("...Creating MemoryElement(" + i + ", 0)");
				newMB.setElement(i, new MemoryElement(i, (byte)0));
			}
			
			memory[mAddress] = newMB;
		}else{
			if(DEBUG_LEVEL >= 3)System.out.println("...memory[" + address + "] HIT");
		}
		
		cacheStats.ACCESS++;
		cacheStats.BLOCKREAD++;
		
		MemoryBlock newMB = memory[mAddress].clone();
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Returning " + newMB + ")");
		
		return newMB;
	}
	
	/**
	 * Updates a MemoryBlock in Memory
	 * 
	 * @param block The MemoryBlock being written
	 * @return boolean If the memory block was written true, otherwise false
	 */
	public boolean putBlock(MemoryBlock block){
		if(DEBUG_LEVEL >= 1)System.out.println("Memory.putBlock(" + block.getBlockAddress() + ")");
		if(block == null)throw new NullPointerException("block Can Not Be Null");
		
		Integer memoryBlock = block.getBlockAddress() / blockSize;
		
		memory[memoryBlock] = block;
		
		cacheStats.ACCESS++;
		cacheStats.BLOCKWRITE++;
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Returning True");
		return true;
	}
	
	protected MemoryBlock cloneMemory(Integer memoryBlock){
		if(DEBUG_LEVEL >= 1)System.out.println("Memory.cloneMemory(MemoryBlock)");
		MemoryBlock newMB = new MemoryBlock(blockSize, memory[memoryBlock].getBlockAddress());
		
		for(int i = 0; i < blockSize; i++){
			newMB.setElement(i, memory[memoryBlock].getElement(i).clone());
		}
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Returning " + newMB);
		
		return newMB;
	}

	@Override
	public boolean registerChildCache(CacheController cacheController) {
		if(DEBUG_LEVEL >= 1)System.out.println("Memory.registerChildCache(CacheController)");
		
		if(cacheController == null)throw new NullPointerException("cacheController Can Not Be Null");
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Finished");
		
		return childCaches.add(cacheController);
	}

	@Override
	public boolean unRegisterChildCache(CacheController cacheController) {
		if(DEBUG_LEVEL >= 1)System.out.println("Memory.unRegisterChildCache(CacheController)");
		
		if(cacheController == null)throw new NullPointerException("cacheController Can Not Be Null");
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Finished");
		
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
		if(DEBUG_LEVEL >= 2)System.out.println("...Finished");
	}

	@Override
	public void onWriteUpdate(Integer address) {
		if(DEBUG_LEVEL >= 1)System.out.println("Memory.onWriteUpdate(" + address + ")");
		Iterator<CacheController> childCacheIterator = childCaches.iterator();
		while(childCacheIterator.hasNext()){
			childCacheIterator.next().onWriteUpdate(address);
		}
		// No Local Implementation Needed
		if(DEBUG_LEVEL >= 2)System.out.println("...Finished");
	}

	@Override
	public void onReadMiss(Integer address) {
		if(DEBUG_LEVEL >= 1)System.out.println("Memory.onReadMiss(" + address + ")");
		Iterator<CacheController> childCacheIterator = childCaches.iterator();
		while(childCacheIterator.hasNext()){
			childCacheIterator.next().onReadMiss(address);
		}
		// No Local Implementation Needed
		if(DEBUG_LEVEL >= 2)System.out.println("...Finished");
	}
	
	public CacheStatistics getMemoryStats(){
		return cacheStats;
	}
}
