package edu.arizona.ece.memsim.implementations.markov;

import java.util.ArrayList;

public class BlockState {
	
	public Integer bAddress;
	public Integer bestCandidate;
	//public ArrayList<BlockState> candidateList;
	public ArrayList<Integer> accessList;
	
	public BlockState(Integer numBlocks){
		bestCandidate = -1;
		//candidateList = new ArrayList<BlockState>(numBlocks);
		accessList = new ArrayList<Integer>(numBlocks);
		for(int i = 0; i < numBlocks; i++)
			accessList.add(new Integer(0));
	}
	//Updates tracked best candidates index
	public void updateBest(){
		Integer bestIndex = -1;
		Integer currentBest = 0;
		for(int i = 0; i < accessList.size(); i++){
			if(accessList.get(i) > currentBest)
				bestIndex = i;
		}
		if(bestIndex != -1){
			bestCandidate = bestIndex;
		}
	}
	
	public void incrementCandidateAccess(Integer block){
		accessList.set(block, accessList.get(block) + 1);
	}
}
