/* * ATM Example system - file BillsPanel.java     * * copyright (c) 2001 - Russell C. Bjork * */ package edu.gordon.simulation;import java.awt.Font;import java.awt.GridLayout;import java.awt.Label;import java.awt.Panel;import java.awt.TextField;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;/** The GUI panel that simulates the reading of the ATM card's magnetic stripe *  by asking the user to enter the number */class CardPanel extends Panel{    /** Constructor     */    CardPanel()    {        setLayout(new GridLayout(0, 1, 0, 0));        setFont(new Font("Monospaced", Font.PLAIN, 14));                add(new Label("A real ATM would have a magnetic",                      Label.CENTER));        add(new Label("stripe reader to read the card",                      Label.CENTER));        add(new Label("For purposes of the edu.gordon.simulation,",                      Label.CENTER));        add(new Label("please enter the card number manually.",                      Label.CENTER));        add(new Label("Then press RETURN",                      Label.CENTER));        add(new Label("(An invalid integer or an integer not",                      Label.CENTER));        add(new Label("greater than zero will be treated as",                      Label.CENTER));        add(new Label("an unreadable card)",                      Label.CENTER));                cardNumberField = new TextField(30);        cardNumberField.addActionListener(new ActionListener() {            public void actionPerformed(ActionEvent e)            {                synchronized(CardPanel.this)                {                    CardPanel.this.notify();                }            }        });        GUIQuery.add("cardNumber", cardNumberField);        Panel cardNumberPanel = new Panel();        cardNumberPanel.add(cardNumberField);        add(cardNumberPanel);    }        /** Ask the customer to enter the number on the card     *     *  @return the number entered     */    synchronized int readCardNumber()    {        cardNumberField.setText("");                cardNumberField.requestFocus();        try        {            wait();        }        catch(InterruptedException e)        { }                        int cardNumber;        try        {            cardNumber = Integer.parseInt(cardNumberField.getText());            if (cardNumber <= 0)                cardNumber = -1;        }        catch(NumberFormatException e)        {            cardNumber = -1;        }                return cardNumber;    }    /** The field into which the card number is to be entered     */    private TextField cardNumberField;}