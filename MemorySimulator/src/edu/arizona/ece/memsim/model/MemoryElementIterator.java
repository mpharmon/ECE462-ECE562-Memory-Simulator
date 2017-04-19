package edu.arizona.ece.memsim.model;

import java.util.Iterator;

/**
 * Provides an Iterator to Iterate over {@link MemoryElement}'s in a {@link MemoryBlock}
 * 
 * @author Mike Harmon
 *
 */
class MemoryElementIterator implements Iterator<MemoryElement>{

	/**
	 * {@link MemoryBlock} Being Iterated Over
	 */
	private MemoryBlock memoryBlock;
	
	/**
	 * Current Element Being Accessed
	 */
	private Integer currentElement;
	
	/**
	 * @param memoryBlock {@link MemoryBlock} to Be Iterated Over
	 * @return MemoryElementIterator
	 */
	protected MemoryElementIterator(MemoryBlock memoryBlock){
		this.memoryBlock = memoryBlock;
		currentElement = new Integer(0);
	}

	@Override
	public boolean hasNext() {
		if(currentElement < memoryBlock.getSize())return true;
		return false;
	}

	@Override
	public MemoryElement next() {
		currentElement++;
		return memoryBlock.getElement(currentElement - 1);
	}
	
}
