package se.vegas.tasknz.service.impl;

import org.springframework.stereotype.Component;
import se.vegas.tasknz.exception.NotEnoughCreditException;
import se.vegas.tasknz.service.FinancialOperation;

import java.math.BigDecimal;

@Component
public class FinancialOperationImplDebit implements FinancialOperation {
    @Override
    public BigDecimal execute(BigDecimal currentBalance, BigDecimal amount) throws NotEnoughCreditException {
        BigDecimal newBalance = currentBalance.subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughCreditException("Not enough money on balance");
        }
        return newBalance;
    }
}
