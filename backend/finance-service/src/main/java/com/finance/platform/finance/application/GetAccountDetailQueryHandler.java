package com.finance.platform.finance.application;

import com.finance.platform.finance.domain.Account;
import com.finance.platform.finance.domain.AccountRepository;
import com.finance.platform.finance.domain.Transaction;
import com.finance.platform.finance.domain.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class GetAccountDetailQueryHandler {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public GetAccountDetailQueryHandler(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {
        this.accountRepository     = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public AccountDetail handle(GetAccountDetailQuery query) {
        Account account = accountRepository.findById(query.accountId(), query.userSub())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        List<Transaction> txns = transactionRepository.findByAccountId(query.accountId(), query.userSub());

        BigDecimal balance = txns.stream()
                .map(TransactionSigns::signedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        YearMonth thisMonth = YearMonth.now();
        LocalDate from = thisMonth.atDay(1);
        LocalDate to   = thisMonth.atEndOfMonth();

        List<Transaction> monthTxns = txns.stream()
                .filter(t -> !t.transactionDate().isBefore(from) && !t.transactionDate().isAfter(to))
                .toList();

        BigDecimal monthChange = monthTxns.stream()
                .map(TransactionSigns::signedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal moneyInMonth = monthTxns.stream()
                .map(TransactionSigns::signedAmount)
                .filter(a -> a.signum() > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal moneyOutMonth = monthTxns.stream()
                .map(TransactionSigns::signedAmount)
                .filter(a -> a.signum() < 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .negate();

        BigDecimal avgDailyMonth = moneyOutMonth.divide(
                BigDecimal.valueOf(Math.max(1, LocalDate.now().getDayOfMonth())), 2, RoundingMode.HALF_UP);

        return new AccountDetail(account, balance, monthChange, moneyInMonth, moneyOutMonth, avgDailyMonth);
    }
}
