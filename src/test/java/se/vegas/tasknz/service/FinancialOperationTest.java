package se.vegas.tasknz.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.vegas.tasknz.dto.TransactionDTO;
import se.vegas.tasknz.exception.NotEnoughCreditException;
import se.vegas.tasknz.exception.TransactionIdRedundantException;
import se.vegas.tasknz.exception.WalletNotFoundException;
import se.vegas.tasknz.model.TransactionType;
import se.vegas.tasknz.model.Wallet;
import se.vegas.tasknz.model.WalletTransaction;
import se.vegas.tasknz.repository.TransactionRepository;
import se.vegas.tasknz.repository.WalletRepository;
import se.vegas.tasknz.service.impl.FinancialOperationImplCredit;
import se.vegas.tasknz.service.impl.FinancialOperationImplDebit;
import se.vegas.tasknz.service.impl.TransactionServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Date: 11.07.2025
 *
 * @author Nikolay Zinin
 */
@ExtendWith(MockitoExtension.class)
class FinancialOperationTest {

    private final FinancialOperationImplDebit debitOperation = new FinancialOperationImplDebit();
    private final FinancialOperationImplCredit creditOperation = new FinancialOperationImplCredit();

    @Test
    void testSuccessfulDebit() throws NotEnoughCreditException {
        BigDecimal balance = new BigDecimal("500.00");
        BigDecimal amount = new BigDecimal("100.00");

        BigDecimal result = debitOperation.execute(balance, amount);

        assertEquals(new BigDecimal("400.00"), result);
    }

    @Test
    void testDebitWithInsufficientFunds() {
        BigDecimal balance = new BigDecimal("100.00");
        BigDecimal amount = new BigDecimal("200.00");

        assertThrows(NotEnoughCreditException.class, () ->
                debitOperation.execute(balance, amount));
    }

    @Test
    void testCreditAddsToBalance() {
        BigDecimal balance = new BigDecimal("500.00");
        BigDecimal amount = new BigDecimal("200.00");

        BigDecimal result = creditOperation.execute(balance, amount);

        assertEquals(new BigDecimal("700.00"), result);
    }
}