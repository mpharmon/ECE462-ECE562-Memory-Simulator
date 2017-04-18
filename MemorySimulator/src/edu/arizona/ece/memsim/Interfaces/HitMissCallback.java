package edu.arizona.ece.memsim.Interfaces;

public interface HitMissCallback {
	
	/**
	 * Call Back Method Called When a Cache Miss Occurs
	 * 
	 */
	public void onCacheMiss(Integer address);
	
	/**
	 * Call Back Method Called When a Cache Hit Occurs
	 * 
	 */
	public void onCacheHit(Integer address);
}
