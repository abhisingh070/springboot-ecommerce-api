package com.abhishek.ecommerce.controller;

import com.abhishek.ecommerce.model.dto.OrderRequest;
import com.abhishek.ecommerce.model.dto.OrderResponse;
import com.abhishek.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest){
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    public  ResponseEntity<List<OrderResponse>> getAllOrder(){
        List<OrderResponse> orderResponse = orderService.getAllOrderResponses();
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }
}
