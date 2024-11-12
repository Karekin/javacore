package io.github.dunwu.javacore.concurrent.original.jmm;

import io.github.dunwu.javacore.concurrent.original.annotation.NotThreadSafe;

/**
 * UnsafeLazyInitialization
 * <p/>
 * Unsafe lazy initialization
 *
 * @author Brian Goetz and Tim Peierls
 */
@NotThreadSafe
public class UnsafeLazyInitialization {

    private static Resource resource;

    public static Resource getInstance() {
        if (resource == null) {
            resource = new Resource(); // unsafe publication
        }
        return resource;
    }

    static class Resource { }

}
