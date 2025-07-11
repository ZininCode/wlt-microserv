package se.vegas.tasknz.service;

import org.junit.jupiter.api.BeforeEach;
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
import se.vegas.tasknz.service.impl.TransactionServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Date: 25.01.2025
 *
 * @author Nikolay Zinin
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletService walletService;
    @Mock
    private MappingService mappingService;
    @Mock
    private FinancialOperationFactory financialOperationFactory;
    @Mock
    private FinancialOperation financialOperation;

    @InjectMocks
    private TransactionServiceImpl transactionService;
    

    @Test
    void testMakeTransactionDelegatesCorrectly() throws Exception {
        // Arrange
        TransactionDTO dto = TransactionDTO.builder()
                .transactionId("tx1")
                .walletId("w1")
                .amount(new BigDecimal("100.00"))
                .build();

        Wallet wallet = Wallet.builder()
                .walletId("w1")
                .balance(new BigDecimal("500.00"))
                .build();

        WalletTransaction tx = WalletTransaction.builder()
                .id("tx1")
                .wallet(wallet)
                .amount(new BigDecimal("100.00"))
                .build();

        when(transactionRepository.findById("tx1")).thenReturn(Optional.empty());
        when(walletService.getWalletById("w1")).thenReturn(wallet);
        when(financialOperationFactory.getOperation(TransactionType.DEBIT)).thenReturn(financialOperation);
        when(financialOperation.execute(new BigDecimal("500.00"), new BigDecimal("100.00"))).thenReturn(new BigDecimal("400.00"));
        when(mappingService.mapPaymentDtoToEntity(dto, wallet, TransactionType.DEBIT)).thenReturn(tx);

        transactionService.makeTransaction(dto, TransactionType.DEBIT);

        verify(financialOperationFactory).getOperation(TransactionType.DEBIT);
        verify(financialOperation).execute(new BigDecimal("500.00"), new BigDecimal("100.00"));
        verify(walletRepository).save(wallet);
        verify(transactionRepository).save(tx);
    }
    
    @Test
    void testWalletNotFoundExceptionIsThrown() throws Exception {
        TransactionDTO dto = TransactionDTO.builder()
                .transactionId("tx1")
                .walletId("wX")
                .amount(new BigDecimal("50.00"))
                .build();

        when(transactionRepository.findById("tx1")).thenReturn(Optional.empty());
        when(walletService.getWalletById("wX")).thenThrow(new WalletNotFoundException("Wallet not found"));

        assertThrows(WalletNotFoundException.class, () ->
                transactionService.makeTransaction(dto, TransactionType.CREDIT));
    }

    @Test
    void testDuplicateTransactionIdThrowsException() {
        TransactionDTO dto = TransactionDTO.builder()
                .transactionId("tx1")
                .walletId("w1")
                .amount(new BigDecimal("100.00"))
                .build();

        when(transactionRepository.findById("tx1")).thenReturn(Optional.of(new WalletTransaction()));

        assertThrows(TransactionIdRedundantException.class, () ->
                transactionService.makeTransaction(dto, TransactionType.CREDIT));
    }
}