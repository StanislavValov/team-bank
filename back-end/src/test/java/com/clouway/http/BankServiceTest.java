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

import java.math.BigDecimal;

import static com.clouway.custommatcher.ReplyMatcher.contains;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by emil on 14-9-24.
 */
public class BankServiceTest {

    private BankService bankService;
    private FakeRequestReader fakeRequestReader;
    private TransactionStatus transactionStatus;
    private DTOAmount dtoAmount = new DTOAmount();

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    private BankRepository bankRepository;

    @Mock
    private Request request;

    @Mock
    private Validator validator;

    @Mock
    private SiteMap siteMap;

    @Before
    public void setUp() {

        CurrentUser currentUser = new CurrentUser("Ivan");

        bankService = new BankService(bankRepository, validator, siteMap);

        dtoAmount.setAmount("123");

        fakeRequestReader = new FakeRequestReader(dtoAmount);

        transactionStatus = new TransactionStatus(message("Success"), "100");

    }

    @Test
    public void depositAmount() {

        context.checking(new Expectations() {
            {
                oneOf(request).read(DTOAmount.class);
                will(returnValue(fakeRequestReader));

                oneOf(validator).isValid(dtoAmount);
                will(returnValue(true));

                oneOf(bankRepository).deposit(new BigDecimal(dtoAmount.getAmount()));
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
                oneOf(request).read(DTOAmount.class);
                will(returnValue(fakeRequestReader));

                oneOf(validator).isValid(dtoAmount);
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

        dtoAmount.setAmount("123");

        context.checking(new Expectations() {
            {

                oneOf(request).read(DTOAmount.class);
                will(returnValue(fakeRequestReader));

                oneOf(validator).isValid(dtoAmount);
                will(returnValue(true));

                oneOf(bankRepository).withdraw(new BigDecimal(dtoAmount.getAmount()));
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
                oneOf(request).read(DTOAmount.class);
                will(returnValue(fakeRequestReader));

                oneOf(validator).isValid(dtoAmount);
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
                will(returnValue("5.1"));
            }
        });

        Reply<?> reply = bankService.getCurrentAmount();

        assertThat(reply, contains("5.1"));

    }

    private String message(String message) {
        return message;
    }

    private BigDecimal amount(BigDecimal amount) {
        return amount;
    }

}