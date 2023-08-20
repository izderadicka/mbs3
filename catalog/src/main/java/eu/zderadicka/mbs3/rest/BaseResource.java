package eu.zderadicka.mbs3.rest;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public abstract class BaseResource {
    @ConfigProperty(name = "mbs3.catalog.page-size", defaultValue = "32")
    protected int pageSize;
}
