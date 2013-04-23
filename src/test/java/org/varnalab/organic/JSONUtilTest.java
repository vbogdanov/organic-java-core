package org.varnalab.organic;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtilTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testMerge() throws IOException {
		String json1 = "{ \"hello\": \"world\", \"test\": 5, \"name\": { \"first\": \"Valeri\", \"surname\": \"Bogdanov\" }}";
		String json2 = "{ \"hello\": \"valeri\", \"test\": 5, \"name\": { \"second\": \"Ivanov\" }}";
		String jsonExpected = "{ \"hello\": \"valeri\", \"test\": 5, \"name\": { \"first\": \"Valeri\", \"surname\": \"Bogdanov\", \"second\": \"Ivanov\" }}";
		
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonMap1 = mapper.readValue(json1, Map.class);
		Map<String, Object> jsonMap2 = mapper.readValue(json2, Map.class);
		Map<String, Object> expected = mapper.readValue(jsonExpected, Map.class);
		
		Map<String, Object> actual = JSONUtil.merge(jsonMap2, jsonMap1);
		assertEquals(expected, actual);
	}

}
