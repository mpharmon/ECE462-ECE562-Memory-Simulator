package edu.arizona.ece.memsim.test.MemoryBlock;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.arizona.ece.memsim.model.MemoryBlock;

public class MemoryBlockTest_put {
	private MemoryBlock memoryBlock;
	
	@Before
	public void setUp() throws Exception {
		
		// Memory Block: Size 32B, Block #1
		memoryBlock = new MemoryBlock(32,1);
	}
	
	@After
	public void tearDown(){
		memoryBlock = null;
	}

	@Test
	public final void putTest_Standard() {
		// A 32Byte Memory Block should be able to load 32 Elements (0 to 31)...
		assertTrue(memoryBlock.setElementValue(0, (byte)0));assertTrue(memoryBlock.setElementValue(1, (byte)1));
		assertTrue(memoryBlock.setElementValue(2, (byte)2));assertTrue(memoryBlock.setElementValue(3, (byte)3));
		assertTrue(memoryBlock.setElementValue(4, (byte)4));assertTrue(memoryBlock.setElementValue(5, (byte)5));
		assertTrue(memoryBlock.setElementValue(6, (byte)6));assertTrue(memoryBlock.setElementValue(7, (byte)7));
		assertTrue(memoryBlock.setElementValue(8, (byte)8));assertTrue(memoryBlock.setElementValue(9, (byte)9));
		assertTrue(memoryBlock.setElementValue(10, (byte)10));assertTrue(memoryBlock.setElementValue(11, (byte)11));
		assertTrue(memoryBlock.setElementValue(12, (byte)12));assertTrue(memoryBlock.setElementValue(13, (byte)13));
		assertTrue(memoryBlock.setElementValue(14, (byte)14));assertTrue(memoryBlock.setElementValue(15, (byte)15));
		assertTrue(memoryBlock.setElementValue(16, (byte)16));assertTrue(memoryBlock.setElementValue(17, (byte)17));
		assertTrue(memoryBlock.setElementValue(18, (byte)18));assertTrue(memoryBlock.setElementValue(19, (byte)19));
		assertTrue(memoryBlock.setElementValue(20, (byte)20));assertTrue(memoryBlock.setElementValue(21, (byte)21));
		assertTrue(memoryBlock.setElementValue(22, (byte)22));assertTrue(memoryBlock.setElementValue(23, (byte)23));
		assertTrue(memoryBlock.setElementValue(24, (byte)24));assertTrue(memoryBlock.setElementValue(25, (byte)25));
		assertTrue(memoryBlock.setElementValue(26, (byte)26));assertTrue(memoryBlock.setElementValue(27, (byte)27));
		assertTrue(memoryBlock.setElementValue(28, (byte)28));assertTrue(memoryBlock.setElementValue(29, (byte)29));
		assertTrue(memoryBlock.setElementValue(30, (byte)30));assertTrue(memoryBlock.setElementValue(31, (byte)31));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public final void putTest_OOB(){
		// ...and Fail on the 33nd (32)
		memoryBlock.setElementValue(32, (byte)32);
	}
}
