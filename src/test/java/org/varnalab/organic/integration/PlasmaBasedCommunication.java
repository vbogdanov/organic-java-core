package org.varnalab.organic.integration;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;
import org.varnalab.organic.AsyncTest;
import org.varnalab.organic.api.Cell;
import org.varnalab.organic.impl.DNA;

import com.fasterxml.jackson.core.JsonParseException;

public class PlasmaBasedCommunication {
	
	@Test
	public void testCellWithAandB() throws JsonParseException, IOException {
		final DNA dna = new DNA();
		final AsyncTest async = new AsyncTest();
		dna.loadDir(Paths.get("src/test/resources/integration/test1"), "", new Runnable() {
			@Override
			public void run() {
				new Cell(dna, null);
				
				//simulate access from the outside:
				OrganelA remoteInstance = OrganelA.registry.get(8888);
				remoteInstance.startCommunication(new Runnable() {
					@Override
					public void run() {
						async.done();
						System.out.println("GREAT");
					}
				});
			}
		});
		
		async.awaitAsync();
	}

}
