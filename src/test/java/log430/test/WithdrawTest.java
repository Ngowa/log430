package log430.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.gordon.atm.event.DispenseCashEvent;
import edu.gordon.atm.event.DisplayEvent;
import edu.gordon.atm.event.InitialCashEvent;
import edu.gordon.atm.event.InsertCardEvent;
import edu.gordon.atm.event.PromptMenuChoiceEvent;
import edu.gordon.atm.event.PromptReadPinEvent;
import edu.gordon.banking.Balances;
import edu.gordon.banking.Message;
import edu.gordon.banking.Money;
import edu.gordon.banking.Status;

public class WithdrawTest extends AbstractTest {	
	@Test
	public synchronized void testWithdraw() throws InterruptedException {
		// catch the first ATM event to turn it on
		catchEvent(DisplayEvent.class, (evt)->{
			atm.switchOn();
		});
		
		sync(InitialCashEvent.class, ()->{
			atm.setInitialCash(new Money(40));
		});
		
		// catch the first atm message to start a session
		catchEvent(DisplayEvent.class, (evt)->{
			atm.startSession();
		});
		
		sync(InsertCardEvent.class, ()->{
			atm.insertCard(1);
		});
		
		sync(PromptReadPinEvent.class, ()->{
			atm.setPIN("42");
		});
		
		// Select withdraw
		sync(PromptMenuChoiceEvent.class, ()->{
			atm.setMenuChoice("1");
		});
		
		// Select checking
		sync(PromptMenuChoiceEvent.class, ()->{
			atm.setMenuChoice("1");
		});
		
		// Select 20$
		sync(PromptMenuChoiceEvent.class, ()->{
			atm.setMenuChoice("1");
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
			
			Money left = new Money(80);
			assertEquals(left, balances.getTotal());
			assertEquals(left, balances.getAvailable());
		});
		
		catchEvent(DispenseCashEvent.class, (evt)->{
			Money dispensed = ((DispenseCashEvent)evt).getAmount();
			Money anticipated = new Money(20);
			assertEquals(anticipated, dispensed);
		});
	}
}
