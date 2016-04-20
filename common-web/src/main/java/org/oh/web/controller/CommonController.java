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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class CommonController<T extends Default> {
	protected Log log = LogFactory.getLog(getClass());

	@Resource(name = "commonService")
	protected CommonService<T> commonService;

	@RequestMapping(value = "/get", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<T> get(T t) throws Exception {
		t = commonService.get(t);

		return new ResponseEntity<T>(t, HttpStatus.OK);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<List<T>> list(T t) throws Exception {
//	public List<T> list(@Valid T t, BindingResult bindingResult) throws Exception {
		List<T> list = commonService.list(t);

		return new ResponseEntity<List<T>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/count", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<Integer> count(T t) throws Exception {
		int count = commonService.count(t);

		return new ResponseEntity<Integer>(count, HttpStatus.OK);
	}

	@RequestMapping(value = "/page", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<PageNavigator<T>> page(T t, Page<T> page) throws Exception {
		PageNavigator<T> pageNavi = new PageNavigator.Builder<T>(page).build();

		page = commonService.page(t, page);

		pageNavi.setList(page.getList());
		return new ResponseEntity<PageNavigator<T>>(pageNavi, HttpStatus.OK);
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<Integer> insert(T t) throws Exception {
		commonService.insert(t);

		return new ResponseEntity<Integer>(1, HttpStatus.OK);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<Integer> update(T t) throws Exception {
		commonService.update(t);

		return new ResponseEntity<Integer>(1, HttpStatus.OK);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> delete(T t) throws Exception {
		commonService.delete(t);

		return new ResponseEntity<Integer>(1, HttpStatus.OK);
	}
}
