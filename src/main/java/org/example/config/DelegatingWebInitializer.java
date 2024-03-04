package org.example.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class DelegatingWebInitializer extends AbstractSecurityWebApplicationInitializer {
    @Override
    protected boolean enableHttpSessionEventPublisher() {
        return true;
    }
}
