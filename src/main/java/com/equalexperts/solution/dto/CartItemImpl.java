package com.equalexperts.solution.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class CartItemImpl implements CartItem {

	private Product product;
	private int quantity;

	public CartItemImpl(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	@Override
	public String getItemName() {
		return product.getName();
	}

	@Override
	public float getItemPrice() {
		return product.getPrice();
	}

	@Override
	public int getQuantity() {
		return quantity;
	}

}
