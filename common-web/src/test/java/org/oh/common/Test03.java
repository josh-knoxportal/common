package org.oh.common;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.oh.common.storage.LocalFileStorageAccessor;
import org.oh.common.storage.StorageAccessor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "file:conf/config-spring02.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test03 {
	protected StorageAccessor storageAccessor = LocalFileStorageAccessor.getInstance();

	@BeforeClass
	public static void beforeClass() throws Exception {
	}

	@Before
	public void init() {
	}

	@Test
	public void test01() {
//		InnerClass inner = new InnerClass();
//		inner.setField01("skoh1");
//		inner.setField02("skoh2");
//		System.out.println(inner);
		
		ParentClass inner = new ParentClass();
		System.out.println(inner);
	}

//	@ToString(callSuper = true)
	protected class ParentClass {
		@Getter
		@Setter
		protected String field01 = null;

		public String toString() {
			return "Test03.ParentClass(super=" + super.toString() + ", field01=" + getField01() + ")";
		}
	}

	@ToString(callSuper = true)
	protected class InnerClass extends ParentClass {
		@Getter
		@Setter
		protected String field02 = null;
	}
}
