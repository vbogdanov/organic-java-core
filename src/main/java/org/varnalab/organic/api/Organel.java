/**
 * 
 */
package org.varnalab.organic.api;

import java.util.Map;

/**
 * An organel is a user defined implementation that reacts to chemicals,
 * somewhat similar to Controller from MVC
 * 
 * @author valeri
 * 
 */
public abstract class Organel implements Organic {
	private Plasma plasma;
	protected Organic parent;
	protected Map<String, Object> config;

	public Organel(Plasma plasma, Organic parent, Map<String, Object> config) {
		super();
		
		if (plasma == null || parent == null || config == null) {
			throw new IllegalArgumentException();
		}
		
		this.plasma = plasma;
		this.parent = parent;
		this.config = config;
	}
	
	/**
	 * Used from inside the organel to send chemicals to the outside world
	 * @param chemical
	 * @param callback
	 */
	protected void emit(Chemical chemical, Runnable callback) {
		plasma.emit(chemical, callback, this);
	}
	
	protected void on(Object chemicalPattern, ChemicalHandler handler) {
		plasma.on(chemicalPattern, handler, this);
	}
	
	protected void once(Object chemicalPattern, ChemicalHandler handler) {
		plasma.once(chemicalPattern, handler, this);
	}
	
	protected void off(Object chemicalPattern, ChemicalHandler handler) {
		plasma.off(chemicalPattern, handler, this);
	}
		
}
