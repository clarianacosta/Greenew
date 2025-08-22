package com.greenew.arvores.repository;

import com.greenew.arvores.model.entity.ArvoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArvoreRepository extends JpaRepository<ArvoreEntity, Long> {
}