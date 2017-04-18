/**
 * 
 */
package edu.arizona.ece.memsim.model;

/**
 * Models the Elements of a {@link MemoryBlock}
 * 
 * @author Mike Harmon
 * 
 */
public class MemoryElement {
	
	protected Byte data;
	protected Integer eAddress;
	
	/**
	 * Constructor
	 * 
	 * @param address Address of the MemoryElement Being Created
	 */
	public MemoryElement(Integer address){
		eAddress = address;
	}
	
	/**
	 * Constructor
	 * 
	 * @param address Address of the MemoryElement Being Created
	 * @param bite Initial Value of data
	 */
	public MemoryElement(Integer address, Byte bite){
		this(address);
		data = bite;
	}
	
	/**
	 * Get'er for the Data
	 * 
	 * @return Current Value of data
	 */
	public Byte getData(){
		return data;
	}
	
	/**
	 * Set'er for the Data
	 * 
	 * @param bite Byte to be Written to data
	 */
	public void setData(Byte bite){
		data = bite;
	}
	
	public Integer getElementAddress(){
		return eAddress;
	}
	
	@Override
	public MemoryElement clone(){
		return new MemoryElement(this.eAddress, this.data);
	}
}
