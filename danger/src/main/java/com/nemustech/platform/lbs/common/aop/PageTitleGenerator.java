package com.nemustech.platform.lbs.common.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class PageTitleGenerator {
	private Log logger = LogFactory.getLog(getClass());
	private String pageTitle = "pageTitle"; //default
	
	/**
	 * 메소드 실행후 페이지 타이틀 매핑
	 *
	 * @param thisJoinPoint
	 * @throws Throwable
	 */
	public void afterTargetMethod(JoinPoint thisJoinPoint) throws Throwable {

		//PageTitle Annotation 가져오기
		MethodSignature signature = (MethodSignature)thisJoinPoint.getSignature();
		logger.info("PageTitleGenerator.pageTitleAnnotation.value(): " + signature);
		
		PageTitle pageTitleAnnotation = signature.getMethod().getAnnotation(PageTitle.class);
		logger.info("PageTitleGenerator.pageTitleAnnotation.value(): " + pageTitleAnnotation);
		//Annotation 정보가 있을때만 매핑
		if(pageTitleAnnotation != null)
		{			
			//request정보에 Title정보를 담는다.
			RequestAttributes requestAttribute = RequestContextHolder.getRequestAttributes();
			Object oldPageTitle = requestAttribute.getAttribute(this.pageTitle, RequestAttributes.SCOPE_REQUEST);
			//Controller에서 미리 셋팅되어있는 값이면 등록하지 않는다.
			if(oldPageTitle == null) requestAttribute.setAttribute(this.pageTitle, pageTitleAnnotation.value(), RequestAttributes.SCOPE_REQUEST);			
			logger.info("PageTitleGenerator.pageTitleAnnotation.value(): " + pageTitleAnnotation.value());
		}
		
	}
	

}
