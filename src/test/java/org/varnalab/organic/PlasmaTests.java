package org.varnalab.organic;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
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
    public Class<Plasma> plasmaClass;
    
    private Plasma plasma;
    
    @Before
    public void setUp() throws InstantiationException, IllegalAccessException {
    	System.out.println("creating plasma");
    	plasma = plasmaClass.newInstance();
    }
    
    private Organel getFakeOrganel(Plasma plasma) {
    	return new Organel(plasma, null, null){};
    }

    public PlasmaTests(Class<Plasma> plasmaClass) {
		super();
		this.plasmaClass = plasmaClass;
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
	
	private static final String PATTERN = "pattern_1";
	
	@Test
	public void testEmitSingleOn() throws Exception {
		final Organel _receiver = getFakeOrganel(plasma);
		final Organel _sender = getFakeOrganel(plasma);
		final Chemical _chemical = new SimpleChemical<Object>(PATTERN, null);
		
		final AsyncTest async = new AsyncTest(); 
		
		plasma.on(PATTERN, new ChemicalHandler() {
			@Override
			public boolean handle(Chemical chemical, Organel sender, Runnable callback) {
				assertNull(callback);
				assertSame(_chemical, chemical);
				assertSame(_sender, sender);
				
				async.done();
				return true; //finish walking
			}
		}, _receiver);

		plasma.emit(_chemical, null, _sender);
	
		async.awaitAsync();
	}
	
	@Test
	public void testEmitMultiOnWithStop() throws Exception {
		final Organel _receiver1 = getFakeOrganel(plasma);
		final Organel _receiver2 = getFakeOrganel(plasma);
		final Organel _sender = getFakeOrganel(plasma);
		final Chemical _chemical = new SimpleChemical<Object>(PATTERN, null);
		
		final AsyncTest async = new AsyncTest(); 
		
		plasma.on(PATTERN, new ChemicalHandler() {
			@Override
			public boolean handle(Chemical chemical, Organel sender, Runnable callback) {
				assertNull(callback);
				assertSame(_chemical, chemical);
				assertSame(_sender, sender);
				
				async.done();
				return true; //finish walking
			}
		}, _receiver1);
		
		plasma.on(PATTERN, new ChemicalHandler() {
			@Override
			public boolean handle(Chemical chemical, Organel sender, Runnable callback) {
				fail("this one should not be invoked");
				return false;
			}
		}, _receiver2);
		
		plasma.emit(_chemical, null, _sender);
	
		async.awaitAsync();
	}
	
	@Test
	public void testEmitMultiOnWithoutStop() throws Exception {
		final Organel _receiver1 = getFakeOrganel(plasma);
		final Organel _receiver2 = getFakeOrganel(plasma);
		final Organel _sender = getFakeOrganel(plasma);
		final Chemical _chemical = new SimpleChemical<Object>(PATTERN, null);
		
		final AsyncTest async = new AsyncTest(); 
		
		plasma.on(PATTERN, new ChemicalHandler() {
			@Override
			public boolean handle(Chemical chemical, Organel sender, Runnable callback) {
				assertNull(callback);
				assertSame(_chemical, chemical);
				assertSame(_sender, sender);
				
				async.done();
				return false; //finish walking
			}
		}, _receiver1);
		
		plasma.on(PATTERN, new ChemicalHandler() {
			@Override
			public boolean handle(Chemical chemical, Organel sender, Runnable callback) {
				assertNull(callback);
				assertSame(_chemical, chemical);
				assertSame(_sender, sender);
				
				async.done();
				return true;
			}
		}, _receiver2);
		
		plasma.emit(_chemical, null, _sender);
	
		async.awaitAsync();
	}
	
    @Parameterized.Parameters
    public static Collection<Object[]> instancesToTest() {
    	Collection<Object[]> result = new ArrayList<>();
        result.add(new Object[]{  PlasmaImpl.class  });
        return result;
    }
}
