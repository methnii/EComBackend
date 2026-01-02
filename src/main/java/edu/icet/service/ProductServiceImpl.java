package edu.icet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.Product;
import edu.icet.entity.ProductEntity;
import edu.icet.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ObjectMapper mapper;

    @Override
    public Product addProduct(Product product) {
        ProductEntity entity = mapper.convertValue(product, ProductEntity.class);
        ProductEntity saved = repository.save(entity);
        return mapper.convertValue(saved, Product.class);
    }

    @Override
    public Product updateProduct(Product product) {
        ProductEntity existing = repository.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());

        ProductEntity updated = repository.save(existing);
        return mapper.convertValue(updated, Product.class);
    }

    @Override
    public Boolean deleteProduct(Integer id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public Product getProductById(Integer id) {
        ProductEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapper.convertValue(entity, Product.class);
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.findAll().stream()
                .map(e -> mapper.convertValue(e, Product.class))
                .toList();
    }
}
