package com.tedtalks.assignment.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        //todo: retrieve the user from the security context once the login is implemented

        return Optional.of("TED_TALKS_MS");
    }
}
