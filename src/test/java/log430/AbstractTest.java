package log430;

import static edu.gordon.simulation.GUIQuery.clickButton;

import org.junit.After;
import org.junit.Before;

import edu.gordon.atm.ATM;
import edu.gordon.banking.Balances;
import edu.gordon.banking.Message;
import edu.gordon.banking.Status;
import edu.gordon.simulation.SimulatedBank;
import edu.gordon.simulation.Simulation;

public abstract class AbstractTest {
	protected static final int WAIT_FOR_BANK = 4000;
	protected static final int WAIT_FOR_RECEIPT = 10000;
	protected static final int WAIT_FOR_CARD_ANIMATION = 3000;
	protected Message message;
	protected Status status;
	protected Balances balances;
	protected Thread thread;
	protected ATM atm;
	
	@Before
	public void setup() throws InterruptedException{
		atm = new ATM(42, "Gordon College", "First National Bank of Podunk", null);
		Simulation simulation = new Simulation(atm, new SimulatedBank(){
			@Override
			public Status handleMessage(Message msg, Balances bal) {
				// Intercept transaction message, balances and status for validation
				message = msg;
				balances = bal;
				status = super.handleMessage(msg, bal);
				return status;
			}
		});
		thread = new Thread(atm);
		thread.start();
		// TODO - No magic number
		Thread.sleep(1000);
	}
	
	@After
	public void after() throws InterruptedException {
		Thread.sleep(WAIT_FOR_RECEIPT);
		// Terminate transaction
		clickButton("2");
		Thread.sleep(WAIT_FOR_CARD_ANIMATION);
		// Close the ATM
		clickButton("onBtn");
		atm.killThread();
		thread.interrupt();
		Thread.sleep(10);	
		thread.join(0);
	}
}
