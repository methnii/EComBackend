package edu.icet.controller;

import edu.icet.dto.Customer;
import edu.icet.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping("/add-customer")
    public void addCustomer(@RequestBody Customer customer){
        service.addCustomer(customer);
    }

    @GetMapping("/getAll")
    public List<Customer> getAll(){
        return  service.getAll();
    }

    @PutMapping("/update-customer")
    public void updateCustomer(@RequestBody Customer customer){
        service.updateCustomer(customer);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteCustomer(@PathVariable Integer id){
        return service.deleteCustomer(id);
    }

    @GetMapping("/searchById/{id}")
    public Customer searchCustomerById (@PathVariable Integer id){
        return service.searchCustomerById(id);
    }
}
