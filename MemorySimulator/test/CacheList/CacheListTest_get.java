package edu.arizona.ece.memsim.test.CacheList;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.arizona.ece.memsim.model.CacheList;
import edu.arizona.ece.memsim.model.MemoryBlock;

public class CacheListTest_get {
	private CacheList cacheList;
	
	private MemoryBlock memoryBlock1, memoryBlock2;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("Running setUp");
		
		// Small 8 Entry Cache List
		cacheList = new CacheList(8);
	
		// Three Small 8B Memory Blocks
		memoryBlock1 = new MemoryBlock(8, 1);
		memoryBlock2 = new MemoryBlock(8, 2);
		
		cacheList.put(memoryBlock1);
		cacheList.put(memoryBlock2);
	}
	
	@Test
	public final void getTest_Standard(){
		System.out.println("Starting get Test");
		
		// Standard Get Tests
		assertEquals(cacheList.get(1), memoryBlock1);
		assertEquals(cacheList.get(2), memoryBlock2);
	}
	
	@Test
	public final void getTest_Unused(){
		System.out.println("Starting getTest_Unused");
		
		// Unused Memory Location
		assertEquals(cacheList.get(0), null);
	}
	
	@Test(expected = NullPointerException.class)
	public final void getTest_Null(){
		System.out.println("Starting getTest_Null");
		
		// Null Get
		cacheList.get(null);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public final void getTest_OOB(){
		System.out.println("Starting getTest_OOB");
		
		// Out of Bounds Get
		cacheList.get(123);
	}
	
	@Test
	public void containsTest(){
		System.out.println("Starting contains Test");
		
		// Expected True
		assertTrue(cacheList.contains(1));
		
		// Expected False
		assertFalse(cacheList.contains(22));
	}
	
	@Test
	public void getSizeTest(){
		System.out.println("Starting getSize Test");
		
		// Expected
		assertTrue(cacheList.getSize().equals(8));
	}
}
