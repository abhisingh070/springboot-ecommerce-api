package com.abhishek.ecommerce.service;

import com.abhishek.ecommerce.model.Order;
import com.abhishek.ecommerce.model.OrderItem;
import com.abhishek.ecommerce.model.Product;
import com.abhishek.ecommerce.model.dto.OrderItemRequest;
import com.abhishek.ecommerce.model.dto.OrderItemResponse;
import com.abhishek.ecommerce.model.dto.OrderRequest;
import com.abhishek.ecommerce.model.dto.OrderResponse;
import com.abhishek.ecommerce.repo.OrderRepo;
import com.abhishek.ecommerce.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private ProductRepo productRepo;

    public OrderResponse placeOrder(OrderRequest orderRequest) {

        Order order = new Order();
        String orderId = UUID.randomUUID().toString().substring(0,7).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(orderRequest.customerName());
        order.setEmail(orderRequest.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for(OrderItemRequest itemReq : orderRequest.items()){

            Product product = productRepo.findById(itemReq.productId())
                    .orElseThrow(() -> new RuntimeException("Product not Found"));
            product.setStockQuantity(product.getStockQuantity() - itemReq.quantity());
            productRepo.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.quantity())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())))
                    .order(order)
                    .build();

            orderItems.add(orderItem);
        }
        order.setItems(orderItems);
        Order orderSaved = orderRepo.save(order);

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for(OrderItem orderItem : order.getItems()){
            OrderItemResponse orderItemResponse = new OrderItemResponse(
                    orderItem.getProduct().getName(),
                    orderItem.getQuantity(),
                    orderItem.getTotalPrice()
            );
            itemResponses.add(orderItemResponse);
        }

        OrderResponse orderResponse  = new OrderResponse(
                orderSaved.getOrderId(),
                orderSaved.getCustomerName(),
                orderSaved.getEmail(),
                orderSaved.getStatus(),
                orderSaved.getOrderDate(),
                itemResponses
                );
        return orderResponse;
    }

    public List<OrderResponse> getAllOrderResponses() {

        List<Order> orders = orderRepo.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();

        for(Order order : orders){

            List<OrderItemResponse> itemResponses = new ArrayList<>();
            for(OrderItem item : order.getItems()){
                OrderItemResponse orderItemResponse = new OrderItemResponse(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getTotalPrice()
                );
                itemResponses.add(orderItemResponse);
            }

            OrderResponse orderResponse = new OrderResponse(
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getEmail(),
                    order.getStatus(),
                    order.getOrderDate(),
                    itemResponses
            );
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }
}
