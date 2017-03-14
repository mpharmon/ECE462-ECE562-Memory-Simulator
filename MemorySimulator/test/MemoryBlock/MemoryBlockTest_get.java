package edu.arizona.ece.memsim.test.MemoryBlock;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.arizona.ece.memsim.model.MemoryBlock;

public class MemoryBlockTest_get {
	private static MemoryBlock memoryBlock;
	
	@BeforeClass
	public static void setUp() throws Exception {
		
		// Memory Block: Size 32B, Block #1
		memoryBlock = new MemoryBlock(32,1);
		
		// Load Memory Block
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
	
	@Test
	public final void getTest_Standard(){
		
		assertEquals(memoryBlock.getElementValue(0), new Byte((byte)0));assertEquals(memoryBlock.getElementValue(1), new Byte((byte)1));
		assertEquals(memoryBlock.getElementValue(2), new Byte((byte)2));assertEquals(memoryBlock.getElementValue(3), new Byte((byte)3));
		assertEquals(memoryBlock.getElementValue(4), new Byte((byte)4));assertEquals(memoryBlock.getElementValue(5), new Byte((byte)5));
		assertEquals(memoryBlock.getElementValue(6), new Byte((byte)6));assertEquals(memoryBlock.getElementValue(7), new Byte((byte)7));
		assertEquals(memoryBlock.getElementValue(8), new Byte((byte)8));assertEquals(memoryBlock.getElementValue(9), new Byte((byte)9));
		assertEquals(memoryBlock.getElementValue(10), new Byte((byte)10));assertEquals(memoryBlock.getElementValue(11), new Byte((byte)11));
		assertEquals(memoryBlock.getElementValue(12), new Byte((byte)12));assertEquals(memoryBlock.getElementValue(13), new Byte((byte)13));
		assertEquals(memoryBlock.getElementValue(14), new Byte((byte)14));assertEquals(memoryBlock.getElementValue(15), new Byte((byte)15));
		assertEquals(memoryBlock.getElementValue(16), new Byte((byte)16));assertEquals(memoryBlock.getElementValue(17), new Byte((byte)17));
		assertEquals(memoryBlock.getElementValue(18), new Byte((byte)18));assertEquals(memoryBlock.getElementValue(19), new Byte((byte)19));
		assertEquals(memoryBlock.getElementValue(20), new Byte((byte)20));assertEquals(memoryBlock.getElementValue(21), new Byte((byte)21));
		assertEquals(memoryBlock.getElementValue(22), new Byte((byte)22));assertEquals(memoryBlock.getElementValue(23), new Byte((byte)23));
		assertEquals(memoryBlock.getElementValue(24), new Byte((byte)24));assertEquals(memoryBlock.getElementValue(25), new Byte((byte)25));
		assertEquals(memoryBlock.getElementValue(26), new Byte((byte)26));assertEquals(memoryBlock.getElementValue(27), new Byte((byte)27));
		assertEquals(memoryBlock.getElementValue(28), new Byte((byte)28));assertEquals(memoryBlock.getElementValue(29), new Byte((byte)29));
		assertEquals(memoryBlock.getElementValue(30), new Byte((byte)30));assertEquals(memoryBlock.getElementValue(31), new Byte((byte)31));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public final void getTest_OOB(){
		memoryBlock.getElement(32);
	}
}
