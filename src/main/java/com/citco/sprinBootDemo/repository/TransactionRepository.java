package com.citco.sprinBootDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.citco.sprinBootDemo.entity.Transactions;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, String> {

}
