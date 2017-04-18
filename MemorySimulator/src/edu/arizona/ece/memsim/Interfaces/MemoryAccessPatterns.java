package edu.arizona.ece.memsim.Interfaces;

public interface MemoryAccessPatterns {
	
	/**
	 * Access Memory Sequentially
	 * @throws Exception When Something Screws Up
	 */
	public void SequentialAccess() throws Exception;
	
	/**
	 * Access Memory Randomly
	 * @throws Exception When Something Screws Up
	 */
	public void RandomAccess() throws Exception;
	
	/**
	 * Access Sequential Memory Elements in Random Memory Blocks
	 * @throws Exception When Something Screws Up
	 */
	public void StrideAccess() throws Exception;
}
