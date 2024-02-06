package com.equalexperts.solution.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.equalexperts.solution.dto.Cart;
import com.equalexperts.solution.dto.ProductRequest;
import com.equalexperts.solution.service.CartService;

 

@Controller
@RequestMapping("/cart")
public class CartController {
	@Autowired
	CartService cartService;

	@PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<Cart> addToCart(@RequestBody ProductRequest request) throws Exception {
		Cart cart = cartService.addProductToCart(request.getName(), request.getQuantity());
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}

	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Cart> viewCart() {
		return new ResponseEntity<Cart>(cartService.getCart(), HttpStatus.OK);
	}

}
