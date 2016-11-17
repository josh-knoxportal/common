package com.nemustech.web.controller;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nemustech.common.model.Default;
import com.nemustech.sample.model.Sample;

@Controller
@RequestMapping(value = "template")
public class TemplateController<T extends Default> {
	@RequestMapping(value = "test", method = { RequestMethod.GET })
	public String test(Sample model) throws Exception {
		return "test01";
	}

	@RequestMapping(value = "create", method = { RequestMethod.GET })
	public ModelAndView create(Sample model, ModelAndView mav) throws Exception {
		mav.setViewName("templateMapper");

		mav.addObject("table", "sample");
		mav.addObject("column_list", Arrays.asList("id", "name"));

		return mav;
	}
}
