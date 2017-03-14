package edu.arizona.ece.memsim.test.CacheList;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.arizona.ece.memsim.model.CacheList;
import edu.arizona.ece.memsim.model.MemoryBlock;

public class CacheListTest_put {
	private CacheList cacheList;
	
	private MemoryBlock memoryBlock1, memoryBlock2, memoryBlockFail;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("Running setUp");
		
		// Small 8 Entry Cache List
		cacheList = new CacheList(8);
	
		// Three Small 8B Memory Blocks
		memoryBlock1 = new MemoryBlock(8, 1);
		memoryBlock2 = new MemoryBlock(8, 2);
		memoryBlockFail = new MemoryBlock(8, 8);
	}

	@Test
	public final void putTest_Standard() {
		System.out.println("Starting putTest_Standard");
		
		// Standard Put Test
		assertTrue(cacheList.put(memoryBlock1));
		assertTrue(cacheList.put(memoryBlock2));
	}
	
	@Test(expected=NullPointerException.class)
	public final void putTest_Null(){
		System.out.println("Starting putTest_Null");
		// Null Put
		cacheList.put(null);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public final void putTest_OOB(){
		System.out.println("Starting putTest_OOB");
		
		// Out of Bounds Put
		cacheList.put(memoryBlockFail);
	}
	
	@Test
	public void containsTest(){
		System.out.println("Starting contains Test");
		
		// Prep memory Array
		cacheList.put(memoryBlock1);
		cacheList.put(memoryBlock2);
		
		// Expected True
		assertTrue(cacheList.contains(1));
		
		// Expected False
		assertFalse(cacheList.contains(22));
	}
	
	@Test
	public void getSizeTest(){
		System.out.println("Starting getSize Test");
		
		// Expected Pass
		assertTrue(cacheList.getSize().equals(8));
	}
}
