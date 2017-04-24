package edu.arizona.ece.memsim.implementations.markov;

import java.util.ArrayList;

public class HistoryTable {
	
	public static ArrayList<BlockState> trackedBlocks;
	
	public static void initialize(Integer numBlocks){
		trackedBlocks = new ArrayList<BlockState>(numBlocks);
		
		for(int i = 0; i < numBlocks; i++)
			trackedBlocks.add(new BlockState(numBlocks));
	}

	public static Integer getNextCandidate(Integer currentBlock){
		return trackedBlocks.get(currentBlock).bestCandidate;
	}
	
}
