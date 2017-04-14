<<<<<<< HEAD
package edu.arizona.ece.memsim.implementations.CP;

import edu.arizona.ece.memsim.model.CacheController;
import edu.arizona.ece.memsim.model.Memory;
import edu.arizona.ece.memsim.model.MemoryBlock;
import edu.arizona.ece.memsim.model.MemoryResult;

import java.util.Random;  // for the random function 


public class ProgramPatternController extends CacheController {
	
	public ProgramPatternController(Integer level, Integer tSize, Integer bSize, Integer assoc, Integer aTime,
			CacheController pCache) throws InterruptedException {
		super(level, tSize, bSize, assoc, aTime, pCache);
		// TODO Auto-generated constructor stub
	}
	
	public ProgramPatternController(Integer level, Integer tSize, Integer bSize, Integer assoc, Integer aTime,
			Memory pMemory) throws InterruptedException {
		super(level, tSize, bSize, assoc, aTime, pMemory);
		// TODO Auto-generated constructor stub
	}
		
	 // calls the object memory result
	public MemoryResult get(Integer eAddress) throws NullPointerException, IllegalArgumentException, IllegalAccessException{
		if(DEBUG_LEVEL >= 1)System.out.println("\nL" + cacheLevel + " CacheController.get(" + eAddress + ")");	
		if(childCaches.size() > 0)throw new IllegalAccessException("Can Not Call put if Child Caches Are Present");
		if(eAddress == null)throw new NullPointerException("NULL Address");
		if(eAddress < 0)throw new IllegalArgumentException("Imposible address");
		MemoryResult returnValue = new MemoryResult();
		returnValue.addMemoryElement(cache.get(eAddress)); // goes to the cache to check if the memory accessed is on the cache,in case is not there it resolves the issue 
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Returning " + returnValue);		
		cacheStats.ACCESS++;
		return returnValue;
	}
	/**
	 * Gets a MemoryBlock from Cache, and loads it from a Parent Cache or Parent Memory If Necessary
	 * 
	 * @param bAddress Address of the MemoryBlock desired
	 * @return MemoryBlock
	 * @throws NullPointerException When address is NULL
	 * @throws IllegalArgumentException When address is less than zero
	 * @throws IllegalAccessException
	 */
	public MemoryBlock getBlock(Integer bAddress) throws IllegalAccessException, NullPointerException, IllegalArgumentException {
		if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + " CacheController.getBlock(" + bAddress + ")");	
		// Prevent Block Access if Child Cache(s) is/are not Present
		if(childCaches.size() == 0)throw new IllegalAccessException("Can Not Call getBlock if Child Caches Are Not Present"); //Validity Checks
		if(bAddress == null)throw new NullPointerException("address Can Not Be Null");
		if(bAddress < 0)throw new IllegalArgumentException("address Must Be Greater Than Zero");	
		MemoryBlock returnValue = cache.getBlock(bAddress);		
		if(DEBUG_LEVEL >= 2)System.out.println("...Returning " + returnValue);	
		cacheStats.ACCESS++;	
		return returnValue;
	}	
	/**
	 * Puts a Value into Memory Element in This Level of Cache
	 * 
	 * @param eAddress Address of the data being written
	 * @param bite Byte of the data being written
	 * @throws NullPointerException When address or byte is null
	 * @throws IllegalArgumentException When address is less than zero
	 * @throws IllegalAccessException When Child Caches Are Present
	 */
	/*
	 *	public void put(Integer eAddress, Byte bite) throws NullPointerException, IndexOutOfBoundsException, IllegalAccessException{
		if(DEBUG_LEVEL >= 1)System.out.println("Cache.put(" + eAddress + ", " + bite + ")");
		// Validation
		if(eAddress == null)throw new NullPointerException("eAddress Can Not Be NULL");
		if(eAddress < 0)throw new IndexOutOfBoundsException("eAddress Must Positive");
		if(bite == null)throw new NullPointerException("bite Can Not Be NULL");
		
		Integer bAddress = eAddress / blockSize; // the value above just have you eddress+1 , what i think what baddress should do is divide the address 
		Integer offset = eAddress % blockSize; // cache offset is to find the data within the block 
		
		// Cache Hit?
		for(Integer mAddress : getPossibleMemoryAddressArray(eAddress)){
			if(DEBUG_LEVEL >= 3)System.out.print("...Looking in Memory[" + mAddress + "] for bAddress " + bAddress);
			if(memory[mAddress] != null){
				if(memory[mAddress].getBlockAddress().equals(bAddress)){
					if(DEBUG_LEVEL >= 3)System.out.println("...HIT");
					cacheController.cacheStats.WRITE_HIT++;
					// Adjust loadQueue
					memory[mAddress].getElement(offset).setData(bite);
					return;
				}
				if(DEBUG_LEVEL >= 3)System.out.println("...MISS");
			}
			if(DEBUG_LEVEL >= 3)System.out.println("...MISS");
		}
		
		// If We Get Here We have a Miss
		if(DEBUG_LEVEL >= 3)System.out.println("...Cache Miss");
		cacheController.cacheStats.WRITE_MISS++;
		Integer mAddress = getNextWriteLocation(eAddress);
		//Write Back If Necessary
		if(memory[mAddress] != null)writeBack(mAddress);
		//Resolve Miss
		resolveMiss(mAddress, eAddress);
		//Update Value
		memory[mAddress].getElement(offset).setData(bite);
	}
	 * */
	public void put(Integer eAddress, Byte bite) throws IllegalAccessException, NullPointerException, IllegalArgumentException {
		if(DEBUG_LEVEL >= 1)System.out.println("\nL" + cacheLevel + " CacheController.put(" + eAddress + ", " + bite +")");
		//Prevent Element Access if ChildCache(s) is/are Present
		if(childCaches.size() > 0)throw new IllegalAccessException("Can Not Call put if Child Caches Are Present");		
		// Validity Checks
		if(eAddress == null)throw new NullPointerException("address Can Not Be Null");
		if(eAddress < 0)throw new IllegalArgumentException("address Must Be Zero or Greater");		
		if(bite == null)throw new NullPointerException("var Can Not Be Null");
		//cache.put(eAddress,((byte)0)); // puts the next block
		//cache.put(eAddress,bite); // puts the next block

		int[] address;
		address =  Table(eAddress);

		Integer Temp;
		
		//cache.put(eAddress,bite); // puts the next block
		 for(int j = 0; (j < 10) ; j++){
			 //cache.put(eAddress,bite); // puts the next block
			if(address[j] != -1){
				Temp = address[j];  // to use the same type of variable that the function ask for 
				cache.put(Temp,bite); // puts the next block
				cacheStats.ACCESS++;
				//break;
			 }
		}
		
	}
	
	/**
	 * Puts a MemoryBlock Into This Level of Cache
	 * 
	 * @param block MemoryBlock to be written
	 * @throws NullPointerException When block is NULL
	 * @throws IllegalAccessException When Method is Called when childCache(s) are Present
	 */
	public void putBlock(MemoryBlock block) throws IllegalAccessException, NullPointerException{
		if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + " CacheController.putBlock(" + block.getBlockAddress() + ")");		
		// Prevent Block Access if Child Cache(s) is/are not Present
		if(childCaches.size() == 0)throw new IllegalAccessException("Can Not Call getBlock if Child Caches Are Not Present");	
		// Validity Checking
		if(block == null)throw new NullPointerException("block Can Not Be NULL");
		cache.putBlock(block);	
		cacheStats.ACCESS++;
		//cacheStats.BLOCKWRITE++;	
		if(DEBUG_LEVEL >= 2)System.out.println("...Finished");
	}
	
	public int[] Table(int eAddress) throws IllegalAccessException{
		int address[] = new int[10];
		for(int i=0;i < 10 ;i++){
			address[i] = -1;
		}
		
		address =  Sequential(eAddress,address);
		// address = Random(10000); // random access pattern 
		/*
		 *table to check what kind of access pattern 
		 * 
		 * 
		 * */
		
		
         // then call one of the fuctions below 		
		return address;
	}
	
	public int[] Sequential(int eAddress,int address[]) throws IllegalAccessException{
     	int  j=0;
     	Integer line = cache.getBlockSize();
     	Integer size = cache.getAssoc()*cache.getBlockSize();
     	Integer bAddress = eAddress / line;
		Integer offset	 = eAddress % line; 
     	if(offset == (line-2)){
	    	for (int i = (bAddress+1)*line ; (i < 8*line+((bAddress+1)*line))&&(i < 8*size); i=i+line){
	    		if(j < 10){
				address[j] = i;  
				j++;
	    		}
			}
     	}
 
 //    	address[0] = eAddress;
		return address;		
	}
	// row is a how many values a row is 
	public static int[] Strided(int eAddress,int address[],int row)throws IllegalAccessException{
     	int  j=0 ;
     	    for (int i = eAddress+row; i < (eAddress+3*row); i = i + row){
     	    	address [j] = i ;
     	    	j++;
     	    }     
		return address;
	}
/*	
	public static int[] Linear(int eAddress) throws IllegalAccessException{
		int address[] ;
     	int  i = 0;
     	int  j=0 ;
		
		address = new int[6];
		for (i = eAddress ; i < eAddress+6 ; i++ )
		{
			address[j] = i;  
			j++;
		}

		return address;
			
	}
*/	
	/*
	 * Example
	 * A :	1
	 * A :	2
	 * A :	3
	 * A :	4
	 * A :	10
	 * A : 	11
	 * A :	12
	 * A :	50
	 * A :	51
	 * */
	public int[] Scatter(int eAddress,int address[])throws IllegalAccessException{
		address = Sequential(eAddress,address); // keep calling the function with the new eaddress as long as is this kind of pattern
		return address;
	}

	public int[] Random(int size,int address[])throws IllegalAccessException{
		int  i = 0 ;
		Random rand = new Random();
		for(int j = 0; j < 5; j++){
			i =  rand.nextInt(size);// from zero to size as random value 
			address[j] = i ; 				
		}
		return address;	
	}
	
}
=======
package edu.arizona.ece.memsim.implementations.CP;

import edu.arizona.ece.memsim.model.CacheController;
import edu.arizona.ece.memsim.model.Memory;
import edu.arizona.ece.memsim.model.MemoryBlock;
import edu.arizona.ece.memsim.model.MemoryResult;


import java.util.Random;  // for the random function 


public class ProgramPatternController extends CacheController {
	
	public ProgramPatternController(Integer level, Integer tSize, Integer bSize, Integer assoc, Integer aTime,
			CacheController pCache) throws InterruptedException {
		super(level, tSize, bSize, assoc, aTime, pCache);
		// TODO Auto-generated constructor stub
	}
	
	public ProgramPatternController(Integer level, Integer tSize, Integer bSize, Integer assoc, Integer aTime,
			Memory pMemory) throws InterruptedException {
		super(level, tSize, bSize, assoc, aTime, pMemory);
		// TODO Auto-generated constructor stub
	}
	
	public static void main(int args) throws InterruptedException, NullPointerException, IllegalArgumentException, IllegalAccessException{
		//Object memoria = MemoryResult.getMemoryElement(); tienes que cambiar cosas a static 
		//MemoryResult Pendeja  = new MemoryResult(); // eso seria si quisiera hacer call el onject en memory resoult no el instance del que hacemos en esta funcion 
		 
		//ProgramPatternController.(args);
		//get(args);
	}	
	
	
	 // calls the object memory result
	public MemoryResult get(Integer eAddress) throws NullPointerException, IllegalArgumentException, IllegalAccessException{
		if(DEBUG_LEVEL >= 1)System.out.println("\nL" + cacheLevel + " CacheController.get(" + eAddress + ")");	
		if(childCaches.size() > 0)throw new IllegalAccessException("Can Not Call put if Child Caches Are Present");
		if(eAddress == null)throw new NullPointerException("NULL Address");
		if(eAddress < 0)throw new IllegalArgumentException("Imposible address");
		int j = 0 ;
		// Create 
		MemoryResult returnValue = new MemoryResult();
		int[] address;
		address =  Table(eAddress);
		returnValue.addMemoryElement(cache.get(eAddress));
		for(j = 0; (address[j] != 0) ; j++){
			cache.get(address[j]);
		}
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Returning " + returnValue);
		
		cacheStats.ACCESS++;
		//cacheStats.READ++;
		
	
		return returnValue;
	}
	


	/**
	 * Gets a MemoryBlock from Cache, and loads it from a Parent Cache or Parent Memory If Necessary
	 * 
	 * @param bAddress Address of the MemoryBlock desired
	 * @return MemoryBlock
	 * @throws NullPointerException When address is NULL
	 * @throws IllegalArgumentException When address is less than zero
	 * @throws IllegalAccessException
	 */
	public MemoryBlock getBlock(Integer bAddress) throws IllegalAccessException, NullPointerException, IllegalArgumentException {
		if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + " CacheController.getBlock(" + bAddress + ")");
		
		// Prevent Block Access if Child Cache(s) is/are not Present
		if(childCaches.size() == 0)throw new IllegalAccessException("Can Not Call getBlock if Child Caches Are Not Present");
		
		// Validity Checks
		if(bAddress == null)throw new NullPointerException("address Can Not Be Null");
		if(bAddress < 0)throw new IllegalArgumentException("address Must Be Greater Than Zero");
		
		MemoryBlock returnValue = cache.getBlock(bAddress);
		
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Returning " + returnValue);
		
		cacheStats.ACCESS++;
		//cacheStats.BLOCKREAD++;
		
		return returnValue;
	}
	
	/**
	 * Puts a Value into Memory Element in This Level of Cache
	 * 
	 * @param eAddress Address of the data being written
	 * @param bite Byte of the data being written
	 * @throws NullPointerException When address or byte is null
	 * @throws IllegalArgumentException When address is less than zero
	 * @throws IllegalAccessException When Child Caches Are Present
	 */
	public void put(Integer eAddress, Byte bite) throws IllegalAccessException, NullPointerException, IllegalArgumentException {
		if(DEBUG_LEVEL >= 1)System.out.println("\nL" + cacheLevel + " CacheController.put(" + eAddress + ", " + bite +")");
		//Prevent Element Access if ChildCache(s) is/are Present
		if(childCaches.size() > 0)throw new IllegalAccessException("Can Not Call put if Child Caches Are Present");		
		// Validity Checks
		if(eAddress == null)throw new NullPointerException("address Can Not Be Null");
		if(eAddress < 0)throw new IllegalArgumentException("address Must Be Zero or Greater");		
		if(bite == null)throw new NullPointerException("var Can Not Be Null");	
		cache.put(eAddress, bite);		
		cacheStats.ACCESS++;
		//cacheStats.WRITE++;	
		if(DEBUG_LEVEL >= 2)System.out.println("...Finished");
	}
	
	/**
	 * Puts a MemoryBlock Into This Level of Cache
	 * 
	 * @param block MemoryBlock to be written
	 * @throws NullPointerException When block is NULL
	 * @throws IllegalAccessException When Method is Called when childCache(s) are Present
	 */
	public void putBlock(MemoryBlock block) throws IllegalAccessException, NullPointerException{
		if(DEBUG_LEVEL >= 1)System.out.println("L" + cacheLevel + " CacheController.putBlock(" + block.getBlockAddress() + ")");		
		// Prevent Block Access if Child Cache(s) is/are not Present
		if(childCaches.size() == 0)throw new IllegalAccessException("Can Not Call getBlock if Child Caches Are Not Present");	
		// Validity Checking
		if(block == null)throw new NullPointerException("block Can Not Be NULL");
		cache.putBlock(block);	
		cacheStats.ACCESS++;
		//cacheStats.BLOCKWRITE++;	
		if(DEBUG_LEVEL >= 2)System.out.println("...Finished");
	}
	
	
	
	
	public static int[] Table(int eAddress) throws IllegalAccessException{
		int address[] ;
		address = new int[10];
		address =  Sequential(eAddress);
		
		/*
		 *table to check what kuind of acces pattern 
		 * 
		 * 
		 * */
         // then call one of the fuctions below 		
		return address;
	}
	
	
	public static int[] Sequential(int eAddress) throws IllegalAccessException{
		int address[] ;
     	int  i = 0;
     	int  j=0 ;	
		address = new int[10];
		for(i=0;i < 10 ;i++){
			address[i] = 0;
		}
		for (i = eAddress ; i < eAddress+6 ; i++ )
		{
			address[j] = i;  
			j++;
		}
		return address;
		
	}
	// row is a how many values a row is 
	public static int[] Strided(int eAddress,int row)throws IllegalAccessException{
		int address[] ; 
     	int  i = 0;
     	int  j=0 ;
     	address = new int[10];
		for(i=0;i < 11 ;i++){
			address[i] = 0;
		}
     	    for ( i = eAddress+row; i < (eAddress+3*row); i = i + row){
     	    	address [j] = i ;
     	    	j++;
     	    }
     	     
		return address;
	}
/*	
	public static int[] Linear(int eAddress) throws IllegalAccessException{
		int address[] ;
     	int  i = 0;
     	int  j=0 ;
		
		address = new int[6];
		for (i = eAddress ; i < eAddress+6 ; i++ )
		{
			address[j] = i;  
			j++;
		}

		return address;
			
	}
*/	
	
	/*
	 * Example
	 * A :	1
	 * A :	2
	 * A :	3
	 * A :	4
	 * A :	10
	 * A : 	11
	 * A :	12
	 * A :	50
	 * A :	51
	 * */
	public static int[] Scatter(int eAddress)throws IllegalAccessException{
		int address[] ; 
		int i = 0 ;
     	address = new int[10];
		for(i=0;i < 11 ;i++){
			address[i] = 0;
		}
		address = Sequential(eAddress); // keep calling the function with the new eaddress as long as is this kind of pattern
		return address;
		
	}
	/*
	public static void Gather()throws IllegalAccessException{
		
	}
	*/
	public static int[] Random(int size)throws IllegalAccessException{
		int address[] ;
     	int  j=0 ; 	
		int  i = 0 ;

     	address = new int[10];
		for(i=0;i < 11 ;i++){
			address[i] = 0;
		}
		Random rand = new Random();
		for(j = 0; j < 5; j++){
			i =  rand.nextInt(size);// from zero to size as random value 
			address[j] = i ; 				
		}
		return address;	
	}
	



}
>>>>>>> 31af15d37264bbf9c1482d94513f2246d1e8c7a2
