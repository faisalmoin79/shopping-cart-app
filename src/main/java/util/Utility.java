package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class Utility {
	public static final ObjectMapper objMapper = new ObjectMapper();

	public static String getJsonString(Object obj) {
		try {
			return objMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	public static double round(double value) {
		return Math.round(value * 100.0) / 100.0;
	}

}
