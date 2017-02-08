package log430;

import static edu.gordon.simulation.GUIQuery.clickButton;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import edu.gordon.atm.ATMProcess;
import edu.gordon.atm.event.AcceptEnvelopeEvent;
import edu.gordon.atm.event.DispenseCashEvent;
import edu.gordon.atm.event.DisplayEvent;
import edu.gordon.atm.event.EjectCardEvent;
import edu.gordon.atm.event.InitialCashEvent;
import edu.gordon.atm.event.LogEvent;
import edu.gordon.atm.event.PrintReceiptLineEvent;
import edu.gordon.atm.event.PromptMenuChoiceEvent;
import edu.gordon.atm.event.PromptReadPinEvent;
import edu.gordon.atm.event.InsertCardEvent;
import edu.gordon.atm.event.RequestAmountEvent;
import edu.gordon.atm.physical.SimulatedBank;
import edu.gordon.banking.Balances;
import edu.gordon.banking.Message;
import edu.gordon.banking.Status;
import edu.gordon.simulation.Simulation;

public abstract class AbstractTest {
	protected static final Logger logger = LogManager.getLogger("User");
	
	protected static final int WAIT_TO_START = 1000;
	protected static final int WAIT_FOR_BANK = 4000;
	protected static final int WAIT_FOR_RECEIPT = 10000;
	protected static final int WAIT_FOR_CARD_ANIMATION = 3000;
	protected static final int WAIT_FOR_CASH_DISPENSER_ANIMATION = 5000;
	
	protected Message message;
	protected Status status;
	protected Balances balances;
	
	private Thread thread;
	private ATMProcess atm;
	
	@Before
	public void setup() throws InterruptedException, IOException{
		Injector injector = Guice.createInjector(new TestModule());
    	
        atm = injector.getInstance(ATMProcess.class);
        EventBus eventBus = injector.getInstance(EventBus.class);
        Simulation simulation = injector.getInstance(Simulation.class);
        
        eventBus.register(simulation);
        eventBus.register(new EventInterceptor());
		
		thread = new Thread(atm);
		thread.start();

		Thread.sleep(WAIT_TO_START);
	}
	
	@After
	public void after() throws InterruptedException {
		Thread.sleep(WAIT_FOR_RECEIPT);
		// Terminate transaction
		clickButton("2");
		Thread.sleep(WAIT_FOR_CARD_ANIMATION);
		// Close the ATM
		clickButton("onBtn");
		// Kill the thread
		atm.killThread();
		thread.interrupt();
		Thread.sleep(10);	
		thread.join(0);
	}
	
	class MockBank extends SimulatedBank{
		@Override
		public Status handleMessage(Message msg, Balances bal) {
			// Intercept transaction message, balances and status for validation
			message = msg;
			balances = bal;
			status = super.handleMessage(msg, bal);
			return status;
		}
	}
	
	class TestModule extends AbstractModule {
		@Override
		protected void configure() {
			bind(EventBus.class).in(Singleton.class);
			bind(SimulatedBank.class).toInstance(new MockBank());;
		}
		
	}
	
	class EventInterceptor {
		private final Logger logger = LogManager.getLogger("*EVENT*");
		@Subscribe 
		public void recordInsertCard(InsertCardEvent evt){
			logger.info("insert card");
		}
		
		@Subscribe 
		public void recordLogMessage(LogEvent evt){
			logger.info("log message: " + evt.getMessage());
		}
		
		@Subscribe 
		public void recordEjectCard(EjectCardEvent evt){
			logger.info("eject card");
		}
		
		@Subscribe 
		public void recordCashDispencer(DispenseCashEvent evt){
			logger.info("cash dispense, amount of " + evt.getAmount());	
		}
		
		@Subscribe 
		public void recordInsertEnvelope(AcceptEnvelopeEvent evt){
			logger.info("insert envelope");
		}
		
		@Subscribe
		public void recordInitialCash(InitialCashEvent evt){
			logger.info("initial cash");
		}
		
		@Subscribe
		public void recordPrintReceiptLine(PrintReceiptLineEvent evt){
			logger.info("print receipt line: " + evt.getReceiptLine());
		}
		
		@Subscribe
		public void recordDisplayToConsole(DisplayEvent evt){
			logger.info("to console: " + evt.getMessage());
		}
		
		@Subscribe
		public void recordRequestPIN(PromptReadPinEvent evt){
			logger.info("prompt pin: " + evt.getPrompt());
		}
		
		@Subscribe
		public void recordRequestMenu(PromptMenuChoiceEvent evt){
			StringBuilder builder = new StringBuilder();
			for(String menu : evt.getMenu()){
				builder.append(" -");
				builder.append(menu);
			}
			logger.info("prompt menu: " + evt.getPrompt() + ", options: " + builder.toString());
		}
		
		@Subscribe
		public void recordRequestAmount(RequestAmountEvent evt){
			logger.info("request amount");
		}
	}
}
