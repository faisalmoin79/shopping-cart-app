package com.equalexperts.solution.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.equalexperts.solution.dto.ProductRequest;
import com.equalexperts.solution.service.CartService;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;
import util.Utility;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class CartControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CartService cartService;

	@Autowired
	private CartController cartController;

	@Test
	public void contextLoads() {
		assertThat(cartController).isNotNull();
	}
	
	@AfterEach
	public void reset() {
		CartService.resetCart();
	}

	@Test
	public void shouldReturnEmptyCartStateWhenViewCart() throws Exception {
		ResultActions result = this.mockMvc
			.perform(get("/cart/"));
//		String response = result.andReturn().getResponse().getContentAsString();
//		JsonPath.read(response, "$.cartItems");

		result
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.cartItems", IsEmptyCollection.empty()))
			.andExpect(jsonPath("$.taxRate", is(0.125)))
			.andExpect(jsonPath("$.total", is(0.0)))
			.andExpect(jsonPath("$.subTotal", is(0.0)))
			.andExpect(jsonPath("$.totalTax", is(0.0)));
	}

	@Test
	public void shouldReturnValidCartStateWhenProductAddedToCart() throws Exception {
		ProductRequest request = new ProductRequest("shreddies", 1);
		
		ResultActions result = this.mockMvc.perform(post("/cart/").content(Utility.getJsonString(request))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		
//		String response = result.andReturn().getResponse().getContentAsString();
//		JsonPath.read(response, "$.cartItems");

		result
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.cartItems", IsCollectionWithSize.hasSize(1)))
			.andExpect(jsonPath("$.cartItems[0].product.title", is("Shreddies")))
			.andExpect(jsonPath("$.cartItems[0].product.price", is(4.68)))
			.andExpect(jsonPath("$.cartItems[0].quantity", is(1)))
			.andExpect(jsonPath("$.cartItems[0].itemPrice", is(4.68)))
			.andExpect(jsonPath("$.cartItems[0].itemTotal", is(4.68)))
			.andExpect(jsonPath("$.taxRate", is(0.125)))
			.andExpect(jsonPath("$.totalTax", is(0.59)))
			.andExpect(jsonPath("$.subTotal", is(4.68)))
			.andExpect(jsonPath("$.total", is(5.27)));
	}

	@Test
	public void shouldReturnCartStateWhenInvalidProductAddedToCart() throws Exception {
		ProductRequest request = new ProductRequest("oats", 1);
		
		ResultActions result = this.mockMvc.perform(post("/cart/")
				.content(Utility.getJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		String response = result.andReturn().getResponse().getContentAsString();
		JsonPath.read(response, "$.cartItems");

		result
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.cartItems", IsEmptyCollection.empty()))
			.andExpect(jsonPath("$.taxRate", is(0.125)))
			.andExpect(jsonPath("$.totalTax", is(0.0)))
			.andExpect(jsonPath("$.subTotal", is(0.0)))
			.andExpect(jsonPath("$.total", is(0.0)));
	}

	@Test
	public void shouldUpdateQuantityWhenSameProductIsAddedToCart() throws Exception {
		ProductRequest request = new ProductRequest("shreddies", 1);
		
		ResultActions result = this.mockMvc.perform(post("/cart/")
				.content(Utility.getJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result = this.mockMvc.perform(post("/cart/")
				.content(Utility.getJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		String response = result.andReturn().getResponse().getContentAsString();
		JsonPath.read(response, "$.cartItems");

		result
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.cartItems", IsCollectionWithSize.hasSize(1)))
			.andExpect(jsonPath("$.cartItems[0].product.title", is("Shreddies")))
			.andExpect(jsonPath("$.cartItems[0].product.price", is(4.68)))
			.andExpect(jsonPath("$.cartItems[0].quantity", is(2))) // quantity should be 2 
			.andExpect(jsonPath("$.cartItems[0].itemPrice", is(4.68)))
			.andExpect(jsonPath("$.cartItems[0].itemTotal", is(9.36)))
			.andExpect(jsonPath("$.taxRate", is(0.125)))
			.andExpect(jsonPath("$.totalTax", is(1.17)))
			.andExpect(jsonPath("$.subTotal", is(9.36)))
			.andExpect(jsonPath("$.total", is(10.53)));
	}
	
	@Test
	public void shouldMatchExpectedResultInAssignment() throws Exception {
		ProductRequest request = new ProductRequest("cornflakes", 1);
		
		// Add 1 cornflakes @ 2.25
		this.mockMvc.perform(post("/cart/")
				.content(Utility.getJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		// Add 1 cornflakes @ 2.25
		this.mockMvc.perform(post("/cart/")
				.content(Utility.getJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		// Add 1 weetabix @ 9.98
		request = new ProductRequest("weetabix", 1);
		
		ResultActions result = this.mockMvc.perform(post("/cart/")
				.content(Utility.getJsonString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
//		String response = result.andReturn().getResponse().getContentAsString();
//		JsonPath.read(response, "$.cartItems");

		result
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.cartItems", IsCollectionWithSize.hasSize(2)))
			.andExpect(jsonPath("$.cartItems[0].product.title", is("Corn Flakes")))
			.andExpect(jsonPath("$.cartItems[0].product.price", is(2.52)))
			.andExpect(jsonPath("$.cartItems[0].quantity", is(2))) // qu
			.andExpect(jsonPath("$.cartItems[0].itemPrice", is(2.52)))
			.andExpect(jsonPath("$.cartItems[0].itemTotal", is(5.04)))
			.andExpect(jsonPath("$.cartItems[1].product.title", is("Weetabix")))
			.andExpect(jsonPath("$.cartItems[1].product.price", is(9.98)))
			.andExpect(jsonPath("$.cartItems[1].quantity", is(1))) // qu
			.andExpect(jsonPath("$.cartItems[1].itemPrice", is(9.98)))
			.andExpect(jsonPath("$.cartItems[1].itemTotal", is(9.98)))
			.andExpect(jsonPath("$.taxRate", is(0.125)))
			.andExpect(jsonPath("$.totalTax", is(1.88)))
			.andExpect(jsonPath("$.subTotal", is(15.02)))
			.andExpect(jsonPath("$.total", is(16.9)));
	}
}
