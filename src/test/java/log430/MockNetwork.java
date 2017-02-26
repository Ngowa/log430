package log430;

import java.net.InetAddress;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import edu.gordon.atm.physical.Log;
import edu.gordon.atm.physical.NetworkToBank;
import edu.gordon.atm.physical.SimulatedBank;
import edu.gordon.banking.Balances;
import edu.gordon.banking.Message;
import edu.gordon.banking.Status;

public class MockNetwork extends NetworkToBank{
	private EventBus eventBus;
	
	@Inject
	public MockNetwork(Log log, InetAddress bankAddress, SimulatedBank simulatedBank, EventBus eventBus) {
		super(log, bankAddress, simulatedBank);
		this.eventBus = eventBus;
	}
	
	@Override
	public Status sendMessage(Message message, Balances balances) {
		Status status = super.sendMessage(message, balances);
		eventBus.post(new MockNetworkEvent(message, balances, status));
		return status;
	}
}
