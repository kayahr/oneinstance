/*
 * Copyright (C) 2012 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.oneinstance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.prefs.Preferences;

import org.junit.After;
import org.junit.Test;

import de.ailis.oneinstance.OneInstance;
import de.ailis.oneinstance.OneInstanceListener;

/**
 * Tests the OneInstance class.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class OneInstanceTest
{
    /**
     * Unregisteres the test instance after each test.
     */
    @After
    public void cleanUp()
    {
        OneInstance.getInstance().unregisterInstance(OneInstanceTest.class);
    }
    
    /**
     * Without a listener additional instances should be prevented.
     */
    @Test
    public void testWithoutListener()
    {
        OneInstance one = OneInstance.getInstance();

        // First instance must be accepted.
        boolean firstResult = one.registerInstance(OneInstanceTest.class,
            new String[] { "arg1", "arg2" });
        assertTrue(firstResult);

        // Check if port is written to preferences
        int port = Preferences.userNodeForPackage(OneInstanceTest.class).
            getInt(OneInstance.PORT_KEY, -1);
        assertTrue(port >= OneInstance.MIN_PORT && port <= OneInstance.MAX_PORT);

        // Second instance must be refused.
        boolean secondResult = one.registerInstance(OneInstanceTest.class,
            new String[] { "arg1", "arg2" });
        assertFalse(secondResult);

        // Third instance must also be refused.
        boolean thirdResult = one.registerInstance(OneInstanceTest.class,
            new String[] { "arg3", "arg4" });
        assertFalse(thirdResult);
        
        // Check if port is still the same
        int portAfter = Preferences.userNodeForPackage(OneInstanceTest.class).
            getInt(OneInstance.PORT_KEY, -1);
        assertEquals(port, portAfter);
        
        // Unregister instance and check if port has been removed
        one.unregisterInstance(OneInstance.class);
        port = Preferences.userNodeForPackage(OneInstanceTest.class).
            getInt(OneInstance.PORT_KEY, -1);
        assertEquals(-1, port);
    }

    /**
     * Test if listener receives arguments and can decide the fate of the
     * new instances
     */
    @Test
    public void testWithListener()
    {
        OneInstance one = OneInstance.getInstance();

        // First instance must be accepted.
        boolean firstResult = one.registerInstance(OneInstanceTest.class,
            new String[] { "arg1", "arg2" });
        assertTrue(firstResult);
        
        // Install listener
        one.addListener(new OneInstanceListener()
        {
            @Override 
            public boolean newInstanceCreated(String[] args)
            {
                return args[0].equals("letMeIn");
            }
        });

        // Instance without "letMeIn" argument must be refused
        boolean secondResult = one.registerInstance(OneInstanceTest.class,
            new String[] { "dontLetMeIn" });
        assertFalse(secondResult);

        // Instance with "letMeIn" argument must be accepted
        boolean thirdResult = one.registerInstance(OneInstanceTest.class,
            new String[] { "letMeIn" });
        assertTrue(thirdResult);
    }
}