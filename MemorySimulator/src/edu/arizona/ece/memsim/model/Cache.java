package edu.arizona.ece.memsim.model;

import java.util.concurrent.ArrayBlockingQueue;

/**
 *  Models the Physical Cache
 * 
 * @author Mike Harmon (mpharmon@email.arizona.edu)
 * 
 */
public class Cache {
	
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
	 * Link Back to Cache Controller
	 */
	protected CacheController cacheController;
	
	/**
	 * Total Size of the Cache
	 */
	protected Integer totalSize;
	
	/**
	 * Block Size of the Cache
	 */
	protected Integer blockSize;
	
	/**
	 * Number of Offset Bits of the Cache
	 */
	protected Integer offsetShift;
	
	/**
	 * Number of Rows Per Set
	 */
	protected Integer wayRows;
	
	/**
	 * Associativity of the Cache
	 */
	protected Integer associativity;
	
	/**
	 * List Holding the MemoryBlocks Currently in this Cache
	 */
	protected MemoryBlock[] memory;
	
	/**
	 * Queue Holding the Memory Address of the Least Recently Used (LRU) {@link MemoryBlock}
	 */
	protected ArrayBlockingQueue<Integer>[] loadQueue;
	
	protected CacheStatistics cacheStats;
	
	/**
	 * Creates a Cache
	 * 
	 * @param tSize Total Size of the Cache
	 * @param bSize Block Size of the Cache
	 * @param assoc Associativity of the Cache
	 * @param cc CacheController of the Cache
	 * @throws InterruptedException 
	 * @throws NullPointerException When newSize is NULL
	 * @throws IllegalArgumentException When newSize is Negative
	 */
	@SuppressWarnings("unchecked")
	public Cache(Integer tSize, Integer bSize, Integer assoc, CacheController cc) throws InterruptedException{
		if(DEBUG_LEVEL >= 1)System.out.println("Cache(" + tSize + ", " + bSize + ", " + assoc +", CacheController)");
		
		if(tSize == null)throw new NullPointerException("tSize Can Not Be Null");
		if(tSize < 1)throw new IllegalArgumentException("tSize Must Be Greater Than One");
		
		if(bSize == null)throw new NullPointerException("bSize Can Not Be Null");
		if(bSize < 1)throw new IllegalArgumentException("bSize Must Be Greater Than One");
		
		if(assoc == null)throw new NullPointerException("bSize Can Not Be Null");
		if(assoc < 0)throw new IllegalArgumentException("bSize Must Be Positive");
		
		cacheController = cc;
		totalSize = tSize;
		blockSize = bSize;
		offsetShift = (int)(Math.log(blockSize) / Math.log(2));
		associativity = assoc;
		
		if(associativity == 0){
			wayRows = 1;
		}else if(associativity == 1){
			wayRows = totalSize / blockSize;
		}else{
			wayRows = totalSize / associativity / blockSize;
		}
		
		// Check Values for Fully and Set Associative Caches
		if(associativity > 0){
			Integer waySize = totalSize / associativity;
					
			if(tSize % waySize != 0)throw new ArrayIndexOutOfBoundsException("Associativity Not Aligned with Total Size");
				
			loadQueue = (ArrayBlockingQueue<Integer>[]) new ArrayBlockingQueue<?>[associativity];
			for(int i = 0; i < associativity; i++){
				loadQueue[i] = new ArrayBlockingQueue<Integer>(wayRows);
				for(int j = 0; j < wayRows; j++){
					loadQueue[i].put(new Integer(i * wayRows + j));
					if(DEBUG_LEVEL == 5)System.out.println("Way " + i + " Line " + (i * wayRows + j));
				}
			}
		};
		
		// Create Storage Array 
		Integer numBlocks = tSize / bSize;
		if(DEBUG_LEVEL >= 4)System.out.println("...Creating memory[" + numBlocks + "]");
		memory = new MemoryBlock[numBlocks];
	}
	
	/**
	 * Returns the MemoryElement given by eAddress,
	 * 
	 * @param eAddress Element Address 
	 * @return The MemoryElement Requested
	 * @throws IllegalAccessException 
	 * @throws NullPointerException When eAddress is NULL
	 * @throws IndexOutOfBoundsException When eAddress is Less Than Zero
	 */
	public MemoryElement get(Integer eAddress) throws IllegalAccessException, NullPointerException{
		if(DEBUG_LEVEL >= 1)System.out.println("Cache.get(" + eAddress + ")");
		// Validation
		if(eAddress == null)throw new NullPointerException("eAddress Can Not Be NULL");
		if(eAddress < 0)throw new IndexOutOfBoundsException("eAddress Must Be Positive (Given " + eAddress);

		Integer bAddress = eAddress / blockSize;
		Integer offset = eAddress % blockSize;
		
		for(Integer mAddress : getPossibleMemoryAddressArray(eAddress)){
			if(DEBUG_LEVEL >= 3)System.out.print("...Looking in Memory[" + mAddress + "] for bAddress " + bAddress);
			if(memory[mAddress] != null){
				if(memory[mAddress].getBlockAddress().equals(bAddress)){
					if(DEBUG_LEVEL >= 3)System.out.println("...HIT");
					// Adjust loadQueue
					cacheController.cacheStats.READ_HIT++;
					return memory[mAddress].getElement(offset);
				}
				if(DEBUG_LEVEL >= 3)System.out.println("...MISS");
			}
			if(DEBUG_LEVEL >= 3)System.out.println("...MISS");
		}
		
		// If We Get Here We have a Miss
		cacheController.cacheStats.READ_MISS++;
		if(DEBUG_LEVEL >= 3)System.out.println("...Cache Miss");
		Integer mAddress = getNextWriteLocation(bAddress);
		//Write Back If Necessary
		if(memory[mAddress] != null)writeBack(mAddress);
		//Resolve Miss
		resolveMiss(mAddress, bAddress);
		//Return Value
		MemoryElement mElement = memory[mAddress].getElement(offset);
		if(DEBUG_LEVEL >= 2)System.out.println("Returning MemoryElement " + mElement);
		return mElement;
	}
	
	public MemoryBlock getBlock(Integer bAddress) throws IllegalAccessException, NullPointerException, IllegalArgumentException{
		if(DEBUG_LEVEL >= 1)System.out.println("Cache.getBlock(" + bAddress + ")");
		
		// Validation
		if(bAddress == null)throw new NullPointerException("eAddress Can Not Be NULL");
		if(bAddress < 0)throw new IndexOutOfBoundsException("eAddress Must Be Positive (Given " + bAddress);
		
		// Cache Hit?
		for(Integer mAddress : getPossibleMemoryAddressArray(bAddress)){
			if(DEBUG_LEVEL >= 3)System.out.print("...Looking in Memory[" + mAddress + "] for bAddress " + bAddress);
			if(memory[mAddress] != null){  
				if(memory[mAddress].getBlockAddress().equals(bAddress)){
					if(DEBUG_LEVEL >= 3)System.out.println("...HIT");
					cacheController.cacheStats.BLOCKREAD_HIT++;
					// Adjust loadQueue
					return memory[mAddress];
				}
				if(DEBUG_LEVEL >= 3)System.out.println("...MISS");
			}
			if(DEBUG_LEVEL >= 3)System.out.println("...MISS");
		}
		
		// If We Get Here We Have a Cache Miss
		cacheController.cacheStats.BLOCKREAD_MISS++;
		if(DEBUG_LEVEL >= 3)System.out.println("...Cache Miss");
		Integer mAddress = getNextWriteLocation(bAddress);
		//Write Back If Necessary
		if(memory[mAddress] != null)writeBack(mAddress);
		//Resolve Miss
		resolveMiss(mAddress, bAddress);
		//Return Value
		return memory[mAddress].clone();
	}
	
	/**
	 * Puts the Byte given by bite into the local Cache.
	 * 
	 * @param eAddress Memory Location to Place the block
	 * @param bite Byte to be Written
	 * @throws NullPointerException When block or memAddress is NULL
	 * @throws IllegalAccessException 
	 * @throws IndexOutOfBoundsException
	 */
	public void put(Integer eAddress, Byte bite) throws NullPointerException, IndexOutOfBoundsException, IllegalAccessException{
		if(DEBUG_LEVEL >= 1)System.out.println("Cache.put(" + eAddress + ", " + bite + ")");
		// Validation
		if(eAddress == null)throw new NullPointerException("eAddress Can Not Be NULL");
		if(eAddress < 0)throw new IndexOutOfBoundsException("eAddress Must Positive");
		if(bite == null)throw new NullPointerException("bite Can Not Be NULL");
		
		Integer bAddress = eAddress / blockSize; // the value above just have you eddress+1 , what i think what baddress should do is divide the address 
		Integer offset = eAddress % blockSize; // cache offset is to find the data within the block 
		
		// Cache Hit?
		for(Integer mAddress : getPossibleMemoryAddressArray(eAddress)){
			if(DEBUG_LEVEL >= 3)System.out.print("...Looking in Memory[" + mAddress + "] for bAddress " + bAddress);
			if(memory[mAddress] != null){
				if(memory[mAddress].getBlockAddress().equals(bAddress)){
					if(DEBUG_LEVEL >= 3)System.out.println("...HIT");
					cacheController.cacheStats.WRITE_HIT++;
					// Adjust loadQueue
					memory[mAddress].getElement(offset).setData(bite);
					return;
				}
				if(DEBUG_LEVEL >= 3)System.out.println("...MISS");
			}
			if(DEBUG_LEVEL >= 3)System.out.println("...MISS");
		}
		
		// If We Get Here We have a write Miss that means that the block is not in the cache
		if(DEBUG_LEVEL >= 3)System.out.println("...Cache Miss");
		cacheController.cacheStats.WRITE_MISS++;
		Integer mAddress = getNextWriteLocation(eAddress);
		//Write Back If Necessary
		if(memory[mAddress] != null)writeBack(mAddress);
		//Resolve Miss
		resolveMiss(mAddress, eAddress);
		//Update Value
		memory[mAddress].getElement(offset).setData(bite);
	}
	
	
	/*
	 * Sets aprefetch block without affecting the stats
	 * */
	public void putPrefetch(Integer eAddress, Byte bite) throws NullPointerException, IndexOutOfBoundsException, IllegalAccessException{
		if(DEBUG_LEVEL >= 1)System.out.println("Cache.put(" + eAddress + ", " + bite + ")");
		// Validation
		if(eAddress == null)throw new NullPointerException("eAddress Can Not Be NULL");
		if(eAddress < 0)throw new IndexOutOfBoundsException("eAddress Must Positive");
		if(bite == null)throw new NullPointerException("bite Can Not Be NULL");
		
		Integer bAddress = eAddress / blockSize;
		Integer offset = eAddress % blockSize; // cache offset is to find the data within the block 
		// to prefetch we first check if the parent has the memory
		for(Integer mAddress : getPossibleMemoryAddressArray(eAddress)){
			if(memory[mAddress] != null){ //we already have the memory at the cache 
				if(memory[mAddress].getBlockAddress() == bAddress ){	
						// Adjust loadQueue
						memory[mAddress].getElement(offset).setData(bite); 
						return;
				}
			}
		}
		
		// prefetch if is not there 

		Integer mAddress = getNextWriteLocation(eAddress);
		//Write Back If Necessary
		if(memory[mAddress] != null)writeBack(mAddress);
		//Resolve Miss
		resolveMiss(mAddress, eAddress);
		//Update Value
		memory[mAddress].getElement(offset).setData(bite);
	}
	
	
	public void putBlock(MemoryBlock block) throws IllegalAccessException, NullPointerException{
		if(DEBUG_LEVEL >= 1)System.out.println("Cache.putBlock(" + block.getBlockAddress() + ")");
		
		// Validation
		if(block == null)throw new NullPointerException("block Can Not Be NULL");
		
		// Cache Hit?
		for(Integer mAddress : getPossibleMemoryAddressArray(block.getBlockAddress())){
			if(DEBUG_LEVEL >= 3)System.out.print("...Looking in Memory[" + mAddress + "] for bAddress " + block.getBlockAddress());
			if(memory[mAddress] != null){
				if(memory[mAddress].getBlockAddress().equals(block.getBlockAddress())){
					if(DEBUG_LEVEL >= 3)System.out.println("...HIT");
					cacheController.cacheStats.BLOCKWRITE_HIT++;
					// Adjust loadQueue
					memory[mAddress] = block;
					return;
				}
				if(DEBUG_LEVEL >= 3)System.out.println("...MISS");
			}
			if(DEBUG_LEVEL >= 3)System.out.println("...MISS");
		}
		
		// If We Get Here We have a Miss
		if(DEBUG_LEVEL >= 3)System.out.println("...Cache Miss");
		cacheController.cacheStats.BLOCKWRITE_MISS++;
		Integer mAddress = getNextWriteLocation(block.getBlockAddress());
		//Write Back If Necessary
		if(memory[mAddress] != null)writeBack(mAddress);
		//Resolve Miss
		resolveMiss(mAddress, block.getBlockAddress());
		//Update Value
		memory[mAddress] = block;
	}
	
		public Integer getAssoc(){
			return wayRows;
		}
		public Integer getBlockSize(){
			return blockSize;
		}
	
	private Integer[] getPossibleMemoryAddressArray(Integer address){
		if(DEBUG_LEVEL >= 1)System.out.println("Cache.getPossibleMemoryAddressArray(" + address + ")");
		Integer[] returnArray = new Integer[wayRows];
		if(associativity == 0){// Direct Mapped Cache
				if(DEBUG_LEVEL >= 3)System.out.print("...Direct Mapped Cache, Only 1 Possible Location: ");
				returnArray[0] = (address & ((totalSize / blockSize - 1) << offsetShift)) >> offsetShift;
				if(DEBUG_LEVEL >= 4)System.out.println(returnArray[0]);
		}else if(associativity == 1){// Fully Associative Cache
				if(DEBUG_LEVEL >= 3)System.out.print("...Fully Associatve Cache, " + wayRows + " Possible Locations");
				for(int i = 0; i < wayRows; i++){
					returnArray[i] = i;
					if(DEBUG_LEVEL >= 4)System.out.println("..." + i);
				}
		}else{// Set Associative Cache
			Integer set = (address & ((associativity - 1) << offsetShift)) >> offsetShift;
			Integer setSize = totalSize / blockSize / associativity;
			Integer setStart = set * setSize;
			if(DEBUG_LEVEL >= 3)System.out.println("...Set Associatve Cache, " + setSize + " Possible Locations");
			for(int i = 0; i < setSize; i++){
				returnArray[i] = setStart + i;
				if(DEBUG_LEVEL >= 4)System.out.println("..." + (setStart + i));
			}
		}
		return returnArray;
	}
	
	private Integer getNextWriteLocation(Integer address){
		if(DEBUG_LEVEL >= 1)System.out.println("Cache.getNextWriteLocation(" + address + ")");
		Integer nextLocation;
		if(associativity == 0){// Direct Mapped Cache
			nextLocation = (address & ((totalSize / blockSize - 1) << offsetShift)) >> offsetShift;
		}else if(associativity == 1){// Fully Associative Cache
			nextLocation = loadQueue[0].poll();
			loadQueue[0].add(nextLocation);
		}else{// Set Associative Cache
			Integer set = (address & ((associativity - 1) << offsetShift)) >> offsetShift;
			nextLocation = loadQueue[set].poll();
			loadQueue[set].add(nextLocation);
		}
		return nextLocation;
	}
	
	private void resolveMiss(Integer mAddress, Integer address) throws IllegalAccessException, NullPointerException, IllegalArgumentException{
		if(DEBUG_LEVEL >= 1)System.out.println("Cache.resolveMiss(" + mAddress + ", " + address + ")");
		
		MemoryBlock block;
		
		if(cacheController.parentMemory != null){ //if the parent memory is not null
			if(DEBUG_LEVEL >= 3)System.out.println("...Getting From Parent Memory");
			block = cacheController.parentMemory.getBlock(address); //the address of the block was 1024 but when the block was gotten it has addres 16 
			if(DEBUG_LEVEL >= 4)System.out.println("Cache.resolveMiss(" + address + ")\n...Got " + block + ")");
		}else if(cacheController.parentCache != null){
			if(DEBUG_LEVEL >= 3)System.out.println("...Getting From Parent Cache");
			block = cacheController.parentCache.getBlock(address);
			if(DEBUG_LEVEL >= 4)System.out.println("Cache.resolveMiss(" + address + ")\n...Got " + block + ")");
		}else{
			throw new NullPointerException("Parent Memory and Cache is NULL");
		}
		
		if(memory[mAddress] == null){
			if(DEBUG_LEVEL >= 4)System.out.println("...Setting memory[" + mAddress + "] to " + block);
			memory[mAddress] = block;
		}else{
			throw new IllegalStateException("...memory[" + mAddress + "] is not NULL");
		}
	}
	
	private void writeBack(Integer mAddress) throws IllegalAccessException, NullPointerException{
		if(DEBUG_LEVEL >= 1)System.out.println("Cache.writeBack(" + mAddress + ")");
		if(cacheController.parentMemory != null ){ //when we replace a block on the cache with information we replace it back to main memory
			cacheController.parentMemory.putBlock(memory[mAddress].clone());
		}else if(cacheController.parentCache != null){
			cacheController.parentCache.putBlock(memory[mAddress].clone());
		}else{
			throw new NullPointerException("Parent Memory and Cache is NULL");
		}
		cacheController.cacheStats.REPLACEMENT++;	
		memory[mAddress] = null;
	}
}
