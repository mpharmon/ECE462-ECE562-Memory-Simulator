package edu.arizona.ece.memsim.test.MemoryBlock;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.arizona.ece.memsim.model.MemoryBlock;

public class MemoryBlockTest {
	private MemoryBlock memoryBlock;
	
	@Before
	public void setUp() throws Exception {
		
		// Memory Block: Size 32B, Block #1
		memoryBlock = new MemoryBlock(32,1);
	}
	
	@Test
	public final void getBlockNumber_32B(){
		assertEquals(memoryBlock.getBlockAddress(), new Integer(1));
	}
	
	@Test
	public final void constructor_Standard(){
		new MemoryBlock(4,1);
	}
	
	@Test(expected=NullPointerException.class)
	public final void constructor_sizeNull(){
		new MemoryBlock(null,1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void constructor_sizeLT1(){
		new MemoryBlock(-1, 1);
	}
	
	@Test(expected=NullPointerException.class)
	public final void constructor_numberNull(){
		new MemoryBlock(4,null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void constructor_numberLT0(){
		new MemoryBlock(4,-1);
	}
}
