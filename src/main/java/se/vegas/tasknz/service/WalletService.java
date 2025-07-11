package se.vegas.tasknz.service;



import se.vegas.tasknz.dto.BalanceDto;
import se.vegas.tasknz.dto.TransactionHistoryDto;
import se.vegas.tasknz.exception.WalletIdRedundantException;
import se.vegas.tasknz.exception.WalletNotFoundException;
import se.vegas.tasknz.model.Wallet;
import java.util.List;

/**
 * Date: 10.01.2025
 *
 * @author Nikolay Zinin
 */

public interface WalletService {

    Wallet getWalletById(String walletId) throws WalletNotFoundException;

    BalanceDto getWalletBalance(String walletId) throws WalletNotFoundException;

    List<TransactionHistoryDto> getAllTransactionsByWalletId(String walletId) throws WalletNotFoundException;

    void createWallet(String walletId) throws WalletIdRedundantException;

    void deleteWallet(String walletId) throws WalletNotFoundException;

}
