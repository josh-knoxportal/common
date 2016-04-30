package org.oh.sample.controller;

import org.oh.sample.model.Test;
import org.oh.web.controller.CommonController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/test")
public class TestController extends CommonController<Test> {
}
