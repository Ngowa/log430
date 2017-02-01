package edu.gordon.atm.event;

import edu.gordon.banking.Money;

public class DispenseCashEvent implements ATMEvent {
	private Money amount;
	
	public DispenseCashEvent(Money amount){
		this.amount = amount;
	}
	
	public Money getAmount(){
		return amount;
	}
}