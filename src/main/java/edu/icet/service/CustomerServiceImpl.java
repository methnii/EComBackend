package edu.icet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.Customer;
import edu.icet.entity.CustomerEntity;
import edu.icet.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor


public class  CustomerServiceImpl implements CustomerService {
    final CustomerRepository repository;
    final ObjectMapper mapper;

    @Override
    public Customer addCustomer(Customer customer) {
        customer.setId(null);
        CustomerEntity customerEntity = mapper.convertValue(customer, CustomerEntity.class);
        CustomerEntity savedEntity = repository.save(customerEntity);
        return mapper.convertValue(savedEntity, Customer.class);

    }

    @Override
    public Customer updateCustomer(Customer customer) {

        CustomerEntity existingEntity = repository.findById(customer.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customer.getId()));

        existingEntity.setId(customer.getId());
        existingEntity.setName(customer.getName());
        existingEntity.setAddress(customer.getAddress());

        CustomerEntity updatedEntity = repository.save(existingEntity);
        return mapper.convertValue(updatedEntity, Customer.class);
    }

    @Override
    public Boolean deleteCustomer(Integer id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public Customer searchCustomerById(Integer id) {
        CustomerEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
        return mapper.convertValue(entity, Customer.class);
    }


    @Override
    public List<Customer> getAll() {
        List <CustomerEntity> entities = repository.findAll();
        List<Customer> customerList=new ArrayList<>();
        entities.forEach(customerEntity ->
                customerList.add(
                        mapper.convertValue(
                                customerEntity, Customer.class
                        )
                ));
        return customerList;
    }


}
