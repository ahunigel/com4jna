/*
 * Copyright 2010 Digital Rapids Corporation.
 * All rights reserved.
 */

package com.sun.jna.platform.win32.jnacom;

import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.ptr.PointerByReference;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author scott.palmer
 */
public class COMTest {

    public COMTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitializeCOM() {
        assertFalse(ComObject.isComInitialized());
        ComObject.initializeCOM();
        assertTrue(ComObject.isComInitialized());
    }

    @Test
    public void testDispose() {
        System.out.println("dispose");
        ComObject.initializeCOM();
        PointerByReference ptr = new PointerByReference();
        HRESULT hr = Shell32.INSTANCE.SHGetDesktopFolder(ptr);
        IUnknown iUnknown = ComObject.wrapNativeInterface(ptr.getValue(), IUnknown.class);
        iUnknown.dispose();
        try {
            iUnknown.addRef();
        } catch (NullPointerException npe) {
            return;
        }
        fail("A NPE should have been thrown after the interface was disposed.");
    }

    @Test
    public void testAddRefRelease() {
        System.out.println("addRef/release");
        ComObject.initializeCOM();
        IUnknown iUnknown = ComObject.createInstance(IUnknown.class, "{6BF52A52-394A-11D3-B153-00C04F79FAA6}");

        iUnknown.addRef();
        iUnknown.addRef();
        assertTrue(iUnknown.addRef() == 4);

        iUnknown.release();
        iUnknown.release();
        assertTrue(iUnknown.release() == 1);

        // this is the final release that will free the object
        assertTrue(iUnknown.release() == 0);

        // this should fail but it returns 0 as well
        //assertTrue(iUnknown.release() == 0);
    }

    /**
     * Test of queryInterface method, of class IUnknown.
     */
    @Test
    public void testQueryInterface() {
        System.out.println("queryInterface");
        ComObject.initializeCOM();
        PointerByReference ptr = new PointerByReference();
        HRESULT hr = Shell32.INSTANCE.SHGetDesktopFolder(ptr);
        IUnknown iUnknown = ComObject.wrapNativeInterface(ptr.getValue(), IUnknown.class);

        IShellFolder_ iShellFolder = iUnknown.queryInterface(IShellFolder_.class);
        assertTrue (iShellFolder != null);

        iShellFolder.dispose();
        iUnknown.dispose();
    }


    @Test
    public void testCreateInstance() {
        System.out.println("createInstance");
        ComObject.initializeCOM();
        // Make an instance of Windows Media Player 7, 9, or 10
        // IWMPPlayer or IWMPCore would usually be returned
        IUnknown sample = ComObject.createInstance(IUnknown.class, "{6BF52A52-394A-11D3-B153-00C04F79FAA6}");
        assertTrue(sample != null);
        sample.dispose();
    }

    /**
     * Test of copy method, of class ComObject.
     */
    @Test
    public void testCopy() {
        System.out.println("copy");
        ComObject.initializeCOM();
        PointerByReference ptr = new PointerByReference();
        HRESULT hr = Shell32.INSTANCE.SHGetDesktopFolder(ptr);
        IShellFolder_ sample = ComObject.wrapNativeInterface(ptr.getValue(), IShellFolder_.class);
        //IUnknown sample = ComObject.createInstance(IUnknown.class, "{}");

        IUnknown sampleCopy = ComObject.copy(sample);
        assertTrue(sampleCopy != null);
        // Now sample and sampleCopy refer to the same native Object.

        // makes some calls on either interface copy, they will happen on the same
        // native object

        // disposing one will not free the underlying COM object
        // (but too many calls to release() will!)
        sampleCopy.dispose();

        // until all copies are disposed.
        sample.dispose();
    }

    @Test
    public void testCreateComThread() throws InterruptedException {
        System.out.println("createComThread");
        final boolean [] success = new boolean[1];
        Thread t = ComObject.createComThread(new Runnable() {
            public void run() {
                success[0] = ComObject.isComInitialized();
            }
        });
        t.start();
        t.join();
        assertTrue(success[0]);
    }

}