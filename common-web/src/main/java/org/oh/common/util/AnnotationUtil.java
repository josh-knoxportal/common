package org.oh.common.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.core.annotation.AnnotationUtils;

public abstract class AnnotationUtil extends AnnotationUtils {
	public static void setAnnotation(Annotation annotation) throws Exception {
		Field field = Class.class.getDeclaredField("annotations");
		field.setAccessible(true);

		Map<Class<? extends Annotation>, Annotation> annotations = (Map<Class<? extends Annotation>, Annotation>) field
				.get(Foobar.class);
		annotations.put(Something.class, annotation);
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface Something {
		String someProperty();
	}

	@Something(someProperty = "some")
	public class Foobar {
	}

	public static void main(String[] args) throws Exception {
//		System.out.println(isAnnotationDeclaredLocally(Component.class, Controller.class));

		final Something annotation = (Something) Foobar.class.getAnnotations()[0];
		System.out.println(annotation.someProperty());

		Annotation newAnnotation = new Something() {
			@Override
			public String someProperty() {
				return "another";
			}

			@Override
			public Class<? extends Annotation> annotationType() {
				return annotation.annotationType();
			}
		};
		setAnnotation(newAnnotation);

		Something annotation2 = (Something) Foobar.class.getAnnotations()[0];
		System.out.println(annotation2.someProperty());
	}
}
