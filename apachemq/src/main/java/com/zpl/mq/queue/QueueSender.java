package com.zpl.mq.queue;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 发送者
 * 
 * @author zhangpengliang
 *
 */
public class QueueSender {

	public static void main(String[] args) throws Exception {
		// 创建activeMQ的链接工厂
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
				"tcp://localhost:61616");
		// 创建链接
		Connection con = factory.createConnection();
		con.start();// 启动连接
		// 创建会话session,第一个参数是：是否开启事务，第二个参数是：应答模式。如果开启事务那么当session.commit()时就自动确认消息。
		// 当事务关闭时，才会启用应答模式：有自动应答和客户端应答以及延迟应答三种模式
		Session session = con.createSession(true, Session.AUTO_ACKNOWLEDGE);
		// 创建目的地
		Destination queue = session.createQueue("my-queue-st");
		// 创建一个生产者
		MessageProducer pro = session.createProducer(queue);
		// 设置消息发送的模式为。持久化模式。
		/**
		 * JMSDeliveryMode，传送模式，有两种：一个持久化模式：一条持久性的消息应该被传送一次仅仅一次，当JMS
		 * 提供者出现故障，该消息并不会丢失，他会在服务器回复之后再次传递，一条非持久模式，最多会传递一次，如果丢失，那么就会永远丢失
		 */
		pro.setDeliveryMode(DeliveryMode.NON_PERSISTENT);// 持久化模式

		for (int i = 0; i < 3; i++) {
			// 我们发送三个消息
			TextMessage msg = session.createTextMessage("dev----zpl" + i);
			// 这里我们也可以往消息里放一些properties
			msg.setStringProperty("name", "zhangpengliang");
			msg.setIntProperty("age", 12);
			// 通过消息生产者发送出去消息
			pro.send(msg);
		}
		// 只有在事务提交的时候才会把消息发送到mq
		session.commit();
		session.close();
		con.close();

	}

}
