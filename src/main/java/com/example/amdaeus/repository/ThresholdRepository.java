package com.example.amdaeus.repository;

import com.example.amdaeus.entity.Threshold;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThresholdRepository extends CrudRepository<Threshold, Long> {

    @Override
    Optional<Threshold> findById(Long id);
}
