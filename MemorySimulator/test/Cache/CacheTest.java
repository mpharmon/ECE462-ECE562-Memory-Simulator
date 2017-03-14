package edu.arizona.ece.memsim.test.Cache;

import static org.junit.Assume.*;
import static org.hamcrest.core.Is.isA;

import org.junit.Before;
import org.junit.Test;

import edu.arizona.ece.memsim.model.Cache;
import edu.arizona.ece.memsim.model.Memory;

public class CacheTest {

	private Cache parentCache;
	private Memory parentMemory;
	
	@Before
	public final void setUp(){
		parentMemory = new Memory(8192,64,200);
		assumeThat(parentMemory, isA(Memory.class));
		parentCache = new Cache(8192, 16, 1, 50, parentMemory);
		assumeThat(parentCache, isA(Cache.class));
	}
	
	@Test
	public final void constructorTest_Standard_parentCache() {
		new Cache(8192, 64, 4, 10, parentCache);
	}
	
	@Test
	public final void constructorTest_Standard_parentMemory() {
		new Cache(8192, 64, 4, 10, parentMemory);
	}
	
	@Test(expected = NullPointerException.class)
	public final void constructorTest_tSizeNull(){
		new Cache(null, 64, 4, 10, parentCache);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public final void constructorTest_tSizeLT1(){
		new Cache(0, 64, 4, 10, parentMemory);
	}
	
	@Test(expected = NullPointerException.class)
	public final void constructorTest_bSizeNull(){
		new Cache(8192, null, 4, 10, parentMemory);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public final void constructorTest_bSizeLT1(){
		new Cache(8192, 0, 4, 10, parentCache);
	}
	
	@Test(expected = NullPointerException.class)
	public final void constructorTest_assocNull(){
		new Cache(8192, 64, null, 10, parentCache);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public final void constructorTest_assocLT1(){
		new Cache(8192, 64, 0, 10, parentMemory);
	}
	
	@Test(expected = NullPointerException.class)
	public final void constructorTest_aTimeNull(){
		new Cache(8192, 64, 4, null, parentMemory);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public final void constructorTest_aTimeLT0(){
		new Cache(8192, 64, 4, -1, parentCache);
	}
	
	@Test(expected = NullPointerException.class)
	public final void constructorTest_parentCacheNull(){
		new Cache(8192, 64, 4, 10, (Cache)null);
	}

	@Test(expected = NullPointerException.class)
	public final void constructorTest_parentMemoryNull(){
		new Cache(8192, 64, 4, 10, (Memory)null);
	}
}
