package com.finance.platform.goals.web;

import com.finance.platform.common.domain.Money;
import com.finance.platform.goals.application.ContributeToGoalCommandHandler;
import com.finance.platform.goals.application.CreateGoalCommand;
import com.finance.platform.goals.application.CreateGoalCommandHandler;
import com.finance.platform.goals.application.DeleteGoalCommandHandler;
import com.finance.platform.goals.application.ListGoalContributionsQueryHandler;
import com.finance.platform.goals.application.ListGoalsQuery;
import com.finance.platform.goals.application.ListGoalsQueryHandler;
import com.finance.platform.goals.application.UpdateGoalCommandHandler;
import com.finance.platform.goals.domain.Goal;
import com.finance.platform.goals.domain.GoalNotFoundException;
import com.finance.platform.security.PlatformSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GoalController.class)
@Import({PlatformSecurityConfiguration.class, GoalExceptionHandler.class, GoalDtoMapper.class})
class GoalControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListGoalsQueryHandler listGoalsQueryHandler;

    @MockBean
    private CreateGoalCommandHandler createGoalCommandHandler;

    @MockBean
    private UpdateGoalCommandHandler updateGoalCommandHandler;

    @MockBean
    private DeleteGoalCommandHandler deleteGoalCommandHandler;

    @MockBean
    private ListGoalContributionsQueryHandler listGoalContributionsQueryHandler;

    @MockBean
    private ContributeToGoalCommandHandler contributeToGoalCommandHandler;

    @Test
    void listGoals_withoutAuth_returnsForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/goals"))
                .andExpect(status().isForbidden());
    }

    @Test
    void listGoals_withDevHeader_returns200() throws Exception {
        var goal = new Goal(UUID.fromString("c1000001-0000-4000-8000-000000000001"), "seed-user-alice",
                "Emergency Fund", "goal.positive", Money.of("15600"), Money.of("20000"), LocalDate.of(2026, 12, 15), 0L);
        when(listGoalsQueryHandler.handle(any(ListGoalsQuery.class))).thenReturn(List.of(goal));

        mockMvc.perform(get("/api/v1/goals").header("X-Dev-User-Sub", "seed-user-alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Emergency Fund"))
                .andExpect(jsonPath("$[0].current").value("15600"));
    }

    @Test
    void createGoal_withBlankName_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/goals")
                        .header("X-Dev-User-Sub", "seed-user-alice")
                        .contentType("application/json")
                        .content("{\"name\":\"\",\"target\":\"1000\",\"targetDate\":\"2027-01-01\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"));
    }

    @Test
    void createGoal_withValidBody_returns201() throws Exception {
        var goal = new Goal(UUID.randomUUID(), "seed-user-alice", "New Car", "goal.warning",
                Money.zero(), Money.of("30000"), LocalDate.of(2027, 1, 1), 0L);
        when(createGoalCommandHandler.handle(any(CreateGoalCommand.class))).thenReturn(goal);

        mockMvc.perform(post("/api/v1/goals")
                        .header("X-Dev-User-Sub", "seed-user-alice")
                        .contentType("application/json")
                        .content("{\"name\":\"New Car\",\"target\":\"30000\",\"targetDate\":\"2027-01-01\",\"colorToken\":\"goal.warning\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Car"));
    }

    @Test
    void updateGoal_whenGoalMissing_returns404() throws Exception {
        var goalId = UUID.randomUUID();
        when(updateGoalCommandHandler.handle(any())).thenThrow(new GoalNotFoundException(goalId));

        mockMvc.perform(patch("/api/v1/goals/" + goalId)
                        .header("X-Dev-User-Sub", "seed-user-alice")
                        .contentType("application/json")
                        .content("{\"name\":\"Renamed\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("NOT_FOUND"));
    }
}
