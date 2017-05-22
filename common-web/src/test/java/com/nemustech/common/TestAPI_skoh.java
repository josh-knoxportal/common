package com.nemustech.common;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import com.nemustech.common.test.TestAPI;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAPI_skoh extends TestAPI {
	@BeforeClass
	public static void initClass() throws Exception {
		arrayNode.add(readFile("src/test/resources/json/skoh/vehicle/ngms_device_information_get.json"));
	}
}
