package log430;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.eventbus.Subscribe;

import edu.gordon.atm.event.DisplayEvent;
import edu.gordon.atm.event.PromptMenuChoiceEvent;
import edu.gordon.banking.Balances;
import edu.gordon.banking.Message;
import edu.gordon.banking.Money;
import edu.gordon.banking.Status;

public class DepositTest extends AbstractTest {	
	
	int menuChoiceSequence = 0;
	
	@Test
	public synchronized void testDeposit() throws InterruptedException {
	
		atm.acceptEnvelope(true);			
		atm.insertCard(1);
		atm.setInitialCash(new Money(40));
		atm.setPIN("42");
		atm.setAmount("3000");
		
		startThread();
		
		// catch the first ATM event to turn it on
		catchEvent(DisplayEvent.class, (evt)->{
			
			simpleWait(WAIT_FOR_ATM);
			atm.switchOn();
					
		});
		
		// catch the second ATM DisplayEvent to start the session
		catchEvent(DisplayEvent.class, (evt)->{
			simpleWait(WAIT_FOR_ATM);
			atm.startSession();
		});

		// Catch MockNetWorkEvent to validate Initiate Deposit
		catchEvent(MockNetworkEvent.class, (evt)->{
			simpleWait(100);
			Message message = ((MockNetworkEvent) evt).getMessage();
			Balances balances = ((MockNetworkEvent) evt).getBalances();
			Status status = ((MockNetworkEvent) evt).getStatus();

			assertEquals(false, status.isInvalidPIN());
			assertEquals(true, status.isSuccess());
			
			assertEquals(1, message.getCard().getNumber());
			assertEquals(42, message.getPIN());
			assertEquals(Message.INITIATE_DEPOSIT, message.getMessageCode());
		});
		
		// Catch MockNetWorkEvent to validate Complete Deposit
		catchEvent(MockNetworkEvent.class, (evt)->{
			
			Message message = ((MockNetworkEvent) evt).getMessage();
			Balances balances = ((MockNetworkEvent) evt).getBalances();
			Status status = ((MockNetworkEvent) evt).getStatus();
			
			assertEquals(Message.COMPLETE_DEPOSIT, message.getMessageCode());
			
			Money totalExpected = new Money(130);
			Money availableExpected = new Money(100);
			assertEquals(totalExpected, balances.getTotal());
			assertEquals(availableExpected, balances.getAvailable());
		});
	}

	@Subscribe
	public void interceptRequestMenu(PromptMenuChoiceEvent evt){
		switch(menuChoiceSequence){
		case 0:
			// Select Deposit
			atm.setMenuChoice("2");
			menuChoiceSequence++;
			break;
		case 1:
			// Select Saving
			atm.setMenuChoice("1");
			menuChoiceSequence++;
			break;
		case 2:
			// Select NO
			atm.setMenuChoice("2");
			menuChoiceSequence++;
			break;
		}
	}
}
