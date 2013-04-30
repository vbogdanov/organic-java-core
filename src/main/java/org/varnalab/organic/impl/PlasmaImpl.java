package org.varnalab.organic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.varnalab.organic.api.Chemical;
import org.varnalab.organic.api.ChemicalHandler;
import org.varnalab.organic.api.Organel;
import org.varnalab.organic.api.Plasma;

public class PlasmaImpl implements Plasma {
	
	private Map<Object, List<ChainAwareChemicalHandler>> handlers = new HashMap<>(); 
	
	@Override
	public void emit(Chemical chemical, Runnable callback, Organel sender) {
		Object chemicalPattern = chemical.getType();
		Iterator<ChainAwareChemicalHandler> chain;
		synchronized (this) {
			chain = new ArrayList<ChainAwareChemicalHandler>( //copy the list to avoid parallel access 
							getQueue(chemicalPattern)).iterator();
		}
		
		ChainAwareChemicalHandler.enqueChain(chain, chemical, sender, callback);
	}

	@Override
	public synchronized void on(Object chemicalPattern, ChemicalHandler handler,
			Organel organel) {
		getQueue(chemicalPattern).add(new ChainAwareChemicalHandler(handler, organel));
	}


	@Override
	public synchronized void once(Object chemicalPattern, ChemicalHandler handler,
			Organel organel) {
		getQueue(chemicalPattern).add(new ChainAwareChemicalHandler(handler, organel, true));
	}

	@Override
	public synchronized void off(Object chemicalPattern, ChemicalHandler handler,
			Organel organel) {
		getQueue(chemicalPattern).remove(new ChainAwareChemicalHandler(handler, organel));
	}


	@Override
	public synchronized void unregisterAll(Organel organel) {
		for (List<ChainAwareChemicalHandler> queue: handlers.values()) {
			List<ChainAwareChemicalHandler> toRemove = new ArrayList<>();
			for (ChainAwareChemicalHandler h: queue) {
				if (h.isReceivedBy(organel)) {
					toRemove.add(h);
				}
			}
			queue.removeAll(toRemove);
		}
	}	

	private List<ChainAwareChemicalHandler> getQueue(Object chemicalPattern) {
		List<ChainAwareChemicalHandler> queue = handlers.get(chemicalPattern);
		if (queue == null) {
			queue = new LinkedList<>();
			handlers.put(chemicalPattern, queue);
		}
		return queue;
	}

}
