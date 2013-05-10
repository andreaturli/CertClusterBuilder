package com.cloudera.cert;

import brooklyn.config.BrooklynProperties;
import brooklyn.entity.basic.Attributes;
import brooklyn.entity.basic.Lifecycle;
import brooklyn.event.SensorEvent;
import brooklyn.event.SensorEventListener;
import brooklyn.launcher.BrooklynLauncher;
import brooklyn.launcher.BrooklynServerDetails;
import brooklyn.location.Location;
import brooklyn.location.basic.BasicLocationRegistry;
import brooklyn.management.ManagementContext;
import brooklyn.util.CommandLineUtil;
import io.cloudsoft.cloudera.SampleClouderaManagedCluster;
import io.cloudsoft.cloudera.brooklynnodes.ClouderaManagerNode;
import io.cloudsoft.cloudera.brooklynnodes.WhirrClouderaManager;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 *
 * @author daniel@cloudera.com
 */
public class CertClusterBuilder {
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
                CertClusterBuilder.deployCluster(cloudCode, cloudSpecifier, identity, credentials);
            }
        });
    }
    
    private static void deployCluster(String cloudCode, String cloudSpecifier, String identity, String credentials) {
        BrooklynServerDetails details = BrooklynLauncher.newLauncher().
                webconsolePort(CommandLineUtil.getCommandLineOption(Collections.EMPTY_LIST, "--port", "8081+")).
                launch();
        
        port = details.getWebServer().getActualPort();

        // get the mgmt context (and also the webconsole URL) from this details container
        ManagementContext mgmt;
        mgmt = details.getManagementContext();

        
        String cloudName = cloudCode;
        
        if (cloudSpecifier != null) {
            cloudName += ":" + cloudSpecifier;
        }

        // to set cloud credentials (needed to roll-out the application)
        ((BrooklynProperties) mgmt.getConfig()).put("brooklyn.jclouds." + cloudCode + ".identity", identity);
        ((BrooklynProperties) mgmt.getConfig()).put("brooklyn.jclouds." + cloudCode + ".credential", credentials);

        final SampleClouderaManagedCluster app1 = new SampleClouderaManagedCluster();
        app1.setDisplayName("Brooklyn Cloudera Managed Cluster");
        
        details.getManagementContext().getEntityManager().manage(app1);
        mgmt.getSubscriptionManager().subscribe(app1, Attributes.SERVICE_STATE, new SensorEventListener<Lifecycle>() {
            @Override
            public void onEvent(SensorEvent<Lifecycle> event) {
                if (event.getValue() == Lifecycle.RUNNING) {
                    updateStatus(event.getValue(), app1.getAttribute(ClouderaManagerNode.CLOUDERA_MANAGER_URL));
                } else {
                    updateStatus(event.getValue());
                }
            }
        });
        mgmt.getSubscriptionManager().subscribe(app1, SampleClouderaManagedCluster.CLOUDERA_MANAGER_URL, new SensorEventListener<String>() {
            @Override
            public void onEvent(SensorEvent<String> event) {
                updateStatus(app1.getAttribute(Attributes.SERVICE_STATE), event.getValue());
            }
        });

        List<Location> locations = new BasicLocationRegistry(details.getManagementContext()).resolve(Collections.singleton(cloudName));

        // to start app (NB this call is synchronous, takes about 10m to return)
        app1.start(locations);

        // to find out the CDH endpoint
        System.out.println("CDH Manager running at: " + app1.getManager().getAttribute(WhirrClouderaManager.CLOUDERA_MANAGER_URL));

        // to collect metrics (again, synchronous)
//        String directory = app1.getServices().collectMetrics();
//        System.out.println("Metrics stored in: " + directory);
//
//        // to stop:
//        app1.stop();

        // can also start multiple apps...            
//            app2.start(locations);
    }
    
    private static void updateStatus(Lifecycle appStatus) {
        frame.setStatus(appStatus.toString().toUpperCase());
    }
    
    private static void updateStatus(Lifecycle appStatus, String url) {
        frame.setStatus(appStatus.toString().toUpperCase() + ": " + url);
    }
}
