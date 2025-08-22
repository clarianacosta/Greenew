package com.greenew.arvores.repository;

import com.greenew.arvores.model.entity.BiomaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiomaRepository extends JpaRepository<BiomaEntity, Long> {
}