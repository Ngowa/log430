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
		logger.info("press on");
		clickButton("onBtn");
		
		logger.info("set bill number to 20");
		setTextField("billsNumber", "20");
		
		logger.info("insert card #1");
		clickButton("insertCardBtn");
		setTextField("cardNumber", "1");
		
		logger.info("enter the pin 42");
		clickButton("4");
		clickButton("2");
		clickButton("enterKey");
		
		logger.info("select deposit");
		clickButton("2");
		
		logger.info("select checking");
		clickButton("1");
		
		logger.info("select deposit amount of 20$");
		clickButton("2");
		clickButton("0");
		clickButton("0");
		clickButton("0");
		clickButton("enterKey");
		
		// Wait for transaction info
		// Check the information selected beforehand
		Thread.sleep(WAIT_FOR_BANK);
		// Insert envolope containning the money
		logger.info("insert envelope");
		clickButton("insertEnvelopeBtn");
		
		assertEquals(false, status.isInvalidPIN());
		assertEquals(true, status.isSuccess());
		assertEquals(1, message.getCard().getNumber());
		assertEquals(42, message.getPIN());
		assertEquals(Message.INITIATE_DEPOSIT, message.getMessageCode());		
		
		// Wait for transaction info
		Thread.sleep(WAIT_FOR_BANK);
		assertEquals(Message.COMPLETE_DEPOSIT, message.getMessageCode());
		
		Money totalExpected = new Money(120);
		Money availableExpected = new Money(100);
		assertEquals(totalExpected, balances.getTotal());
		assertEquals(availableExpected, balances.getAvailable());		
	}

}
