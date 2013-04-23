package org.varnalab.organic.impl;

import org.varnalab.organic.api.Chemical;

public class SimpleChemical<T> implements Chemical {
	private Object type;
	private T data;
	public SimpleChemical(Object type, T data) {
		super();
		this.type = type;
		this.data = data;
	}
	public Object getType() {
		return type;
	}
	public T getData() {
		return data;
	}
}
