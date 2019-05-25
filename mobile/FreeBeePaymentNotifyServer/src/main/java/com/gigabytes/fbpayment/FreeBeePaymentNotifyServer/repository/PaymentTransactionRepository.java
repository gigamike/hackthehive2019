package com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.repository;

import com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.model.PaymentTransaction;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PaymentTransactionRepository extends CrudRepository<PaymentTransaction, Long> {

    @Query("SELECT p FROM PaymentTransaction p WHERE transactionCode = :transactionCode")
    PaymentTransaction getPaymentTransactionDetailsByTransactionCode(@Param("transactionCode") String transactionCode);

    @Modifying
    @Query("UPDATE PaymentTransaction p SET status = 'PAID' WHERE p.transactionCode = :transactionCode")
    @Transactional
    void completePaymentTransaction(@Param("transactionCode") String transactionCode);

}
