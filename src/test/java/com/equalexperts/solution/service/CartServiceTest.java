package com.equalexperts.solution.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.equalexperts.solution.dto.Cart;
import com.equalexperts.solution.dto.Product;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

	@Mock
	private RestTemplate restTemplate;
	@InjectMocks
	private CartService cartService;

	Product testProduct = new Product("test", 1);

	@BeforeEach
	public void setup() {
		cartService.resetCart();
	}

	@SuppressWarnings("unchecked")
	@Test
	void testGetProductSuccess() {
		// given
		when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any(), Mockito.any(Map.class)))
				.thenReturn(ResponseEntity.ok(testProduct));
		// when
		Product result = cartService.getProduct("test");

		// then
		assertEquals(testProduct.getTitle(), result.getTitle());
		assertEquals(testProduct.getPrice(), result.getPrice());
	}

	@SuppressWarnings("unchecked")
	@Test
	void testGetProductFailure() {
		// given
		when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any(), Mockito.any(Map.class))).thenReturn(null);

		// when
		Product result = cartService.getProduct("test");

		// then
		assertNull(result);
	}

	@Test
	void testAddProductToCartSuccess() {
		// given
		when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any(), Mockito.any(Map.class)))
				.thenReturn(ResponseEntity.ok(testProduct));
		// when
		Cart cartState = cartService.addProductToCart("test", 10);

		// then
		assertNotNull(cartState);
		assertNotNull(cartState.getCartItems());
		assertEquals(1, cartState.getCartItems().size());
		assertEquals("test", cartState.getCartItems().get(0).getItemName());
		assertEquals(10, cartState.getCartItems().get(0).getQuantity());
		assertEquals(1.0, cartState.getCartItems().get(0).getItemPrice());
		assertEquals(0.125, cartState.getTaxRate());
		assertEquals(1.25, cartState.getTotalTax());
		assertEquals(10, cartState.getSubTotal());
		assertEquals(11.25, cartState.getTotal());
	}

	@Test
	void testAddProductToCartFailure() {
		// given
		when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any(), Mockito.any(Map.class))).thenReturn(null);

		// when
		Cart cartState = cartService.addProductToCart("test", 10);

		// then  cart should be empty
		assertTrue(cartState.getCartItems().isEmpty());
		assertEquals(0.0,cartState.getTotal());
	}

	@Test
	void testGetCart() {
		// when
		Cart cartState = cartService.getCart();

		// then
		assertNotNull(cartState);
		assertEquals(CartService.TAX_RATE_IN_PERCENTAGE / 100, cartState.getTaxRate());
		assertEquals(0.0, cartState.getTotalTax());
		assertEquals(0.0, cartState.getSubTotal());
		assertEquals(0.0, cartState.getTotal());
	}

}
