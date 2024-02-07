package com.equalexperts.solution.service;

import java.util.Collections;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.equalexperts.solution.dto.Cart;
import com.equalexperts.solution.dto.CartItem;
import com.equalexperts.solution.dto.CartItemImpl;
import com.equalexperts.solution.dto.Product;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartService {
	public static final double TAX_RATE_IN_PERCENTAGE = 12.5f;
	public static Cart cart = new Cart(TAX_RATE_IN_PERCENTAGE); // static in memory cart

	@Autowired
	RestTemplate restTemplate;

	public Product getProduct(String productName) {
		// call product api to fetch Product
		String productApiBaseUrl = "https://equalexperts.github.io";
		String viewProductUrl = productApiBaseUrl + "/backend-take-home-test-data/{productName}.json";
		ResponseEntity<Product> response = null;
		try {
			response = restTemplate.getForEntity(viewProductUrl, Product.class,
					Collections.singletonMap("productName", productName));
		} catch (HttpClientErrorException e) {
			log.error(e.getMessage());
			if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
				log.info("Cannot find product {} in the Product API",productName);
			}
		}

		if (response != null) {
			return response.getBody();
		}
		return null;
	}

	public Cart addProductToCart(@NotNull String productName, int quantity) {
		Product product = getProduct(productName.trim());
		if (product != null) {
			CartItem cartItem = new CartItemImpl(product, quantity);
			cart.addToCart(cartItem);
		}else {
			log.info("No price found for Product {}, unable to add to cart ", productName);
		}
		return cart;
	}

	public Cart getCart() {
		return cart;
	}
	
	public static void resetCart() { // clearing the in memory cart
		cart.reset();
	}
}
