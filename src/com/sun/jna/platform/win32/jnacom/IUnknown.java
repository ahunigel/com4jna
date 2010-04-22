/*
 * Copyright 2009-2010 Digital Rapids Corp.
 * All rights reserved.
 */

package com.sun.jna.platform.win32.jnacom;

/**
 * Base interface for COM interfaces.  Provides IUnknown and a dispose() method.
 * @author scott.palmer
 */
public interface IUnknown {
    /** dispose is like release, but it forces the internal native pointer to NULL. */
    void dispose();
    @VTID(0)
    <T extends IUnknown> T queryInterface(Class<? extends IUnknown> comInterface);
    @VTID(1)
    void addRef();
    @VTID(2)
    void release();
}
