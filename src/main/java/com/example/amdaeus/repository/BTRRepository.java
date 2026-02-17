package com.example.amdaeus.repository;

import com.example.amdaeus.dto.BussinessTripRequest;
import com.example.amdaeus.entity.BTRStatus;
import com.example.amdaeus.entity.BussinessTripRequests;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BTRRepository extends CrudRepository<BussinessTripRequests, Long> {

    @Query("SELECT b FROM BussinessTripRequests b WHERE b.user.userName = :username")
    List<BussinessTripRequests> findByUserName(@Param("username") String username);

    List<BussinessTripRequests> findByStatus(BTRStatus status);

    List<BussinessTripRequest> findAllByStatus(BTRStatus status);
}
