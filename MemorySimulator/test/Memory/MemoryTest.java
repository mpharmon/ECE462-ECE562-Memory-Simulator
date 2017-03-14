package edu.arizona.ece.memsim.test.Memory;

import static org.junit.Assert.*;

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
public class MemoryTest {
	Memory memory;
	MemoryBlock memoryBlock;

	@Test
	public final void constructorTest_Standard(){
		assertEquals(new Memory(8192, 64, 200).getClass(), Memory.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void constructorTest_tSizeLT0(){
		new Memory(-1,64,200);
	}
	
	@Test(expected=NullPointerException.class)
	public final void constructorTest_tSizeNull(){
		new Memory(null,64,200);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void constructorTest_bSizeLT0(){
		new Memory(8192,-1,200);
	}
	
	@Test(expected=NullPointerException.class)
	public final void constructorTest_bSizeNull(){
		new Memory(8192,null,200);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void constructorTest_aTimeLT0(){
		new Memory(8192,64,-1);
	}
	
	@Test(expected=NullPointerException.class)
	public final void constructorTest_aTimeNull(){
		new Memory(8192,64,null);
	}
}
