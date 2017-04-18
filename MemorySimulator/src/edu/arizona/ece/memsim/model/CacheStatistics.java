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
	 * Counts Cache Read Hits
	 */
	public Integer READ_HIT;
	
	/**
	 * Counts Cache Read Misses
	 */
	public Integer READ_MISS;
	
	/**
	 * Counts Cache Block Read Misses
	 */
	public Integer BLOCKREAD_HIT;
	
	/**
	 * Counts Cache Block Read Misses
	 */
	public Integer BLOCKREAD_MISS;
	
	/**
	 * Counts Cache Block Write Hits
	 */
	public Integer BLOCKWRITE_HIT;
	
	/**
	 * Counts Cache Block Write Misses
	 */
	public Integer BLOCKWRITE_MISS;
	
	/**
	 * Counts Cache Write Misses
	 */
	public Integer WRITE_MISS;

	/**
	 * Counts Cache Write Hits
	 */
	public Integer WRITE_HIT;
	
	/**
	 * Counts Cache Element Reads
	 */
	//public Integer READ;
	
	/**
	 * Counts Cache Block Reads
	 */
	//public Integer BLOCKREAD;
	
	/**
	 * Counts Cache Element Writes
	 */
	//public Integer WRITE;
	
	/**
	 * Counts Cache Block Writes
	 */
	//public Integer BLOCKWRITE;
	
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
	/*
	 * Keeps Track of The Hit-Miss Ratio for number of cache Acceses 
	 * 
	 * */
	
	public Double HitRatio;
	
	public CacheStatistics(){
		ACCESS = new Integer(0);
		READ_HIT = new Integer(0);
		READ_MISS = new Integer(0);
		WRITE_HIT = new Integer(0);
		WRITE_MISS = new Integer(0);
		BLOCKREAD_HIT = new Integer(0);
		BLOCKREAD_MISS = new Integer(0);
		BLOCKWRITE_HIT = new Integer(0);
		BLOCKWRITE_MISS = new Integer(0);
		REPLACEMENT = new Integer(0);
		WRITEBACK = new Integer(0);
		INVALIDATE = new Integer(0);
		HitRatio = new Double(0);
	}
	
}
