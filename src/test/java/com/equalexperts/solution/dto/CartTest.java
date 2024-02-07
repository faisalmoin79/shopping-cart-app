package com.equalexperts.solution.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class CartTest {
	
	Cart cart = new Cart(7.5);
	CartItem cartItem = new CartItemImpl(new Product("test",5.25), 2);

	@Test
	void testAddToCart() {
		// when 
		cart.addToCart(cartItem);
		
		// then
		assertNotNull(cart);
		assertNotNull(cart.getCartItems());
		assertEquals(1, cart.getCartItems().size());
		assertEquals("test", cart.getCartItems().get(0).getItemName());
		assertEquals(2, cart.getCartItems().get(0).getQuantity());
		assertEquals(5.25, cart.getCartItems().get(0).getItemPrice());
		assertEquals(0.075, cart.getTaxRate());
		assertEquals(0.79, cart.getTotalTax());
		assertEquals(10.5, cart.getSubTotal());
		assertEquals(11.29, cart.getTotal());
	}

	@Test
	void testGetCartItems() {
		// when 
		cart.addToCart(cartItem);
		
		// then
		assertNotNull(cart);
		assertNotNull(cart.getCartItems());
		assertEquals(1, cart.getCartItems().size());
	}
	
	@Test
	void testCalculateSubtotal() {
		CartItem cartItem2 = new CartItemImpl(new Product("test",5.25), 2);
		CartItem cartItem3 = new CartItemImpl(new Product("test",5.25), 2);
		// when 
		cart.addToCart(cartItem); // @ 5.25 * 2
		cart.addToCart(cartItem2); // @ 5.25 * 2
		cart.addToCart(cartItem3); // @ 5.25 * 2

		// then
		assertNotNull(cart);
		assertEquals(31.5, cart.getSubTotal());
	}
	
	@Test
	void testGetMatchingCartItemSuccess() {
		// given
		cart.addToCart(cartItem); // @ 5.25 * 2
		
		// when 
		CartItem result = cart.getMatchingCartItem("test"); // @ 5.25 * 2
 
		// then
		assertNotNull(result);
		assertEquals("test", result.getItemName());
		assertEquals(5.25, result.getItemPrice());
		assertEquals(2, result.getQuantity());
		assertEquals(10.5, result.getItemTotal());
	}
}
