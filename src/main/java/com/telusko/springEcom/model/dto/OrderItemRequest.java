package com.telusko.springEcom.model.dto;

public record OrderItemRequest(

        int productId,
        int quantity
) {
}
