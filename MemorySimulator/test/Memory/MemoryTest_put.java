package edu.arizona.ece.memsim.test.Memory;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.arizona.ece.memsim.model.Memory;
import edu.arizona.ece.memsim.model.MemoryBlock;

/**
 * 
 */

/**
 * @author Mike Harmon
 *
 */
public class MemoryTest_put {
	Memory memory;
	MemoryBlock memoryBlock;
	
	@Before
	public void setUp() throws Exception {
		// 16KB Memory, 64B Block, 100 Cycle Access Time
		memory = new Memory(16384,64,100);
		
		// Minimal Memory Block (8B)
		memoryBlock = new MemoryBlock(8,1);
		
	}

	@Test
	public void putBlockTest_Standard(){
		// Test Standard Case
		assertTrue(memory.putBlock(memoryBlock));
	}
	
	@Test(expected=NullPointerException.class)
	public void putBlockTest_Null(){
		//Test null Case
		memory.putBlock(null);
	}
}
