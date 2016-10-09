package org.oh.common.util;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

public abstract class AnnotationUtil extends AnnotationUtils {
	public static void main(String[] args) throws Exception {
		System.out.println(isAnnotationDeclaredLocally(Component.class, Controller.class));
	}
}
