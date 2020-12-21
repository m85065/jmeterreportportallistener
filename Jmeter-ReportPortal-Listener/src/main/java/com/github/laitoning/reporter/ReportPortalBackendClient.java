package com.github.laitoning.reporter;




import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.visualizers.backend.AbstractBackendListenerClient;
import org.apache.jmeter.visualizers.backend.BackendListenerContext;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jorphan.collections.SearchByClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.http.HttpClient;

import java.util.*;
import java.util.regex.*;

/**
 * The base class for turning text/strings (from the JMeter GUI, or a .jmx file, etc.) into Reporter
 * objects.  Along with being a TestElement so that it can be serialized (for the .jmx file) it.
 *
 * There are also static utility functions to actually instantiate a Reporter of a given type from a configuration.
 *
 *
 *
 * @author Raiden Tang
 *
 */

public class ReportPortalBackendClient extends AbstractBackendListenerClient
{

    /**
     *
     */
    private static final long serialVersionUID = 7524636493683391900L;
    private static final Logger logger = LoggerFactory.getLogger(ReportPortalBackendClient.class);
    private static JMeterContext jContext ;

    private static final String matchpattern = "(.+):\\/\\/(.+):(\\d+)";

    private static final String samplerpattern = ".+sampler.+";

    private static Pattern pattern = Pattern.compile(samplerpattern);

    private static SearchByClass testStepSearch = new SearchByClass(Object.class);

    private static List<Object> stepsList;

    public static String HOSTNAME = "reporter.host";
    public static String PORT = "reporter.port";
    public static String PROTOCOL = "reporter.protocol";
    public static String LAUNCHNAME = "reporter.launchname";
    public static String APIKEY = "reporter.apikey";
    public static String PROJECTNAME = "reporter.projectname";

    private static final Map<String,String> DEFAULT_ARGS = new LinkedHashMap<>();
    static {
        DEFAULT_ARGS.put(HOSTNAME, "http://localhost:8080");
        DEFAULT_ARGS.put(LAUNCHNAME,"JmeterTestLaunch");
        DEFAULT_ARGS.put(PROJECTNAME,"");
        DEFAULT_ARGS.put(APIKEY,"");
    }


    @Override
    public Arguments getDefaultParameters()
    {
        Arguments arguments = new Arguments();
        DEFAULT_ARGS.forEach(arguments::addArgument);
        return arguments;

    }


    @Override
    public void setupTest(BackendListenerContext context) throws Exception
    {

        
        super.setupTest(context);

    }

    @Override
    public void handleSampleResults(List<SampleResult> results,BackendListenerContext context)
    {
        JMeterContextService.getContext().getCurrentSampler();

    }

    @Override
    public void teardownTest(BackendListenerContext context) throws Exception
    {


        super.teardownTest(context);

    }


    
}