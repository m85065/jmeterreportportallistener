package com.github.laitoning.reporter;

import com.github.laitoning.reporter.Request.Attribute;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.backend.AbstractBackendListenerClient;
import org.apache.jmeter.visualizers.backend.BackendListenerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;
import java.util.regex.*;

/**
 * The base class for turning text/strings (from the JMeter GUI, or a .jmx file,
 * etc.) into Reporter objects. Along with being a TestElement so that it can be
 * serialized (for the .jmx file) it.
 *
 * There are also static utility functions to actually instantiate a Reporter of
 * a given type from a configuration.
 *
 *
 *
 * @author Raiden Tang
 *
 */

public class ReportPortalBackendClient extends AbstractBackendListenerClient {

    /**
     *
     */
    private static final long serialVersionUID = 7524636493683391900L;
    private static final Logger logger = LoggerFactory.getLogger(ReportPortalBackendClient.class);

    private static final String matchpattern = "(.+):\\/\\/(.+):(\\d+)";

    private static final String samplerpattern = ".+sampler.+";

    private static Pattern pattern = Pattern.compile(samplerpattern);
    
    //private static SearchByClass<Object> testStepSearch = new SearchByClass<>(Object.class);


    private static ReportPortalClient sender;

    private static Instant instant = Instant.now();

    public static String HOSTNAME = "reporter.host";
    public static String PORT = "reporter.port";
    public static String PROTOCOL = "reporter.protocol";
    public static String LAUNCHNAME = "reporter.launchname";
    public static String APIKEY = "reporter.apikey";
    public static String PROJECTNAME = "reporter.projectname";
    public static String LAUNCHDESCRIPTION = "reporter.launchdescription";
    public static String LAUNCHID;
    public static String SUITEID;
    public static String CASEID;
    private static String threadname = "";
    private SampleResult lastSampleResult;

    private static final Map<String, String> DEFAULT_ARGS = new LinkedHashMap<>();
    static {
        DEFAULT_ARGS.put(HOSTNAME, "http://localhost:8080");
        DEFAULT_ARGS.put(LAUNCHNAME, "JmeterTestLaunch");
        DEFAULT_ARGS.put(PROJECTNAME, "");
        DEFAULT_ARGS.put(APIKEY, "");
        DEFAULT_ARGS.put(LAUNCHDESCRIPTION, "");
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        DEFAULT_ARGS.forEach(arguments::addArgument);
        return arguments;

    }

    @Override
    public void setupTest(BackendListenerContext context) throws Exception {
        if (sender == null) {
            sender = new ReportPortalClient(context.getParameter(HOSTNAME), context.getParameter(PROJECTNAME),
                    context.getParameter(LAUNCHNAME), context.getParameter(APIKEY));
        }

        Attribute launchattribute = new Attribute();
        launchattribute.key = "Jmeter";
        launchattribute.value = "Functional";

        List<Attribute> launchattributes = new ArrayList<>();
        launchattributes.add(launchattribute);

        Attribute suiteattribute = new Attribute();
        suiteattribute.key = "Jmeter TestSuite";
        suiteattribute.value = "Functional";

        List<Attribute> suiteattributes = new ArrayList<>();
        suiteattributes.add(suiteattribute);
        logger.info(sender.toString());
        logger.info(sender.APIKEY);
        logger.info(sender.PROJECTNAME);

        try {
            LAUNCHID = sender.createLaunch(Long.toString(instant.toEpochMilli()),
                    context.getParameter(LAUNCHDESCRIPTION), launchattributes, "DEFAULT");

            SUITEID = sender.createSuite(context.getParameter(LAUNCHNAME), Long.toString(instant.toEpochMilli()),
                    LAUNCHID, context.getParameter(LAUNCHDESCRIPTION), suiteattributes);

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        logger.info("Report Success " + LAUNCHID);
        logger.info("Report Success " + SUITEID);

        super.setupTest(context);

    }

    @Override
    public void handleSampleResults(List<SampleResult> results, BackendListenerContext context) {


        for (SampleResult sr : results) {
            if ((threadname == null) || !threadname.equals(sr.getThreadName())) {
                threadname = sr.getThreadName();
                Attribute attribute = new Attribute();
                attribute.key = "Jmeter Testcase";
                attribute.value = "Functional";

                List<Attribute> attributes = new ArrayList<>();
                attributes.add(attribute);
                if (CASEID == null) {
                    try {
                        CASEID = sender.createCase(sr.getThreadName(), Long.toString(instant.toEpochMilli()), LAUNCHID,
                                "", attributes, SUITEID);
                        logger.info("Report Case Success: "+CASEID);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    }

                } else {
                    String newcaseid = "";
                    try {
                        newcaseid = sender.createCase(sr.getThreadName(), Long.toString(instant.toEpochMilli()),
                                LAUNCHID, "", attributes, SUITEID);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    }

                    if (CASEID != newcaseid && !newcaseid.isEmpty()) {
                        try {
                            sender.FinishStep(Long.toString(sr.getEndTime()), sr.isSuccessful() ? "passed" : "failed",
                                    LAUNCHID, sr.isSuccessful() ? "Test Passed" : "Test Failed", attributes, CASEID);
                            CASEID = newcaseid;
                            logger.info("Report Case Success: "+CASEID);
                        }
                        catch (Exception e) {
                            logger.error(e.getMessage());
                            e.printStackTrace();
                        }

                        }
                    }

                }
                
                try {
                    
                    Attribute attribute = new Attribute();
                    attribute.key = "Jmeter Teststep";
                    attribute.value = "Functional";
                    List<Attribute> attributes = new ArrayList<>();
                    attributes.add(attribute);
                    String stepid =sender.createStep(sr.getSampleLabel(), Long.toString(sr.getStartTime()) , LAUNCHID , sr.getSamplerData() , attributes, CASEID);
                    logger.info("Report Step Success: "+stepid);
                    sender.FinishStep(Long.toString(sr.getEndTime()), sr.isSuccessful() ? "passed" : "failed", LAUNCHID, sr.isSuccessful() ? sr.getResponseDataAsString() : sr.getResponseMessage(), attributes, stepid);
                    lastSampleResult = sr;
                    
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }
    
            }
        



    }

    @Override
    public void teardownTest(BackendListenerContext context) throws Exception
    {
        Attribute caseattribute = new Attribute();
        caseattribute.key = "Jmeter TestSuite";
        caseattribute.value = "Functional";

        List<Attribute> caseattributes = new ArrayList<>();
        caseattributes.add(caseattribute);
        sender.FinishStep(Long.toString(lastSampleResult.getEndTime()), this.lastSampleResult.isSuccessful() ? "passed" : "failed",
        LAUNCHID, this.lastSampleResult.isSuccessful() ? "Test Passed" : "Test Failed", caseattributes, CASEID);

        Attribute suiteattribute = new Attribute();
        suiteattribute.key = "Jmeter TestSuite";
        suiteattribute.value = "Functional";

        List<Attribute> suiteattributes = new ArrayList<>();
        suiteattributes.add(suiteattribute);
        sender.FinishStep(Long.toString(instant.toEpochMilli()), null, LAUNCHID, "TestSuite Completed", suiteattributes, SUITEID);

        Attribute launchattribute = new Attribute();
        launchattribute.key = "Jmeter";
        launchattribute.value = "Functional";

        List<Attribute> launchattributes = new ArrayList<>();
        launchattributes.add(launchattribute);
        sender.FinishLaunch(Long.toString(instant.toEpochMilli()), LAUNCHID, "Jmeter Finish Launch", launchattributes);

        clearSetting();
        super.teardownTest(context);

    }

    private void clearSetting()
    {
        LAUNCHID = null;
        SUITEID = null ;
        CASEID = null;


    }

    
}