package edu.icet.service;

import edu.icet.dto.Customer;

import java.util.List;

public interface CustomerService {
    Customer addCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    Boolean deleteCustomer(Integer id);
    Customer searchCustomerById(Integer id);
    List<Customer> getAll();
}
