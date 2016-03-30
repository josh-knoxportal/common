package org.oh.common;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.oh.common.storage.LocalFileStorageAccessor;
import org.oh.common.storage.StorageAccessor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "file:conf/config-spring02.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test01 {
	protected StorageAccessor storageAccessor = LocalFileStorageAccessor.getInstance();
	
	private String targetClass = "1";

	@BeforeClass
	public static void beforeClass() throws Exception {
	}

	@Before
	public void init() {
	}

//	@Test
	public void test01() {
		System.out.println(storageAccessor.toString());
	}

	@Cacheable(value = "test", key = "#root.caches[0].name + '_' + #root.targetClass + '_' + #root.methodName + '_' + #root.args[0]")
	@Test
	public void test02() throws Exception {
		String s = "";
//		s = "'Hello World'";
//		s = "'Hello World'.concat('!')";
//		s = "'Hello World'.bytes.length";
//		s = "'Hello World'.getBytes().length";

//		s = "new String('hello world').toUpperCase()";
//		s = "new org.oh.common.Test01().targetClass";
//		s = "new org.oh.common.Test01().getTargetClass()";

//		s = "T(java.lang.String).valueOf(true)";
//		s = "T(java.lang.Math).random()";
//		s = "T(org.oh.common.Test01).targetClass";
		s = "T(org.apache.commons.lang.builder.ReflectionToStringBuilder).toString('a')";

//		s = "#this";
		
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression(s);
		Object message = exp.getValue();
//		System.out.println(message);

//		List<Integer> primes = new ArrayList<Integer>();
//		primes.addAll(Arrays.asList(2, 3, 5, 7, 11, 13, 17));
//
//		// create parser and set variable 'primes' as the array of integers
//		ExpressionParser parser = new SpelExpressionParser();
//		StandardEvaluationContext context = new StandardEvaluationContext();
//		context.setVariable("primes", primes);
//
//		// all prime numbers > 10 from the list (using selection ?{...})
//		// evaluates to [11, 13, 17]
//		List<Integer> primesGreaterThanTen = (List<Integer>) parser.parseExpression("#primes.?[#this>10]")
//				.getValue(context);
//		System.out.println(primesGreaterThanTen);

//		Annotation[] annos;
//		annos = this.getClass().getAnnotations();
//		for (Annotation anno : annos) {
//			System.out.println(anno);
//		}

//		Method meth = this.getClass().getMethod("test02");
//		annos = meth.getAnnotations();
//		for (Annotation anno : annos) {
//			System.out.println(anno);
//			if (anno instanceof Cacheable) {
//				Cacheable cache = (Cacheable) anno;
//				System.out.println(cache);
//				System.out.println(Utils.toString(cache.value()));
//				System.out.println(Utils.toString(cache.key()));
//			}
//		}
	}

	public String getTargetClass() {
		return targetClass;
	}

	public static void main(String[] args) throws Exception {
		Test01 test01 = new Test01();
//		test01.test02();
		System.out.println(ReflectionToStringBuilder.toString(test01));
//		System.out.println(BeanUtils.describe(test01));
	}
}
