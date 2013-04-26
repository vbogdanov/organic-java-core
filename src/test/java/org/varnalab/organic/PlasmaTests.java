package org.varnalab.organic;

import static org.junit.Assert.*;

import java.lang.invoke.VolatileCallSite;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.varnalab.organic.api.Chemical;
import org.varnalab.organic.api.ChemicalHandler;
import org.varnalab.organic.api.Organel;
import org.varnalab.organic.api.Plasma;
import org.varnalab.organic.impl.PlasmaImpl;
import org.varnalab.organic.impl.SimpleChemical;

@RunWith(Parameterized.class)
public class PlasmaTests {
    public Plasma plasma;
    private Organel getFakeOrganel(Plasma plasma) {
    	return new Organel(plasma, null, null){};
    }

    public PlasmaTests(Plasma plasma) {
		super();
		this.plasma = plasma;
	}

	@Test
    public final void testParameter() {
        assertTrue(plasma != null);
    }

	@Test
	public void testEmitWithNoListener() throws Exception {
		Chemical chem = new SimpleChemical<Object>("NO ONE LISTENS FOR THIS", null);
		plasma.emit(chem, null, null);
		
		//no exceptions were thrown
	}
	
	private static final String PATTERN_1 = "pattern_1";
	@Test
	public void testEmitSingleOn() throws Exception {
		final Organel _receiver = getFakeOrganel(plasma);
		final Organel _sender = getFakeOrganel(plasma);
		final Chemical _chemical = new SimpleChemical<Object>(PATTERN_1, null);
		
		final Semaphore callRequired = new Semaphore(0); 
		
		plasma.on(PATTERN_1, new ChemicalHandler() {
			@Override
			public boolean handle(Chemical chemical, Organel sender, Runnable callback) {
				assertNull(callback);
				assertSame(_chemical, chemical);
				assertSame(_sender, sender);
				
				System.out.println("testEmitSingleOn - handler invoked");
				
				callRequired.release();
				return true; //finish walking
			}
		}, _receiver);

		plasma.emit(_chemical, null, _sender);
		
		if (! callRequired.tryAcquire(15, TimeUnit.SECONDS)) {
			fail("Timed out waiting for the handler to be invoked");
		}
	}
	
    @Parameterized.Parameters
    public static Collection<Object[]> instancesToTest() {
    	Collection<Object[]> result = new ArrayList<>();
        result.add(new Object[]{  new PlasmaImpl()  });
        return result;
    }
}
