package edu.arizona.ece.memsim.test.Cache;

import static org.hamcrest.core.Is.isA;
import static org.junit.Assume.assumeThat;

import org.junit.Before;
import org.junit.Test;

import edu.arizona.ece.memsim.model.Cache;
import edu.arizona.ece.memsim.model.Memory;
import edu.arizona.ece.memsim.model.MemoryBlock;

public class CacheTest_put {
	
	private Memory parentMemory;
	private Cache cache, parentCache;
	
	@Before
	public void setUp() throws Exception {
		parentMemory = new Memory(8192,64,200);
		assumeThat(parentMemory, isA(Memory.class));
		
		parentCache = new Cache(8192, 16, 1, 50, parentMemory);
		assumeThat(parentCache, isA(Cache.class));
		
		cache = new Cache(4096, 8, 1, 10, parentCache);
	}

	@Test
	public final void putTest_Standard() throws Exception {
		cache.put(16, new Byte((byte) 123));
	}
	
	@Test
	public final void putBlockTest_Standard(){
		cache.putBlock(new MemoryBlock(8,1));
	}

	@Test(expected = NullPointerException.class)
	public final void putTest_addressNull() throws Exception{
		cache.put(null, (byte)1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public final void putTest_addressLT0() throws Exception{
		cache.put(-1, new Byte((byte) 63));
	}
	
	@Test(expected = NullPointerException.class)
	public final void putTest_varNull() throws Exception{
		cache.put(0, null);
	}
	
	@Test(expected = NullPointerException.class)
	public final void putBlockTest_blockNull(){
		cache.putBlock(null);
	}
}
