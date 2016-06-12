package com.liang.controller;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/hello", produces = "text/html;charset=UTF-8")
public class HelloController {

	@RequestMapping("/test")
	@ResponseBody
	public String test(String name) {
		Map<String, Object> result = new HashMap<String, Object>();
		System.out.println("name = " + name);
		result.put("name", name);
		return JSONObject.fromObject(result).toString();
	}
}
