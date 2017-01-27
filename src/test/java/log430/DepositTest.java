package log430;

import static edu.gordon.simulation.GUIQuery.clickButton;
import static edu.gordon.simulation.GUIQuery.setTextField;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.gordon.banking.Message;
import edu.gordon.banking.Money;

public class DepositTest extends AbstractTest{
	
	@Test
	public void testRequirementDeposit() throws InterruptedException{
		clickButton("onBtn");
		setTextField("billsNumber", "20");
		clickButton("insertCardBtn");
		setTextField("cardNumber", "1");
		clickButton("4");
		clickButton("2");
		clickButton("enterKey");
		// Select deposit
		clickButton("2");
		// Select checking
		clickButton("1");
		// Select a deposit amount of 20$
		clickButton("2");
		clickButton("0");
		clickButton("0");
		clickButton("0");
		clickButton("enterKey");
		
		// Wait for transaction info
		// Check the information selected beforehand
		Thread.sleep(WAIT_FOR_BANK);
		assertEquals(false, status.isInvalidPIN());
		assertEquals(true, status.isSuccess());
		assertEquals(1, message.getCard().getNumber());
		assertEquals(42, message.getPIN());
		assertEquals(Message.INITIATE_DEPOSIT, message.getMessageCode());
		
		// Insert envolope containning the money
		clickButton("insertEnvelopeBtn");
		
		
		// Wait for transaction info
		Thread.sleep(3000);
		assertEquals(Message.COMPLETE_DEPOSIT, message.getMessageCode());
		
		
		Money totalExpected = new Money(120);
		Money availableExpected = new Money(100);
		assertEquals(totalExpected, balances.getTotal());
		assertEquals(availableExpected, balances.getAvailable());		
	}

}