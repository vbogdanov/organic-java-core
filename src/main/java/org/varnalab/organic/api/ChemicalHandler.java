package org.varnalab.organic.api;

public interface ChemicalHandler {
	public boolean handle(Chemical chemical, Organel sender, Runnable callback);
}
