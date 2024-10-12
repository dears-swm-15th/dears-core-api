package com.teamdears.core.member.repository;

import com.teamdears.core.member.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUUID(String separator);

    boolean existsByNickname(String nickname);
}
