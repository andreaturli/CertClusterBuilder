package com.cloudera.cert;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.getOnlyElement;
import io.cloudsoft.cloudera.SampleClouderaManagedClusterInterface;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brooklyn.config.BrooklynProperties;
import brooklyn.entity.basic.Entities;
import brooklyn.entity.basic.Lifecycle;
import brooklyn.entity.proxying.EntitySpecs;
import brooklyn.launcher.BrooklynLauncher;
import brooklyn.location.cloud.CloudLocationConfig;

import com.google.common.base.Charsets;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.io.Files;

/**
 *
 * @author daniel@cloudera.com
 */
public class CertClusterBuilder {
    
    private static final Logger log = LoggerFactory.getLogger(CertClusterBuilder.class);
    private static int port = 8081;
    private static ClusterBuilderFrame frame = null;
    private static final ExecutorService pool = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "CertClusterDeployThread");
            
            t.setDaemon(true);
            
            return t;
        }
    });
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        frame = new ClusterBuilderFrame();
        frame.pack();
        frame.setVisible(true);
        
        // Tee all of the stdout console logging; send to stdout _and_ to frame
        PrintStream out = new PrintStream(new OutputStream() {
            private StringBuilder line = new StringBuilder();
            
            @Override
            public void write(int i) throws IOException {
                if (i != '\r') {
                    line.append((char)i);
                    
                    if (i == '\n') {
                        frame.addOutput(line.toString());
                    }
                }
            }
        });
        
        System.setOut(out);
    }
    
    public static int getPort() {
        return port;
    }
    
    static void launch(final String cloudCode, final String cloudSpecifier, final String identity, final String credentials) {
        pool.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    CertClusterBuilder.deployCluster(cloudCode, cloudSpecifier, identity, credentials);
                } catch (Exception e) {
                    log.error("Cannot deploy cluster with provider: " + cloudCode + 
                            (cloudSpecifier != null ? ":" + cloudSpecifier : "") + ", identity: " + identity + 
                            ", credential: " + credentials, e);
                    System.exit(1);
                }
            }
        });
    }
    
    private static void deployCluster(String cloudCode, String cloudSpecifier, String identity, String credentials) throws IOException {
        BrooklynProperties brooklynProperties = BrooklynProperties.Factory.newEmpty();
        String access_identity = checkNotNull(Strings.emptyToNull(identity), "identity must not be null");
        String access_credential = checkNotNull(Strings.emptyToNull(credentials), "credentials must not be null");
        brooklynProperties.put(CloudLocationConfig.ACCESS_IDENTITY, access_identity);
        brooklynProperties.put(CloudLocationConfig.ACCESS_CREDENTIAL, access_credential);
        String location = checkNotNull(Strings.emptyToNull(cloudCode), "cloudCode must not be null");
        if(!Strings.isNullOrEmpty(cloudSpecifier)) { location += ":" + cloudSpecifier; }

        log.info("Start time for CDH deployment on '" + location +"'");
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        BrooklynLauncher launcher = BrooklynLauncher.newInstance()
                                                    .application(
                                                            EntitySpecs.appSpec(SampleClouderaManagedClusterInterface.class)
                                                            .displayName("Brooklyn Cloudera Managed Cluster"))
                                                    .webconsolePort(port)
                                                    .location(location)
                                                    .brooklynProperties(brooklynProperties)
                                                    .shutdownOnExit(true)
                                                    .start();
        Entities.dumpInfo(launcher.getApplications());
        final SampleClouderaManagedClusterInterface app = 
                (SampleClouderaManagedClusterInterface) getOnlyElement(launcher.getApplications());
        app.startServices(true, false);
        stopwatch.stop(); 
        log.info("Time to deploy " + location + ": " + stopwatch.elapsed(TimeUnit.SECONDS) + " seconds");
    }

}
