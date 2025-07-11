package se.vegas.tasknz.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import se.vegas.tasknz.dto.BalanceDto;
import se.vegas.tasknz.dto.TransactionDTO;
import se.vegas.tasknz.dto.TransactionHistoryDto;
import se.vegas.tasknz.model.TransactionType;
import se.vegas.tasknz.model.Wallet;
import se.vegas.tasknz.model.WalletTransaction;

import java.math.BigDecimal;

/**
 * Date: 10.01.2025
 *
 * @author Nikolay Zinin
 */

public interface MappingService {

   WalletTransaction mapPaymentDtoToEntity(TransactionDTO transactionDTO, Wallet wallet, TransactionType transactionType);

   BalanceDto mapWalletToBalanceDto(Wallet wallet);

   TransactionHistoryDto transactionToDto(WalletTransaction transaction);
}
