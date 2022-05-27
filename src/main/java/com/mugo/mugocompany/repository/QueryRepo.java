package com.mugo.mugocompany.repository;

import com.mugo.mugocompany.entity.ClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QueryRepo extends JpaRepository<ClientDetails, String> {

    @Query("SELECT p FROM ClientDetails p WHERE  p.registrationNumber LIKE %?1%")
    public List<ClientDetails> pickMatchingEntries(String keyword);

}
