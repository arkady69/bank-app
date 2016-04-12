package com.sharkansky;

/**
 * Created by arkady69 on 4/11/16.
 */
public class Main {
    public static void main(String...args) throws BankAccountException {
        BankService bankService = new BankService();

        BankAccount myAccount = BankAccount.builder().accountNumber("100").amount(877.34).build();

        bankService.deposit(myAccount, 343.33);
        System.out.println(myAccount);

        bankService.withdraw(myAccount, 66.23);
        System.out.println(myAccount);

        BankAccount yourAccount = BankAccount.builder().accountNumber("2342").amount(10).build();

        bankService.withdraw(yourAccount, 5.66);
        System.out.println(yourAccount);

        bankService.transfer(myAccount, yourAccount, 500.22);
        System.out.printf("my account: %s -- your account: %s", myAccount, yourAccount);
    }
}
