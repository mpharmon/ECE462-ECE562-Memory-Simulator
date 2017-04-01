package edu.arizona.ece.memsim.model;

import java.lang.Integer;

import edu.arizona.ece.memsim.enums.MemoryState;

/**
 * Models a Block of Memory That Is Transfered Between (and Stored In) Levels of Cache and Memory 
 * 
 * @author Mike Harmon (mpharmon@email.arizona.edu)
 * 
 */
public class MemoryBlock implements Cloneable{
	
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
	 * Array of Elements in the MemoryBlock
	 */
	private MemoryElement[] memory;
	
	/**
	 * Block Size of the MemoryBlock
	 */
	private Integer blockSize;
	
	/**
	 * Block Address of the MemoryBlock
	 */
	private Integer blockAddress;
	
	/**
	 * State of the MemoryBlock
	 */
	private MemoryState state;
	
	/**
	 * Standard Constructor for MemoryBlock
	 * 
	 * @param size Number of MemoryElement's in the Block
	 * @param address Address of the MemoryBlock
	 * @throws NullPointerException If size or address is NULL
	 * @throws IllegalArgumentException If Size is Less Than 1 or If Address is Less Than Zero
	 */
	public MemoryBlock(Integer size, Integer address){
		if(DEBUG_LEVEL >= 1)System.out.println("Cache(" + size + ", " + address + ")");
		
		if(size == null)throw new NullPointerException("size Can Not Be Null");
		if(size < 1)throw new IllegalArgumentException("size Must Be Greater Than Zero");
		
		if(address == null)throw new NullPointerException("number Can Not Be Null");
		if(address < 0)throw new IllegalArgumentException("number Must Be Positive or Zero");
		
		blockSize = size;
		memory = new MemoryElement[size];
		blockAddress = address;
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Finished");
	}
	
	/**
	 * Get'er for the Address of the MemoryBlock
	 * 
	 * @return Integer Address of the MemoryBlock
	 */
	public Integer getBlockAddress(){
		if(DEBUG_LEVEL >= 4)System.out.println("Memory.getBlockAddress()");
		
		if(DEBUG_LEVEL >= 5)System.out.println("...Returning " + blockAddress);
		
		return blockAddress;
	}
	
	/**
	 * Get'er for the {@link MemoryElement} at offfset
	 * 
	 * @param offset Offset of the Element Requested
	 * @return MemoryElement At address
	 */
	public MemoryElement getElement(Integer offset){
		if(DEBUG_LEVEL >= 4)System.out.println("Memory.getElement(" + offset + ")");
		
		if(offset == null)throw new NullPointerException("address Can Not Be Null");
		
		if(offset < blockSize){
			if(DEBUG_LEVEL >= 5)System.out.println("...Returning " + memory[offset]);
			return memory[offset];
		}else{
			throw new IndexOutOfBoundsException("address Out of Bounds; Given: " + offset);
		}
	}
	
	/**
	 * Set'er to Place {@link MemoryElement} at address
	 * 
	 * @param address Address of the MemoryElement to be Written
	 * @param memoryElement MemoryElement to be Written
	 * @return TRUE if MemoryElement was set, otherwise FALSE
	 */
	public boolean setElement(Integer address, MemoryElement memoryElement){
		if(DEBUG_LEVEL >= 4)System.out.println("Memory.setElement(" + address + ", " + memoryElement +")");
		
		if(address < blockSize){
			memory[address] = memoryElement;
			if(DEBUG_LEVEL >= 5)System.out.println("...Returning True");
			return true;
		}else{
			throw new IndexOutOfBoundsException("address Out of Bounds");
		}
	}
	
	/**
	 * Sets the Memory State to "Modified"
	 */
	public void makeModified(){
		state = MemoryState.MODIFIED;
	}
	
	/**
	 * Sets the Memory State to "Exclusive"
	 */
	public void makeExclusive(){
		state = MemoryState.EXCLUSIVE;
	}
	
	/**
	 * Sets the Memory State to "Invalid"
	 */
	public void makeInvalid(){
		state = MemoryState.INVALID;
	}
	
	/**
	 * Sets the Memory State to Shared
	 */
	public void makeShared(){
		state = MemoryState.SHARED;
	}
	
	/**
	 * Get'er for the State of the Memory
	 * 
	 * @return MemoryState
	 */
	public MemoryState getMemoryState(){
		return state;
	}
	
	public MemoryBlock clone(){
		if(DEBUG_LEVEL >= 1)System.out.println("Memory.clone()");
		
		MemoryBlock newBlock = new MemoryBlock(blockSize, blockAddress);
		
		MemoryElement[] newMemory = new MemoryElement[blockSize];
		
		int i=0;
		for(MemoryElement element : memory){
			if(element != null)newMemory[i] = element.clone();
			i++;
		}
		
		newBlock.memory = newMemory;
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Returning " + newBlock);
		
		return newBlock;
	}

	public MemoryElementIterator getIterator(){
		return new MemoryElementIterator(this);
	}
	
	public Integer getSize(){
		return blockSize;
	}
}