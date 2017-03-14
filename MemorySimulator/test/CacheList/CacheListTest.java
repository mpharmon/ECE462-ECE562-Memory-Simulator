package edu.arizona.ece.memsim.test.CacheList;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.arizona.ece.memsim.model.CacheList;
import edu.arizona.ece.memsim.model.MemoryBlock;

public class CacheListTest {
	private CacheList cacheList;
	
	private MemoryBlock memoryBlock1;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("Running setUp");
		
		// Small 8 Entry Cache List
		cacheList = new CacheList(8);
	
		// Three Small 8B Memory Blocks
		memoryBlock1 = new MemoryBlock(8, 1);
		
		cacheList.put(memoryBlock1);
	}
	
	@Test
	public final void constructorTest_Standard(){
		new CacheList(8);
	}
	
	@Test(expected=NullPointerException.class)
	public final void constructorTest_sNull(){
		new CacheList(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void constructorTest_sLT1(){
		new CacheList(0);
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
