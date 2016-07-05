package org.oh.common.util;

import org.oh.sample.controller.SampleController;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

public abstract class AnnotationUtil extends AnnotationUtils {
	public static void main(String[] args) throws Exception {
		System.out.println(isAnnotationDeclaredLocally(Service.class, SampleController.class));
	}
}
