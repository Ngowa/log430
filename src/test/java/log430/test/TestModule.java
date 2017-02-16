package log430.test;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import edu.gordon.atm.physical.NetworkToBank;

public class TestModule extends AbstractModule{
	@Override
	protected void configure() {
		bind(EventBus.class).in(Singleton.class);
		bind(NetworkToBank.class).to(MockNetwork.class);
	}
}
