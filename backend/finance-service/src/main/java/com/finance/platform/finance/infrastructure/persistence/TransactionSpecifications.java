package com.finance.platform.finance.infrastructure.persistence;

import com.finance.platform.finance.domain.TransactionFilter;
import com.finance.platform.finance.domain.TransactionStatus;
import com.finance.platform.finance.domain.TransactionType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

class TransactionSpecifications {

    static Specification<TransactionJpaEntity> forFilter(String userSub, TransactionFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("userSub"), userSub));

            if (filter.typeGroup() != null) {
                switch (filter.typeGroup()) {
                    case "INCOME"    -> predicates.add(cb.equal(root.get("type"), TransactionType.CREDIT));
                    case "EXPENSE"   -> predicates.add(root.get("type").in(TransactionType.DEBIT, TransactionType.FEE));
                    case "TRANSFERS" -> predicates.add(cb.equal(root.get("type"), TransactionType.TRANSFER));
                }
            }

            if (filter.search() != null && !filter.search().isBlank()) {
                String pattern = "%" + filter.search().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("merchantName")), pattern),
                        cb.like(cb.lower(root.get("description")),  pattern)
                ));
            }

            if (filter.month() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("transactionDate"), filter.month().atDay(1)));
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("transactionDate"), filter.month().atEndOfMonth()));
            }

            if (filter.accountId() != null) {
                predicates.add(cb.equal(root.get("accountId"), filter.accountId()));
            }

            if (filter.category() != null && !filter.category().isBlank()) {
                predicates.add(cb.equal(root.get("category"), filter.category()));
            }

            if (filter.status() != null && !filter.status().isBlank()) {
                predicates.add(cb.equal(root.get("status"),
                        TransactionStatus.valueOf(filter.status().toUpperCase())));
            }

            if (query != null) {
                query.orderBy(cb.desc(root.get("transactionDate")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
