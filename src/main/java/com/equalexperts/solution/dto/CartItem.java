package com.equalexperts.solution.dto;

public interface CartItem {
	public String getItemName();

	public float getItemPrice();

	public int getQuantity();

	default float getItemTotal() {
		return getItemPrice() * this.getQuantity();
	}; // default method
}
