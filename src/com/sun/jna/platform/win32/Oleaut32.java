/*
 * Copyright 2010 Digital Rapids Corp.
 * All rights reserved.
 */

package com.sun.jna.platform.win32;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author scott.palmer
 */
public interface Oleaut32 extends W32API {
    /** Standard options to use the unicode version of a w32 API. */
    @SuppressWarnings("unchecked")
    Map UNICODE_OPTIONS = new HashMap() {

        {
            put(OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
            put(OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
        }
    };
    Oleaut32 INSTANCE = (Oleaut32) Native.loadLibrary("oleaut32", Oleaut32.class, UNICODE_OPTIONS);

    Pointer SysAllocString(String str);
    void SysFreeString(Pointer bstr);

}
