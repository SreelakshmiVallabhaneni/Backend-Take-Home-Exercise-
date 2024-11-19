package com.processreceipts.process_receipts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ReceiptService {

	private static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);

	private final Map<String, Integer> receiptPointsMap = new HashMap<>();

	public String processReceipt(Map<String, Object> receipt) {
		if (receipt == null) {
			logger.error("Received null receipt for processing");
			throw new NullPointerException("Receipt data is null");
		}

		// Validate receipt fields
		if (!isReceiptValid(receipt)) {
			logger.error("Receipt validation failed due to missing fields or invalid data");
			throw new NullPointerException("Receipt has null or invalid fields");
		}

		// Process the receipt and calculate points
		int points = calculatePoints(receipt);

		// Generate unique receipt ID
		String receiptId = UUID.randomUUID().toString();
		receiptPointsMap.put(receiptId, points);

		logger.info("Receipt processed. Points calculated: {} for ID: {}", points, receiptId);
		return receiptId;
	}

	public int getPoints(String receiptId) {
		if (receiptId == null) {
			logger.error("Received null receipt ID for fetching points");
			throw new NullPointerException("Receipt ID is null");
		}

		logger.info("Fetching points for receipt ID: {}", receiptId);

		// Fetch points or throw exception if not found
		if (receiptPointsMap.containsKey(receiptId)) {
			int points = receiptPointsMap.get(receiptId);
			logger.info("Points retrieved for ID {}: {}", receiptId, points);
			return points;
		} else {
			logger.error("Receipt ID {} not found", receiptId);
			throw new IllegalArgumentException("Receipt not found");
		}
	}

	private int throwReceiptNotFoundException(String receiptId) {
		logger.error("Receipt ID {} not found", receiptId);
		throw new IllegalArgumentException("Receipt not found");
	}

	private boolean isNullOrEmpty(Object value) {
		if (value == null)
			return true;
		if (value instanceof String)
			return ((String) value).isEmpty();
		if (value instanceof List<?>)
			return ((List<?>) value).isEmpty();
		return false;
	}

	private boolean isReceiptValid(Map<String, Object> receipt) {
		// Validate required fields
		return !isNullOrEmpty(receipt.get("retailer")) && !isNullOrEmpty(receipt.get("total"))
				&& !isNullOrEmpty(receipt.get("items")) && !isNullOrEmpty(receipt.get("purchaseDate"))
				&& !isNullOrEmpty(receipt.get("purchaseTime"))
				&& validateItems((List<Map<String, String>>) receipt.get("items"));
	}

	private boolean validateItems(List<Map<String, String>> items) {
		if (items == null || items.isEmpty())
			return false;

		// Validate each item
		for (Map<String, String> item : items) {
			if (isNullOrEmpty(item.get("shortDescription")) || isNullOrEmpty(item.get("price"))) {
				return false;
			}
		}
		return true;
	}

	private int calculatePoints(Map<String, Object> receipt) {
		int points = 0;

		// Get necessary data from the receipt
		String retailer = (String) receipt.get("retailer");
		String totalStr = (String) receipt.get("total");
		@SuppressWarnings("unchecked")
		List<Map<String, String>> items = (List<Map<String, String>>) receipt.get("items");
		String purchaseDate = (String) receipt.get("purchaseDate");
		String purchaseTime = (String) receipt.get("purchaseTime");

		// 1. One point for every alphanumeric character in the retailer name
		points += countAlphanumeric(retailer);

		// 2. 50 points if the total is a round dollar amount with no cents
		points += isRoundDollar(totalStr) ? 50 : 0;

		// 3. 25 points if the total is a multiple of 0.25
		points += isMultipleOf(totalStr, 0.25) ? 25 : 0;

		// 4. 5 points for every two items on the receipt
		points += (items != null) ? (items.size() / 2) * 5 : 0;

		// 5. Points for item descriptions with a trimmed length multiple of 3
		points += calculateItemPoints(items);

		// 6. 6 points if the day in the purchase date is odd
		points += isOddDay(purchaseDate) ? 6 : 0;

		// 7. 10 points if the time of purchase is after 2:00pm and before 4:00pm
		points += isBetweenTimes(purchaseTime, "14:00", "16:00") ? 10 : 0;

		return points;
	}

	private int countAlphanumeric(String text) {
		return (text != null) ? text.replaceAll("[^a-zA-Z0-9]", "").length() : 0;
	}

	private boolean isRoundDollar(String totalStr) {
		return totalStr != null && Double.parseDouble(totalStr) == Math.floor(Double.parseDouble(totalStr));
	}

	private boolean isMultipleOf(String totalStr, double factor) {
		return totalStr != null && Double.parseDouble(totalStr) % factor == 0;
	}

	private int calculateItemPoints(List<Map<String, String>> items) {
		int points = 0;
		if (items != null) {
			for (Map<String, String> item : items) {
				String description = item.get("shortDescription");
				String priceStr = item.get("price");
				if (description != null && description.trim().length() % 3 == 0 && priceStr != null) {
					double price = Double.parseDouble(priceStr);
					points += Math.ceil(price * 0.2);
				}
			}
		}
		return points;
	}

	private boolean isOddDay(String purchaseDate) {
		if (purchaseDate != null) {
			int day = Integer.parseInt(purchaseDate.split("-")[2]);
			return day % 2 != 0;
		}
		return false;
	}

	private boolean isBetweenTimes(String purchaseTime, String startTime, String endTime) {
		if (purchaseTime != null) {
			LocalTime time = LocalTime.parse(purchaseTime, DateTimeFormatter.ofPattern("HH:mm"));
			return time.isAfter(LocalTime.parse(startTime)) && time.isBefore(LocalTime.parse(endTime));
		}
		return false;
	}
}
