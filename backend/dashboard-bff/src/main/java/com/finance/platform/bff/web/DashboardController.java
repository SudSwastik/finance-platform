package com.finance.platform.bff.web;

import com.finance.platform.bff.application.OverviewComposer;
import com.finance.platform.bff.web.dto.OverviewResponseDto;
import com.finance.platform.common.tenant.TenantContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final OverviewComposer overviewComposer;

    public DashboardController(OverviewComposer overviewComposer) {
        this.overviewComposer = overviewComposer;
    }

    @GetMapping("/overview")
    public OverviewResponseDto getOverview() {
        String userSub = TenantContext.requireUserSub();
        return overviewComposer.compose(userSub).block();
    }
}
