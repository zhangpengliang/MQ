package com.zpl.mq.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消息的消费者
 * 
 * @author zhangpengliang
 *
 */
public class QueueConsumer {

	public static void main(String[] args) throws Exception {
		// 创建一个链接工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory(
				"tcp://localhost:61626");
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
		MessageConsumer consumer = session.createConsumer(queue);
		int i = 0;
		while (i < 3) {
			// 接收消息
			Message msg = consumer.receive();// 同步接收，会阻塞，直到接收到消息才会往下走
			// 获取消息的属性
			String name = msg.getStringProperty("name");
			Integer age = msg.getIntProperty("age");
			// session.commit();
			// if (i == 1) {
			// msg.acknowledge();// 在第二个消息时确认消息，那么前两个就会被确认，剩下的就不会被确认，可能就会被重新消费
			// }
			TextMessage m = (TextMessage) msg;
			System.out.println(m.getText() + "  " + name + " age=" + age);
			i++;
			msg.acknowledge();
		}
		// 只有当事务提交了，那么中间件才会收到我们的确认消息（只是指事务开启的模式）
		session.close();
		con.close();

	}

}
