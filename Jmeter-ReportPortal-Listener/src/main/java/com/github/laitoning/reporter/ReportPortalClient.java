package com.github.laitoning.reporter;

import com.github.laitoning.reporter.Response.*;
import com.github.laitoning.reporter.Request.*;
import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;
import org.jsoup.helper.HttpConnection.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ReportPortalClient {

    private static final Logger logger = LoggerFactory.getLogger(ReportPortalClient.class);
    private String HOST;
    private String NAME;
    private String STARTTIME;
    private String DESCRIPTION;
    private String ATTRIBUTES;
    private String MODE;
    private Boolean RERUN;
    private String RERUNOF;
    private String APIKEY;
    private String PROJECTNAME;
    private CloseableHttpClient httpClient;

    public ReportPortalClient(String host, String projectname, String name, String apikey) {
        this.NAME = StringUtils.isBlank(name) ? "JmeterLaunchTest" : name;
        this.HOST = StringUtils.isBlank(host) ? "http://localhost:8080" : host;
        this.PROJECTNAME = StringUtils.isBlank(projectname) ? "" : projectname;
        this.APIKEY = StringUtils.isBlank(apikey) ? "" : apikey;
        this.httpClient = this.httpClient == null ? HttpClientBuilder.create().build() : this.httpClient;

    }

    public String createLaunch(String startTime, String description, List<Attribute> attributes, String mode)
            throws ClientProtocolException, IOException, Exception {
        if (this.httpClient == null) {
            this.httpClient = HttpClientBuilder.create().build();
        }
        Gson gson = new Gson();
        HttpPost httpPost = new HttpPost(this.HOST + "/api/v1/" + this.PROJECTNAME + "/launch");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + this.APIKEY);
        LaunchRequest request = new LaunchRequest();
        request.name = this.NAME;
        request.startTime = startTime;
        request.description = description;
        request.attributes = attributes;
        request.mode = mode;
        request.rerun = false;
        request.rerunOf = "";
        StringEntity body = new StringEntity(gson.toJson(request));
        httpPost.setEntity(body);

        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            LaunchResponse result = gson.fromJson(EntityUtils.toString(response.getEntity(), "UTF-8"),
                    LaunchResponse.class);
            return result.id;
        } catch (ClientProtocolException e) {
            logger.error("Request Error : %s", e.getMessage());
            return "failed";
        } catch (IOException e) {
            logger.error("Request Error : %s", e.getMessage());
            return "failed";
        } catch (Exception e) {
            logger.error("Logic Error", e.getMessage());
            return "failed";
        }

    }

    public String createSuite(String name,String startTime,String launchid, String description, List<Attribute> attributes)  throws ClientProtocolException, IOException, Exception {
        if(this.httpClient == null)
        {
            this.httpClient = HttpClientBuilder.create().build();
        }
        Gson gson = new Gson();
        HttpPost httpPost = new HttpPost(this.HOST+"/api/v1/"+this.PROJECTNAME+"/item");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Authorization", "Bearer "+this.APIKEY);
        ItemRequest request = new ItemRequest();
        request.name = name;
        request.startTime = startTime;
        request.description = description;
        request.attributes = attributes;
        request.type = "suite";
        request.launchUuid = launchid;
        request.hasStats = true;
        
        StringEntity body = new StringEntity(gson.toJson(request));
        httpPost.setEntity(body);

        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            ItemResponse result = gson.fromJson(EntityUtils.toString(response.getEntity(), "UTF-8"),ItemResponse.class);
            return result.id;
        } catch (ClientProtocolException e) {
           logger.error("Request Error : %s", e.getMessage());
           return "failed";
        }
        catch (IOException e)
        {
            logger.error("Request Error : %s", e.getMessage());
            return "failed";
        }
        catch (Exception e)
        {
            logger.error("Logic Error", e.getMessage());
            return "failed";
        }
        
    }

    public String createCase(String name,String startTime,String launchid, String description, List<Attribute> attributes,String suiteid)  throws ClientProtocolException, IOException, Exception
    {
        if(this.httpClient == null)
        {
            this.httpClient = HttpClientBuilder.create().build();
        }
        Gson gson = new Gson();
        HttpPost httpPost = new HttpPost(this.HOST+"/api/v1/"+this.PROJECTNAME+"/item/"+suiteid);
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Authorization", "Bearer "+this.APIKEY);
        ItemRequest request = new ItemRequest();
        request.name = name;
        request.startTime = startTime;
        request.description = description;
        request.attributes = attributes;
        request.type = "test";
        request.launchUuid = launchid;
        request.hasStats = true;
        
        StringEntity body = new StringEntity(gson.toJson(request));
        httpPost.setEntity(body);

        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            ItemResponse result = gson.fromJson(EntityUtils.toString(response.getEntity(), "UTF-8"),ItemResponse.class);
            return result.id;
        } catch (ClientProtocolException e) {
           logger.error("Request Error : %s", e.getMessage());
           return "failed";
        }
        catch (IOException e)
        {
            logger.error("Request Error : %s", e.getMessage());
            return "failed";
        }
        catch (Exception e)
        {
            logger.error("Logic Error", e.getMessage());
            return "failed";
        }
    }

    public String createStep(String name,String startTime,String launchid, String description, List<Attribute> attributes,String caseid)  throws ClientProtocolException, IOException, Exception
    {
        if(this.httpClient == null)
        {
            this.httpClient = HttpClientBuilder.create().build();
        }
        Gson gson = new Gson();
        HttpPost httpPost = new HttpPost(this.HOST+"/api/v1/"+this.PROJECTNAME+"/item/"+caseid);
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Authorization", "Bearer "+this.APIKEY);
        ItemRequest request = new ItemRequest();
        request.name = name;
        request.startTime = startTime;
        request.description = description;
        request.attributes = attributes;
        request.type = "step";
        request.launchUuid = launchid;
        request.hasStats = true;
        
        StringEntity body = new StringEntity(gson.toJson(request));
        httpPost.setEntity(body);

        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            ItemResponse result = gson.fromJson(EntityUtils.toString(response.getEntity(), "UTF-8"),ItemResponse.class);
            return result.id;
        } catch (ClientProtocolException e) {
           logger.error("Request Error : %s", e.getMessage());
           return "failed";
        }
        catch (IOException e)
        {
            logger.error("Request Error : %s", e.getMessage());
            return "failed";
        }
        catch (Exception e)
        {
            logger.error("Logic Error", e.getMessage());
            return "failed";
        }       

    }

    public String FinishStep(String endTime,String status,String launchid, String description, List<Attribute> attributes,String stepid)  throws ClientProtocolException, IOException, Exception
    {
        if(this.httpClient == null)
        {
            this.httpClient = HttpClientBuilder.create().build();
        }
        Gson gson = new Gson();
        HttpPut httpPut = new HttpPut(this.HOST+"/api/v1/"+this.PROJECTNAME+"/item/"+stepid);
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setHeader("Authorization", "Bearer "+this.APIKEY);
        FinishItemRequest request = new FinishItemRequest();
        request.endTime = endTime;
        request.status = status;
        request.description = description;
        request.attributes = attributes;
        request.launchUuid = launchid;
        
        StringEntity body = new StringEntity(gson.toJson(request));
        httpPut.setEntity(body);

        try {
            CloseableHttpResponse response = httpClient.execute(httpPut);

            FinishItemResponse result = gson.fromJson(EntityUtils.toString(response.getEntity(), "UTF-8"),FinishItemResponse.class);
            return result.message;
        } catch (ClientProtocolException e) {
           logger.error( e.getMessage());
           return "failed";
        }
        catch (IOException e)
        {
            logger.error( e.getMessage());
            return "failed";
        }
        catch (Exception e)
        {
            logger.error( e.getMessage());
            return "failed";
        }       

    }


    public String FinishLaunch(String endTime,String launchid, String description, List<Attribute> attributes)  throws ClientProtocolException, IOException, Exception
    {
        if(this.httpClient == null)
        {
            this.httpClient = HttpClientBuilder.create().build();
        }
        Gson gson = new Gson();
        HttpPut httpPut = new HttpPut(this.HOST+"/api/v1/"+this.PROJECTNAME+"/launch/"+launchid+"/finish");
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setHeader("Authorization", "Bearer "+this.APIKEY);
        FinishLaunchRequest request = new FinishLaunchRequest();
        request.endTime = endTime;
        request.description = description;
        request.attributes = attributes;
        
        StringEntity body = new StringEntity(gson.toJson(request));
        httpPut.setEntity(body);

        try {
            CloseableHttpResponse response = httpClient.execute(httpPut);
            FinishLaunchResponse result = gson.fromJson(EntityUtils.toString(response.getEntity(), "UTF-8"),FinishLaunchResponse.class);
            return result.id;
        } catch (ClientProtocolException e) {
           logger.error("Request Error : %s", e.getMessage());
           return "failed";
        }
        catch (IOException e)
        {
            logger.error("Request Error : %s", e.getMessage());
            return "failed";
        }
        catch (Exception e)
        {
            logger.error("Logic Error", e.getMessage());
            return "failed";
        }       

    }
}
