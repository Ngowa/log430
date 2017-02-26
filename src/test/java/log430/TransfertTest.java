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

public class TransfertTest extends AbstractTest {

	int menuChoiceSequence = 0;
	
	@Test
	public synchronized void testTransfert() throws InterruptedException {
	
		atm.acceptEnvelope(true);			
		atm.insertCard(1);
		atm.setInitialCash(new Money(40));
		atm.setPIN("42");
		atm.setAmount("10000");
		
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
		
		// Catch MockNetWorkEvent to validate results of the target account
		catchEvent(MockNetworkEvent.class, (evt)->{
			
			Message message = ((MockNetworkEvent) evt).getMessage();
			Balances balances = ((MockNetworkEvent) evt).getBalances();
			Status status = ((MockNetworkEvent) evt).getStatus();
			
			assertEquals(false, status.isInvalidPIN());
			assertEquals(true, status.isSuccess());
			assertEquals(1, message.getCard().getNumber());
			assertEquals(42, message.getPIN());
			assertEquals(Message.TRANSFER, message.getMessageCode());
			
			
			
			Money totalExpected = new Money(200);
			Money availableExpected = new Money(200);
			assertEquals(totalExpected, balances.getTotal());
			assertEquals(availableExpected, balances.getAvailable());
		});
		
		// Catch MockNetWorkEvent to validate results of the souce account 
		catchEvent(MockNetworkEvent.class, (evt)->{
			
			Message message = ((MockNetworkEvent) evt).getMessage();
			Balances balances = ((MockNetworkEvent) evt).getBalances();
			Status status = ((MockNetworkEvent) evt).getStatus();
			
			assertEquals(false, status.isInvalidPIN());
			assertEquals(true, status.isSuccess());
			assertEquals(1, message.getCard().getNumber());
			assertEquals(42, message.getPIN());
			assertEquals(Message.INQUIRY, message.getMessageCode());
			
			
			
			Money totalExpected = new Money(900);
			Money availableExpected = new Money(900);
			assertEquals(totalExpected, balances.getTotal());
			assertEquals(availableExpected, balances.getAvailable());
		});
	}

	@Subscribe
	public void interceptRequestMenu(PromptMenuChoiceEvent evt){
		switch(menuChoiceSequence){
		case 0:
			// Select Transfert
			atm.setMenuChoice("3");
			menuChoiceSequence++;
			break;
		case 1:
			// Select Saving
			atm.setMenuChoice("2");
			menuChoiceSequence++;
			break;
		case 2:
			// Select Checking
			atm.setMenuChoice("1");
			menuChoiceSequence++;
			break;
		case 3:
			// Select YES
			atm.setMenuChoice("1");
			menuChoiceSequence++;
			break;
		case 4:
			// Select Inquiry
			atm.setMenuChoice("4");
			menuChoiceSequence++;
			break;
		case 5:
			// Select Saving
			atm.setMenuChoice("2");
			menuChoiceSequence++;
			break;
		case 6:
			// Select NO
			atm.setMenuChoice("2");
			menuChoiceSequence++;
			break;
		}
	}
}
