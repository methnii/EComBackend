package edu.icet.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.icet.dto.Customer;
import edu.icet.entity.CustomerEntity;
import edu.icet.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final ObjectMapper mapper;

    @Override
    public Customer signup(Customer customerDTO) {
        // Check if email already exists
        if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Save customer
        CustomerEntity entity = mapper.convertValue(customerDTO, CustomerEntity.class);
        CustomerEntity saved = customerRepository.save(entity);
        return mapper.convertValue(saved, Customer.class);
    }

    @Override
    public Customer login(String email, String password) {
        CustomerEntity entity = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not registered"));

        if (!entity.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return mapper.convertValue(entity, Customer.class);
    }
}
