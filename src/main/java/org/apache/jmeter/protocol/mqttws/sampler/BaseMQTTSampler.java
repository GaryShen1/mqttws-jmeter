/**
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at
 
    http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License. 


*/

package org.apache.jmeter.protocol.mqttws.sampler;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;

public abstract class BaseMQTTSampler extends AbstractSampler {
    private static final long serialVersionUID = 240L;
    private static final String PROVIDER_URL = "mqtt.provider_url"; // $NON-NLS-1$
    private static final String DEST = "mqtt.topic"; // $NON-NLS-1$
    private static final String PRINCIPAL = "mqtt.security_principle"; // $NON-NLS-1$
    private static final String CREDENTIALS = "mqtt.security_credentials"; // $NON-NLS-1$
    private static final String ITERATIONS = "mqtt.iterations"; // $NON-NLS-1$
    private static final String USE_AUTH = "mqtt.authenticate"; // $NON-NLS-1$
    private static final String REQUIRED = JMeterUtils.getResString("mqtt_auth_required"); // $NON-NLS-1$
    private static final String TEXT_MSG = "mqtt.text_message"; //$NON-NLS-1$
    private static final String CONNECTION_TIMEOUT = "mqtt.connection_timeout"; // $NON-NLS-1$
	private static final String CONNECTION_TIMEOUT_DEFAULT = "5000"; // $NON-NLS-1$
	
	private static final String PUBLISHER_THROTTLE = "mqtt.publisher_throttle"; // $NON-NLS-1$
	private static final String PUBLISHER_THROTTLE_DEFAULT = "100"; // $NON-NLS-1$
	private static final String CLEAN_SESSION="mqtt.clean.session";//$NON-NLS-1$
	private static final String CLEAN_SESSION_DEFAULT = "false";//$NON-NLS-1$
 
   
    /**
     * Constructor
     */
    public BaseMQTTSampler() {
	}

    @Override
	public SampleResult sample(Entry e) {
    	 return this.sample();
	}
	public abstract SampleResult sample() ;
	
	  // ------------- get/set properties ----------------------//
	
	 /**
     * 
     * @param url the provider URL
     */
    public void setProviderUrl(String url) {
        setProperty(PROVIDER_URL, url);
    }
    
    
    public void setTextMessage(String msg) {
    	setProperty(TEXT_MSG, msg);
    }
    
    public String getTextMessage() {
        return getPropertyAsString(TEXT_MSG);
    }
    
    /**
     * 
     * @return the provider URL
     */
    public String getProviderUrl() {
        return getPropertyAsString(PROVIDER_URL);
    }
    /**
     * set the destination (topic or queue name)
     *
     * @param dest the destination
     */
    public void setDestination(String dest) {
        setProperty(DEST, dest);
    }
    /**
     * return the destination (topic or queue name)
     *
     * @return the destination
     */
    public String getDestination() {
        return getPropertyAsString(DEST);
    }
    
    /**
     * set the username to login into the mqtt server if needed
     *
     * @param user
     */
    public void setUsername(String user) {
        setProperty(PRINCIPAL, user);
    }

    /**
     * return the username used to login to the mqtt server
     *
     * @return the username used to login to the mqtt server
     */
    public String getUsername() {
        return getPropertyAsString(PRINCIPAL);
    }
    /**
     * Set the password to login to the mqtt server
     *
     * @param pwd
     */
    public void setPassword(String pwd) {
        setProperty(CREDENTIALS, pwd);
    }

    /**
     * return the password used to login to the mqtt server
     *
     * @return the password used to login to the mqtt server
     */
    public String getPassword() {
        return getPropertyAsString(CREDENTIALS);
    }
    /**
     * set the number of iterations the sampler should aggregate
     *
     * @param count
     */
    public void setIterations(String count) {
        setProperty(ITERATIONS, count);
    }

    /**
     * get the iterations as string
     *
     * @return the number of iterations
     */
    public String getIterations() {
        return getPropertyAsString(ITERATIONS);
    }
    
    
    /**
     * set connection timeout after which sampler should stop trying to connect 
     *
     * @param count
     */
    public void setConnectionTimeout(String count) {
        setProperty(CONNECTION_TIMEOUT, count, CONNECTION_TIMEOUT_DEFAULT);
    }
    
    
    /**
     * set interval between successive publish messages 
     *
     * @param count
     */
    public void setPublisherThrottle(String count) {
        setProperty(PUBLISHER_THROTTLE, count, PUBLISHER_THROTTLE_DEFAULT);
    }

    
    /**
     * get connection timeout as int
     *
     * @return the number of iterations
     */
    public String getConnectionTimeout() {
        return getPropertyAsString(CONNECTION_TIMEOUT,CONNECTION_TIMEOUT_DEFAULT);
    }
    
    /**
     * get connection timeout as int
     *
     * @return the number of iterations
     */
    public String getPublisherThrottle() {
        return getPropertyAsString(PUBLISHER_THROTTLE, PUBLISHER_THROTTLE_DEFAULT);
    }
    
    /**
     * return the number of iterations as int instead of string
     *
     * @return the number of iterations as int instead of string
     */
    public int getIterationCount() {
        return getPropertyAsInt(ITERATIONS);
    }

    /**
     * Set whether authentication is required for mqtt server
     *
     * @param useAuth
     */
    public void setUseAuth(boolean useAuth) {
        setProperty(USE_AUTH, useAuth);
    }
    /**
     * 
     *
     * @return whether mqtt server requires authentication
     */
    public boolean isUseAuth() {
        final String useAuth = getPropertyAsString(USE_AUTH);
        return useAuth.equalsIgnoreCase("true") || useAuth.equals(REQUIRED); // $NON-NLS-1$
    }
    
    
    public String getCLEANSESSION() {
		return getPropertyAsString(CLEAN_SESSION, CLEAN_SESSION_DEFAULT);
	}

	public void setCLEANSESSION(boolean clean) {		
		
		if(clean) {
			setProperty(CLEAN_SESSION, "true");
		}
		else 
			setProperty(CLEAN_SESSION, "false");
	}
 
	
}
