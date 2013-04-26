package org.varnalab.organic;

import static org.junit.Assert.*;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class AsyncTest {
	
	private Semaphore callsMade = new Semaphore(0);
	
	public void awaitAsync(long timeout, TimeUnit unit) {
		try {
			if(! callsMade.tryAcquire(timeout, unit)) {
				fail("Timeout while waiting for async.done()");
			}
		} catch (InterruptedException e) {
			fail("Thread interupted");
		}
	}
	
	public void awaitAsync() {
		awaitAsync(5, TimeUnit.SECONDS);
	}
	
	public void done() {
		callsMade.release();
	}
	
	

	public void noMore(long timeout, TimeUnit unit) {
		try {
			if(callsMade.tryAcquire(timeout, unit)) {
				fail("async.done() invoked");
			}
		} catch (InterruptedException e) {
			fail("Thread interupted");
		}
	}

	public void noMore() {
		noMore(5, TimeUnit.SECONDS);
	}
}
