package com.samba;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jcifs.context.BaseContext;
import jcifs.CIFSContext;
import jcifs.CIFSException;
import jcifs.config.PropertyConfiguration;
import com.samba.CIFSSingletonContext;

import jcifs.context.SingletonContext;

/**
 * Global singleton context
 *
 * @author mbechler
 * Borrowed and modified by Shawn Stark
 * Used outsid of JCIFS to set up app specific implementation.
 */
public class CIFSSingletonContext extends BaseContext implements CIFSContext {

    private static final Logger log = LoggerFactory.getLogger(com.samba.CIFSSingletonContext.class);
    private static com.samba.CIFSSingletonContext INSTANCE;


    /**
     * Initialize singleton context using custom properties
     *
     * This method can only be called once.
     *
     * @param props
     * @throws CIFSException
     */
    public static synchronized final void init ( Properties props ) throws CIFSException {
        if ( INSTANCE != null ) {
            throw new CIFSException("Singleton context is already initialized");
        }
        Properties p = new Properties();
        try {
            String filename = System.getProperty("jcifs.properties");
            if ( filename != null && filename.length() > 1 ) {

                try ( FileInputStream in = new FileInputStream(filename) ) {
                    p.load(in);
                }
            }

        }
        catch ( IOException ioe ) {
            log.error("Failed to load config", ioe); //$NON-NLS-1$
        }
        p.putAll(System.getProperties());
        if ( props != null ) {
            p.putAll(props);
        }
        INSTANCE = new com.samba.CIFSSingletonContext(p);
    }



    /**
     * Get singleton context
     *
     * The singleton context will use system properties for configuration as well as values specified in a file
     * specified through this <tt>jcifs.properties</tt> system property.
     *
     * @return a global context, initialized on first call
     */
    public static synchronized final com.samba.CIFSSingletonContext getInstance () {
        if ( INSTANCE == null ) {
            try {
                log.debug("Initializing singleton context");
                init(null);
            }
            catch ( CIFSException e ) {
                log.error("Failed to create singleton JCIFS context", e);
            }
        }
        return INSTANCE;
    }


    /**
     * This static method registers the SMB URL protocol handler which is
     * required to use SMB URLs with the <tt>java.net.URL</tt> class. If this
     * method is not called before attempting to create an SMB URL with the
     * URL class the following exception will occur:
     * <blockquote>
     *
     * <pre>
     * Exception MalformedURLException: unknown protocol: smb
     *     at java.net.URL.&lt;init&gt;(URL.java:480)
     *     at java.net.URL.&lt;init&gt;(URL.java:376)
     *     at java.net.URL.&lt;init&gt;(URL.java:330)
     *     at jcifs.smb.SmbFile.&lt;init&gt;(SmbFile.java:355)
     *     ...
     * </pre>
     *
     * <blockquote>
     *
     */
    public static void registerSmbURLHandler () {
        com.samba.CIFSSingletonContext.getInstance();
        String pkgs = System.getProperty("java.protocol.handler.pkgs");
        if ( pkgs == null ) {
            System.setProperty("java.protocol.handler.pkgs", "jcifs");
        }
        else if ( pkgs.indexOf("jcifs") == -1 ) {
            pkgs += "|jcifs";
            System.setProperty("java.protocol.handler.pkgs", pkgs);
        }
    }


    /**
     *
     */
    private CIFSSingletonContext ( Properties p ) throws CIFSException {
        super(new PropertyConfiguration(p));
    }

}
