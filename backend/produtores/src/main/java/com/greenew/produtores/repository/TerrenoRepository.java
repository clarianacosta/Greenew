package com.greenew.produtores.repository;

import com.greenew.produtores.model.entity.TerrenoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TerrenoRepository extends JpaRepository<TerrenoEntity, UUID> {

}