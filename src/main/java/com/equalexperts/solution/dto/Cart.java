package com.equalexperts.solution.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.util.CollectionUtils;

import lombok.Data;

@Data
public class Cart {
	List<CartItem> cartItems = new ArrayList<>();
	double taxRate;
	double subTotal;

	public Cart(double taxRate) {
		this.taxRate = taxRate / 100;
	}

	public void addToCart(CartItem cartItem) {
		// lookup for item
		CartItem matchingCartItem = getMatchingCartItem(cartItem.getItemName());
		int quantity = cartItem.getQuantity();
		if (matchingCartItem != null) { // if item already exists
			int existingQuantity = matchingCartItem.getQuantity();
			quantity += existingQuantity;
			CartItemImpl cartItemImpl = (CartItemImpl) matchingCartItem;
			cartItemImpl.setQuantity(quantity); // update quantity of existing items
		} else {
			cartItems.add(cartItem); // else add new item
		}
		subTotal = calculateSubtotal();// calculating subtotal when item is added
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	private double calculateSubtotal() { // sum of price for all items
		Optional<Double> subTotal;
		if (cartItems != null && !cartItems.isEmpty()) {
			subTotal = cartItems.stream().map(item -> item.getItemTotal()).reduce((a, b) -> a + b);
			if (subTotal.isPresent()) {
				return subTotal.get();
			}
		}
		// loop through cartItems and add item total
		return 0;
	}

	public double getSubTotal() {
		return round(subTotal);
	}
	
	public double getTotalTax() { // charged at 12.5% on the subtotal
		return round(taxRate * subTotal);
	}

	public double getTotal() { // subtotal + tax
		return round(subTotal + getTotalTax());
	}

	private CartItem getMatchingCartItem(String productName) {
		if (!CollectionUtils.isEmpty(getCartItems())) {
			Optional<CartItem> optCartItem = getCartItems().stream()
					.filter(cartItem -> cartItem.getItemName().equals(productName)).findFirst();
			if (optCartItem.isPresent()) {
				return optCartItem.get();
			}
		}
		return null;
		// iterates through list and returns cartItem with matching product name
	}

	private double round(double value) {
		return Math.round(value * 100.0) / 100.0;
	}
//	private boolean isCartItemExist(CartItem cartItem) {
//		return getMatchingCartItem(cartItem.getItemName()) == null ? false : true;
//		// iterates through list and returns true if cartItem is found with matching
//		// product name
//	}
}
