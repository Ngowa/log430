package log430;

import java.io.IOException;
import java.util.function.Consumer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.gordon.atm.ATMProcess;
import edu.gordon.atm.event.ATMEvent;

public class AbstractTest {
	protected static final Logger logger = LogManager.getLogger("User");
	
	protected static final int WAIT_FOR_ATM = 100; 
	protected Thread thread;
	private EventBus eventBus;
	
	private boolean waiting;
	private Class<? extends ATMEvent> eventClass;
	private ATMEvent evt;
	
	protected ATMProcess atm;
	
	@Before
	public void setup() throws InterruptedException, IOException{
		Injector injector = Guice.createInjector(new TestModule());
    	
        atm = injector.getInstance(ATMProcess.class);
        eventBus = injector.getInstance(EventBus.class);
        eventBus.register(new LogSubscriber());
        eventBus.register(this);
		
	}
	protected void startThread(){
		simpleWait(WAIT_FOR_ATM);
		thread = new Thread(atm);
		thread.start();
	}

	@After
	public void after() throws InterruptedException {
		atm.switchOff();
		atm.killThread();
		
		thread.interrupt();
		Thread.sleep(10);	
		thread.join(0);
	}
	protected synchronized void catchEvent(Class<? extends ATMEvent> eventClass, Consumer<ATMEvent> consumer){
		waitFor(eventClass);
		consumer.accept(evt);
		evt = null;
	}
	
	@SuppressWarnings("deprecation")
	protected synchronized void sync(Class<? extends ATMEvent> eventClass, Runnable runnable){
		runnable.run();
		waitFor(eventClass);
	}
	
	private synchronized void waitFor(Class<? extends ATMEvent> eventClass){
		this.eventClass = eventClass;
		this.waiting = true;
		
		while(waiting){
			try {
				wait(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
				
			}
		}
	}
	
	protected void simpleWait(int sleepTime){
		try {
			wait(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Subscribe
	public synchronized void onEvent(ATMEvent evt) throws InterruptedException {
	
		if(evt.getClass() == eventClass){
			
			this.waiting = false;
			this.evt = evt;
			eventClass = null;
				
		}
		notify();
	}
}
