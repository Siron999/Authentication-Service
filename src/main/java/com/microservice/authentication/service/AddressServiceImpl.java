package com.microservice.authentication.service;

import com.microservice.authentication.entity.Address;
import com.microservice.authentication.entity.User;
import com.microservice.authentication.exception.RecordAlreadyExistsException;
import com.microservice.authentication.exception.RecordNotFoundException;
import com.microservice.authentication.repository.AddressRepository;
import com.microservice.authentication.repository.AuthenticationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AuthenticationRepository authenticationRepository;

    @Override
    public Address saveAddress(String username, Address address) throws RecordAlreadyExistsException {
        log.info("Inside saveAddress");
        Optional<User> user = authenticationRepository.findByUsername(username);
        if (user.isPresent()) {
            log.info("User {} found in database", username);
        } else {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        }

        if (Objects.nonNull(user.get().getAddress())) {
            log.error("Address is already assigned to the user");
            throw new RecordAlreadyExistsException("Address is already assigned to the user");
        }
        address.setUser(user.get());
        user.get().setAddress(address);
        return address;
    }

    @Override
    public String deleteAddress(String username) throws RecordNotFoundException {
        log.info("Inside deleteAddress");
        Optional<User> user = authenticationRepository.findByUsername(username);
        if (user.isPresent()) {
            log.info("User {} found in database", username);
        } else {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        if (Objects.nonNull(user.get().getAddress())) {
            log.info("Deleting address");
            addressRepository.deleteById(user.get().getAddress().getId());
            user.get().setAddress(null);
            log.info("deleted");
        } else {
            log.error("No Address found");
            throw new RecordNotFoundException("No Address found");
        }
        return "Address Deleted Successfully.";
    }

    @Override
    public Address updateAddress(Long id, Address address) throws RecordNotFoundException {
        log.info("Inside updateAddress");

        Optional<Address> oldAddress = addressRepository.findById(id);

        if (oldAddress.isPresent()) {
            log.info("Address found in database");
        } else {
            log.error("Address not found");
            throw new RecordNotFoundException("Address not found");
        }

        if (Objects.nonNull(address.getProvince())) {
            oldAddress.get().setProvince(address.getProvince());
        }
        if (Objects.nonNull(address.getDistrict()) && !"".equalsIgnoreCase(address.getDistrict())) {
            oldAddress.get().setDistrict(address.getDistrict());
        }
        if (Objects.nonNull(address.getStreet()) && !"".equalsIgnoreCase(address.getStreet())) {
            oldAddress.get().setStreet(address.getStreet());
        }
        return oldAddress.get();
    }
}
