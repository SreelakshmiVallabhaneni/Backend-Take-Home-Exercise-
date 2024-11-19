package com.processreceipts.process_receipts;

@SuppressWarnings("serial")
public class ReceiptNotFoundException extends RuntimeException {
	public ReceiptNotFoundException(String id) {
        super("Receipt ID not found: " + id);
    }
}
