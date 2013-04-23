package org.varnalab.organic.api.annot;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.varnalab.organic.JSONUtil;
import org.varnalab.organic.api.Chemical;
import org.varnalab.organic.api.ChemicalHandler;
import org.varnalab.organic.api.Organel;
import org.varnalab.organic.api.Plasma;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AnnotationUtil {
	public static Map<String, Object> calcDefaultConfig(Class<? extends Organel> clazz, Map<String, Object> config) throws JsonParseException, JsonMappingException, IOException {
		DefaultConfig conf = (DefaultConfig) clazz.getAnnotation(DefaultConfig.class);
		if (conf == null) {
			throw new IllegalArgumentException("Must have default config");
		}
		String json = conf.value();
	
		ObjectMapper mapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String, Object> defaultConfig = mapper.readValue(json, Map.class);
		
		return JSONUtil.merge(config, defaultConfig);
	}

	public static void registerChemicalAnnotations(Organel org, Plasma plasma) {
		Method[] methods = org.getClass().getMethods();
		for (Method m: methods) {
			if (m.getAnnotations().length > 0) {
				handleChemicalAnnotationsOnMethod(m, org, plasma);
			}
		}
	}

	
	private static class AnnotatedChemicalHandler implements ChemicalHandler {
		private Method m;
		private Organel org;
		
		private AnnotatedChemicalHandler(Method m, Organel org) {
			super();
			this.m = m;
			this.org = org;
		}

		@Override
		public void handle(Chemical chemical, Organel sender,
				Organel receiver, Runnable callback) {
			int size = m.getParameterTypes().length;
			Object args[] = null;
			switch (size) {
			case 1:
				args = new Object[]{ chemical };
				break;
			case 2:
				args = new Object[]{ chemical, callback };
				break;
			case 3:
				args = new Object[]{ chemical, sender, callback };
				break;
			case 4:
				args = new Object[]{ chemical, sender, receiver, callback };
				break;
			default:
				break;
			}
			try {
				m.invoke(org, args);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void handleChemicalAnnotationsOnMethod(final Method m, final Organel org, Plasma plasma) {
		Chem on = m.getAnnotation(Chem.class);
		if (on != null) {
			Object pattern = on.value();
			if (pattern.equals("[type]")) {
				pattern = on.type();
			}
			ChemicalHandler handler = new AnnotatedChemicalHandler(m, org);
			if (on.once()) {
				plasma.once(pattern, handler, org);
			} else {
				plasma.on(pattern, handler, org);
			}
		}
	}
	
}
