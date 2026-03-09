package com.telusko.springEcom.service;

import com.telusko.springEcom.model.Product;
import com.telusko.springEcom.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public Product getProductByid(int id) {
        return productRepo.findById(id).orElse(null);
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }


    public Product addProduct(Product product, MultipartFile image) throws IOException {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        product.setImageData(image.getBytes());

        return productRepo.save(product);
    }

    public Product updateProduct(int productId, Product product, MultipartFile imageFile) throws IOException {

        Product existingProduct = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id : " + productId));

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setReleaseDate(product.getReleaseDate());
        existingProduct.setProductAvailable(product.getProductAvailable());
        existingProduct.setStockQuantity(product.getStockQuantity());

        if(imageFile != null && !imageFile.isEmpty()){
            existingProduct.setImageName(imageFile.getOriginalFilename());
            existingProduct.setImageType(imageFile.getContentType());
            existingProduct.setImageData(imageFile.getBytes());
        }

        return productRepo.save(existingProduct);
    }

    public void deleteProduct(int productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new RuntimeException("Product not found with id : " + productId));
        productRepo.delete(product);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepo.findByNameContainingIgnoreCaseOrBrandContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, keyword);
    }
}
