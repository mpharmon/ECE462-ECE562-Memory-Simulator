package edu.arizona.ece.memsim.implementations.markov;

import java.util.ArrayList;

public class HistoryTable {
	
	public static Integer lastBlock;
	public static ArrayList<BlockState> trackedBlocks;
	
	public static void initialize(Integer numBlocks){
		lastBlock = -1;
		trackedBlocks = new ArrayList<BlockState>(numBlocks);
		for(int i = 0; i < numBlocks; i++){
			trackedBlocks.add(new BlockState(numBlocks));
		}
	}

	public static Integer getNextCandidate(Integer currentBlock, Integer blockSize){
		
		Integer nextBlock;
		
		if(lastBlock >= 0){
			//Update access for current block that was accessed
			trackedBlocks.get(lastBlock).incrementCandidateAccess(currentBlock);
			trackedBlocks.get(lastBlock).updateBest();
			//Set next block to prefetch as current blocks best candidate
			
		}
		if(trackedBlocks.get(currentBlock).bestCandidate > 0)
			nextBlock = trackedBlocks.get(currentBlock).bestCandidate*blockSize;
		else
			nextBlock = currentBlock;
		
		lastBlock = currentBlock;
		return nextBlock;
	}
	
}
