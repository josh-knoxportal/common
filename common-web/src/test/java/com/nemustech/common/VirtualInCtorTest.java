package com.nemustech.common;

public class VirtualInCtorTest {
	public static void main(String[] args) {
		new Child(); // prints 0 then 10
	}

	public static class Parent {
		Parent() {
			super(); // Implicit even without call
			virtualMethod(); // Defect, virtual in constructor
		}

		public void virtualMethod() {
		}
	}

	public static class Child extends Parent {
		// 2
		int field = 10;

		// 1
		Child() {
			super(); // Implicit even without call
			// field = 10; // Initialization implicitly happens here
			System.out.println("Child: " + field); // prints 10
		}

		public void virtualMethod() {
			System.out.println("virtualMethod: " + field); // prints 0
		}
	}
}
