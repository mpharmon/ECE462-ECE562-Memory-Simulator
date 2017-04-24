package edu.arizona.ece.memsim.model;

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
	 * Hold the n-th (in Ln) Level of Cache, Only Used For Debuging
	 */
	protected Integer cacheLevel;
	
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
	 * Total Number of Blocks In Memory (Used When Iterating Through Memory Array)
	 */
	protected Integer numBlocks;
	
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
	 * List Holding the {@link MemoryBlock}s Currently in this Cache
	 */
	protected MemoryBlock[] memory;
	
	/**
	 * Queue Holding the Memory Address of the Least Recently Used (LRU) {@link MemoryBlock}
	 */
	protected LoadQueue[] loadQueue;
	
	/**
	 * Statistics for the Level of Cache
	 */
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
	//@SuppressWarnings("unchecked")
	public Cache(Integer tSize, Integer bSize, Integer assoc, CacheController cc, Integer level) throws InterruptedException{
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache(" + tSize + ", " + bSize + ", " + assoc +", CacheController, " + level + ")");
		
		if(tSize == null)throw new NullPointerException("tSize Can Not Be Null");
		if(tSize < 1)throw new IllegalArgumentException("tSize Must Be Greater Than One");
		
		if(bSize == null)throw new NullPointerException("bSize Can Not Be Null");
		if(bSize < 1)throw new IllegalArgumentException("bSize Must Be Greater Than One");
		
		if(assoc == null)throw new NullPointerException("bSize Can Not Be Null");
		if(assoc < 0)throw new IllegalArgumentException("bSize Must Be Positive");
		
		cacheLevel = level;
		cacheController = cc;
		totalSize = tSize;
		blockSize = bSize;
		numBlocks = totalSize / blockSize;
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
			
			loadQueue = new LoadQueue[associativity];
			
			Integer mAddress = new Integer(0);
			
			for(int i = 0; i < associativity; i++){
				loadQueue[i] = new LoadQueue(wayRows);
				for(int j = 0; j < wayRows; j++){
					loadQueue[i].put(new Integer(mAddress));
					mAddress++;
				}
			}
		};
		
		// Create Storage Array 
		if(DEBUG_LEVEL >= 4)System.out.println("L"+ cacheLevel + "-Cache()...Creating memory[" + numBlocks + "]");
		memory = new MemoryBlock[numBlocks];
		
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache()...Finished");
	}
	
	/**
	 * Returns the MemoryElement given by eAddress,
	 * 
	 * @param eAddress Element Address 
	 * @return The MemoryElement Requested
	 * @throws Exception 
	 * @throws IndexOutOfBoundsException When eAddress is Less Than Zero
	 */
	public MemoryElement get(Boolean trackStats, Integer eAddress) throws Exception{
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.get(" + eAddress + ")");
		
		// Validation
		if(eAddress == null)throw new NullPointerException("eAddress Can Not Be NULL");
		if(eAddress < 0)throw new IndexOutOfBoundsException("eAddress Must Be Positive (Given " + eAddress);
		
		// Reminder: Integer Division
		Integer bAddress = (eAddress / blockSize) * blockSize;
		Integer offset = eAddress - bAddress;
		
		if(DEBUG_LEVEL >= 4)System.out.println("L" + cacheLevel + "-Cache.get()...bAddress: " + bAddress + " offset:" + offset);
		
		Integer mAddress = findBlockInMemory(bAddress);
		
		if(mAddress != null){
			if(memory[mAddress].getBlockAddress().equals(bAddress)){
				if(DEBUG_LEVEL >= 3)System.out.println("L"+ cacheLevel + "-Cache.get()...Cache Hit");
				
				if(associativity > 0)loadQueue[getSet(bAddress)].roll(mAddress);
				
				if(trackStats)cacheController.cacheStats.READ_HIT++;
				
				if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.get()...Finished");
				
				return memory[mAddress].getElement(offset);
			}
		}
		
		// If We Get Here We have a Read Miss
		if(DEBUG_LEVEL >= 3)System.out.println("L"+ cacheLevel + "-Cache.get()...Cache Miss");
		if(trackStats)cacheController.cacheStats.READ_MISS++;
		
		// Get Next Write Location For Missed Block
		Integer wAddress = getNextWriteLocationLRU(bAddress);
		
		//Write Back If Necessary
		if(memory[wAddress] != null)writeBack(trackStats, wAddress);
		
		//Resolve Miss
		resolveMiss(trackStats, wAddress, bAddress);
		
		//Return Value
		MemoryElement mElement = memory[wAddress].getElement(offset).clone();
		
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.get()...Finished");
		
		return mElement;
	}
	
	/**
	 * Pulls a Block of Memory from this cache and prepares it for insertion into another level of cache
	 * 
	 * @param bAddress Block Address of Cache to Pull
	 * @param size Number of Memory Elements
	 * @return 
	 * @throws Exception
	 */
	public MemoryBlock getBlock(Boolean trackStats, Integer bAddress, Integer size) throws Exception{
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.getBlock(" + bAddress + ", " + size + ")");
		
		// Validation
		if(bAddress == null)throw new NullPointerException("address Can Not Be NULL");
		if(bAddress < 0)throw new IndexOutOfBoundsException("address Must Be Positive (Given: " + bAddress);
		
		if(size == null)throw new NullPointerException("size Can Not Be NULL");
		if(size < 0)throw new IndexOutOfBoundsException("size Must Be Positive (Given: " + size);
		if(size > blockSize)throw new IllegalArgumentException("size (" + size + ") Must Be Less Than blockSize (" + blockSize +")");
		
		Integer localStartBlockAddress = (bAddress / blockSize) * blockSize;
		Integer localEndBlockAddress = ((bAddress + size - 1) / blockSize) * blockSize;
		
		if(DEBUG_LEVEL >= 4)System.out.println("L" + cacheLevel + "-Cache.getBlock()...localStartBlockAddress: " + localStartBlockAddress + " localEndBlockAddress: " + localEndBlockAddress);
		
		Integer localStartMemoryAddress = findBlockInMemory(localStartBlockAddress);
		Integer localEndMemoryAddress = findBlockInMemory(localEndBlockAddress);
		
		if(DEBUG_LEVEL >= 3)System.out.println("L"+ cacheLevel + "-Cache.getBlock()...Checking For Start and End Block Miss");
		
		if(localStartMemoryAddress != null && localEndMemoryAddress != null){// Found Both Start and End Blocks
			if(DEBUG_LEVEL >= 3)System.out.println("L"+ cacheLevel + "-Cache.getBlock()...Start and End Block Hit");

			//TODO: Correct Exception Thrown
			if(localEndMemoryAddress - localStartMemoryAddress > 1 || localEndMemoryAddress - localStartMemoryAddress < 0)throw new Exception("start and end are not in adjcent MemoryBlocks");
			
			if(trackStats)cacheController.cacheStats.BLOCKREAD_HIT++;
		}else{// Did Not Find Start and/or End Blocks
			if(DEBUG_LEVEL >= 3)System.out.println("L"+ cacheLevel + "-Cache.getBlock()...Start and/or End Block Miss");
			
			// Count as a MISS if one or both Blocks are not in memory
			if(trackStats)cacheController.cacheStats.BLOCKREAD_MISS++;
			
			if(localStartMemoryAddress == null){// Resolve Start Block Miss
				if(DEBUG_LEVEL >= 3)System.out.println("L"+ cacheLevel + "-Cache.getBlock()...Resolving Start Block Miss");
				
				// Get Next Write Location For Missed Block
				Integer mAddress = getNextWriteLocationLRU(localStartBlockAddress);
				
				// Write Back
				if(memory[mAddress] != null)writeBack(trackStats, mAddress);
				
				// Resolve Miss
				resolveMiss(trackStats, mAddress, localStartBlockAddress);
				
				// Get Next Write Location For Missed Block and Verify it is in Memory
				localStartMemoryAddress = findBlockInMemory(localStartBlockAddress);
				if(localStartMemoryAddress == null)throw new IllegalStateException("localStartMemoryAddress Should NEVER Be NULL");
			}
			
			// Only Process this Miss if it is 
			if(localEndMemoryAddress == null && !localStartBlockAddress.equals(localEndBlockAddress)){// Resolve End Block Miss
				if(DEBUG_LEVEL >= 3)System.out.println("L"+ cacheLevel + "-Cache.getBlock()...Resolving End Block Miss");
				
				// Get Next Write Location For Missed Block
				Integer mAddress = getNextWriteLocationLRU(localEndBlockAddress);
				
				// Write Back
				if(memory[mAddress] != null)writeBack(trackStats, mAddress);
				
				// Resolve Miss
				resolveMiss(trackStats, mAddress, localEndBlockAddress);
			}
			
			// Get Next Write Location For Missed Block and Verify it is in Memory
			localEndMemoryAddress = findBlockInMemory(localEndBlockAddress);
			if(localStartMemoryAddress == null)throw new IllegalStateException("localEndMemoryAddress Should NEVER Be NULL");
		}
		
		if(DEBUG_LEVEL >= 4)System.out.println("L" + cacheLevel + "-Cache.getBlock()...localStartMemoryAddress: " + localStartMemoryAddress + " localEndMemoryAddress: " + localEndMemoryAddress);
		
		// Create MemoryBlock to Return
		MemoryBlock newMB = new MemoryBlock(size, bAddress);
		
		if(DEBUG_LEVEL >= 4)System.out.println("L" + cacheLevel + "-Cache.getBlock()...Starting at " + localStartBlockAddress + " going to " + localEndBlockAddress + " (Block Addresses)");
		
		// Load MemoryBlock to be Returned
		for(int i = 0; i < size; i++){
			Integer localElementAddress = localStartBlockAddress + i;
			Integer localElementBlockAddress = (localElementAddress / blockSize) * blockSize;
			Integer localElementBlockOffset = (bAddress + i) % blockSize;
			
			if(DEBUG_LEVEL >= 5)System.out.println("L" + cacheLevel + "-Cache.getBlock()...localElementBlockAddress: " + localElementBlockAddress + " localElementBlockOffset:" + localElementBlockOffset);
			
			loadQueue[getSet(localElementBlockAddress)].roll(findBlockInMemory(localElementBlockAddress));
			
			newMB.setElement(i, memory[findBlockInMemory(localElementBlockAddress)].getElement(localElementBlockOffset));
		}
		
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.getBlock()...Finished");
		
		return newMB;
	}
	
	/**
	 * Puts the Byte given by bite into the local Cache.
	 * 
	 * @param eAddress Memory Location to Place the block
	 * @param bite Byte to be Written
	 * @throws Exception 
	 */
	public void put(Boolean trackStats, Integer eAddress, Byte bite) throws Exception{
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.put(" + eAddress + ", " + bite + ")");
	
		// Validation
		if(eAddress == null)throw new NullPointerException("eAddress Can Not Be NULL");
		if(eAddress < 0)throw new IndexOutOfBoundsException("eAddress Must Positive");
		if(bite == null)throw new NullPointerException("bite Can Not Be NULL");
		
		// Reminder: Integer Division
		Integer bAddress = (eAddress / blockSize) * blockSize;
		Integer offset = eAddress - bAddress;
		
		Integer mAddress = findBlockInMemory(bAddress);
		
		if(mAddress != null){
			if(memory[mAddress].getBlockAddress().equals(bAddress)){
				if(DEBUG_LEVEL >= 3)System.out.println("L"+ cacheLevel + "-Cache.put()...Cache Hit");
				
				if(associativity > 0)loadQueue[getSet(bAddress)].roll(mAddress);
				
				if(trackStats)cacheController.cacheStats.WRITE_HIT++;
				
				memory[mAddress].getElement(offset).setData(bite);
				
				if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.put()...Finished");
				
				return;
			}
		}
		
		// If We Get Here We have a Miss
		if(DEBUG_LEVEL >= 3)System.out.println("L"+ cacheLevel + "-Cache.put()...Cache Miss");
		if(trackStats)cacheController.cacheStats.WRITE_MISS++;
		
		// Get Next Write Location For Missed Block
		Integer wAddress = getNextWriteLocationLRU(bAddress);
		
		// Write Back If Necessary
		if(memory[wAddress] != null)writeBack(trackStats, wAddress);
		
		// Resolve Miss
		resolveMiss(trackStats, wAddress, bAddress);
		
		// Update Value
		memory[wAddress].getElement(offset).setData(bite);
		
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.put()...Finished");
	}
	
	/**
	 * Places a Block of Memory into this level of Cache from a Lower Level of Cache
	 * 
	 * @param block {@link MemoryBlock} to be Written to this Level of Cache
	 * @throws Exception
	 */
	public void putBlock(Boolean trackStats, MemoryBlock block) throws Exception{
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.putBlock([blockAddress]" + block.getBlockAddress() + ")");
		
		// Validation
		if(block == null)throw new NullPointerException("block Can Not Be NULL");
		
		Integer localStartBlockAddress = (block.getBlockAddress() / blockSize) * blockSize;
		Integer localEndBlockAddress = ((block.getBlockAddress() + block.getSize() - 1) / blockSize) * blockSize;
		
		if(DEBUG_LEVEL >= 4)System.out.println("L" + cacheLevel + "-Cache.putBlock()...localStartBlockAddress: " + localStartBlockAddress + " localEndBlockAddress: " + localEndBlockAddress);
		
		Integer localStartMemoryAddress = findBlockInMemory(localStartBlockAddress);
		Integer localEndMemoryAddress = findBlockInMemory(localEndBlockAddress);
		
		if(DEBUG_LEVEL >= 3)System.out.println("L"+ cacheLevel + "-Cache.getBlock()...Checking For Start and End Block Miss");
		
		if(localStartMemoryAddress != null && localEndMemoryAddress != null){// Found Both Start and End Blocks
			if(DEBUG_LEVEL >= 3)System.out.println("L"+ cacheLevel + "-Cache.getBlock()...Start and End Block Hit");
			
			//TODO: Correct Exception Thrown
			if(localEndMemoryAddress - localStartMemoryAddress > 1 || localEndMemoryAddress - localStartMemoryAddress < 0)throw new Exception("start and end are not in adjcent MemoryBlocks");
			
			if(trackStats)cacheController.cacheStats.BLOCKWRITE_HIT++;
		}else{// Did not Find Start and/or End Blocks
			if(DEBUG_LEVEL >= 3)System.out.println("L"+ cacheLevel + "-Cache.getBlock()...Start and/or End Block Miss");
			
			// Count as a MISS if one or both Blocks are not in memory
			if(trackStats)cacheController.cacheStats.BLOCKWRITE_MISS++;
			
			if(localStartMemoryAddress == null){// Resolve Start Block Miss
				if(DEBUG_LEVEL >= 4)System.out.println("L"+ cacheLevel + "-Cache.putBlock()...Resolving Start Block Miss");
				
				// Get Next Write Location For Missed Block
				Integer mAddress = getNextWriteLocationLRU(localStartBlockAddress);
				
				// Write Back
				if(memory[mAddress] != null)writeBack(trackStats, mAddress);
				
				// Resolve Miss
				resolveMiss(trackStats, mAddress, localStartBlockAddress);
				
				// Get Next Write Location For Missed Block and Verify it is in Memory
				localStartMemoryAddress = findBlockInMemory(localStartBlockAddress);
				if(localStartMemoryAddress == null)throw new IllegalStateException("localStartMemoryAddress Should NEVER Be NULL");
			}
			
			// If localEndBlockAddress is Equal to localStartblockAddress, That Miss was Resolved Above
			if(localEndBlockAddress == null && !localStartBlockAddress.equals(localEndBlockAddress)){// Resolve End Block Miss
				if(DEBUG_LEVEL >= 4)System.out.println("L"+ cacheLevel + "-Cache.putBlock()...Resolving End Block Miss");
				Integer mAddress = getNextWriteLocationLRU(localEndBlockAddress);
				// Write Back
				if(memory[mAddress] != null)writeBack(trackStats, mAddress);
				// Resolve Miss
				resolveMiss(trackStats, mAddress, localEndBlockAddress);
			}
			
			// Get Next Write Location For Missed Block and Verify it is in Memory
			localEndBlockAddress = findBlockInMemory(localEndBlockAddress);
			if(localEndBlockAddress == null)throw new IllegalStateException("localEndBlockAddress Should NEVER Be NULL");
		}
		
		if(DEBUG_LEVEL >= 5)System.out.println("L"+ cacheLevel + "-Cache.putBlock()...Getting MemoryElementIterator");
		
		// Place Block Information Into This Level of Cache
		MemoryElementIterator mbIterator = block.getIterator();
		while(mbIterator.hasNext()){
			if(DEBUG_LEVEL >= 5)System.out.println("L"+ cacheLevel + "-Cache.putBlock()...Writing memoryElement to Cache");
			
			MemoryElement nextME = mbIterator.next();
			
			Integer elementBlockAddress = (nextME.getElementAddress() / blockSize) * blockSize;
			Integer elementBlockOffset = nextME.getElementAddress() % blockSize;
			
			if(DEBUG_LEVEL >= 4)System.out.println("L" + cacheLevel + "-Cache.putBlock()...elementBlockAddress: " + elementBlockAddress + " elementBlockOffset: " + elementBlockOffset);
			
			// Find and Validate mAddress
			Integer mAddress = findBlockInMemory(elementBlockAddress);
			if(mAddress == null)throw new NullPointerException("mAddress Can Not Be NULL");
			
			// Adjust Load Queue
			loadQueue[getSet(elementBlockAddress)].roll(mAddress);
			
			// Write Value
			memory[mAddress].setElement(elementBlockOffset, nextME);
		}
		
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.putBlock() Finished");
	}
	
	/**
	 * Gets an Array of the Possible Cache Location (Slots) for the Given Address (Used for n-Way Associative Caches, Responds for all Cache Types)
	 * 
	 * @param address Address in Memory
	 * @return Array of mAddress'es
	 */
	protected final Integer[] getPossibleMemoryAddressArray(Integer address){
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.getPossibleMemoryAddressArray(" + address + ")");
		
		Integer[] returnArray = new Integer[wayRows];
		
		if(associativity == 0){// Direct Mapped Cache
			if(DEBUG_LEVEL >= 3)System.out.print("L"+ cacheLevel + "-Cache.getPossibleMemoryAddressArray()...Direct Mapped Cache, Only 1 Possible Location: ");
			
			returnArray[0] = (address & ((totalSize / blockSize - 1) << offsetShift)) >> offsetShift;
			
			if(DEBUG_LEVEL >= 4)System.out.println(returnArray[0]);
		}else if(associativity == 1){// Fully Associative Cache
			if(DEBUG_LEVEL >= 3)System.out.print("L"+ cacheLevel + "-Cache.getPossibleMemoryAddressArray()...Fully Associatve Cache, " + wayRows + " Possible Locations");
			
			for(int i = 0; i < wayRows; i++)returnArray[i] = i;
		}else{// Set Associative Cache
			Integer set = (address & ((associativity - 1) << offsetShift)) >> offsetShift;
			Integer setSize = totalSize / blockSize / associativity;
			Integer setStart = set * setSize;
			
			if(DEBUG_LEVEL >= 3)System.out.print("L"+ cacheLevel + "-Cache.getPossibleMemoryAddressArray()...Set Associatve Cache, " + setSize + " Possible Locations: ");
			
			for(int i = 0; i < setSize; i++){
				if(DEBUG_LEVEL >= 3)System.out.print(" " + (setStart + i));
				returnArray[i] = setStart + i;
			}
			
			if(DEBUG_LEVEL >= 3)System.out.println("");
		}
		
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.getPossibleMemoryAddressArray()...Finished");
		
		return returnArray;
	}
	
	/**
	 * Gets the Next Write Location Based on LRU Replacement Policy
	 * 
	 * @param address Address (Block or Element) to find the next Memory Address to write
	 * @return
	 */
	protected final Integer getNextWriteLocationLRU(Integer address){
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.getNextWriteLocationLRU(" + address + ")");
		
		Integer nextLocation;
		
		if(associativity == 0){// Direct Mapped Cache
			nextLocation = (address & ((totalSize / blockSize - 1) << offsetShift)) >> offsetShift;
		}else if(associativity == 1){// Fully Associative Cache
			nextLocation = loadQueue[0].getAndRoll();
		}else{// Set Associative Cache
			nextLocation = loadQueue[getSet(address)].getAndRoll();
		}
		
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.getNextWriteLocationLRU()...Finished...Returning: " + nextLocation);
		
		return nextLocation;
	}
	
	/**
	 * Resolves a Cache Miss at Block Address bAddress and places it at mAddress
	 * 
	 * @param mAddress Memory Address (index) to place the new block
	 * @param bAddress Block Address to place in the mAddress
	 * @throws Exception
	 */
	protected final void resolveMiss(Boolean trackStats, Integer mAddress, Integer bAddress) throws Exception{
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.resolveMiss(" + mAddress + ", " + bAddress + ")");
		
		MemoryBlock block;
		
		if(cacheController.parentMemory != null){
			if(DEBUG_LEVEL >= 3)System.out.println("L"+ cacheLevel + "-Cache.resolveMiss()...Getting From Parent Memory");
			block = cacheController.parentMemory.getBlock(trackStats, bAddress, blockSize);
		}else if(cacheController.parentCache != null){
			if(DEBUG_LEVEL >= 3)System.out.println("L"+ cacheLevel + "-Cache.resolveMiss()...Getting From Parent Cache");
			block = cacheController.parentCache.getBlock(trackStats, bAddress, blockSize);
		}else{
			throw new NullPointerException("Parent Memory and Cache is NULL");
		}
		 
		if(memory[mAddress] == null){
			memory[mAddress] = block;
		}else{
			throw new IllegalStateException("L"+ cacheLevel + "-Cache.resolveMiss()...memory[" + mAddress + "] is not NULL");
		}
		
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.resolveMiss()...Finished");
	}
	
	/**
	 * Writes back the {@link MemoryBlock} at mAddress to the Next Higher Level of Cache/Memory
	 * 
	 * @param mAddress Cache Location (Slot) to be Written Back
	 * @throws Exception
	 */
	protected final void writeBack(Boolean trackStats, Integer mAddress) throws Exception{
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.writeBack(" + mAddress + ")");
		
		// Determine Where to Write Back the Block at mAddress
		if(cacheController.parentMemory != null){
			cacheController.parentMemory.putBlock(trackStats, memory[mAddress]);
		}else if(cacheController.parentCache != null){
			cacheController.parentCache.putBlock(trackStats, memory[mAddress]);
		}else{
			throw new NullPointerException("Parent Memory and Cache is NULL");
		}
		
		cacheController.cacheStats.REPLACEMENT++;	
		
		// Null the Memory Block to Provide Indicator that Block is Available For Reuse
		memory[mAddress] = null;
		
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.writeBack()...Finished");
	}
	
	/**
	 * Finds the location (slot) in Cache given by bAddress
	 * 
	 * @param bAddress Block Address for the 
	 * @return Location/Slot in Cache if found, NULL otherwise
	 */
	protected final Integer findBlockInMemory(Integer bAddress){
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.findBlockInMemory(" + bAddress + ")");
		
		for(Integer index : getPossibleMemoryAddressArray(bAddress)){
			if(memory[index] != null){
				if(memory[index].getBlockAddress().equals(bAddress)){
					
					if(DEBUG_LEVEL >= 3)System.out.println("L"+ cacheLevel + "-Cache.findBlockInMemory()...Hit at " + index);
					
					if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.findBlockInMemory()...Finished");
					
					return index;
				}
			}
		}

		if(DEBUG_LEVEL >= 3)System.out.println("L"+ cacheLevel + "-Cache.findBlockInMemory()...Miss");
		
		if(DEBUG_LEVEL >= 1)System.out.println("L"+ cacheLevel + "-Cache.findBlockInMemory()...Finished");
		
		// Element is Not In Memory
		return null;
	}
	
	/**
	 * Finds the Set in Memory that Contains address
	 * 
	 * @param address Address (Block or Element)
	 * @return Integer of the Set
	 */
	protected final Integer getSet(Integer address){
		if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + "-Cache.getSet(" + address + ")");
		
		// Validation
		if(address == null)throw new NullPointerException("bAddress Can Not Be NULL");
		if(address < 0)throw new IllegalArgumentException("bAddress Must Be Positive");
		
		if(associativity == 0){
			if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + "-Cache.getSet(" + address + ")...Finished");
			return null;
		}else if(associativity == 1){
			if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + "-Cache.getSet(" + address + ")...Finished");
			return 0;
		}else{
			if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + "-Cache.getSet(" + address + ")...Finished");
			return (address & ((associativity - 1) << offsetShift)) >> offsetShift;
		}
	}
	
	public Integer getBlockSize(){return blockSize;}
	
}
