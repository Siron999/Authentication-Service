package com.microservice.authentication.controller;

import com.microservice.authentication.entity.Address;
import com.microservice.authentication.exception.RecordAlreadyExistsException;
import com.microservice.authentication.exception.RecordNotFoundException;
import com.microservice.authentication.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequestMapping("api/address")
@RestController
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping("")
    public Address saveAddress(Authentication authentication, @Valid @RequestBody Address address) throws RecordAlreadyExistsException {
        log.info("Inside saveAddress : {}", authentication.getName());
        return addressService.saveAddress(authentication.getName(), address);
    }

    @PutMapping("{id}")
    public Address updateAddress(@PathVariable Long id, @RequestBody Address address) throws RecordNotFoundException {
        log.info("Inside updateAddress");
        return addressService.updateAddress(id, address);
    }

    @DeleteMapping("")
    public String deleteAddressBy(Authentication authentication) throws RecordNotFoundException {
        log.info("Inside deleteAddress : {}", authentication.getName());
        return addressService.deleteAddress(authentication.getName());
    }
}
