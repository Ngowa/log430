package log430.test;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.eventbus.Subscribe;

import edu.gordon.atm.event.ATMEvent;
import edu.gordon.atm.event.AcceptEnvelopeEvent;
import edu.gordon.atm.event.DispenseCashEvent;
import edu.gordon.atm.event.DisplayEvent;
import edu.gordon.atm.event.EjectCardEvent;
import edu.gordon.atm.event.InitialCashEvent;
import edu.gordon.atm.event.InsertCardEvent;
import edu.gordon.atm.event.LogEvent;
import edu.gordon.atm.event.PrintReceiptLineEvent;
import edu.gordon.atm.event.PromptMenuChoiceEvent;
import edu.gordon.atm.event.PromptReadPinEvent;
import edu.gordon.atm.event.RequestAmountEvent;

public class LogSubscriber {
	private final Logger logger = LogManager.getLogger("");
	
	private String name(ATMEvent evt){
		return String.format("[%s] ", evt.getClass().getSimpleName());
	}
	
	@Subscribe 
	public void recordInsertCard(InsertCardEvent evt){
		logger.info(name(evt));
	}
	
	@Subscribe 
	public void recordLogMessage(LogEvent evt){
		logger.info(name(evt) + evt.getMessage());
	}
	
	@Subscribe 
	public void recordEjectCard(EjectCardEvent evt){
		logger.info(name(evt));
	}
	
	@Subscribe 
	public void recordCashDispencer(DispenseCashEvent evt){
		logger.info(name(evt) + evt.getAmount());	
	}
	
	@Subscribe 
	public void recordInsertEnvelope(AcceptEnvelopeEvent evt){
		logger.info(name(evt));
	}
	
	@Subscribe
	public void recordInitialCash(InitialCashEvent evt){
		logger.info(name(evt));
	}
	
	@Subscribe
	public void recordPrintReceiptLine(PrintReceiptLineEvent evt){
		logger.info(name(evt) + evt.getReceiptLine());
	}
	
	@Subscribe
	public void recordDisplayToConsole(DisplayEvent evt){
		logger.info(name(evt) + evt.getMessage());
	}
	
	@Subscribe
	public void recordRequestPIN(PromptReadPinEvent evt){
		logger.info(name(evt) + evt.getPrompt());
	}
	
	@Subscribe
	public void recordRequestMenu(PromptMenuChoiceEvent evt){
		StringBuilder builder = new StringBuilder();
		for(String menu : evt.getMenu()){
			builder.append(" -");
			builder.append(menu);
		}
		logger.info(name(evt) + evt.getPrompt() + ", options: " + builder.toString());
	}
	
	@Subscribe
	public void recordRequestAmount(RequestAmountEvent evt){
		logger.info(name(evt));
	}
}
