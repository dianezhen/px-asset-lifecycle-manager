package com.ge.predix.alm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ge.predix.alm.dto.Domain;

@Repository(value="jpaDomainRepository")
public interface JpaDomainRepository extends JpaRepository<Domain, String> {
}
