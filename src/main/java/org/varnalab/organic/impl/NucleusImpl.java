package org.varnalab.organic.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.varnalab.organic.api.DNA;
import org.varnalab.organic.api.Nucleus;
import org.varnalab.organic.api.Organel;
import org.varnalab.organic.api.Organic;
import org.varnalab.organic.api.Plasma;

public class NucleusImpl implements Nucleus {

	private DNA dna;
	private Plasma plasma;
	
	public NucleusImpl(DNA dna, Plasma plasma) {
		super();
		this.dna = dna;
		this.plasma = plasma;
	}

	
	private static final String SOURCE = "source";
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Organel> createNamespace(String namespace, Organic parent) {
		Collection<Organel> result = new ArrayList<>();
		Map<String, Object> branch = dna.selectBranch(namespace);
	    for(String key: branch.keySet()) {
	      Object objectConfig = branch.get(key);
	      if (objectConfig instanceof Map)
	    	  throw new IllegalStateException("a map was expected at namespace" + namespace);
	      
	      Map<String, Object> config = (Map<String, Object>) objectConfig;
	      
	      if(!config.containsKey(SOURCE))
	    	  throw new IllegalStateException("can not create object without source at "+namespace);
	        
	      String source = (String) config.get(SOURCE);
	      Organel instance = null;
	      
	      try {
			instance = (Organel) Class.forName(source)
			  .getConstructor(Plasma.class, Map.class, Organic.class)
			  .newInstance(plasma, config, parent);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	      result.add(instance);
	      
	      //FIXME:
	      //var address = (typeof objectConfig.address !== "undefined") ? objectConfig.address : (namespace + objectId);
	      //this.organellesMap[address] = { "instance": instance };
	    }
	  	return result;
	}

}
