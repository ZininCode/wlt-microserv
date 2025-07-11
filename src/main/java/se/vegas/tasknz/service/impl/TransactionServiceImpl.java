package se.vegas.tasknz.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vegas.tasknz.dto.TransactionDTO;
import se.vegas.tasknz.exception.NotEnoughCreditException;
import se.vegas.tasknz.exception.TransactionIdRedundantException;
import se.vegas.tasknz.exception.WalletNotFoundException;
import se.vegas.tasknz.model.TransactionType;
import se.vegas.tasknz.model.Wallet;
import se.vegas.tasknz.model.WalletTransaction;
import se.vegas.tasknz.repository.TransactionRepository;
import se.vegas.tasknz.repository.WalletRepository;
import se.vegas.tasknz.service.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

/**
 * Date: 10.01.2025
 *
 * @author Nikolay Zinin
 */
@Service
@AllArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final WalletService walletService;
    private final MappingService mappingService;
    private final FinancialOperationFactory financialOperationFactory;

    @Override
    @Transactional
    public void makeTransaction(TransactionDTO transactionDTO, TransactionType transactionType)
            throws TransactionIdRedundantException, WalletNotFoundException, NotEnoughCreditException {
        checkIfTransactionIdIsUnique(transactionDTO.getTransactionId());
        Wallet wallet = walletService.getWalletById(transactionDTO.getWalletId());
        FinancialOperation operation = financialOperationFactory.getOperation(transactionType);
        BigDecimal newBalance = operation.execute(wallet.getBalance(), transactionDTO.getAmount());
        wallet.setBalance(newBalance);
        persistTransactionAndWallet(transactionDTO, wallet, transactionType);
    }

    private void checkIfTransactionIdIsUnique(String transactionId) throws TransactionIdRedundantException {
        Optional<WalletTransaction> transactionById = transactionRepository.findById(transactionId);
        if (transactionById.isPresent()) {
            log.error(String.format("Transaction with id %s already exists", transactionId));
            throw new TransactionIdRedundantException("Transaction with id " + transactionId + " already exists");
        }
    }

    private void persistTransactionAndWallet(TransactionDTO transactionDTO, Wallet wallet, TransactionType transactionType) {
        WalletTransaction transaction = mappingService.mapPaymentDtoToEntity(transactionDTO, wallet, transactionType);
        walletRepository.save(wallet);
        transactionRepository.save(transaction);
    }
}

