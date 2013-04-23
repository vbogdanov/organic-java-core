package org.varnalab.organic.api;

import java.util.Collection;

public class Membrane implements Organic {
	private Plasma plasma;
	private Collection<Organel> holes;
	
	public Membrane(Plasma plasma, Nucleus nucleus) {
		super();
		this.plasma = plasma;
		this.holes = nucleus.createNamespace("membrane", this);
	}
	
	public Plasma getPlasma() {
		return plasma;
	}
	public Collection<Organel> getHoles() {
		return holes;
	}
	
	
}
