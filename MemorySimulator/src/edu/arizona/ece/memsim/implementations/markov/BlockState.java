package edu.arizona.ece.memsim.implementations.markov;

import java.util.ArrayList;

public class BlockState {
	
	public Integer bAddress;
	public Integer bestCandidate;
	public ArrayList<BlockState> candidateList;
	public ArrayList<Float> probabilityList;
	
	public BlockState(Integer numBlocks){
		
		bestCandidate = -1;
		candidateList = new ArrayList<BlockState>(numBlocks);
		probabilityList = new ArrayList<Float>(numBlocks);
	}
	//Updates tracked best candidates index
	public void updateBest(){
		Integer bestIndex = -1;
		Float currentBest = (float) 0;
		for(int i = 0; i < probabilityList.size(); i++){
			if(probabilityList.get(i) > currentBest)
				bestIndex = i;
		}
		if(bestIndex != -1){
			bestCandidate = bestIndex;
		}
	}
}
