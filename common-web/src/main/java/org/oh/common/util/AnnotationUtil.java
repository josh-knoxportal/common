package org.oh.common.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.core.annotation.AnnotationUtils;

public abstract class AnnotationUtil extends AnnotationUtils {
	public static <T> Object getValue(Class<T> cls, Class<? extends Annotation> annotationType) {
		return getValue(cls.getAnnotation(annotationType));
	}

	public static <T> Object getValue(Class<T> cls, Class<? extends Annotation> annotationType, String attributeName) {
		return getValue(cls.getAnnotation(annotationType), attributeName);
	}

	public static void setAnnotation(Annotation annotation) throws Exception {
		Field field = Class.class.getDeclaredField("annotations");
		field.setAccessible(true);

		Map<Class<? extends Annotation>, Annotation> annotations = (Map<Class<? extends Annotation>, Annotation>) field
				.get(Foobar.class);
		annotations.put(Something.class, annotation);
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Something {
		String value() default "";

		int number() default 1;
	}

	@Something("some")
	public class Foobar {
	}

	public static void main(String[] args) throws Exception {
//		System.out.println(isAnnotationDeclaredLocally(Component.class, Controller.class));

		Annotation annotation = Foobar.class.getAnnotation(Something.class);
		System.out.println(annotation);
		System.out.println(getValue(Foobar.class, Something.class));
		System.out.println(getValue(Foobar.class, Something.class, "number"));

//		Method method = annotation.annotationType().getDeclaredMethod("value");
//		System.out.println(getValue(annotation));
//		System.out.println(method.invoke(annotation));
//		System.out.println(getDefaultValue(annotation));
//		System.out.println(method.getDefaultValue());

//		final Annotation[] annotation = Foobar.class.getAnnotations();
//		System.out.println(Utils.toString(annotation));
//
//		Annotation newAnnotation = new Something() {
//			@Override
//			public String value() {
//				return "another";
//			}
//
//			@Override
//			public Class<? extends Annotation> annotationType() {
//				return annotation[0].annotationType();
//			}
//		};
//		setAnnotation(newAnnotation);
//
//		Annotation[] annotation2 = Foobar.class.getAnnotations();
//		System.out.println(Utils.toString(((Something) annotation2[0]).value()));
	}
}
