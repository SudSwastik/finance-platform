package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.Transaction;
import com.finance.platform.finance.domain.TransactionRepository;
import com.finance.platform.finance.domain.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class GetMonthlySummaryQueryHandler {

    private final TransactionRepository transactionRepository;

    public GetMonthlySummaryQueryHandler(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public MonthlySummary handle(GetMonthlySummaryQuery query) {
        LocalDate from = query.month().atDay(1);
        LocalDate to   = query.month().atEndOfMonth();

        List<Transaction> txns = transactionRepository.findByUserSubAndDateBetween(query.userSub(), from, to);

        BigDecimal income = txns.stream()
                .filter(t -> t.type() == TransactionType.CREDIT)
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal spending = txns.stream()
                .filter(t -> t.type() == TransactionType.DEBIT)
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new MonthlySummary(income, spending, income.subtract(spending));
    }
}
