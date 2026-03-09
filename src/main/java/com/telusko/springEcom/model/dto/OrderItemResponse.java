package com.telusko.springEcom.model.dto;

import java.math.BigDecimal;

public record OrderItemResponse(

        String productName,
        int quantity,
        BigDecimal totalPrice
) {
}
