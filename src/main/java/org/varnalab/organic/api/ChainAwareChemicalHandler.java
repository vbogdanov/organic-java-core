package org.varnalab.organic.api;

import java.util.Iterator;

public class ChainAwareChemicalHandler {
	
	private ChemicalHandler handler;
	private Organel receiver;
	private volatile boolean used = false;
	private boolean once = false;
	
	public ChainAwareChemicalHandler(ChemicalHandler handler, Organel reciever) {
		super();
		this.handler = handler;
		this.receiver = reciever;
	}
	
	public ChainAwareChemicalHandler(ChemicalHandler handler, Organel reciever,
			boolean once) {
		super();
		this.handler = handler;
		this.receiver = reciever;
		this.once = once;
	}

	private void handle(Chemical chemical, Organel sender, Iterator<ChainAwareChemicalHandler> chain, Runnable callback) {
		boolean done = false;
		boolean skip = once && used;
		skip |= receiver == sender;
		
		if (! skip) { 
			done = handler.handle(chemical, sender,  callback);
		}
		if (! done) {
			enqueChain(chain, chemical, sender, callback);
		}
	}

	private void enqueue(final Chemical chemical, final Organel sender, final Iterator<ChainAwareChemicalHandler> chain, final Runnable callback) {
		receiver.enqueue(new Runnable() {
			@Override
			public void run() {
				ChainAwareChemicalHandler.this.handle(chemical, sender, chain, callback);
			}
		});
	}
	
	public static void enqueChain(final Iterator<ChainAwareChemicalHandler> chain, final Chemical chemical, final Organel sender, final Runnable callback){
		if (chain.hasNext())
			chain.next().enqueue(chemical, sender, chain, callback);
	}

	//Auto-generated on receiver, handler and once
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handler == null) ? 0 : handler.hashCode());
		result = prime * result + (once ? 1231 : 1237);
		result = prime * result
				+ ((receiver == null) ? 0 : receiver.hashCode());
		return result;
	}

	//Auto-generated on receiver, handler and once
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChainAwareChemicalHandler other = (ChainAwareChemicalHandler) obj;
		if (handler == null) {
			if (other.handler != null)
				return false;
		} else if (!handler.equals(other.handler))
			return false;
		if (once != other.once)
			return false;
		if (receiver == null) {
			if (other.receiver != null)
				return false;
		} else if (!receiver.equals(other.receiver))
			return false;
		return true;
	}

	public boolean isReceivedBy(Organel organel) {
		return organel == receiver;
	}
}
