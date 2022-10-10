package com.fsoft.team.repository;

import com.fsoft.team.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CreditRepository extends JpaRepository<Credit, String> {
    public boolean existsCreditByCardNumber(String cardNumber);
    @Modifying
    @Query(value = "insert into tbl_credits(card_number, cvc, expiration_date, username) values (:cardNumber, :cvc, :expirationDate, :username)", nativeQuery = true)
    @Transactional
    public void insertCredit(@Param("cardNumber")String cardNumber, @Param("cvc")String cvc,
                             @Param("expirationDate")String expirationDate, @Param("username")String username);
}
