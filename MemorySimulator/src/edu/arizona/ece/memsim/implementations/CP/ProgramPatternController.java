package edu.arizona.ece.memsim.implementations.CP;


import edu.arizona.ece.memsim.model.CacheController;
import edu.arizona.ece.memsim.model.Memory;
import edu.arizona.ece.memsim.model.MemoryResult;

import java.util.Random;  // for the random function 


public class ProgramPatternController extends CacheController {
	
	public static String State; // this keeps track of the pattern state whatever pattern is following 
	public static Double HitsRatio ; //Keeps track of the  previous 100 hits to find the correlation  
	public static Integer TotalMemSize;
	
		
		public ProgramPatternController(Integer level, Integer tSize, Integer bSize, Integer assoc, Integer aTime,
				CacheController pCache, Integer memSize) throws InterruptedException {
			super(level, tSize, bSize, assoc, aTime, pCache);
			State = "Start";
			TotalMemSize = memSize;
		}
		
		public ProgramPatternController(Integer level, Integer tSize, Integer bSize, Integer assoc, Integer aTime,
				Memory pMemory, Integer memSize) throws InterruptedException {
			super(level, tSize, bSize, assoc, aTime, pMemory);
			//State = null;
			State = "Start";
			TotalMemSize = memSize;
			//HitsRatio = 0.0;
		}
	 // calls the object memory result
	public MemoryResult get(Boolean trackStats,Integer eAddress) throws Exception{
		if(DEBUG_LEVEL >= 1)System.out.println("\nL" + cacheLevel + " CacheController.get(" + eAddress + ")");	
		if(childCaches.size() > 0)throw new IllegalAccessException("Can Not Call put if Child Caches Are Present");
		if(eAddress == null)throw new NullPointerException("NULL Address");
		if(eAddress < 0)throw new IllegalArgumentException("Imposible address");
		MemoryResult returnValue = new MemoryResult();
		returnValue.addMemoryElement(cache.get(true, eAddress)); // goes to the cache to check if the memory accessed is on the cache,in case is not there it resolves the issue 
		int[] address;
		Integer Temp;

		//State = "off";// uncomment this to check values without prefetching 
		if((cacheStats.ACCESS > 1) && (State != "off")){ // Correlation prefetcher starts prefetching when it has enougth data to find a pattern  
			address =  Table(eAddress);
			 for(int j = 0; (j < 10) ; j++){// max of 10 blocks being prefetch this design choice 
				if(address[j] != -1){ //the array is initialize to an impossible number to make sure its not put into the memory 
					Temp = address[j];  // to use the same type of variable that the function ask for 
					cache.put(false,Temp,(byte)(1)); // puts the next block with  a 1 
					//cacheStats.ACCESS++;
				 }
			 }
		}
		
		if(DEBUG_LEVEL >= 2)System.out.println("...Returning " + returnValue);		
		if(trackStats)cacheStats.ACCESS++;
		return returnValue;
	}
	
	

	public void put(Boolean trackStats,Integer eAddress, Byte bite) throws Exception {
		if(DEBUG_LEVEL >= 1)System.out.println("\nL" + cacheLevel + " CacheController.put(" + eAddress + ", " + bite +")");
		//Prevent Element Access if ChildCache(s) is/are Present
		if(childCaches.size() > 0)throw new IllegalAccessException("Can Not Call put if Child Caches Are Present");		
		// Validity Checks
		if(eAddress == null)throw new NullPointerException("address Can Not Be Null");
		if(eAddress < 0)throw new IllegalArgumentException("address Must Be Zero or Greater");		
		if(bite == null)throw new NullPointerException("var Can Not Be Null");
		int[] address;
		Integer Temp;

		if((cacheStats.ACCESS > 1) && (State != "off")){ // Correlation prefetcher starts prefetching when it has enougth data to find a pattern  
			address =  Table(eAddress);
			 for(int j = 0; (j < 10) ; j++){// max of 10 blocks being prefetch this design choice 
				if(address[j] != -1){ //the array is initialize to an impossible number to make sure its not put into the memory 
					Temp = address[j];  // to use the same type of variable that the function ask for 
					cache.put(false,Temp,(byte)(1)); // puts the next block with  a 1 
				 }
			 }
		}
		cache.put(trackStats, eAddress, bite); // puts the next block
		if(trackStats)cacheStats.ACCESS++;
	}
	

	public int[] Table(int eAddress) throws IllegalAccessException{
		int address[] = new int[10];
		for(int i=0;i < 10 ;i++){
			address[i] = -1;
		}
		double temp = (cacheStats.READ_HIT+cacheStats.WRITE_HIT);
		temp = ((temp/cacheStats.ACCESS)*100); // the ratio is total hits minus the misses divided by total access in percent
		// 1% sample  
		if(cacheStats.ACCESS < TotalMemSize*(0.01) && (temp!=0) && (State != "Scatter") && (State != "Stream") ){ // Cooling state for the algorithm first checks the outcome of the prefetcher 
			 address = Strided(eAddress,address); // strided is always the first option thinking that the compiler optimizations sets memory for prefetching 
			 String Stemp = "Initial";
			 State = Stemp;
			 HitsRatio = temp ; 
		}
		else if(cacheStats.ACCESS < TotalMemSize*(0.02) && (temp < 5) ){ // Cooling state for the algorithm first checks the outcome of the prefetcher 
		    temp = (cacheStats.READ_HIT+cacheStats.WRITE_HIT);
			temp = (temp/((cacheStats.ACCESS-(cacheStats.ACCESS*0.01))))*100 ;
			 address = Scatter(eAddress,address);
			 String Stemp = "Initial";
			 if(temp > 6){ //this implies that we are increasing the ratio 
				  Stemp = "Scatter";
			 }
			 State = Stemp;
			 HitsRatio = temp ; 
		}
		// if the hit rate is so low it could be that the strided acces was completely out of context we try another sample with stream prefetching 
		else if(cacheStats.ACCESS < TotalMemSize*(0.03) && (temp < 6)&& (State != "Scatter")){ // Cooling state for the algorithm first checks the outcome of the prefetcher 
		    temp = (cacheStats.READ_HIT+cacheStats.WRITE_HIT);
			temp = (temp/((cacheStats.ACCESS-(cacheStats.ACCESS*0.02))))*100 ;
			 address = Stream(eAddress,address);
			 String Stemp = "Initial";
			 if(temp > 3){ //this implies that we are increasing the ratio 
				  Stemp = "Stream";
			 }
			 State = Stemp;
			 HitsRatio = temp ; 
		}
		else if( (HitsRatio >= 85) && ( (State == "Initial")||(State == "Strided") )  ){
			address = Strided(eAddress,address); // we might be getting a hits because sometime we prefetch next block 
			String Stemp = "Strided";
			State = Stemp;
			HitsRatio = temp ;
		}
		else if( ((HitsRatio  >= 50)&&( HitsRatio < 85)) && (State == "Initial" ||State == "Strided" || State == "Sequential") ){ // if the ratio is positive and greater than the threshold sequential is working 
			 address =  Sequential(eAddress,address);
			 String Stemp = "Sequential";
			 State = Stemp;
			 HitsRatio = temp ;
		}
		else if( (HitsRatio  >= 10)&&(HitsRatio < 60)&&(State == "Scatter"|| State == "Initial" ||State == "Strided" || State == "Sequential" ) ){ // if we get hits from sequential but not as much this migth be an Scatter memory pattern
			 address =  Scatter(eAddress,address);
			 String Stemp = "Scatter";
			 State = Stemp;
			 HitsRatio = temp; // if we do get improvement form Scatter we dont want the logic of on top to overwrite the Scatter access pattern 
		}
		else if(State == "Stream" && HitsRatio > 5  ){ // if the ratio is positive and greater than the threshold sequential is working 
			 address =  Stream(eAddress,address);
			 String Stemp = "Stream";
			 State = Stemp;
			 HitsRatio = temp ;
		}

		else if( (HitsRatio >= 5)&&(HitsRatio < 10 ) ){
			address = Random(address); // if prefetching is not working random access pattern might be usefull in a small memory but if memory is too big, The state will surely end onf the off state 
			String Stemp = "Random";
			State = Stemp;
			HitsRatio = temp;
		}
		else if(HitsRatio <  5){ // Prefetcher is not working just stop using resources 
		//	String Stemp = "off";
		//	State = Stemp;
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
     		Integer size = TotalMemSize;//cache.getCacheController().getParentMem().getMemorySize();
	    	for (int i = (bAddress+1)*Block ; (i < (4*Block+((bAddress+1)*Block)))&&(i < size); i=i+Block){
	    		if(j < 10 && (address[j] == -1)){ // check if there is something in the block
				address[j] = i;  
	    		}
	    		j++;
			}
     	}
		return address;		
	}
	public int[] Stream(int eAddress,int address[]) throws IllegalAccessException{
     	int  j=0;
     	Integer Block = cache.getBlockSize();
     	//Integer bAddress = eAddress / Block;
		//Integer boffset	 = eAddress % Block; 
     		Integer size = TotalMemSize;//cache.getCacheController().getParentMem().getMemorySize();
	    	if(eAddress > size+4*Block){
	    		eAddress = eAddress-4*Block;//eAddress+2*Block;
	    	}
	    	address[1] = eAddress+1;
	    	address[2] = eAddress+32;
	    	address[3] = eAddress+64;
	    	address[4] = eAddress+96;
	    	address[5] = eAddress+128;

     	
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
	
	public int[] Scatter(int eAddress,int address[])throws IllegalAccessException{
    	Integer Block = cache.getBlockSize();
    	int i = 0;
      	Integer size = TotalMemSize;
	
    	Random rand = new Random();
		for(int j = 0; j < 4; j++){ // Random is  too inconsistent to prefetch, therefore in case of its use we only prefetch two blocks
			 i =  rand.nextInt(100);// from zero to size as random value 
			 if(eAddress + i*Block< size){
			 address[j] = eAddress + i*Block;
			 }
		}
		//Integer bAddress = i / Block; 
     
		return address;
	}

	public int[] Random(int address[])throws IllegalAccessException{
		int  i = 0 ;
		Random rand = new Random();
		Integer size = TotalMemSize;
		for(int j = 0; j < 2; j++){ // Random is  too inconsistent to prefetch, therefore in case of its use we only prefetch two blocks
			i =  rand.nextInt(size);// from zero to size as random value 
			address[j] = i ; 				
		}
		return address;	
	}
	
	
}
	

