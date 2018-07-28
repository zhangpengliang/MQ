package com.zpl.mq.Th_Con_Network;

import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消息的消费者集群
 * 
 * @author zhangpengliang
 *
 */
public class TH_QueueConsumer3 {

	public static void main(String[] args) throws Exception {
		// 创建一个链接工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory(
				"tcp://localhost:61616");
		// 创建一个链接
		Connection con = ((ActiveMQConnectionFactory) factory)
				.createConnection();
		con.start();// 启动链接
		// 创建一个会话session，参数：事务、应答模式，如果事务开启那么应答模式失效，当事务提交时消息才会被确认
		Session session = con.createSession(Boolean.FALSE,
				Session.CLIENT_ACKNOWLEDGE);
		// 创建一个目的地，消费该队列中的消息
		Destination queue = session.createQueue("my-queue-st");
		// 创建一个消费者
		for (int i = 0; i < 2; i++) {
			// ************* 主要是通过公用一个会话来实现消费者的集群****************
			Thread t = new ThreadConsumer(session, queue);
			t.setName("61616线程" + i);
			t.start();
		}
	}

}

class ThreadConsumer extends Thread {
	private Session session = null;
	private Destination queue = null;

	public ThreadConsumer(Session session, Destination queue) {
		super();
		this.session = session;
		this.queue = queue;
	}

	@Override
	public void run() {
		super.run();
		try {

			MessageConsumer consumer = session.createConsumer(queue);
			consumer.setMessageListener(new MessageListener() {

				public void onMessage(Message message) {
					try {
						TextMessage m = (TextMessage) message;
						System.out.println(Thread.currentThread().getName()
								+ "---" + m.getText());
						try {
							TimeUnit.SECONDS.sleep(4);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						message.acknowledge();
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
