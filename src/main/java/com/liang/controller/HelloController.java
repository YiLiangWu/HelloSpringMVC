package com.liang.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liang.filter.CountingFilter;

@Controller
@RequestMapping(value = "/hello", produces = "text/html;charset=UTF-8")
public class HelloController {

	@RequestMapping("/test")
	@ResponseBody
	public String test(String name, String password, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("name", name);
		result.put("num", CountingFilter.num.get());
		return JSONObject.fromObject(result).toString();
	}

}
