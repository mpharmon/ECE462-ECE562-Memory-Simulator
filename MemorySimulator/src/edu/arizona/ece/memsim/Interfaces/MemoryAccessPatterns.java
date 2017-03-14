package edu.arizona.ece.memsim.Interfaces;

public interface MemoryAccessPatterns {
	
	/**
	 * Access Memory Sequentially
	 * @throws Exception When Something Screws Up
	 */
	public void SequentialAccess() throws Exception;
	/**
	 * Access Memory Randomly
	 */
	public void RandomAccess();
	/**
	 * Access Sequential Memory Elements in Random Memory Blocks
	 */
	public void StrideAccess();
}
