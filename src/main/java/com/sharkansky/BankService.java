package com.sharkansky;


import lombok.NonNull;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.RoundingMode;

/**
 * Contains the basic business logic that insures the {@link BankAccount} contains appropriate values.
 *
 * Uses {@see Money} from joda-money to handle see floating point arithmetic problems with irrational numbers. Example:
 * 100-66.66 should equal 34.34, not 34.3300000003.
 *
 * * TODO: Consider adding an overdraft strategy to a appropriately handle account negative balances
 */
public class BankService {

    /**
     * Moves the specific amount of money from source account to the dest account.
     *
     * @param from The source account. Must not be null.
     * @param to   The dest account. Must not be null.
     * @param amount The amount to transfer. Must be a positive number.
     *
     * @throws BankAccountException is thrown if the amount is negative.
     * @throws NullPointerException if {@code to} or {@code from} are null.
     */
    public void transfer(@NonNull BankAccount from, @NonNull BankAccount to, double amount) throws BankAccountException {
        withdraw(from, amount);
        deposit(to, amount);
    }


    /**
     *  Adds a specified amount of money to the specified account object.
     *
     * @param account Account where the funds are going. Must not be null.
     * @param amount Amount to deposit. Must not be negative.
     *
     * @throws BankAccountException is thrown if the amount is negative.
     * @throws NullPointerException if {@code to} is null.
     */
    public void deposit(@NonNull BankAccount account, double amount) throws BankAccountException {
        if (amount < 0.0) {
            throw new BankAccountException("can't deposit negative numbers: " + amount);
        }

        account.setAmount(adjustAccount(account, amount));
    }

    /**
     * Removes the specified amount from the account in question.
     *
     * @param account Account where the funds are going. Must not be null.
     * @param amount Ammount to withdraw. Must not be negative.
     * @throws BankAccountException is thrown if the amount is negative.
     * @throws NullPointerException if {@code account} is null.
     */
    public void withdraw(@NonNull BankAccount account, double amount) throws BankAccountException {
        if (amount < 0.0) {
            throw new BankAccountException("can't withdraw negative numbers: " + amount);
        }

        double newAmount = adjustAccount(account, -amount);
        if (newAmount < 0) {
            throw new BankAccountException("no overdraft protection. resulting balance: " + newAmount);
        }

        account.setAmount(newAmount);
    }


    /**
     * Adds the amount to the account. Amount is expected to be negative for the withdrawl operation.
     *
     * <p>Uses joda-money {@link Money} class to handle silly floating math problems. </p>
     *
     * @param account The account to adjust. Must not be null.
     * @param amount The amount the account will adjusted by.
     *
     * @return the new ballance.
     * @throws NullPointerException is thrown if account is null.
     */
    private double adjustAccount(@NonNull BankAccount account, double amount) {
        Money deposit = Money.of(CurrencyUnit.USD, amount, RoundingMode.HALF_DOWN);
        Money accountAmount = Money.of(CurrencyUnit.USD, account.getAmount(), RoundingMode.HALF_DOWN);

        return Money.total(accountAmount, deposit).getAmount().doubleValue();
    }
}
