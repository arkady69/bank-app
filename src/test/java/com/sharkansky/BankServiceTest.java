package com.sharkansky;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by arkady69 on 4/11/16.
 */
public class BankServiceTest {
    private BankService bankService;

    @Before
    public void setUp() throws Exception {
        bankService = new BankService();
    }

    /**
     * Testing valid inputs, including a 0 withdrawl.
     */
    @Test
    public void testWithdrawValidInput() throws Exception {
        BankAccount a666 = BankAccount.builder().accountNumber("a666").amount(400.0).build();

        bankService.withdraw(a666, 100);
        assertThat(a666.getAmount(), is(300.0));

        bankService.withdraw(a666, 0);
        assertThat(a666.getAmount(), is(300.0));

        bankService.withdraw(a666, 1.66);
        assertThat("testing floating point silliness", a666.getAmount(), is(298.34));
    }


    /**
     * Testing bad inputs.
     *
     * <ul>
     *     <li>null account</li>
     *     <li>negative withdrawl amounts</li>
     *     <li>insufficent funds</li>
     * </ul>
     */
    @Test
    public void testWithdrawInvalidInput() {
        try {
            bankService.withdraw(null, 0);
            fail("expecting an null pointer exception");
        } catch (NullPointerException e) {
            assertThat(e.getMessage(), is("account"));
        } catch (BankAccountException e) {
            fail("expecting an null pointer exception");
        }

        BankAccount a666 = BankAccount.builder().accountNumber("a666").build();
        try {
            bankService.withdraw(a666, -999);
            fail("expecting a bank account exception");
        } catch (BankAccountException e) {
            assertThat("withdrawing negative number", e.getMessage(), is("can't withdraw negative numbers: -999.0"));
        }

        a666.setAmount(12.00);
        try {
            bankService.withdraw(a666, 100);
            fail("expecting a bank account exception");
        } catch (BankAccountException e) {
            assertThat("no overdraft protection", e.getMessage(), is("no overdraft protection. resulting balance: -88.0"));
        }
    }

    /**
     * Tests common deposit scenarios, including a 0 deposit.
     * @throws Exception
     */
    @Test
    public void testDepositValidInputs() throws Exception {
        BankAccount account = BankAccount.builder().accountNumber("999").build();
        assertThat("this account should be empty", account.getAmount(), is(0.0));

        bankService.deposit(account, 100.0);
        assertThat("should have 100 dollars", account.getAmount(), is(100.0));

        BankAccount accountWithBallance = BankAccount.builder().accountNumber("666").amount(1000.0).build();
        bankService.deposit(accountWithBallance, 666.0);
        assertThat("should have 1666.0", accountWithBallance.getAmount(), is(1666.0));

        bankService.deposit(accountWithBallance, 0);
        assertThat("should have 1666.0", accountWithBallance.getAmount(), is(1666.0));
    }

    /**
     * Test bad deposit scenarios
     * <ul>
     *     <li>null accounts</li>
     *     <li>negative deposits</li>
     * </ul>
     */
    @Test
    public void testDepositInvalidInputs() {
        try {
            bankService.deposit(null, 100.0);
            fail("expecting a BankAccountException");
        } catch (NullPointerException e) {
            assertThat("bad account", e.getMessage(), is("account"));
        } catch (BankAccountException e) {
            fail("Expecting an NPE for a null account");
        }

        BankAccount account = BankAccount.builder().accountNumber("666").build();
        try {
            bankService.deposit(account, -100.0);
            fail("expecting BankAccountException due to negative deposit");
        } catch (BankAccountException e) {
            assertThat(e.getMessage(), is("can't deposit negative numbers: -100.0"));
        }
    }

    /**
     * Testing negative transfer scenarios
     * <ul>
     *     <li>null accounts</li>
     *     <li>negative transfer amounts</li>
     *     <li>insufficient fund transfers</li>
     * </ul>
     */
    @Test
    public void testTransferInvalidInputs() {
        BankAccount from = BankAccount.builder().accountNumber("111").amount(333).build();
        BankAccount to = BankAccount.builder().accountNumber("222").amount(666).build();

        try {
            bankService.transfer(null, to, 100);
            fail("expecting a BankAccountException");
        } catch (NullPointerException e) {
            assertThat("bad account", e.getMessage(), is("from"));
        } catch (BankAccountException e) {
            fail("Expecting an NPE for a null account");
        }

        try {
            bankService.transfer(from, null, 100);
        } catch (NullPointerException e) {
            assertThat("bad account", e.getMessage(), is("to"));
        } catch (BankAccountException e) {
            fail("Expecting an NPE for a null account");
        }

        try {
            bankService.transfer(from, to, -100.0);
        }
        catch (BankAccountException e) {
            assertThat("bad account", e.getMessage(), is("can't withdraw negative numbers: -100.0"));
        }

        try {
            bankService.transfer(from, to, 666.33);
        }
        catch (BankAccountException e) {
            assertThat("bad account", e.getMessage(), is("no overdraft protection. resulting balance: -333.33"));
        }
    }

    /**
     * Tests valid transfer scenarios, include 0 transfer.
     * @throws Exception
     */
    @Test
    public void testTransferValidInput() throws Exception {
        BankAccount from = BankAccount.builder().accountNumber("111").amount(1000).build();
        BankAccount to = BankAccount.builder().accountNumber("222").amount(555).build();

        bankService.transfer(from, to, 100.66);
        assertThat("from account", from.getAmount(), is(899.34));
        assertThat("to account", to.getAmount(), is(655.66));


        bankService.transfer(from, to, 0);
        assertThat("from account", from.getAmount(), is(899.34));
        assertThat("to account", to.getAmount(), is(655.66));
    }


}