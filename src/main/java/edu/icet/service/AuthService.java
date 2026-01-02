package edu.icet.service;

import edu.icet.dto.Customer;

public interface AuthService {
    Customer signup(Customer customer);
    Customer login(String email, String password);
}
