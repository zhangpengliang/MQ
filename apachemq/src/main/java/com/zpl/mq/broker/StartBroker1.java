package com.zpl.mq.broker;

import org.apache.activemq.broker.BrokerService;

/**
 * 嵌入式启动Broker方式1
 * 
 * @author zhangpengliang
 *
 */
public class StartBroker1 {

	public static void main(String[] args) throws Exception {
		// 这种方式没有页面管理
		BrokerService bs = new BrokerService();
		bs.setUseJmx(true);
		bs.addConnector("tcp://localhost:61616");
		bs.start();

	}

}
