package com.github.vspro.cg.template.egcimpl;

import com.github.vspro.cg.config.context.ContextHolder;
import com.github.vspro.cg.template.EngineClient;

public abstract class AbstractEngineClient implements EngineClient {

    protected ContextHolder contextHolder;

    public AbstractEngineClient(ContextHolder contextHolder) {
        this.contextHolder = contextHolder;
    }

    public ContextHolder getContextHolder() {
        return contextHolder;
    }
}
