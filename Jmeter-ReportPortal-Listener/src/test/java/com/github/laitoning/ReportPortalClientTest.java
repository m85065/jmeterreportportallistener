package com.github.laitoning;

import com.github.laitoning.reporter.ReportPortalClient;
import com.github.laitoning.reporter.Request.LaunchRequest;
import com.github.laitoning.reporter.Request.Attribute;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.Instant;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ReportPortalClientTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void createLaunchTest() throws ClientProtocolException, IOException, Exception
    {
        Instant instant = Instant.now();
        Attribute attribute = new Attribute();
        attribute.key = "Test";
        attribute.value = "Success";
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute);
        ReportPortalClient portalclient = new ReportPortalClient("http://localhost:8080","superadmin_personal", "UnitTestLaunch", "795d1998-0d95-4184-8780-f29d46cece7d");
        String id = portalclient.createLaunch( Long.toString(instant.toEpochMilli()) , "A Plugin Unit Test", attributes, "DEFAULT");
        assertNotEquals(id, "failed");


    }
    @Test
    public void createSuiteTest() throws ClientProtocolException, IOException, Exception
    {
        Instant instant = Instant.now();
        Attribute attribute = new Attribute();
        attribute.key = "Test";
        attribute.value = "Success";
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute);
        ReportPortalClient portalclient = new ReportPortalClient("http://localhost:8080","superadmin_personal", "UnitTestLaunch", "795d1998-0d95-4184-8780-f29d46cece7d");
        String launchid = portalclient.createLaunch( Long.toString(instant.toEpochMilli()) , "A Plugin Unit Test", attributes, "DEFAULT");
        assertNotEquals(launchid, "failed");
        String id = portalclient.createSuite("Unit TestSuite", Long.toString(instant.toEpochMilli()), launchid, "A Plugin Unit Test Step", attributes);
        assertNotNull(id);


    }

    @Test
    public void FinishLaunchTest() throws ClientProtocolException, IOException, Exception
    {
        Instant instant = Instant.now();
        Attribute attribute = new Attribute();
        attribute.key = "Test";
        attribute.value = "Success";
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute);
        ReportPortalClient portalclient = new ReportPortalClient("http://localhost:8080","superadmin_personal", "UnitTestLaunch", "795d1998-0d95-4184-8780-f29d46cece7d");
        String launchid = portalclient.createLaunch( Long.toString(instant.toEpochMilli()) , "A Plugin Unit Test", attributes, "DEFAULT");
        assertNotEquals(launchid, "failed");
        String suitid = portalclient.createSuite("Unit TestSuite", Long.toString(instant.toEpochMilli()), launchid, "A Plugin Unit Test Step", attributes);
        assertNotNull(suitid);
        String caseid = portalclient.createCase("FinishStep Test", Long.toString(instant.toEpochMilli()), launchid, "A Testcase to test finish case", attributes, suitid);
        assertNotNull(caseid);
        String stepid = portalclient.createStep("Test Step", Long.toString(instant.toEpochMilli()), launchid, "A Unit Test Step", attributes, caseid);
        assertNotNull(stepid);
        Boolean stepresult = portalclient.FinishStep(Long.toString(instant.toEpochMilli()),"passed", launchid, "A Step description", attributes, stepid);
        assertTrue(stepresult);
        Boolean caseresult = portalclient.FinishStep(Long.toString(instant.toEpochMilli()),"passed", launchid, "A case description", attributes, caseid);
        assertTrue(caseresult);
        Boolean suiteresult = portalclient.FinishStep(Long.toString(instant.toEpochMilli()),"passed", launchid, "A Suite description", attributes, suitid);
        assertTrue(suiteresult);
        String finishid = portalclient.FinishLaunch(Long.toString(instant.toEpochMilli()), launchid, "A Finish Launch", attributes);
        assertNotNull(finishid);



    }
}
