package com.syagbasan.sterilveri.odemelerfragmentlistviews;

public class RowItem {
    private String person;
    private String amount;
    private String date; 
    
    public RowItem(String date,String person, String amount) {
    	this.date = date;
        this.person = person;
        this.amount = amount;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getPerson() {
        return person;
    }
    public void setPerson(String person) {
        this.person = person;
    }
    @Override
    public String toString() {
    	return person;
        //return person + "\n" + amount;
    }   
}