package org.varnalab.organic;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.varnalab.organic.impl.DNA;

import com.fasterxml.jackson.core.JsonParseException;

public class DNATestCase {

	@Test
	public void testLoadDir() throws JsonParseException, IOException {
		final DNA dna = new DNA();
		final AsyncTest async = new AsyncTest();
		dna.loadDir(Paths.get("src/test/resources"), "", new Runnable() {
			
			@Override
			public void run() {
				System.out.println("good job");
				Map<String, Object> expected = new HashMap<>();
				expected.put("superb", 7000);
				assertEquals(expected, dna.selectBranch("example.item1"));
				async.done();
			}
			
		});
		async.awaitAsync();
	}

}
