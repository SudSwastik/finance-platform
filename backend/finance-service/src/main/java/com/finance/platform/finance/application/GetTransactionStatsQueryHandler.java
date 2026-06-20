package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.Transaction;
import com.finance.platform.finance.domain.TransactionRepository;
import com.finance.platform.finance.domain.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class GetTransactionStatsQueryHandler {

    private final TransactionRepository transactionRepository;

    public GetTransactionStatsQueryHandler(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionStats handle(GetTransactionStatsQuery query) {
        LocalDate from = query.month().atDay(1);
        LocalDate to   = query.month().atEndOfMonth();

        List<Transaction> txns = transactionRepository.findByUserSubAndDateBetween(
                query.userSub(), from, to);

        BigDecimal moneyIn = txns.stream()
                .filter(t -> t.type() == TransactionType.CREDIT)
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long moneyInCount = txns.stream()
                .filter(t -> t.type() == TransactionType.CREDIT)
                .count();

        BigDecimal moneyOut = txns.stream()
                .filter(t -> t.type() == TransactionType.DEBIT || t.type() == TransactionType.FEE)
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long moneyOutCount = txns.stream()
                .filter(t -> t.type() == TransactionType.DEBIT || t.type() == TransactionType.FEE)
                .count();

        return new TransactionStats(
                moneyIn, moneyInCount,
                moneyOut, moneyOutCount,
                moneyIn.subtract(moneyOut),
                txns.size());
    }
}
