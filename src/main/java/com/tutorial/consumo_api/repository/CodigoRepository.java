package com.tutorial.consumo_api.repository;

import com.tutorial.consumo_api.model.Codigo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodigoRepository extends JpaRepository<Codigo, Long> {

}
