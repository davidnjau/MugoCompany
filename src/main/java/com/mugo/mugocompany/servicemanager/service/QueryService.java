package com.mugo.mugocompany.servicemanager.service;

import com.mugo.mugocompany.entity.ClientDetails;
import com.mugo.mugocompany.repository.QueryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryService {

    @Autowired
    private QueryRepo repo;

    public List<ClientDetails> listMatchingEntries(String keyword) {
        return repo.pickMatchingEntries(keyword);
    }

}
