package se.vegas.tasknz.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import se.vegas.tasknz.dto.*;
import se.vegas.tasknz.exception.NotEnoughCreditException;
import se.vegas.tasknz.exception.WalletIdRedundantException;
import se.vegas.tasknz.exception.TransactionIdRedundantException;
import se.vegas.tasknz.exception.WalletNotFoundException;
import se.vegas.tasknz.model.TransactionType;
import se.vegas.tasknz.service.TransactionService;
import se.vegas.tasknz.service.WalletService;

import java.util.List;

/**
 * Date: 10.01.2025
 *
 * @author Nikolay Zinin
 */
@RestController
@RequestMapping("/api/wallet")
@AllArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final TransactionService transactionService;

    @PostMapping (path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createWallet(@RequestBody WalletDto walletDto) throws WalletIdRedundantException {
        walletService.createWallet(walletDto.getWalletId());
    }

    @PostMapping (path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteWallet(@RequestBody WalletDto walletDto) throws WalletNotFoundException {
        walletService.deleteWallet(walletDto.getWalletId());
    }

    @GetMapping(path = "/balance")
    public BalanceDto getWalletCurrentBalance(@RequestParam String walletId) throws WalletNotFoundException {
        return walletService.getWalletBalance(walletId);
    }

    @PostMapping(path = "/credit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addCreditToWalletBalance(@RequestBody TransactionDTO transactionDTO)
            throws WalletNotFoundException, TransactionIdRedundantException, NotEnoughCreditException {
        transactionService.makeTransaction(transactionDTO, TransactionType.CREDIT);
    }

    @PostMapping(path = "/debit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void withdrawalFromWalletBalance(@RequestBody TransactionDTO transactionDTO)
            throws WalletNotFoundException, NotEnoughCreditException, TransactionIdRedundantException {
        transactionService.makeTransaction(transactionDTO, TransactionType.DEBIT);
    }

    @GetMapping ("/history")
    public List<TransactionHistoryDto> getWalletTransactionHistory(@RequestParam String walletId)
            throws WalletNotFoundException {
        return walletService.getAllTransactionsByWalletId(walletId);
    }
}
