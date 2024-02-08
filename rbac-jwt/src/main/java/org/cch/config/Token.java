package org.cch.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@StaticInitSafe
@ConfigMapping(prefix = "jwt")
public interface Token {
    
    @WithDefault("3600")
    Long expireMilliseconds();

    String privateKeyPath();
}