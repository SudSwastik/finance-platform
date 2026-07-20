package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.Account;
import com.finance.platform.finance.domain.AccountRepository;
import com.finance.platform.finance.domain.Asset;
import com.finance.platform.finance.domain.AssetRepository;
import com.finance.platform.finance.domain.InvestmentTransaction;
import com.finance.platform.finance.domain.InvestmentTransactionRepository;
import com.finance.platform.finance.domain.Transaction;
import com.finance.platform.finance.domain.TransactionRepository;
import com.finance.platform.finance.domain.TransactionType;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ListTradesQueryHandler {

    private final TransactionRepository transactionRepository;
    private final InvestmentTransactionRepository investmentTransactionRepository;
    private final AssetRepository assetRepository;
    private final AccountRepository accountRepository;

    public ListTradesQueryHandler(
            TransactionRepository transactionRepository,
            InvestmentTransactionRepository investmentTransactionRepository,
            AssetRepository assetRepository,
            AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.investmentTransactionRepository = investmentTransactionRepository;
        this.assetRepository = assetRepository;
        this.accountRepository = accountRepository;
    }

    public List<TradeWithDetails> handle(ListTradesQuery query) {
        List<Transaction> trades = transactionRepository.findAllByUserSub(query.userSub()).stream()
                .filter(t -> t.type() == TransactionType.BUY || t.type() == TransactionType.SELL)
                .sorted(Comparator.comparing(Transaction::transactionDate).reversed())
                .toList();

        Set<UUID> transactionIds = trades.stream().map(Transaction::id).collect(Collectors.toSet());

        Map<UUID, InvestmentTransaction> investmentByTransactionId = investmentTransactionRepository
                .findByTransactionIds(transactionIds).stream()
                .collect(Collectors.toMap(InvestmentTransaction::transactionId, Function.identity()));

        Map<UUID, Asset> assetsById = assetRepository.findAll().stream()
                .collect(Collectors.toMap(Asset::id, Function.identity()));

        Map<UUID, String> accountNames = accountRepository.findAllByUserSub(query.userSub()).stream()
                .collect(Collectors.toMap(Account::id, Account::name));

        return trades.stream()
                .map(t -> toTradeWithDetails(t, investmentByTransactionId, assetsById, accountNames))
                .filter(Objects::nonNull)
                .toList();
    }

    private TradeWithDetails toTradeWithDetails(
            Transaction t,
            Map<UUID, InvestmentTransaction> investmentByTransactionId,
            Map<UUID, Asset> assetsById,
            Map<UUID, String> accountNames) {
        InvestmentTransaction inv = investmentByTransactionId.get(t.id());
        if (inv == null) return null;
        Asset asset = assetsById.get(inv.assetId());
        if (asset == null) return null;
        return new TradeWithDetails(t, inv, asset, accountNames.getOrDefault(t.accountId(), ""));
    }
}
