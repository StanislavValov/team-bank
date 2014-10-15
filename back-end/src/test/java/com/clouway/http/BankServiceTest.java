package com.clouway.http;

import com.clouway.core.*;
import com.clouway.http.fake.FakeRequestReader;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.clouway.custommatcher.ReplyMatcher.contains;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by emil on 14-9-24.
 */
public class BankServiceTest {

    private BankService bankService;
    private FakeRequestReader fakeRequestReader;
    private TransactionStatus transactionStatus;
    private Amount amount;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    private BankRepository bankRepository = null;

    @Mock
    private Request request = null;

    @Mock
    private Validator<Amount> validator;

    @Mock
    private SiteMap siteMap;

    @Before
    public void setUp() {

        amount = new Amount(100d);

        CurrentUser currentUser = new CurrentUser("Ivan");

        bankService = new BankService(bankRepository, validator, siteMap);

        fakeRequestReader = new FakeRequestReader(currentUser.getName(), amount.getAmount());

        transactionStatus = new TransactionStatus(message("Success"), amount(100d));

    }

    @Test
    public void depositAmount() {

        context.checking(new Expectations() {
            {

                oneOf(request).read(Amount.class);
                will(returnValue(fakeRequestReader));

                oneOf(validator).isValid(amount);
                will(returnValue(true));

                oneOf(bankRepository).deposit(amount(100d));
                will(returnValue(transactionStatus));
            }
        });

        Reply<?> reply = bankService.deposit(request);

        assertThat(reply, contains(transactionStatus));

    }

    @Test
    public void failToDeposit() {

        context.checking(new Expectations() {
            {
                oneOf(request).read(Amount.class);
                will(returnValue(fakeRequestReader));

                oneOf(validator).isValid(amount);
                will(returnValue(false));

                oneOf(siteMap).transactionError();
                will(returnValue("error"));
            }
        });
        Reply<?> reply = bankService.deposit(request);
        assertThat(reply, contains("error"));
    }

    @Test
    public void withdrawAmount() {

        context.checking(new Expectations() {
            {

                oneOf(request).read(Amount.class);
                will(returnValue(fakeRequestReader));

                oneOf(validator).isValid(amount);
                will(returnValue(true));

                oneOf(bankRepository).withdraw(amount(100d));
                will(returnValue(transactionStatus));
            }
        });

        Reply<?> reply = bankService.withdraw(request);

        assertThat(reply, contains(transactionStatus));
    }

    @Test
    public void withdrawFailed() {

        context.checking(new Expectations() {
            {
                oneOf(request).read(Amount.class);
                will(returnValue(fakeRequestReader));

                oneOf(validator).isValid(amount);
                will(returnValue(false));

                oneOf(siteMap).transactionError();
                will(returnValue("error"));
            }
        });

        Reply<?> reply = bankService.withdraw(request);

        assertThat(reply, contains("error"));
    }

    @Test
    public void getCurrentAmountOnUser() {

        context.checking(new Expectations() {
            {

                oneOf(bankRepository).getBalance();
                will(returnValue(5.1d));
            }
        });

        Reply<?> reply = bankService.getCurrentAmount();

        assertThat(reply, contains(5.1d));

    }

    private String message(String message) {
        return message;
    }

    private double amount(double amount) {
        return amount;
    }

}