package edu.arizona.ece.memsim.model;

/**
 * Tracks Cache Statistics
 * 
 * @author Mike Harmon (mpharmon@email.arizona.edu)
 *
 */
public class CacheStatistics {
	
	/**
	 * Counts Cache Accesses
	 */
	public Integer ACCESS;
	
	/**
	 * Counts Cache Misses
	 */
	public Integer MISS;
	
	/**
	 * Counts Cache Hits
	 */
	public Integer HIT;
	
	/**
	 * Counts Cache Element Reads
	 */
	public Integer READ;
	
	/**
	 * Counts Cache Block Reads
	 */
	public Integer BLOCKREAD;
	
	/**
	 * Counts Cache Element Writes
	 */
	public Integer WRITE;
	
	/**
	 * Counts Cache Block Writes
	 */
	public Integer BLOCKWRITE;
	
	/**
	 * Counts Cache Replacements
	 */
	public Integer REPLACEMENT;
	
	/**
	 * Counts Cache Write-Backs to Higher Level Cache or Memory
	 */
	public Integer WRITEBACK;
	
	/**
	 * Counts Cache Invalidates
	 */
	public Integer INVALIDATE;
	
	public CacheStatistics(){
		ACCESS = new Integer(0);
		MISS = new Integer(0);
		HIT = new Integer(0);
		READ = new Integer(0);
		BLOCKREAD = new Integer(0);
		WRITE = new Integer(0);
		BLOCKWRITE = new Integer(0);
		REPLACEMENT = new Integer(0);
		WRITEBACK = new Integer(0);
		INVALIDATE = new Integer(0);
	}
	
}
