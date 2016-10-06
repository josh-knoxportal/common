package org.oh.common;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.oh.common.storage.FileStorage;
import org.oh.common.storage.LocalFileStorage;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "file:conf/config-spring02.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test03 {
	public FileStorage fileStorage = LocalFileStorage.getInstance();

	@BeforeClass
	public static void beforeClass() throws Exception {
	}

	@Before
	public void init() throws Exception {
	}

	@Test
	public void test01() throws Exception {
//		InnerClass inner = new InnerClass();
//		inner.setField01("skoh1");
//		inner.setField02("skoh2");
//		System.out.println(inner);

		ParentClass inner = new ParentClass();
		System.out.println(inner);
	}

//	@ToString(callSuper = true)
	public class ParentClass {
//		@Getter
//		@Setter
		public String field01 = null;

//		public String toString() {
//			return "Test03.ParentClass(super=" + super.toString() + ", field01=" + getField01() + ")";
//		}
	}

//	@ToString(callSuper = true)
	public class InnerClass extends ParentClass {
//		@Getter
//		@Setter
		public String field02 = null;
	}
}
