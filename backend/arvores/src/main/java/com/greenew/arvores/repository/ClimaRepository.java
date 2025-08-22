package com.greenew.arvores.repository;

import com.greenew.arvores.model.entity.ClimaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClimaRepository extends JpaRepository<ClimaEntity, Long> {
}