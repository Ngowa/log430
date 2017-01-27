package log430;

import static edu.gordon.simulation.GUIQuery.clickButton;
import static edu.gordon.simulation.GUIQuery.setTextField;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.gordon.banking.Message;
import edu.gordon.banking.Money;

public class WithdrawTest extends AbstractTest{
	
	@Test
	public void test() throws InterruptedException {
		clickButton("onBtn");
		setTextField("billsNumber", "20"); 
		clickButton("insertCardBtn");
		setTextField("cardNumber", "1");
		clickButton("4");
		clickButton("2");
		clickButton("enterKey");
		// Select withdraw
		clickButton("1");
		// Select checking
		clickButton("1");
		// Select amount of 20$
		clickButton("1");
		// Wait for transaction info
		
		Thread.sleep(WAIT_FOR_BANK);
 		assertEquals(false, status.isInvalidPIN());
		assertEquals(true, status.isSuccess());
		
		assertEquals(1, message.getCard().getNumber());
		assertEquals(42, message.getPIN());
		assertEquals(Message.WITHDRAWAL, message.getMessageCode());
		
		
		Money left = new Money(80);
		assertEquals(left, balances.getTotal());
		assertEquals(left, balances.getAvailable());
		
		// TODO - No magic number
		Thread.sleep(5000);
	}
	
}
