package com.infyniteloop.runningroom.config;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.util.UUID;

// Centralized definition
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = UUID.class))
public class TenantFilterConfig {
    // Empty class – only serves to hold the central annotation
}
