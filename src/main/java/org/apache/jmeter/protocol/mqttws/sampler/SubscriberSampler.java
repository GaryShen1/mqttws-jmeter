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

import java.util.Date;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
//import org.apache.jmeter.protocol.mqtt.client.ListenerforSubscribe;
import org.apache.jmeter.protocol.mqttws.client.MqttSubscriber;
import org.apache.jmeter.protocol.mqttws.control.gui.MQTTPublisherGui;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class SubscriberSampler extends BaseMQTTSampler implements
		Interruptible, ThreadListener, TestStateListener {

	private static final long serialVersionUID = 240L;
	private static final Logger log = LoggingManager.getLoggerForClass();
	private static final String DURABLE_SUBSCRIPTION_ID = "mqtt.durableSubscriptionId"; // $NON-NLS-1$
	private static final String DURABLE_SUBSCRIPTION_ID_DEFAULT = "";
	private static final String CLIENT_ID = "mqtt.clientId"; // $NON-NLS-1$
	private static final String CLIENT_ID_DEFAULT = ""; // $NON-NLS-1$
	//Next timeout refers to the sampler as a whole - not to be confused
	//with the connection timeout which refers to the sampler's 
	//paho client connection with the broker 
	private static final String KEEPALIVE_INTERVAL = "mqtt.keepalive_interval"; // $NON-NLS-1$
    private static final String KEEPALIVE_INTERVAL_DEFAULT = "0"; // $NON-NLS-1$
    private static final String MAX_QOS = "mqtt.maxqos"; // $NON-NLS-1$
    private static final String MAX_QOS_DEFAULT = "0"; // $NON-NLS-1$
	private static final String SAMPLER_TIMEOUT = "mqtt.sampler.timeout"; // $NON-NLS-1$
	private static final String SAMPLER_TIMEOUT_DEFAULT = "30000"; // $NON-NLS-1$
	//private static final String QUALITY = "mqtt.quality"; //$NON-NLS-1$
	private static String OneConnectionPerTopic = "mqtt.one_connection_per_topic"; //$NON-NLS-1$
	public transient MqttSubscriber subscriber = null;
	private JavaSamplerContext context = null;
	private static String Length = "mqtt.suffix.length";//$NON-NLS-1$
	private static String RandomSuffix = "mqtt.random_suffix_client_id";//$NON-NLS-1$
	private static String STRATEGY = "mqtt.strategy"; //$NON-NLS-1$
	

	public SubscriberSampler() {
		super();
	}
	
	

	public void setOneConnectionPerTopic(boolean oneConnectionPerTopic) {
		setProperty(OneConnectionPerTopic, oneConnectionPerTopic);
	}

	public boolean isOneConnectionPerTopic() {
		String perTopic = getPropertyAsString(OneConnectionPerTopic);
		if ("TRUE".equalsIgnoreCase(perTopic)) {
			return true;
		} else {
			return false;
		}

	}

//	public void setQuality(String quality) {
//		setProperty(QUALITY, quality);
//	}

//	public String getQuality() {
//		return getPropertyAsString(QUALITY);
//	}

	public String getSTRATEGY() {
		return getPropertyAsString(STRATEGY);

	}

	public void setSTRATEGY(String sTRATEGY) {
		setProperty(STRATEGY, sTRATEGY);

	}

	public void setDurableSubscriptionId(String durableSubscriptionId) {
		setProperty(DURABLE_SUBSCRIPTION_ID, durableSubscriptionId,
				DURABLE_SUBSCRIPTION_ID_DEFAULT);
	}

	public void setClientID(String clientId) {
		setProperty(CLIENT_ID, clientId, CLIENT_ID_DEFAULT);

	}

	public void setSamplerTimeout(String timeout) {
		setProperty(SAMPLER_TIMEOUT, timeout, SAMPLER_TIMEOUT_DEFAULT);
	}
	
    public void setKeepAliveInterval(String second) {
        setProperty(KEEPALIVE_INTERVAL, second, KEEPALIVE_INTERVAL_DEFAULT);
    }
    
    public void setMaxQoS(String qos) {
        setProperty(MAX_QOS, qos);
    }
    

    public String getKeepAliveInterval() {
        return getPropertyAsString(KEEPALIVE_INTERVAL,KEEPALIVE_INTERVAL_DEFAULT);
    }
    
    public String getMaxQoS() {
        return getPropertyAsString(MAX_QOS);
    }
    
	public String getDurableSubscriptionId() {
		return getPropertyAsString(DURABLE_SUBSCRIPTION_ID);
	}

	public String getClientId() {
		return getPropertyAsString(CLIENT_ID, CLIENT_ID_DEFAULT);
	}

	public String getSamplerTimeout() {
		return getPropertyAsString(SAMPLER_TIMEOUT, SAMPLER_TIMEOUT_DEFAULT);
	}

	public void setRandomSuffix(boolean randomSuffix) {
		setProperty(RandomSuffix, randomSuffix);

	}

	public boolean useRandomSuffix() {
		String randomSuffix = getPropertyAsString(RandomSuffix);
		if ("TRUE".equalsIgnoreCase(randomSuffix)) {
			return true;
		} else {
			return false;
		}
	}

	public void setLength(String length) {
		setProperty(Length, length);
	}

	public String getLength() {
		return getPropertyAsString(Length);
	}

	@Override
	public boolean interrupt() {
		log.debug("Thread ended " + new Date());
		if (this.subscriber != null) {
			try {
				this.subscriber.close(context);

			} catch (Exception e) {
				e.printStackTrace();
				log.warn(e.getLocalizedMessage(), e);
			}

		}
		return false;
	}

	@Override
	public void testEnded() {
		log.debug("Thread ended " + new Date());
		if (this.subscriber != null) {
			try {
				this.subscriber.cleanUpOnTestEnd(context);

			} catch (Exception e) {
				e.printStackTrace();
				log.warn(e.getLocalizedMessage(), e);
			}

		}
	}

	@Override
	public void testEnded(String arg0) {
		testEnded();
	}

	@Override
	public void testStarted() {
	}

	@Override
	public void testStarted(String arg0) {
		testStarted();
	}

	// ------------------------------ For Thread---------------------------------//

	private void logThreadStart() {
		if (log.isDebugEnabled()) {
			log.debug("Thread started " + new Date());
			log.debug("MQTTSampler: [" + Thread.currentThread().getName()
					+ "], hashCode=[" + hashCode() + "]");
		}
	}

	@Override
	public void threadStarted() {
		logThreadStart();
		if (subscriber == null) {
			try {
				subscriber = new MqttSubscriber();
			} catch (Exception e) {
				log.warn(e.getLocalizedMessage(), e);
			}
		}
		subscriber.setupTest(context);
	}

	@Override
	public void threadFinished() {
		log.debug("Thread ended " + new Date());
		//System.out.println("Received " + ListenerforSubscribe.count.get() +" messages");
		if (this.subscriber != null) {
			try {
				this.subscriber.close(context);	
			} catch (Exception e) {
				e.printStackTrace();
				log.warn(e.getLocalizedMessage(), e);
			}
		}
	}

	@Override
	public SampleResult sample() {
		//get context just prior to our actual sampling
		//so that we won't miss any updates by other samplers 
		//made prior to that point 
		context = getSamplerContext();
		return this.subscriber.runTest(context);
	}

	//get sampler's JMeter context 
	public JavaSamplerContext getSamplerContext() {
		String host = getProviderUrl();
		String list_topic = getDestination();
		String aggregate = "" + getIterationCount();
		String clientId = getClientId();
		String samplerTimeout = this.getSamplerTimeout();
		String keepaliveinterval = this.getKeepAliveInterval();
		String qos = this.getMaxQoS();
		Arguments parameters = new Arguments();
		parameters.addArgument("SAMPLER_NAME", this.getName());
		parameters.addArgument("HOST", host);
		parameters.addArgument("CLIENT_ID", clientId);
		parameters.addArgument("CONNECTION_TIMEOUT", ""+getConnectionTimeout());
		parameters.addArgument("TOPIC", list_topic);
		parameters.addArgument("KEEPALIVE", keepaliveinterval);
		parameters.addArgument("MAXQOS", qos);
		// ------------------------Strategy-----------------------------------//
		if (MQTTPublisherGui.ROUND_ROBIN.equals(this.getSTRATEGY())) {
			parameters.addArgument("STRATEGY", "ROUND_ROBIN");
		} else {
			parameters.addArgument("STRATEGY", "RANDOM");
		}
		parameters.addArgument("AGGREGATE", aggregate);
		//String quality = getQuality();
		//parameters.addArgument("QOS", quality);
		parameters.addArgument("CLEAN_SESSION",this.getCLEANSESSION());
		parameters.addArgument("SAMPLER_TIMEOUT", samplerTimeout);

		if (this.isUseAuth()) {
			parameters.addArgument("AUTH", "TRUE");
			parameters.addArgument("USER", getUsername());
			parameters.addArgument("PASSWORD", getPassword());
		} else
			parameters.addArgument("AUTH", "FALSE");
		// -------------------------List Topic Or Not-------------------------//

		String[] topics = list_topic.split("\\s*,\\s*");
		if (topics.length <= 1) {
			parameters.addArgument("LIST_TOPIC", "FALSE");
		} else {
			parameters.addArgument("LIST_TOPIC", "TRUE");
		}
		// ------------------------Connection per topic--------------------//

		if (this.isOneConnectionPerTopic()) {
			parameters.addArgument("PER_TOPIC", "TRUE");
		} else {
			parameters.addArgument("PER_TOPIC", "FALSE");
		}
		if (this.useRandomSuffix()) {
			parameters.addArgument("RANDOM_SUFFIX", "TRUE");
			parameters.addArgument("SUFFIX_LENGTH", this.getLength());
		} else {
			parameters.addArgument("RANDOM_SUFFIX", "FALSE");
		}
		return new JavaSamplerContext(parameters);
	}
}
