package org.varnalab.organic.api;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;

public interface DNA {
	public void loadDir(Path dir, String namespace, Runnable successCallback)
			throws JsonParseException, IOException;
	
	public Map<String, Object> selectBranch(String namespace);
}
