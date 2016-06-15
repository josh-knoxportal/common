package org.oh.sample.controller;

import org.oh.sample.model.Sample_Test;
import org.oh.web.controller.CommonController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "sample_test")
public class Sample_TestController extends CommonController<Sample_Test> {
}
