package log430;

import static edu.gordon.simulation.GUIQuery.clickButton;
import static edu.gordon.simulation.GUIQuery.setTextField;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.gordon.banking.Message;
import edu.gordon.banking.Money;

public class InquiryTest extends AbstractTest{	
	
	@Test
	public void testRequirementInquiry() throws InterruptedException{
		clickButton("onBtn");
		setTextField("billsNumber", "20");
		clickButton("insertCardBtn");
		setTextField("cardNumber", "1");
		clickButton("4");
		clickButton("2");
		clickButton("enterKey");
		// Select inquiry
		clickButton("4");
		// Select saving
		clickButton("2");
		
		// Wait for transaction info
		// Check the information selected beforehand	
		Thread.sleep(WAIT_FOR_BANK);
		assertEquals(false, status.isInvalidPIN());
		assertEquals(true, status.isSuccess());
		assertEquals(1, message.getCard().getNumber());
		assertEquals(42, message.getPIN());
		assertEquals(Message.INQUIRY, message.getMessageCode());
		
		
		
		Money totalExpected = new Money(1000);
		Money availableExpected = new Money(1000);
		assertEquals(totalExpected, balances.getTotal());
		assertEquals(availableExpected, balances.getAvailable());

	}

}
