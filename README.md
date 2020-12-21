# Jmeter ReportPortal Backend Listener

## Description

* Base on Jmeter AbstractBackendListenerClient
* ThreadGroup Name as Testcase Name
* SamplerLabel as TestStep Name
* Send Finish Result Request When ThreadName changed(A testcase completed)
* Use SampleResult.getResponseDataasString() as Item description when result is success.


### Arguments

1. `reporter.host`  ReportPortal API url (ex: `http://localhost:8080`)
2. `reporter.launchname` ReportPortal LaunchName
3. `reporter.projectname` ReportPortal ProjectName
4. `reporter.apikey` ReportPortal APIKEY
5. `reporter.launchdescription` ReportPortal Launch Description


****

**Dependency**
1. `com.google.code.gson`

***Build***
```sh
cd Jmeter-ReportPortal-Listener
mvn clean package
```

***Usage***

1. Copy jar file and dependency to Jmeter's lib/ext folder
2. Restart Jmeter
3. Add Backend Listener
4. Select implementation `com.github.laitoning.reporter.ReportPortalBackendClient`



**Limitation**

* Only support for API functional test
* Not Support Nest Steps 
