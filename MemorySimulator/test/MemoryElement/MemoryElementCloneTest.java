package edu.arizona.ece.memsim.test.MemoryElement;

import org.junit.Before;
import org.junit.Test;

import edu.arizona.ece.memsim.model.MemoryElement;

public class MemoryElementCloneTest {
	
	private MemoryElement mE1;

	@Before
	public final void setUp(){
		mE1 = new MemoryElement();
		
		mE1.setData((byte)1);
		mE1.makeModified();
	}
	
	@Test
	public final void test() {
		
		MemoryElement mE2 = mE1.clone();
		
		System.out.println("mE1: " + mE1);
		System.out.println("mE1.getData: " + mE1.getData());
		System.out.println("mE1.getMemoryState: " + mE1.getMemoryState());
		
		mE1.setData((byte)2);
		mE1.makeShared();
		
		System.out.println("\nThe following 3 lines should match the above 3 lines");
		System.out.println("mE2: " + mE2);
		System.out.println("mE2.getData: " + mE2.getData());
		System.out.println("mE2.getMemoryState: " + mE2.getMemoryState());
		System.out.println("If the above lines do not match, MemoryElements are being copied by Refrence");
	}

}
