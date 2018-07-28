package com.zpl.mq.broker;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

/**
 * Broker启动方式2，使用brokerFactory
 * 
 * @author zhangpengliang
 *
 */
public class StartBroker2 {

	public static void main(String[] args) throws Exception {

		String uri = "properties:broker.properties";// 这个properties文件内容如下
		// 内容：useJmx=true persistent=true brokerName=chaddggg
		BrokerService bs = BrokerFactory.createBroker(uri);
		bs.addConnector("tcp://localhost:61616");
		bs.start();

		// 也可以Spring来集成ActiveMQ。具体可以网上查
	}
}
