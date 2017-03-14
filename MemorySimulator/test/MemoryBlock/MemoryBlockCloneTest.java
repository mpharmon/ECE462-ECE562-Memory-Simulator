package edu.arizona.ece.memsim.test.MemoryBlock;

import org.junit.Before;
import org.junit.Test;

import edu.arizona.ece.memsim.model.MemoryBlock;
import edu.arizona.ece.memsim.model.MemoryElement;

public class MemoryBlockCloneTest {

	private MemoryBlock mB1;
	
	@Before
	public void setUp() {
		mB1 = new MemoryBlock(4, 1);
		mB1.setElement(0, new MemoryElement((byte)0));
		mB1.setElement(1, new MemoryElement((byte)1));
		mB1.setElement(2, new MemoryElement((byte)2));
		mB1.setElement(3, new MemoryElement((byte)3));
	}

	@Test
	public final void test() {
		MemoryBlock mB2 = mB1.clone();
		
		String mB1String = new String("mB1: " + mB1);
		String mB1GetElementString = new String("mB1.getElement(1): " + mB1.getElement(1));
		
		mB1.setElement(1, new MemoryElement((byte)11));
		
		System.out.println("The Following Lines Should Not Match");
		System.out.println(mB1String);
		System.out.println("mB2: " + mB2);
		System.out.println("If the above matchs, MemoryBlock's are being copied by Refrence");
		System.out.println("\nThe following Lines Should not Match");
		System.out.println(mB1GetElementString);
		System.out.println("mB2.getElement(1): " + mB2.getElement(1));
		System.out.println("If the above matchs, MemoryElement[]'s are being copied by Refrence");

	}

}
