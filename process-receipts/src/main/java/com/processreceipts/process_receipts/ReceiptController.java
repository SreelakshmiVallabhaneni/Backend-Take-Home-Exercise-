package com.processreceipts.process_receipts;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

	private static final Logger logger = LoggerFactory.getLogger(ReceiptController.class);

	@Autowired
	private ReceiptService receiptService;

	@PostMapping("/process")
	public ResponseEntity<Map<String, String>> processReceipt(@RequestBody Map<String, Object> receiptData) {
		logger.info("Processing receipt: {}", receiptData);

		try {

			if (receiptData == null) {
				throw new NullPointerException("Receipt data is null");
			}
			// Process the receipt and generate an ID
			String receiptId = receiptService.processReceipt(receiptData);
			logger.info("Receipt processed successfully with ID: {}", receiptId);

			return ResponseEntity.ok(Map.of("id", receiptId));

		} catch (NullPointerException e) {
			logger.error("Null pointer exception while processing receipt", e);

			// Return error message and status 400 if an exception occurs
			return ResponseEntity.status(400).body(Map.of("error", "Receipt data is null"));
		} catch (Exception e) {
			logger.error("Error processing receipt", e);

			// Return error message and status 500 if an exception occurs
			return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
		}
	}

	@GetMapping("/{id}/points")
	public ResponseEntity<Object> getPoints(@PathVariable("id") String receiptId) {
		logger.info("Retrieving points for receipt ID: {}", receiptId);

		try {

			if (receiptId == null) {
				throw new NullPointerException("Receipt ID is null");
			}
			// Retrieve the points for the given receipt ID
			int points = receiptService.getPoints(receiptId);
			logger.info("Points for receipt ID {}: {}", receiptId, points);

			// Return points as the response body
			return ResponseEntity.ok(Map.of("points", points));
		} catch (NullPointerException e) {
			logger.error("Null pointer exception while retrieving points for receipt ID: {}", receiptId, e);

			// Return error message and status 400 if an exception occurs
			return ResponseEntity.status(400).body(Map.of("error", "Receipt ID is null"));
		} catch (Exception e) {
			logger.error("Error retrieving points for receipt ID: {}", receiptId, e);

			// Return error message and status 500 if an exception occurs
			return ResponseEntity.status(500).body(Map.of("error", "Receipt not found"));
		}
	}
}