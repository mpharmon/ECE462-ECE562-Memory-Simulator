package edu.arizona.ece.memsim.implementations.CP;

import edu.arizona.ece.memsim.model.CacheController;
import edu.arizona.ece.memsim.model.Memory;
import edu.arizona.ece.memsim.model.MemoryBlock;
import edu.arizona.ece.memsim.model.MemoryResult;

import java.util.Random;  // for the random function 


public class ProgramPatternController extends CacheController {
	
	private String State; // this keeps track of the pattern state whatever pattern is following 
	
	public Double HitsRatio ; //Keeps track of the  previous 100 hits to find the correlation  

	public Integer TotalMemSize;
	
	public ProgramPatternController(Integer level,Integer MemSize ,Integer tSize, Integer bSize, Integer assoc, Integer aTime,
			CacheController pCache) throws InterruptedException {
		super(level, tSize, bSize, assoc, aTime, pCache);
		State = null;
		TotalMemSize = MemSize;
	}
	
	public ProgramPatternController(Integer level,Integer MemSize, Integer tSize, Integer bSize, Integer assoc, Integer aTime,
			Memory pMemory) throws InterruptedException {
		super(level, tSize, bSize, assoc, aTime, pMemory);

		State = null;
		TotalMemSize = MemSize;

	}
		
	 // calls the object memory result
	public MemoryResult get(Integer eAddress) throws NullPointerException, IllegalArgumentException, IllegalAccessException{
		if(DEBUG_LEVEL >= 1)System.out.println("\nL" + cacheLevel + " CacheController.get(" + eAddress + ")");	
		if(childCaches.size() > 0)throw new IllegalAccessException("Can Not Call put if Child Caches Are Present");
		if(eAddress == null)throw new NullPointerException("NULL Address");
		if(eAddress < 0)throw new IllegalArgumentException("Imposible address");
		MemoryResult returnValue = new MemoryResult();
		returnValue.addMemoryElement(cache.get(eAddress)); // goes to the cache to check if the memory accessed is on the cache,in case is not there it resolves the issue 
		int[] address;
		Integer Temp;
		State = "off";// uncomment this to check values without prefetching 
		if((cacheStats.ACCESS > 1) && (State != "off")){ // Correlation prefetcher starts prefetching when it has enougth data to find a pattern  
			address =  Table(eAddress);
			 for(int j = 0; (j < 10) ; j++){// max of 10 blocks being prefetch this design choice 
				if(address[j] != -1){ //the array is initialize to an impossible number to make sure its not put into the memory 
					Temp = address[j];  // to use the same type of variable that the function ask for 
					cache.put(Temp,(byte)(1)); // puts the next block with  a 1 
					cacheStats.ACCESS++;
				 }
			 }
		}else
		{
			double temp = (cacheStats.READ_HIT-cacheStats.READ_MISS);
			temp = ((temp/cacheStats.ACCESS)*100); // the ratio is total hits minus the misses divided by total access in percent
			cacheStats.HitRatio = temp;
		}
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
	

	public void put(Integer eAddress, Byte bite) throws IllegalAccessException, NullPointerException, IllegalArgumentException {
		if(DEBUG_LEVEL >= 1)System.out.println("\nL" + cacheLevel + " CacheController.put(" + eAddress + ", " + bite +")");
		//Prevent Element Access if ChildCache(s) is/are Present
		if(childCaches.size() > 0)throw new IllegalAccessException("Can Not Call put if Child Caches Are Present");		
		// Validity Checks
		if(eAddress == null)throw new NullPointerException("address Can Not Be Null");
		if(eAddress < 0)throw new IllegalArgumentException("address Must Be Zero or Greater");		
		if(bite == null)throw new NullPointerException("var Can Not Be Null");
		cache.put(eAddress,bite); // puts the next block
		cacheStats.ACCESS++;
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
	public void SetMemSize(Integer Size){ // setter for the total Memsize
		TotalMemSize = Size;
		return;
	}
	public int[] Table(int eAddress) throws IllegalAccessException{
		int address[] = new int[10];
		for(int i=0;i < 10 ;i++){
			address[i] = -1;
		}
		double temp = (cacheStats.READ_HIT-cacheStats.READ_MISS);
		temp = ((temp/cacheStats.ACCESS)*100); // the ratio is total hits minus the misses divided by total access in percent
		cacheStats.HitRatio = temp;
		// 1% sample for access pattern determination 
		if(cacheStats.ACCESS < TotalMemSize*(0.01) ){ // Cooling state for the algorithm first checks the outcome of the prefetcher 
			 address = Strided(eAddress,address); // strided is always the first option thinking that the compiler optimizations sets memory for prefetching 
			 String Stemp = "Initial";
			 State = Stemp;
			 HitsRatio = temp ; 
		}
		else if( (HitsRatio >= 90) && ( (State == "Initial")||(State == "Strided") )  ){
			address = Strided(eAddress,address); // we might be getting a hits because sometime we prefetch next block 
			String Stemp = "Strided";
			State = Stemp;
			HitsRatio = temp ;
		}
		else if( ((HitsRatio  >= 75)&&( HitsRatio < 90)) && (State == "Initial" ||State == "Strided" || State == "Sequential") ){ // if the ratio is positive and greater than the threshold sequential is working 
			 address =  Sequential(eAddress,address);
			 String Stemp = "Sequential";
			 State = Stemp;
			 HitsRatio = temp ;
		}
		else if( (HitsRatio  >= 20)&&(HitsRatio < 75)  ){ // if we get hits from sequential but not as much this migth be an Scatter memory pattern
			 address =  Scatter(eAddress,address);
			 String Stemp = "Scatter";
			 State = Stemp;
			 HitsRatio = temp; // if we do get improvement form Scatter we dont want the logic of on top to overwrite the Scatter access pattern 
		}
		else if( (HitsRatio >= 0)&&(HitsRatio < 20 ) ){
			address = Random(address); // if prefetching is not working random access pattern might be usefull in a small memory but if memory is too big, The state will surely end onf the off state 
			String Stemp = "Random";
			State = Stemp;
			HitsRatio = temp;
		}
		else if(HitsRatio <  0){ // Prefetcher is not working just stop using resources 
			String Stemp = "off";
			State = Stemp;
			HitsRatio = temp;
		}
		return address;
	}
	
	public int[] Sequential(int eAddress,int address[]) throws IllegalAccessException{
     	int  j=0;
     	Integer Block = cache.getBlockSize();
     	Integer bAddress = eAddress / Block;
		Integer boffset	 = eAddress % Block; 
     	if(boffset == (Block-2)){
     		Integer size = cache.getCacheController().getParentMem().getMemorySize();
	    	for (int i = (bAddress+1)*Block ; (i < (4*Block+((bAddress+1)*Block)))&&(i < size); i=i+Block){
	    		if(j < 10 && (address[j] == -1)){ // check if there is something in the block
				address[j] = i;  
	    		}
	    		j++;
			}
     	}
		return address;		
	}

	public int[] Strided(int eAddress,int address[])throws IllegalAccessException{ // when you acces memory you get one line as the memory location 
     	int  j=0 ;
     	Integer Block = cache.getBlockSize();
     	Integer boffset	 = eAddress % Block; 
     	if(boffset == (Block-1)){
     	    for (int i = eAddress+1; i < (eAddress+2); i++){
     	    	address [j] = i ;
     	    	j++;
     	    }     
     	}
		return address;
	}
	
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
    	Integer Block = cache.getBlockSize();
     	Integer boffset	 = eAddress % Block; 
     	 if( boffset > (Block-3)){ // if the first index in not empty we are doing scatter access 
     		address[0] = eAddress+Block;
     	 }
		return address;
	}

	public int[] Random(int address[])throws IllegalAccessException{
		int  i = 0 ;
		Random rand = new Random();
		Integer size = cache.getCacheController().getParentMem().getMemorySize();
		for(int j = 0; j < 2; j++){ // Random is  too inconsistent to prefetch, therefore in case of its use we only prefetch two blocks
			i =  rand.nextInt(size);// from zero to size as random value 
			address[j] = i ; 				
		}
		return address;	
	}
}
	

