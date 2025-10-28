package com.greenew.produtores.repository;

import com.greenew.produtores.model.entity.ProdutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProdutorRepository extends JpaRepository<ProdutorEntity, UUID> {

    Optional<ProdutorEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}