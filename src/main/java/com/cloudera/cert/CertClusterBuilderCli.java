package com.cloudera.cert;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.getOnlyElement;
import io.cloudsoft.cloudera.SampleClouderaManagedClusterInterface;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brooklyn.config.BrooklynProperties;
import brooklyn.entity.basic.Entities;
import brooklyn.entity.proxying.EntitySpecs;
import brooklyn.launcher.BrooklynLauncher;
import brooklyn.location.cloud.CloudLocationConfig;
import brooklyn.util.CommandLineUtil;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 *
 * @author andrea.turli@cloudsoftcorp.com
 */
public class CertClusterBuilderCli {
    
    private static final Logger log = LoggerFactory.getLogger(CertClusterBuilderCli.class);
    
    private static final String DEFAULT_LOCATION = "cloudfirst";
    private static int PARAMETERS = 3;
    private static String INVALID_SYNTAX = 
            "Invalid number of parameters. Syntax is: provider identity credential. Option available: --port";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        /*
        if (args.length < PARAMETERS)
            throw new IllegalArgumentException(INVALID_SYNTAX);
        String provider = args[0];
        String identity = args[1];
        String credential = args[2];
        */
        List argv = Lists.newArrayList(args);
        String location = CommandLineUtil.getCommandLineOption(argv, "--location", DEFAULT_LOCATION);
        String port = CommandLineUtil.getCommandLineOption(argv, "--port", "8081+");

        /*
        BrooklynProperties brooklynProperties = BrooklynProperties.Factory.newEmpty();
        String access_identity = checkNotNull(Strings.emptyToNull(identity), "identity must not be null");
        String access_credential = checkNotNull(Strings.emptyToNull(credential), "credential must not be null");
        brooklynProperties.put(CloudLocationConfig.ACCESS_IDENTITY, access_identity);
        brooklynProperties.put(CloudLocationConfig.ACCESS_CREDENTIAL, access_credential);
        String location = checkNotNull(Strings.emptyToNull(provider), "provider must not be null");
        */
        log.info("Start time for CDH deployment on '" + location +"'");
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        BrooklynLauncher launcher = BrooklynLauncher.newInstance()
                                                    .application(
                                                            EntitySpecs.appSpec(SampleClouderaManagedClusterInterface.class)
                                                            .displayName("Brooklyn Cloudera Managed Cluster"))
                                                    .webconsolePort(port)
                                                    .location(location)
                                                    //.brooklynProperties(brooklynProperties)
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
