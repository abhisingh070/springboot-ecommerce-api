package com.abhishek.ecommerce.repo;

import com.abhishek.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    List<Product> findByNameContainingIgnoreCaseOrBrandContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String keyword, String keyword1, String keyword2);
}
