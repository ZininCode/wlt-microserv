package se.vegas.tasknz.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vegas.tasknz.dto.BalanceDto;
import se.vegas.tasknz.dto.TransactionHistoryDto;
import se.vegas.tasknz.exception.WalletIdRedundantException;
import se.vegas.tasknz.exception.WalletNotFoundException;
import se.vegas.tasknz.model.Wallet;
import se.vegas.tasknz.repository.WalletRepository;
import se.vegas.tasknz.service.MappingService;
import se.vegas.tasknz.service.WalletService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Date: 10.01.2025
 *
 * @author Nikolay Zinin
 */
@Slf4j
@Service
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final MappingService mappingService;
    private final WalletRepository walletRepository;

   @Override
    public Wallet getWalletById(String walletId) throws WalletNotFoundException {
        return walletRepository.findByWalletId(walletId).orElseThrow(() -> {
            log.error(String.format("Wallet with id %s not found", walletId));
            return new WalletNotFoundException("Wallet with Id "+ walletId + " not found ");
        });
    }

    @Override
    public BalanceDto getWalletBalance(String walletId) throws WalletNotFoundException {
        Wallet wallet = getWalletById(walletId);
        return mappingService.mapWalletToBalanceDto(wallet);
    }

    @Override
    public List<TransactionHistoryDto> getAllTransactionsByWalletId(String walletId) throws WalletNotFoundException {
        Wallet wallet = getWalletById(walletId);
        return wallet.getWalletTransactions().stream()
                .map(mappingService::transactionToDto)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void createWallet(String walletId) throws WalletIdRedundantException {
        checkWalletId(walletId);
        Wallet wallet = Wallet.builder()
                .walletId(walletId)
                .balance(BigDecimal.valueOf(0))
                .build();
        walletRepository.save(wallet);
    }
    @Override
    @Transactional
    public void deleteWallet(String walletId) throws WalletNotFoundException {
        Wallet wallet = walletRepository.findByWalletId(String.valueOf(walletId)).orElseThrow(() -> new WalletNotFoundException("Wallet not found with id: " + walletId));
        walletRepository.delete(wallet);
    }

    private void checkWalletId(String walletId) throws WalletIdRedundantException {
        Optional<Wallet> wallet = walletRepository.findByWalletId(walletId);
        if (wallet.isPresent()) {
            log.error(String.format("Wallet already exist with id %s", walletId));
            throw new WalletIdRedundantException("Wallet already exists with Id " + walletId);
        }
   }
}
