package com.equalexperts.solution.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.util.CollectionUtils;

import lombok.Data;

@Data
public class Cart {
	List<CartItem> cartItems = new ArrayList<>();
	float taxRate;

	public Cart(float taxRate) {
		this.taxRate = taxRate;
	}

	public void addToCart(CartItem cartItem) {
		CartItem matchingCartItem = getMatchingCartItem(cartItem.getItemName());
		int quantity = cartItem.getQuantity();
		if (matchingCartItem != null) {

			int existingQuantity = matchingCartItem.getQuantity();
			quantity = +existingQuantity;
			CartItemImpl cartItemImpl = (CartItemImpl) matchingCartItem;
			cartItemImpl.setQuantity(quantity);
		} else {
			cartItems.add(cartItem);
		}
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public float getSubtotal() { // sum of price for all items
		Optional<Float> subTotal;
		if (cartItems != null && !cartItems.isEmpty()) {
			subTotal = cartItems.stream().map(item -> item.getItemPrice() * item.getQuantity()).reduce((a, b) -> a + b);
			if (!subTotal.isPresent()) {
				return subTotal.get();
			}
		}
		// loop through cartItems and multiple price to quantity
		return 0;
	}

	public float getTotalTax() { // charged at 12.5% on the subtotal
		return taxRate * getSubtotal();
	}

	public float getTotal() { // subtotal + tax
		return getSubtotal() + getTotalTax();
	}

	public CartItem getMatchingCartItem(String productName) {
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

//	private boolean isCartItemExist(CartItem cartItem) {
//		return getMatchingCartItem(cartItem.getItemName()) == null ? false : true;
//		// iterates through list and returns true if cartItem is found with matching
//		// product name
//	}
}
