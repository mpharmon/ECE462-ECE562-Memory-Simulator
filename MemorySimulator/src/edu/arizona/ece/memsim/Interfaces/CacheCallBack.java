package edu.arizona.ece.memsim.Interfaces;

import edu.arizona.ece.memsim.model.CacheController;

/**
 * @author Mike Harmon
 *
 */
public interface CacheCallBack {
	
	/**
	 * Handle Registering of a Child Caches on a Parent Cache or Memory
	 * 
	 * @param newChild CacheController to be Registered
	 * @return True of Cache was Registered, false Otherwise
	 */
	public boolean registerChildCache(CacheController newChild);
	
	/**
	 * Handle Unregistering of a Child Caches on a Parent Cache or Memory
	 * 
	 * @param oldChild CacheController to be Unregistered
	 * @return True if Cache was Unregistered, false Otherwise
	 */
	public boolean unRegisterChildCache(CacheController oldChild);
}
