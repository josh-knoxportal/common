package org.oh.sample.controller;

import org.oh.sample.model.SampleAndTest;
import org.oh.web.controller.CommonController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "sample_and_test")
public class SampleAndTestController extends CommonController<SampleAndTest> {
}
