package org.varnalab.organic.api;

public interface ChemicalHandler {
	public void handle(Chemical chemical, Organel sender, Organel reciever, Runnable callback);
}
