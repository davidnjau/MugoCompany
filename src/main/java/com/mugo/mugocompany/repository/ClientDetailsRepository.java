package com.mugo.mugocompany.repository;

import com.mugo.mugocompany.entity.ClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientDetailsRepository extends JpaRepository<ClientDetails, String> {

    Boolean existsByRegistrationNumber(String regNo);
    ClientDetails findByRegistrationNumber(String regNo);

}
