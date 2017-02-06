package edu.gordon.atm.physical;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ATM {
	// Instance variables recording information about the ATM
	private int id;
	private String place;
	private String bankName;

	// Instance variables referring to the omponent parts of the ATM
	private boolean switchOn; 
	private CardReader cardReader;
	private CashDispenser cashDispenser;
	private CustomerConsole customerConsole;
	private EnvelopeAcceptor envelopeAcceptor;
	private NetworkToBank networkToBank;
	private OperatorPanel operatorPanel;
	private ReceiptPrinter receiptPrinter;
	
	@Inject
	public ATM(final CardReader cardReader, final CashDispenser cashDispenser,
			final CustomerConsole customerConsole, final EnvelopeAcceptor envelopeAcceptor,
			final NetworkToBank networkToBank, final OperatorPanel operatorPanel, final ReceiptPrinter receiptPrinter)
			throws IOException {
		// Load bank config file
		Properties properties = new Properties();
		InputStream input = new FileInputStream("bank.properties");
		properties.load(input);
		id = Integer.parseInt(properties.getProperty("bank.id"));
		place = properties.getProperty("bank.place");
		bankName = properties.getProperty("bank.name");

		// Inject objects corresponding to component parts
		this.cardReader = cardReader;
		this.cashDispenser = cashDispenser;
		this.customerConsole = customerConsole;
		this.envelopeAcceptor = envelopeAcceptor;
		this.networkToBank = networkToBank;
		this.operatorPanel = operatorPanel;
		this.receiptPrinter = receiptPrinter;
	}

	public void switchOn() {
		switchOn = true;
	}

	public void switchOff() {
		switchOn = false;
	}
	
	public boolean isSwitchOn() {
		return switchOn;
	}

	public int getId() {
		return id;
	}

	public String getPlace() {
		return place;
	}

	public String getBankName() {
		return bankName;
	}

	public CardReader getCardReader() {
		return cardReader;
	}

	public CashDispenser getCashDispenser() {
		return cashDispenser;
	}

	public CustomerConsole getCustomerConsole() {
		return customerConsole;
	}

	public EnvelopeAcceptor getEnvelopeAcceptor() {
		return envelopeAcceptor;
	}

	public NetworkToBank getNetworkToBank() {
		return networkToBank;
	}

	public OperatorPanel getOperatorPanel() {
		return operatorPanel;
	}

	public ReceiptPrinter getReceiptPrinter() {
		return receiptPrinter;
	}
}
