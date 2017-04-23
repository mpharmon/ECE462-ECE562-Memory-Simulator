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
	}
	
	public void print(String prefix){
		System.out.println(prefix + ".ACCESSES\t\t" + ACCESS);
		System.out.println(prefix + ".READ_TOTAL\t\t" + (READ_HIT + READ_MISS));
		System.out.println(prefix + ".READ_HITS\t\t" + READ_HIT);
		System.out.println(prefix + ".READ_MISS\t\t" + READ_MISS);
		System.out.println(prefix + ".BLOCKREAD_TOTAL\t" + (BLOCKREAD_HIT + BLOCKREAD_MISS));
		System.out.println(prefix + ".BLOCKREAD_HITS\t" + BLOCKREAD_HIT);
		System.out.println(prefix + ".BLOCKREAD_MISSES\t" + BLOCKREAD_MISS);
		System.out.println(prefix + ".WRITE_TOTAL\t\t" + (WRITE_HIT + WRITE_MISS));
		System.out.println(prefix + ".WRITE_HIT\t\t" + WRITE_HIT);
		System.out.println(prefix + ".WRITE_MISS\t\t" + WRITE_MISS);
		System.out.println(prefix + ".BLOCKWRITE_TOTAL\t" + (BLOCKWRITE_HIT + BLOCKWRITE_MISS));
		System.out.println(prefix + ".BLOCKWRITE_HITS\t" + BLOCKWRITE_HIT);
		System.out.println(prefix + ".BLOCKWRITE_MISSES\t" + BLOCKWRITE_MISS);
		System.out.println(prefix + ".REPLACEMENTS\t\t" + REPLACEMENT);
		System.out.println(prefix + ".INVALIDATES\t\t" + INVALIDATE);
	}
	
}
