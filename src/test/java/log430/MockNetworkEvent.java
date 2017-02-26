package log430;

import edu.gordon.atm.event.ATMEvent;
import edu.gordon.banking.Balances;
import edu.gordon.banking.Message;
import edu.gordon.banking.Status;

public class MockNetworkEvent implements ATMEvent {
	private Message message;
	private Balances balances;
	private Status status;
	
	public MockNetworkEvent(Message message, Balances balances, Status status){
		this.message = message;
		this.balances = balances;
		this.status = status;
	}
	
	public Message getMessage() {
		return message;
	}
	public Balances getBalances() {
		return balances;
	}
	public Status getStatus() {
		return status;
	}
}
