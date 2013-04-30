package org.varnalab.organic.integration;

import java.util.Map;

import org.varnalab.organic.api.Chemical;
import org.varnalab.organic.api.ChemicalHandler;
import org.varnalab.organic.api.Organel;
import org.varnalab.organic.api.Plasma;
import org.varnalab.organic.impl.SimpleChemical;

public class OrganelB extends Organel {

	public OrganelB(Plasma plasma, Map<String, Object> config, Object parent) {
		super(plasma, config, parent);
		
		on("callB", new ChemicalHandler() {
			@Override
			public boolean handle(Chemical chemical, Organel sender, Runnable callback) {
				emit(new SimpleChemical<>("callA", chemical), callback);
				return true;
			}
		});
	}

}
