package edu.arizona.ece.memsim.model;

/**
 * Wrapper for a Result From a Memory Request
 * 
 * @author Mike Harmon
 *
 */
public class MemoryResult {
	
	/**
	 * {@link MemoryElement} Requested
	 */
	private MemoryElement memoryElement;
	
	/**
	 * Total Access Time for this Request
	 */
	private Integer accessTime;
	
	/**
	 * Default Constructor
	 */
	public MemoryResult(){}
	
	/**
	 * Constructor Setting mElement and Initial Value of Access Time
	 * 
	 * @param mElement MemoryElement 
	 * @param aTime Total Access Time for Request
	 */
	public MemoryResult(MemoryElement mElement, Integer aTime){
		memoryElement = mElement;
		accessTime = aTime;
	}
	
	/**
	 * Adds a MemoryElement to this MemoryResult
	 * 
	 * @param mElement MemoryElement to be Added
	 * @throws NullPointerException When mElement is NULL
	 * @throws IllegalStateException When memoryElement Already Has A Value
	 */
	public void addMemoryElement(MemoryElement mElement){
		if(mElement == null)throw new NullPointerException("mElement Can Not Be NULL");
		if(memoryElement != null)throw new IllegalStateException("memoryElement Already Has a Value");
		memoryElement = mElement;
	}
	
	/**
	 * Get'er for the {@link MemoryElement}
	 * 
	 * @return Existing Value of data
	 */
	public MemoryElement getMemoryElement(){
		return memoryElement;
	}
	
	/**
	 * Incrementer for the Access Time
	 * 
	 * @param time Integer of Time to Be Added to accessTime
	 */
	public void addAccessTime(Integer time){
		accessTime += time;
	}
}
