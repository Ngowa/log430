package edu.gordon.atm.event;

public class PrintReceiptLineEvent implements ATMEvent {
	private String receiptLine;
	
	public PrintReceiptLineEvent(String receiptLine){
		this.receiptLine = receiptLine;
	}
	
	public String  getReceiptLine(){
		return receiptLine;
	}
}
