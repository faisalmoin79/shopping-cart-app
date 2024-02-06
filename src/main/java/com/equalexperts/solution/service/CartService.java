package com.equalexperts.solution.service;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.equalexperts.solution.dto.Cart;
import com.equalexperts.solution.dto.CartItem;
import com.equalexperts.solution.dto.CartItemImpl;
import com.equalexperts.solution.dto.Product;

@Service
public class CartService {
	public static Cart cart = new Cart(12.5f); // static in memory cart
//	@Autowired
	RestTemplate restTemplate = new RestTemplate();

	public Product getProduct(String productName) {

		// call product api to fetch Product
		String productApiBaseUrl = "https://equalexperts.github.io/";
		String viewProductUrl = productApiBaseUrl + "/backend-take-home-test-data/{productName}.json";
		ResponseEntity<Product> response = restTemplate.getForEntity(viewProductUrl, Product.class, Collections.singletonMap("productName", productName));

		if (response != null) {
			return response.getBody();
		}
		return null;
	}

	public Cart addProductToCart(String productName, int quantity) {
		Product product = getProduct(productName);

		CartItem cartItem = new CartItemImpl(product, quantity);

		cart.addToCart(cartItem);
		return cart;
	}

	public Cart getCart() {
		return cart;
	}
}
