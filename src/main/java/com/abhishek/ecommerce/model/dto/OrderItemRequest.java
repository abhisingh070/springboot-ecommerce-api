package com.abhishek.ecommerce.model.dto;

public record OrderItemRequest(

        int productId,
        int quantity
) {
}
