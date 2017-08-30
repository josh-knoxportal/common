package com.nemustech.common;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.nemustech.common.storage.FileStorage;
import com.nemustech.common.storage.LocalFileStorage;
import com.nemustech.common.util.StringUtil;

import lombok.Getter;
import lombok.Setter;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LombokTest2 {
	public FileStorage fileStorage = LocalFileStorage.getInstance();

	@BeforeClass
	public static void before() throws Exception {
	}

	@Before
	public void init() throws Exception {
	}

	@Test
	public void test01() throws Exception {
//		System.out.println("1234567890".getBytes());
//		System.out.println("1234567890".getBytes().getClass());
//		System.out.println(new byte[0].getClass());
//		System.out.println(byte.class);
//		System.out.println(Arrays.toString(ArrayUtils.addAll(new int[] { 1, 2 }, new int[] { 3, 4 })));

		Child obj = new Child();
		obj.setField01("skoh1");
		obj.setField02("skoh2");
		obj.setField03("1234567890".getBytes());
		System.out.println(obj.toString());

//		System.out.println(Arrays.toString(obj.getClass().getDeclaredFields())); // Child
//		System.out.println();
//		System.out.println(ReflectionUtil.getFields(obj.getClass())); // Child + Parent
//		System.out.println();
//		System.out.println(ReflectionUtil.getFields(obj.getClass(), String.class));
//		System.out.println(ReflectionUtil.getFields(obj.getClass(), new byte[0].getClass()));

//		Parent obj = new Child();
//		System.out.println(obj);
	}

	@Getter
	@Setter
	public class Parent {
		public String field01;
		public byte[] field04;

		@Override
		public String toString() {
//			return "Test03.Parent(super=" + super.toString() + ", field01=" + getField01() + ")";
//			return StringUtil.toString(this);
			return StringUtil.toString(this);
		}
	}

	@Getter
	@Setter
//	@ToString(callSuper = true)
	public class Child extends Parent {
		public String field02;
		public byte[] field03;
	}
}
