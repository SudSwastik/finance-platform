package com.finance.platform.identity.web;

import com.finance.platform.identity.application.GetUserProfileQueryHandler;
import com.finance.platform.identity.application.ProvisionMyProfileCommandHandler;
import com.finance.platform.identity.application.UserProfile;
import com.finance.platform.security.PlatformSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IdentityController.class)
@Import(PlatformSecurityConfiguration.class)
class IdentityControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetUserProfileQueryHandler queryHandler;

    @MockBean
    private ProvisionMyProfileCommandHandler provisionCommandHandler;

    @Test
    void me_withoutAuth_isForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/identity/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    void me_withDevHeader_returnsProfile() throws Exception {
        UUID userId   = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        when(queryHandler.handle(any()))
                .thenReturn(new UserProfile(userId, tenantId, "seed-user-alice", "alice@example.com"));

        mockMvc.perform(get("/api/v1/identity/me")
                        .header("X-Dev-User-Sub", "seed-user-alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userSub").value("seed-user-alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void provisionMe_withValidBody_returns200() throws Exception {
        UUID userId   = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        when(provisionCommandHandler.handle(any()))
                .thenReturn(new UserProfile(userId, tenantId, "seed-user-alice", "alice@example.com"));

        mockMvc.perform(post("/api/v1/identity/me")
                        .header("X-Dev-User-Sub", "seed-user-alice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Alice","email":"alice@example.com","accountType":"personal"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userSub").value("seed-user-alice"));
    }

    @Test
    void provisionMe_withInvalidAccountType_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/identity/me")
                        .header("X-Dev-User-Sub", "seed-user-alice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Alice","email":"alice@example.com","accountType":"nonsense"}
                                """))
                .andExpect(status().isBadRequest());
    }
}
