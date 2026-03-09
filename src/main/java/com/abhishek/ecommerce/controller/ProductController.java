package com.abhishek.ecommerce.controller;

import com.abhishek.ecommerce.model.Product;
import com.abhishek.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(){
        return new ResponseEntity<>(productService.getAllProducts(),HttpStatus.OK);
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){
        Product product = productService.getProductByid(productId);
        if(product != null)
            return new ResponseEntity<>(product.getImageData(),HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductByid(@PathVariable int id){
       Product product = productService.getProductByid(id);

       if(product != null)
           return new ResponseEntity<>(product,HttpStatus.OK);
       else{
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile){
        Product savedProduct = null;
        try {
            savedProduct = productService.addProduct(product, imageFile);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/product/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable int productId, @RequestPart Product product, @RequestPart MultipartFile imageFile){
        Product updatedProduct = null;
        try {
            updatedProduct = productService.updateProduct(productId,product,imageFile);
            return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable int productId){
        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>("Deleted",HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProdct(@RequestParam String keyword){
        List<Product> products = productService.searchProducts(keyword);
        System.out.println("Searching With : " + keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
