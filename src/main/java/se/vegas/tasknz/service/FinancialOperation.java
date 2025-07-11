package se.vegas.tasknz.service;

import se.vegas.tasknz.exception.NotEnoughCreditException;

import java.math.BigDecimal;

public interface FinancialOperation {
    BigDecimal execute(BigDecimal currentBalance, BigDecimal amount) throws NotEnoughCreditException;
}
