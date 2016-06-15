package org.oh.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatisorm.Page;
import org.oh.web.model.Default;
import org.oh.web.page.PageNavigator;
import org.oh.web.service.CommonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CommonController<T extends Default> {
	protected Log log = LogFactory.getLog(getClass());

	@Resource(name = "commonService")
	protected CommonService<T> commonService;

	@RequestMapping(value = "get.do", method = { RequestMethod.GET })
	public ResponseEntity<T> get(T t) throws Exception {
		t = commonService.get(t);

		return new ResponseEntity<T>(t, HttpStatus.OK);
	}

	@RequestMapping(value = "list.do", method = { RequestMethod.GET })
	public ResponseEntity<List<T>> list(T t) throws Exception {
		List<T> list = commonService.list(t);

		return new ResponseEntity<List<T>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "count.do", method = { RequestMethod.GET })
	public ResponseEntity<Integer> count(T t) throws Exception {
		int count = commonService.count(t);

		return new ResponseEntity<Integer>(count, HttpStatus.OK);
	}

	@RequestMapping(value = "page.do", method = { RequestMethod.GET })
	public ResponseEntity<PageNavigator<T>> page(T t, Page<T> page) throws Exception {
		page = commonService.page(t, page);

		return new ResponseEntity<PageNavigator<T>>(PageNavigator.getInstance(page), HttpStatus.OK);
	}

	@RequestMapping(value = "insert.do", method = RequestMethod.POST)
	public ResponseEntity<Integer> insert(T t) throws Exception {
		int result = commonService.insert(t);

		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "update.do", method = RequestMethod.PUT)
	public ResponseEntity<Integer> update(T t) throws Exception {
		int result = commonService.update(t);

		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "delete.do", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> delete(T t) throws Exception {
		int result = commonService.delete(t);

		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
}
