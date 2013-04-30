package org.varnalab.organic.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.varnalab.organic.JSONUtil;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class represents an information about the cell to build, stored in JSON-like structure. This class
 * presents method for operating on such structures, including loading
 * additional info from the file system or fetching a subset of it.
 */
public class DNA {
	private Map<String, Object> data = new HashMap<>();
	private ObjectMapper mapper = new ObjectMapper();
	
	public DNA() {
	}
	
	
	public DNA(Map<String, Object> data) {
		super();
		this.data = data;
	}

	public Map<String, Object> getData() {
		return data;
	}
	
	public void loadDir(Path dir, String namespace, Runnable successCallback) throws JsonParseException, IOException {
		//get the current value
		Map<String, Object> current = selectBranch(namespace, true); 
		
		//merge new data with existing data
		current = JSONUtil.merge(parseDir(dir), current);
		
		if (namespace == "") {
			data = current;
		} else {
			int index = namespace.lastIndexOf('.');
			String parentNS = namespace.substring(0, index);
			String key = namespace.substring(index);
			selectBranch(parentNS, true).put(key, current);
		}
		
		successCallback.run();
	}
	
	
	private Map<String, Object> parseDir(Path dir) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = new HashMap<>();
		
		for (Path path: Files.newDirectoryStream(dir)) {
			if (Files.isSymbolicLink(path)) {
				path = Files.readSymbolicLink(path);
			}
			
			if (Files.isDirectory(path)) {
				result.put(path.getFileName().toString(), parseDir(path));
			} else if (Files.isRegularFile(path)) {
				String name = path.getFileName().toString();
				name = name.substring(0, name.length() - 5);
				result.put(name, parse(path));
			} 
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> parse(Path file) throws JsonParseException, JsonMappingException, IOException {
		if (file == null || (! file.toString().endsWith(".json"))) {
			String problem = (file != null)? file.toString(): "null";
			throw new IllegalArgumentException(problem);
		}
		return mapper.readValue(
				new String(
						Files.readAllBytes(file)
						, Charset.forName("utf-8")
				), Map.class);
	}


	public Map<String, Object> selectBranch(String namespace) {
		return selectBranch(namespace, false);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> selectBranch(String namespace, boolean fillData) {
		String[] keys = namespace.split("\\.");
		Map<String, Object> current = data;
		for (String key: keys) {
			Object data = current.get(key);
			if (data instanceof Map) {
				current = (Map<String, Object>) data;
			} else {
				Map<String, Object> value = new HashMap<>();
				if (fillData)
					current.put(key, value);
				current = value;
			}
		}
		return current;
	}
	
}                    