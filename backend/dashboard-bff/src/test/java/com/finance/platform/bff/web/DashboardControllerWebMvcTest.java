package com.finance.platform.bff.web;

import com.finance.platform.bff.web.dto.OverviewResponseDto;
import com.finance.platform.bff.application.OverviewComposer;
import com.finance.platform.bff.client.dto.TotalBudgetsSectionDto;
import com.finance.platform.bff.support.OverviewStubSections;
import com.finance.platform.common.domain.UserId;
import com.finance.platform.security.PlatformSecurityConfiguration;
import com.finance.platform.security.SecurityContextUserIdResolver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {DashboardController.class, HealthController.class})
@Import(PlatformSecurityConfiguration.class)
class DashboardControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OverviewComposer overviewComposer;

    @MockBean
    private SecurityContextUserIdResolver userIdResolver;

    @Test
    void health_isPublic() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void overview_withoutAuth_isForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard/overview"))
                .andExpect(status().isForbidden());
    }

    @Test
    void overview_withDevHeader_returns200() throws Exception {
        var userId = UserId.of("seed-user-alice");
        var overview = new OverviewResponseDto(
                new TotalBudgetsSectionDto("6400", "Expenses", List.of()),
                new OverviewStubSections().spending(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        when(userIdResolver.requireCurrentUserId()).thenReturn(userId);
        when(overviewComposer.compose(userId)).thenReturn(Mono.just(overview));

        mockMvc.perform(get("/api/v1/dashboard/overview")
                        .header("X-Dev-User-Sub", "seed-user-alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalBudgets.total").value("6400"));
    }
}
