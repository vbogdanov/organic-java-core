package org.varnalab.organic.impl;

import org.varnalab.organic.api.Chemical;

/**
 * A simple chemical consisting of a string for type and T for data. This chemical is immutable, as far as the data is immutable.
 * @param <T> - the type of the data contained
 */
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
