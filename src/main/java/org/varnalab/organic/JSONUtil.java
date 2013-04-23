package org.varnalab.organic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

public class JSONUtil {

	@SafeVarargs
	public static Map<String, Object> merge(Map<String, Object>... jsonStructs) {
		@SuppressWarnings("unchecked")
		Map<String, Object>[] jsons = Arrays.copyOf(jsonStructs, jsonStructs.length, jsonStructs.getClass());
		
		if (jsons.length == 0)
			throw new IllegalArgumentException();
		
		Map<String,Object> result = new HashMap<String, Object>();
		ArrayUtils.reverse(jsons);
		
		for (Map<String, Object> obj: jsons) {
			deepCopy(obj, result);
		}
		return result;
	}

	
	private static void deepCopy(Map<String, Object> obj, Map<String, Object> result) {
		for (String key: obj.keySet()) {
			Object value = obj.get(key);
			Object defaultValue = result.get(key);
			value = deepCopyValue(value, defaultValue);	
			result.put(key, value);
		}
	}

	/**
	 * replace the list in target with deep copy of the list in source
	 * @param source
	 * @param target
	 */
	private static void deepCopy(List<Object> source, List<Object> target) {
		target.clear();
		for (Object value: source) {
			target.add(deepCopyValue(value, null));
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Object deepCopyValue(Object value, Object defaultValue) {
		if (value instanceof Map) {
			Map<String, Object> source = (Map<String, Object>) value;
			Map<String, Object> target = new HashMap<>();
			
			if (defaultValue != null && defaultValue instanceof Map) {
				target = (Map<String, Object>) defaultValue;
			}

			deepCopy(source, target);
			value = target;
		}
		
		if (value instanceof List) {
			List<Object> source = (List<Object>) value;
			List<Object> target = new ArrayList<>();
			
			if (defaultValue != null && defaultValue instanceof List) {
				target = (List<Object>) defaultValue;
			}

			deepCopy(source, target);
			value = target;
		}
		
		return value;
	}

}
