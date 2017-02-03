/* * ATM Example system - file ReceiptPrinter.java * * copyright (c) 2001 - Russell C. Bjork * */package edu.gordon.atm.physical;import java.util.Enumeration;import com.google.common.eventbus.EventBus;import com.google.inject.Inject;import edu.gordon.atm.event.PrintReceiptLineEvent;import edu.gordon.banking.Receipt;/** * Manager for the ATM's receipt printer. In a real ATM, this would manage a * physical device; in this edu.gordon.simulation, it uses classes in package * edu.gordon.simulation to simulate the device. */public class ReceiptPrinter {	private final EventBus eventBus;	/**	 * Constructor	 */	@Inject	public ReceiptPrinter(final EventBus eventBus) {		this.eventBus = eventBus;	}	/**	 * Print a receipt	 *	 * @param receipt	 *            object containing the information to be printed	 */	public void printReceipt(Receipt receipt) {		Enumeration<String> receiptLines = receipt.getLines();		// Animate the printing of the receipt		while (receiptLines.hasMoreElements()) {			eventBus.post(new PrintReceiptLineEvent(receiptLines.nextElement()));		}	}}