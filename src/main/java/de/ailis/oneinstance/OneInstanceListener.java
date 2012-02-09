/*
 * Copyright (C) 2012 Klaus Reimer <k@ailis.de>
 * See LICENSE.txt for licensing information.
 */

package de.ailis.oneinstance;

/**
 * Interface for listeners which are informed about a new instance which
 * wants to start.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public interface OneInstanceListener
{
    /**
     * Called when a new application instance was created.
     * 
     * @param args
     *            The command line arguments which was given to the new
     *            application instance.
     * @return True if new application instance is allowed to start, false
     *         if not.
     */
    public boolean newInstanceCreated(String[] args);
}
