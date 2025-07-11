package se.vegas.tasknz.service.impl;

import org.springframework.stereotype.Component;
import se.vegas.tasknz.exception.NotEnoughCreditException;
import se.vegas.tasknz.service.FinancialOperation;

import java.math.BigDecimal;

@Component
public class FinancialOperationImplCredit implements FinancialOperation {
    @Override
    public BigDecimal execute(BigDecimal currentBalance, BigDecimal amount) {
        return currentBalance.add(amount);
    }
}
