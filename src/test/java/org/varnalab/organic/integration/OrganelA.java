package org.varnalab.organic.integration;

import java.util.HashMap;
import java.util.Map;

import org.varnalab.organic.api.Chemical;
import org.varnalab.organic.api.ChemicalHandler;
import org.varnalab.organic.api.Organel;
import org.varnalab.organic.api.Plasma;
import org.varnalab.organic.impl.SimpleChemical;

public class OrganelA extends Organel{
	public static Map<Object, OrganelA> registry = new HashMap<Object, OrganelA>();
	
	public OrganelA(Plasma plasma, Map<String, Object> config, Object parent) {
		super(plasma, config, parent);
		
		on("callA", new ChemicalHandler() {
			@Override
			public boolean handle(Chemical chemical, Organel sender, Runnable callback) {
				callback.run();
				return true;
			}
		});
		initListen(config.get("port"));
	}

	private void initListen(Object object) {
		registry.put(object, this);
	}
	
	public void startCommunication(Runnable callback) {
		emit(new SimpleChemical<Object>("callB", null), callback);
	}
}
