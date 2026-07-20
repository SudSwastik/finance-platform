package com.finance.platform.finance.application;

import java.util.UUID;

public record GetAccountDetailQuery(String userSub, UUID accountId) {}
