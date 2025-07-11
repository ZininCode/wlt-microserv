package se.vegas.tasknz.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.vegas.tasknz.dto.BalanceDto;
import se.vegas.tasknz.dto.TransactionHistoryDto;
import se.vegas.tasknz.exception.WalletIdRedundantException;
import se.vegas.tasknz.exception.WalletNotFoundException;
import se.vegas.tasknz.model.Wallet;
import se.vegas.tasknz.repository.WalletRepository;
import se.vegas.tasknz.service.impl.WalletServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Date: 25.01.2025
 *
 * @author Nikolay Zinin
 */
@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private MappingService mappingService;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    void getWalletByWalletId() throws WalletNotFoundException {
        String walletId = "walletId1";
        Wallet wallet = Wallet.builder()
                .walletId(walletId)
                .balance(BigDecimal.TEN)
                .build();
        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));

        Wallet result = walletService.getWalletById(walletId);

        assertNotNull(result);
        assertEquals(walletId, result.getWalletId());
        assertEquals(BigDecimal.TEN, result.getBalance());
    }

    @Test
    void getWalletByWalletId_NotFound() {
        String walletId = "walletId2";
        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletService.getWalletById(walletId));
    }

    @Test
    void getWalletBalance_ShouldMapWalletToBalanceDto() throws WalletNotFoundException {
        String walletId = "walletId3";
        Wallet wallet = Wallet.builder()
                .walletId(walletId)
                .balance(BigDecimal.valueOf(20))
                .build();
        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));
        when(mappingService.mapWalletToBalanceDto(wallet)).thenReturn(new BalanceDto(walletId, BigDecimal.valueOf(20)));

        BalanceDto result = walletService.getWalletBalance(walletId);

        assertNotNull(result);
        assertEquals(walletId, result.getWalletId());
        assertEquals(BigDecimal.valueOf(20), result.getBalance());
    }

    @Test
    void getAllTransactionsByWalletId_ShouldMapTransactionsToDtoList() throws WalletNotFoundException {
        String walletId = "walletId4";
        Wallet wallet = Wallet.builder()
                .walletId(walletId)
                .balance(BigDecimal.valueOf(30))
                .walletTransactions(new ArrayList<>())
                .build();
        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));

        List<TransactionHistoryDto> result = walletService.getAllTransactionsByWalletId(walletId);

        assertNotNull(result);
        assertEquals(0, result.size()); // Assuming wallet transactions are empty in this test
    }

    @Test
    void createWallet_WhenWalletIdIsNotRedundant_ShouldSaveWallet() {
        String walletId = "walletId5";
        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> walletService.createWallet(walletId));

        verify(walletRepository).save(Mockito.any());
    }

    @Test
    void createWallet_WhenWalletIdIsRedundant_ShouldThrowException() {
        String walletId = "walletId6";
        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(new Wallet()));

        assertThrows(WalletIdRedundantException.class, () -> walletService.createWallet(walletId));
    }
}