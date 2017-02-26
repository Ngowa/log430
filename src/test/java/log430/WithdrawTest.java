package log430;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.eventbus.Subscribe;

import edu.gordon.atm.event.DispenseCashEvent;
import edu.gordon.atm.event.DisplayEvent;
import edu.gordon.atm.event.PromptMenuChoiceEvent;
import edu.gordon.banking.Balances;
import edu.gordon.banking.Message;
import edu.gordon.banking.Money;
import edu.gordon.banking.Status;

public class WithdrawTest extends AbstractTest {
	
	int menuChoiceSequence = 0;
	
	@Test
	public synchronized void testWithdraw() throws InterruptedException {
		
		atm.acceptEnvelope(true);			
		atm.insertCard(1);
		atm.setInitialCash(new Money(40));
		atm.setPIN("42");
		
		startThread();
		
		// catch the first ATM event to turn it on
		catchEvent(DisplayEvent.class, (evt)->{
			simpleWait(WAIT_FOR_ATM);
			atm.switchOn();
		});
		
		// catch the second ATM DisplayEvent to start the session
		catchEvent(DisplayEvent.class, (evt)->{
			// TODO - Sync problem with insert card
			simpleWait(WAIT_FOR_ATM);
			atm.startSession();
		});		
		

		// Catch MockNetWorkEvent to validate results
		catchEvent(MockNetworkEvent.class, (evt)->{
			Message message = ((MockNetworkEvent) evt).getMessage();
			Balances balances = ((MockNetworkEvent) evt).getBalances();
			Status status = ((MockNetworkEvent) evt).getStatus();
			
			assertEquals(false, status.isInvalidPIN());
			assertEquals(true, status.isSuccess());
			
			assertEquals(1, message.getCard().getNumber());
			assertEquals(42, message.getPIN());
			assertEquals(Message.WITHDRAWAL, message.getMessageCode());
			
			Money left = new Money(60);
			assertEquals(left, balances.getTotal());
			assertEquals(left, balances.getAvailable());
		});
		// Catch DispenseCashEvent to validate the Cash dispensed
		catchEvent(DispenseCashEvent.class, (evt)->{
			Money dispensed = ((DispenseCashEvent)evt).getAmount();
			Money anticipated = new Money(40);
			assertEquals(anticipated, dispensed);
		});
	}

	@Subscribe
	public void interceptRequestMenu(PromptMenuChoiceEvent evt){
		switch(menuChoiceSequence){
		case 0:
			// Select Widthdraw
			atm.setMenuChoice("1");
			menuChoiceSequence++;
			break;
		case 1:
			// Select Checking
			atm.setMenuChoice("1");
			menuChoiceSequence++;
			break;
		case 2:
			// Select 40$
			atm.setMenuChoice("2");
			menuChoiceSequence++;
			break;
		case 3:
			// Select NO
			atm.setMenuChoice("2");
			menuChoiceSequence++;
			break;
		}
	}
}
