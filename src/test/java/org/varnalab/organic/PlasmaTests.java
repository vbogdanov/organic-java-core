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
    
    private Organel _receiver;
    private Organel _sender;
    private Chemical _chemical;
	
    private AsyncTest async; 
    
    @Before
    public void setUp() throws InstantiationException, IllegalAccessException {
  	
    	plasma = plasmaClass.newInstance();
    	_receiver = getFakeOrganel(plasma);
    	_sender = getFakeOrganel(plasma);
    	_chemical = new SimpleChemical<Object>(PATTERN, null);
    	
    	async = new AsyncTest(); 
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
		plasma.emit(_chemical, null, null);
		
		//no exceptions were thrown
	}
	
	private static final String PATTERN = "pattern_1";
	
	@Test
	public void testEmitSingleOn() throws Exception {
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
		final Organel _receiver2 = getFakeOrganel(plasma);
		
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
		final Organel _receiver2 = getFakeOrganel(plasma);
		
		plasma.on(PATTERN, new ChemicalHandler() {
			@Override
			public boolean handle(Chemical chemical, Organel sender, Runnable callback) {
				assertNull(callback);
				assertSame(_chemical, chemical);
				assertSame(_sender, sender);
				
				async.done();
				return false; //finish walking
			}
		}, _receiver);
		
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
	
	@Test
	public void testOnTwice() throws Exception {
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
		plasma.emit(_chemical, null, _sender);
	
		async.awaitAsync();
		async.awaitAsync();
	}
	
	@Test
	public void testOnce() throws Exception {
		plasma.once(PATTERN, new ChemicalHandler() {
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
		plasma.emit(_chemical, null, _sender);
	
		async.awaitAsync();
		async.noMore();
	}
	
	
    @Parameterized.Parameters
    public static Collection<Object[]> instancesToTest() {
    	Collection<Object[]> result = new ArrayList<>();
        result.add(new Object[]{  PlasmaImpl.class  });
        //add other implementations here to test them
        return result;
    }
}
