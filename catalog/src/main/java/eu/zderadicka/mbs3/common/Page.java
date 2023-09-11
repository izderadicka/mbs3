package eu.zderadicka.mbs3.common;

import org.eclipse.microprofile.config.ConfigProvider;

public class Page {
    private static int pageSize = ConfigProvider.getConfig()
            .getOptionalValue("mbs3.catalog.page-size", Integer.class).orElse(32);

    int offset;

    public Page(Integer number) {
        if (number == null) {
            number = 0;
        }
        assert number >= 0;
        offset = number * pageSize;
    }

    public int getOffset() {
        return offset;
    }

    public int getPageSize() {
        return pageSize;
    }
}
