package edu.icet.service;

import edu.icet.dto.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Product product);
    Product updateProduct(Product product);
    Boolean deleteProduct(Integer id);
    Product getProductById(Integer id);
    List<Product> getAllProducts();
}

