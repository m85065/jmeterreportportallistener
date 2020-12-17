package com.github.laitoning.reporter;




import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.http.HttpClient;
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

public class BasicReporterConfig extends AbstractTestElement
{

    /**
     *
     */
    private static final long serialVersionUID = 7524636493683391900L;

    private static final String matchpattern = "(.+):\\/\\/(.+):(\\d+)";

    public static String HOSTNAME = "reporter.host";
    public static String PORT = "reporter.port";
    public static String PROTOCOL = "reporter.protocol";
    public static String LAUNCHNAME = "reporter.launchname";




    public void setDomain(String domain)
    {
        this.setProperty(HOSTNAME, domain);
       
    }

    public String getDomain()
    {
        return this.getPropertyAsString(HOSTNAME,"localhost");
    }

    public void setPort(String port)
    {
        this.setProperty(PORT, port);
    }

    public String getPort()
    {
        return this.getPropertyAsString(PORT, "80");
    }

    public void setProtocol(String protocol)
    {
        this.setProperty(PROTOCOL, protocol);
    }

    public String getProtocol()
    {
        return this.getPropertyAsString(PROTOCOL, "http");
    }

    public void setLaunchName(String launchname)
    {
        this.setProperty(LAUNCHNAME, launchname);
    }

    public String getLaunchName()
    {
        return this.getPropertyAsString(LAUNCHNAME, "JmeterLaunch");
    }


    public void setPortal(String url)
    {
        Pattern pattern = Pattern.compile(this.matchpattern);
        Matcher matcher = pattern.matcher(url);
        boolean matchFound = matcher.find();
        if(matchFound)
        {
            this.setProtocol(matcher.group(0));
            this.setDomain(matcher.group(1));
            this.setPort(matcher.group(2));
        }
        else
        {
            this.setProtocol("http");
            this.setPort("8080");
            this.setDomain("localhost");

        }

    }


    
}