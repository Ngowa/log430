package edu.gordon.atm;

import java.util.ArrayList;

public abstract class ATMObservable {
	protected ArrayList<ATMObserver> observers = new ArrayList<ATMObserver>();
	
	public void addObserver(ATMObserver observer){
		observers.add(observer);
	}
	
	protected abstract void notifyObservers();
}
