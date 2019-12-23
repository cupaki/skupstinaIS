package com.skupstina.utils;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Rand {

	private Random random = new Random();
	
	public Integer getRandom() {
		return random.nextInt(10000000);
	}
}
