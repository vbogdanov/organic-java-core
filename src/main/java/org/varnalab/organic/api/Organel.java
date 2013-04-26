/**
 * 
 */
package org.varnalab.organic.api;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
	
	private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
	private final Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				while(! thread.isInterrupted()) {
					queue.take().run();
				}
			} catch (Exception e) {
				e.printStackTrace(); //use logging
				//TODO: react to the organel thread dying
				// Unregister from the plasma
				plasma.unregisterAll(Organel.this);
				unregister();
				//TODO: emit to plasma that this one is dying
			}
		}
	});

	public Organel(Plasma plasma, Organic parent, Map<String, Object> config) {
		super();
		
		if (plasma == null) {
			throw new IllegalArgumentException();
		}
		
		this.plasma = plasma;
		this.parent = parent;
		this.config = config;
		thread.start();
	}
	
	/**
	 * redefine this method to handle organel shutdown
	 */
	protected void unregister() {
		
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

	public void enqueue(Runnable runnable) {
		try {
			queue.put(runnable);
		} catch (InterruptedException e) {
			//FIXME:
			e.printStackTrace();
		}
	}
	
	
}
