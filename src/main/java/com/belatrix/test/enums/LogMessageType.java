package com.belatrix.test.enums;

public enum LogMessageType {
	
	MESSAGE(1), ERROR(2), WARNING(3);

	private Integer value;

	LogMessageType(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return this.value;
	}

}
