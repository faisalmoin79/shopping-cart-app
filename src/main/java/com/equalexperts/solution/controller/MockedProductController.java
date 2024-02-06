package com.equalexperts.solution.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.equalexperts.solution.dto.Product;

@RestController
@RequestMapping("/product-api")
public class MockedProductController {
	@SuppressWarnings("serial")
	/*
	 * Base URL: https://equalexperts.github.io/
	 * 
	 * View Product: GET /backend-take-home-test-data/{product}.json
	 * 
	 * Available products
	 * 
	 * cheerios cornflakes frosties shreddies weetabix
	 * 
	 * Inputs: Add 1 × cornflakes @ 2.52 each Add another 1 x cornflakes @2.52 each
	 * Add 1 × weetabix @ 9.98 each Results Cart contains 2 x cornflakes Cart
	 * contains 1 x weetabix Subtotal = 15.02 Tax = 1.88 Total = 16.90
	 */

	List<Product> mockedProducts = new ArrayList<Product>() {
		{
			add(new Product("cheerios", 1.50f));
			add(new Product("cornflakes", 2.52f));
			add(new Product("frosties", 3.50f));
			add(new Product("shreddies", 4.50f));
			add(new Product("weetabix", 9.98f));

		}
	};

//	@GetMapping(value = "/backend-take-home-test-data/{product}.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/{product}.json", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> getProduct(@PathVariable("product") String productName) {
		if (!ObjectUtils.isEmpty(productName)) {
			Optional<Product> optProduct = mockedProducts.stream()
					.filter(p -> p.getName().equalsIgnoreCase(productName)).findFirst();
			if (optProduct.isPresent())
				return ResponseEntity.ok(optProduct.get());
		}
		return null;
	}

//	@GetMapping(value = "/backend-take-home-test-data/", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Product>> getAllProducts() {
		return ResponseEntity.ok(mockedProducts);
	}

}
