package com.equalexperts.solution.dto;

public interface CartItem {
	public String getItemName();

	public double getItemPrice();

	public int getQuantity();

	default double getItemTotal() {// default method
		return getItemPrice() * this.getQuantity();
	}; 
}
