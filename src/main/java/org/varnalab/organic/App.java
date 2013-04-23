package org.varnalab.organic;

import java.io.IOException;
import java.nio.file.Paths;

import org.varnalab.organic.api.Cell;
import org.varnalab.organic.api.DNA;
import org.varnalab.organic.impl.DNAImpl;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws JsonParseException, IOException
    {
        System.out.println( "Hello World!" );
        final DNA dna = new DNAImpl();
        
        dna.loadDir(Paths.get(args[0]), "", new Runnable() {
			@Override
			public void run() {
				Cell cell = new Cell(dna, null);
			}
		});
        
    }
}
