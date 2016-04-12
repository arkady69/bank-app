package com.sharkansky;

import lombok.Builder;
import lombok.Data;

/**
 * Simple bean to hold the account number and the amount.
 *
 * The @Data generates the getters/setter, equals, hashcode and toString() methods at compile time.
 *
 * The @Builder will generate at compile time a fluent style builder factory.
 *
 * All the business logic is located in the {@link BankService}.
 */
@Data
@Builder
public class BankAccount {
    private String accountNumber;
    private double amount;
}
