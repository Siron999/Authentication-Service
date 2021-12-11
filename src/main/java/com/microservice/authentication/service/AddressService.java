package com.microservice.authentication.service;

import com.microservice.authentication.entity.Address;
import com.microservice.authentication.exception.RecordAlreadyExistsException;
import com.microservice.authentication.exception.RecordNotFoundException;

public interface AddressService {
    Address saveAddress(String username, Address address) throws RecordAlreadyExistsException;

    String deleteAddress(String username) throws RecordNotFoundException;

    Address updateAddress(Long id, Address address) throws RecordNotFoundException;
}
