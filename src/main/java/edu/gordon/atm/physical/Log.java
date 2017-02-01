/* * ATM Example system - file Log.java * * copyright (c) 2001 - Russell C. Bjork * */package edu.gordon.atm.physical;import com.google.common.eventbus.EventBus;import edu.gordon.atm.event.LogEvent;import edu.gordon.banking.Message;import edu.gordon.banking.Money;import edu.gordon.banking.Status;/** * Manager for the ATM's internal log. In a real ATM, this would manage a * physical device; in this edu.gordon.simulation, it uses classes in package * edu.gordon.simulation to simulate the device. */public class Log {	private final EventBus eventBus;		public Log(final EventBus eventBus){		this.eventBus = eventBus;	}		/**	 * Log the sending of a message to the bank	 *	 * @param message	 *            the message to be logged	 */	public void logSend(Message message) {		eventBus.post(new LogEvent("Message:   " + message.toString()));	}	/**	 * Log a response received from a message	 *	 * @param status	 *            the status object returned by the bank in response	 */	public void logResponse(Status response) {		eventBus.post(new LogEvent("Response:  " + response.toString()));	}	/**	 * Log the dispensing of cash by the cash dispenser	 *	 * @param amount	 *            the amount of cash being dispensed	 */	public void logCashDispensed(Money amount) {		eventBus.post(new LogEvent("Dispensed: " + amount.toString()));	}	/**	 * Log accepting an envelope. This method is only called if an envelope is	 * actually received from the customer	 */	public void logEnvelopeAccepted() {		eventBus.post(new LogEvent("Envelope:  received"));	}}