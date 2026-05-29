package com.finance.platform.bff.web;

import com.finance.platform.bff.web.dto.OverviewResponseDto;
import com.finance.platform.bff.application.OverviewComposer;
import com.finance.platform.security.SecurityContextUserIdResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final OverviewComposer overviewComposer;
    private final SecurityContextUserIdResolver userIdResolver;

    public DashboardController(OverviewComposer overviewComposer, SecurityContextUserIdResolver userIdResolver) {
        this.overviewComposer = overviewComposer;
        this.userIdResolver = userIdResolver;
    }

    @GetMapping("/overview")
    public OverviewResponseDto getOverview() {
        var userId = userIdResolver.requireCurrentUserId();
        return overviewComposer.compose(userId).block();
    }
}
