package edu.arizona.ece.memsim.model;

import java.util.Iterator;

class MemoryElementIterator implements Iterator<MemoryElement>{

	private MemoryBlock memoryBlock;
	
	private Integer blockSize;
	
	private Integer currentElement;
	
	protected MemoryElementIterator(MemoryBlock memoryBlock){
		this.memoryBlock = memoryBlock;
		currentElement = new Integer(0);
		blockSize = new Integer(memoryBlock.getSize());
	}

	@Override
	public boolean hasNext() {
		if(currentElement < blockSize)return true;
		return false;
	}

	@Override
	public MemoryElement next() {
		currentElement++;
		return memoryBlock.getElement(currentElement - 1);
	}
	
}
