/*
 * Copyright (c) 2014 Semih YAGBASAN, YAGBASAN HO
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.syagbasan.sterilveri.generaltwocontentfragmentlistviews;

public class RowItemFour {
	private String date;
	private String person;
    private String product;
    private String amount;
    
     
    public RowItemFour(String date, String person,String product, String amount) {
        this.date = date;
    	this.person = person;
    	this.product = product;
        this.amount = amount;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
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