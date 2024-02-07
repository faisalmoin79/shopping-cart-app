package com.equalexperts.solution.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.util.CollectionUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import util.Utility;

@Slf4j
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
		int quantity = cartItem.getQuantity();
		CartItem matchingCartItem = getMatchingCartItem(cartItem.getItemName());
		
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
			subTotal = cartItems.stream()
//					.peek(item -> log.info("itemTotal: "+item.getItemTotal()))
					.map(item -> item.getItemTotal()).reduce((a, b) -> a + b);
			if (subTotal.isPresent()) {
				return subTotal.get();
			}
		}
		// loop through cartItems and add item total
		return 0;
	}
	
	public void removeFromCart(CartItem cartItem) {
		// lookup for item
		CartItem matchingItem = getMatchingCartItem(cartItem.getItemName());
		if(matchingItem!=null) {
			log.info("Item {} found in cart: ", cartItem.getItemName());
			if(matchingItem.getQuantity()>1) {
				log.info("Reducing quanity of item by 1");
				CartItemImpl cartItemImpl = (CartItemImpl) matchingItem;
				cartItemImpl.setQuantity(matchingItem.getQuantity()-1); // update quantity of existing items
			}else {
				log.info("Removing item: {} from cart", cartItem.getItemName());
				cartItems.remove(matchingItem);
			}
		}
		log.info("Could not find item: {} in cart", cartItem.getItemName());
		
		
//		boolean isRemoved = cartItems.removeIf(item -> item.getItemName().equalsIgnoreCase(cartItem.getItemName()));
//		log.info(isRemoved ? "Found matching item, removed and item");
	}

	public double getSubTotal() {
		return Utility.round(subTotal);
	}
	
	public double getTotalTax() { // charged at 12.5% on the subtotal
		return Utility.round(taxRate * subTotal);
	}

	public double getTotal() { // subtotal + tax
		return Utility.round(subTotal + getTotalTax());
	}

	CartItem getMatchingCartItem(String productName) {
		if (!CollectionUtils.isEmpty(getCartItems())) {
			Optional<CartItem> optCartItem = getCartItems().stream()
					.filter(cartItem -> cartItem.getItemName().equalsIgnoreCase(productName)).findFirst();
			if (optCartItem.isPresent()) {
				return optCartItem.get();
			}
		}
		return null;
		// iterates through list and returns cartItem with matching product name
	}

	public void reset() {
		this.cartItems =  new ArrayList<>();
		subTotal = 0;
	}


}
