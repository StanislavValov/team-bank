package com.clouway.http;

import com.clouway.core.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;

import java.math.BigDecimal;

/**
 * Created by emil on 14-9-25.
 */
@At("/amount")
@Service
@Singleton
public class BankService {

    private final BankRepository bankRepository;
    private final Validator validator;
    private final SiteMap siteMap;

    @Inject
    public BankService(BankRepository bankRepository, @Named("AmountValidator") Validator validator, SiteMap siteMap) {

        this.bankRepository = bankRepository;
        this.validator = validator;
        this.siteMap = siteMap;
    }

    @Get
    public Reply<?> getCurrentAmount() {

        return Reply.with(bankRepository.getBalance()).ok();
    }

    @At("/deposit")
    @Post
    public Reply<?> deposit(Request request) {

        DTOAmount dtoAmount = request.read(DTOAmount.class).as(Json.class);

        if (validator.isValid(dtoAmount)) {
            TransactionStatus info = bankRepository.deposit(new BigDecimal(dtoAmount.getAmount()));
            return Reply.with(info).as(Json.class);
        }
        return Reply.with(siteMap.transactionError()).error();
    }

    @At("/withdraw")
    @Post
    public Reply<?> withdraw(Request request) {

        DTOAmount DTOAmount = request.read(DTOAmount.class).as(Json.class);

        if (validator.isValid(DTOAmount)) {
            TransactionStatus info = bankRepository.withdraw(new BigDecimal(DTOAmount.getAmount()));
            return Reply.with(info).as(Json.class);
        }
        return Reply.with(siteMap.transactionError()).error();
    }
}