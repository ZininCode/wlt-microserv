package se.vegas.tasknz.service;

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


import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

/**
 * Date: 10.01.2025
 *
 * @author Nikolay Zinin
 */

public interface TransactionService {

    void makeTransaction(TransactionDTO transactionDTO, TransactionType transactionType) throws TransactionIdRedundantException, WalletNotFoundException, NotEnoughCreditException;

}

