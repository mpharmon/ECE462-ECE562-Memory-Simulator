package edu.arizona.ece.memsim.model;

import java.util.concurrent.ArrayBlockingQueue;

public class LoadQueue extends ArrayBlockingQueue<Integer> {

	/**
	 * Required by Serializable Interface, Not Used
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 * 
	 * @param capacity Fixed Size of The Queue
	 */
	public LoadQueue(int capacity) {
		super(capacity);
	}
	
	/**
	 * Returns the Value at the Top of the Queue and Adds it to the Back of the Queue
	 * 
	 * @return Integer of the Top Value of the Queue
	 */
	public Integer getAndRoll(){
		Integer get = this.poll();
		this.add(get);
		return get;
	}
	
	/**
	 * Moves a Specific Element and Places it at the Back of the Queue
	 * 
	 * @param spot The Index to Place at the End of the Queue
	 * @throws Exception
	 */
	public void roll(Integer spot) throws Exception{
		// Validation
		if(spot == null)throw new NullPointerException("spot Can Not Be NULL");
		if(spot < 0)throw new IllegalArgumentException("spot Must Be Positive");
		
		if(this.peek().equals(spot)){
			// If the spot to be moved is at the top of the queue, roll it to the bottom of the list and stop (this is an efficiency enhancement)
			this.add(this.poll());
		}else{
			if(!this.remove(spot))throw new Exception("Element (spot: " + spot + ") Not Found");
			
			this.add(spot);
		}
	}

}