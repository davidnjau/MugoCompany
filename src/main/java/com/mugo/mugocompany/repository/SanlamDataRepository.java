package com.mugo.mugocompany.repository;

import com.mugo.mugocompany.entity.SanlamData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SanlamDataRepository extends JpaRepository<SanlamData, String> {

    List<SanlamData> findBySanlamListId(String sanlamId);

}
