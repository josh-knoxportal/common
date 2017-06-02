package com.nemustech.adapter.eai;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.nemustech.adapter.exception.AdapterException;
import com.nemustech.common.util.PropertyUtils;

import COM.activesw.api.client.BrokerClient;
import COM.activesw.api.client.BrokerEvent;
import COM.activesw.api.client.BrokerException;

/**
 * EAI 기본 커넥터
 */
public class EAIConnector {
	private static final Log log = LogFactory.getLog(EAIConnector.class);

	protected String broker_host;
	protected String broker_name;
	protected String client_group;
	protected int broker_port;
	protected int event_waiting;

	public EAIConnector() {
		broker_port = PropertyUtils.getInstance().getInt("com.nemustech.eai.connector.port", 80);
		broker_host = PropertyUtils.getInstance().getString("com.nemustech.eai.connector.host", "10.129.29.183") + ":"
				+ String.valueOf(broker_port);
		broker_name = PropertyUtils.getInstance().getString("com.nemustech.eai.connector.name", "APBK");
		client_group = PropertyUtils.getInstance().getString("com.nemustech.eai.connector.group", "Application_CG");
		event_waiting = PropertyUtils.getInstance().getInt("com.nemustech.eai.connector.waiting", 5000);
	}

	public int getRepeatNumerOfTimes() {
		return PropertyUtils.getInstance().getInt("com.nemustech.eai.connector.retry", 3);
	}

	public int getRepeatSleepTimes() {
		return PropertyUtils.getInstance().getInt("com.nemustech.eai.connector.sleep", 5000);
	}

	public <T1, T2> T2 execute(String applicationName, String pubdoc, String subdoc, T1 params,
			EAIMapper<T1, T2> mapper) throws AdapterException {
		int repeat = getRepeatNumerOfTimes();
		int repeatSleep = getRepeatSleepTimes();

		T2 response = null;
		do {
			BrokerClient client = createBrokerClient(applicationName);
			try {
				if (client != null) {
					BrokerEvent event = createEvent(client, pubdoc);
					if (event != null) {
						boolean bPerm = checkPermission(client, pubdoc, subdoc);
						if (bPerm) {
							// set request params
							mapper.mappingRequestParam(event, params);
							response = publish(client, event, subdoc, mapper);
						}
					}

				}
			} catch (AdapterException e1) {
				throw e1;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
				try {
					if (client != null && client.isConnected())
						client.destroy();
				} catch (BrokerException e1) {
					log.error(e1.getMessage(), e1);
				}
			}
			if (response != null)
				break;
			repeat--;
			try {
				Thread.sleep(repeatSleep);
			} catch (Exception e) {
			}
		} while (repeat > 0);

		return response;
	}

	protected <T1, T2> T2 publish(BrokerClient client, BrokerEvent event, String subdoc, EAIMapper<T1, T2> mapper)
			throws AdapterException {
		T2 response = null;

		try {
			client.publish(event);

			log.debug(" client info : [" + client.toString() + "]");
			log.debug(" pub message : [" + event.toString() + "]");

			client.newSubscription(subdoc, null);
			BrokerEvent se = client.getEvent(event_waiting);

			log.debug(" client info : [" + client.toString() + "]");
			log.debug(" sub message : [" + se.toString() + "]");

			if (se.isNullReply()) {
				log.debug("NULL Reply");
			} else if (se.isErrorReply()) {
				log.debug("ERROR Reply");
			} else {
				response = mapper.mappingResponseParam(se);
			}
		} catch (BrokerException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (client.isConnected())
					client.destroy();
			} catch (BrokerException ex) {
				log.debug("Error on client destroy\n" + ex);
			}
		}

		return response;
	}

	protected boolean checkPermission(BrokerClient c, String publish_document, String subscribe_document) {
		boolean can_publish;
		boolean can_subscribe;

		/* Check publish permission */
		try {
			can_publish = c.canPublish(publish_document);
		} catch (BrokerException e) {
			log.error("Check publish permission", e);
			return false;
		} finally {
			log.debug(" Check publish permission.");
		}

		/* Check subscribe permission */
		try {
			can_subscribe = c.canSubscribe(subscribe_document);
		} catch (BrokerException e) {
			log.error("Check subscribe permission", e);
			return false;
		} finally {
			log.debug(" Check subscribe permission.");
		}

		if (can_publish == false || can_subscribe == false) {
			log.debug(" Cannot publish or subscribe event");
			log.debug(
					" Make sure it is loaded in the broker and permission is given to publish or subscribe it in the ...");
			return false;
		} else {
			return true;
		}
	}

	protected BrokerEvent createEvent(BrokerClient client, String public_document) throws AdapterException {
		BrokerEvent pe = null;

		try {
			pe = new BrokerEvent(client, public_document);
		} catch (BrokerException e) {
			throw new AdapterException(AdapterException.ERROR, e.getMessage(), e);
		} finally {
			if (pe != null)
				log.debug(" Create the event.");
		}

		return pe;
	}

	protected BrokerClient createBrokerClient(String applicationName) throws AdapterException {
		BrokerClient client = null;

		try {
			client = new BrokerClient(broker_host, broker_name, null, client_group, applicationName, null);
		} catch (BrokerException e) {
			throw new AdapterException(AdapterException.ERROR, e.getMessage(), e);
		} finally {
			if (client != null)
				log.debug(" Create a client. [" + client.getApiVersionNumber().toString() + "]");
		}
		return client;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
