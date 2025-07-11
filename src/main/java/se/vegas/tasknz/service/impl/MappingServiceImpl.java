package se.vegas.tasknz.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import se.vegas.tasknz.dto.BalanceDto;
import se.vegas.tasknz.dto.TransactionDTO;
import se.vegas.tasknz.dto.TransactionHistoryDto;
import se.vegas.tasknz.model.TransactionType;
import se.vegas.tasknz.model.Wallet;
import se.vegas.tasknz.model.WalletTransaction;
import se.vegas.tasknz.service.MappingService;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Date: 10.01.2025
 *
 * @author Nikolay Zinin
 */
@Service
@AllArgsConstructor
public class MappingServiceImpl implements MappingService {
    @Override
    public WalletTransaction mapPaymentDtoToEntity(TransactionDTO transactionDTO, Wallet wallet, TransactionType transactionType) {
        return WalletTransaction.builder()
                .id(transactionDTO.getTransactionId())
                .amount(transactionDTO.getAmount())
                .wallet(wallet)
                .transactionTime(Instant.now())
                .transactionType(transactionType)
                .build();
    }
    @Override
    public BalanceDto mapWalletToBalanceDto(Wallet wallet) {
        BigDecimal balance = wallet.getBalance();
        if (balance == null) {
            balance = BigDecimal.valueOf(0);
        }
        return BalanceDto.builder()
                .walletId(wallet.getWalletId())
                .balance(balance)
                .build();
    }
    @Override
    public TransactionHistoryDto transactionToDto(WalletTransaction transaction) {
        return TransactionHistoryDto.builder()
                .transactionType(transaction.getTransactionType().name())
                .amount(transaction.getAmount())
                .transactionTime(transaction.getTransactionTime())
                .build();
    }
}
