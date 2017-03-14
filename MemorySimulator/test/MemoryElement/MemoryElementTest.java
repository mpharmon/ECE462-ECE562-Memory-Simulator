package edu.arizona.ece.memsim.test.MemoryElement;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.arizona.ece.memsim.enums.MemoryState;
import edu.arizona.ece.memsim.model.MemoryElement;

public class MemoryElementTest {
	private MemoryElement memElement;
	
	private Byte b;
	
	@Before
	public void setUp() throws Exception {
		memElement = new MemoryElement();
		
		b = new Byte((byte)321);
		
		memElement.setData(b);
	}

	@Test
	public final void constructorTest_Standard() {
		new MemoryElement();
	}
	
	@Test
	public final void getDataTest(){
		Byte by = new Byte((byte) 234);
		
		memElement.setData(new Byte(by));
		
		assertEquals(memElement.getData(), by);
	}
	
	@Test
	public final void setDataTest(){
		assertEquals(memElement.getData(), b);
	}

	@Test
	public final void makeInvalidTest(){
		memElement.makeExclusive();
		memElement.makeInvalid();
		
		assertEquals(memElement.getMemoryState(), MemoryState.INVALID);
	}
	
	@Test
	public final void makeModifiedTest(){
		memElement.makeModified();
		
		assertEquals(memElement.getMemoryState(), MemoryState.MODIFIED);
	}
	
	@Test
	public final void makeExclusiveTest(){
		memElement.makeExclusive();
		
		assertEquals(memElement.getMemoryState(), MemoryState.EXCLUSIVE);
	}
	
	@Test
	public final void makeSharedTest(){
		memElement.makeShared();
		
		assertEquals(memElement.getMemoryState(), MemoryState.SHARED);
	}
}
