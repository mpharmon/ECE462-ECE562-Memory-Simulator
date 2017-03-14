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
public class MemoryTest_get {
	Memory memory;
	MemoryBlock memoryBlock;
	
	@Before
	public void setUp() throws Exception {
		// 16KB Memory, 64B Block, 100 Cycle Access Time
		memory = new Memory(16384,64,100);
		
		// Minimal Memory Block (8B)
		memoryBlock = new MemoryBlock(8,1);
		
		// Set Memory Block Values
		//memoryBlock.put(1, (byte)1);
		//memoryBlock.put(2, (byte)2);
		//memoryBlock.put(3, (byte)3);
		//memoryBlock.put(4, (byte)4);
		
		memory.putBlock(memoryBlock);
	}
	
	@Test
	public void getBlockTest_Standard(){
		assertEquals(memory.getBlock(1), memoryBlock);
	}
	
	@Test(expected=NullPointerException.class)
	public void getBlockTest_Null(){
		memory.getBlock(null);
	}
	
	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void getBlockTest_OOB(){
		memory.getBlock(-1);
	}
}
