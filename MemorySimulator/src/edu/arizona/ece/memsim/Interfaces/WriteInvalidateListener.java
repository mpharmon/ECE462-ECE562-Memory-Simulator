package edu.arizona.ece.memsim.Interfaces;

/**
 * @author Mike Harmon
 *
 */
public interface WriteInvalidateListener {
	
	/**
	 * What to do on a Cache Write Miss
	 * 
	 * @param address Memory Address of Write Miss'ed Block
	 */
	public void onWriteMiss(Integer address);
	
	/**
	 * What to do on a Cache Write Update
	 * 
	 * @param address Memory Address of Write Update'ed Block
	 */
	public void onWriteUpdate(Integer address);
	
	/**
	 * What to do on a Cache Read Miss
	 * 
	 * @param address Memory Address of Read Miss'ed Block
	 */
	public void onReadMiss(Integer address);
}
