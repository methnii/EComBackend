package edu.icet.controller;

import edu.icet.dto.Customer;
import edu.icet.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    // Signup
    @PostMapping("/signup")
    public Customer signup(@RequestBody Customer customerDTO) {
        return authService.signup(customerDTO);
    }

    // Login
    @PostMapping("/login")
    public Customer login(@RequestParam String email, @RequestParam String password) {
        return authService.login(email, password);
    }
}
