package com.finance.platform.bff.web;

import com.finance.platform.bff.web.dto.MeResponseDto;
import com.finance.platform.security.PlatformUserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class MeController {

    @GetMapping("/me")
    public MeResponseDto me(@AuthenticationPrincipal PlatformUserPrincipal principal) {
        List<String> groups = principal.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toList());
        return new MeResponseDto(principal.userId().value(), groups);
    }
}
