package org.varnalab.organic.api;

public interface Plasma {
	public void emit(Chemical chemical, Runnable callback, Organel sender);

	public void on(Object chemicalPattern, ChemicalHandler handler,
			Organel organel);

	public void once(Object chemicalPattern, ChemicalHandler handler,
			Organel organel);

	public void off(Object chemicalPattern, ChemicalHandler handler,
			Organel organel);

}
