package edu.gordon.atm;

public interface ATMObserver {
	void onIdle(ATM atm);
	void onOff(ATM atm);
	void onReadingCard(Session session);
	void onReadingPin(Session session);
	void onChoosingTransaction(Session session);
	void onPerformingTransaction(Session session);
	void onEjectingCard(Session session);
}
