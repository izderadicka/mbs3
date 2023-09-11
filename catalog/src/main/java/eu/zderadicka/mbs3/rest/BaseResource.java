package eu.zderadicka.mbs3.rest;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.inject.Inject;

public abstract class BaseResource {

    protected static int pageSize = ConfigProvider.getConfig()
            .getOptionalValue("mbs3.catalog.page-size", Integer.class).orElse(32);
}
