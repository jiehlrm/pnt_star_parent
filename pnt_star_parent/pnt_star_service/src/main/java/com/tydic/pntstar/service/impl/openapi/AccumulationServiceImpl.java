package com.tydic.pntstar.service.impl.openapi;

import org.springframework.stereotype.Service;

import com.tydic.pntstar.service.openapi.AccumulationService;

@Service("accumulationServiceImpl")
public class AccumulationServiceImpl implements AccumulationService {

	@Override
	public String accumulation(String json, String str,String s) {
		System.out.println(json);
		System.out.println(str);
		System.out.println(s);
		return "OK";
	}

}
