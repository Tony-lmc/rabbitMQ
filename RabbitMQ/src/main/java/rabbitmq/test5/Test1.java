package rabbitmq.test5;

import java.io.IOException;
import java.util.Scanner;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Test1 {
	public static void main(String[] args) throws Exception {
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.145.132");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		Connection c = f.newConnection();
		Channel ch = c.createChannel();
		
		//创建交换机		交换机名称			交换机类型
		ch.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);
		while(true) {
			System.out.println("请输入要发送的消息：");
			String msg = new Scanner(System.in).nextLine();
			if("exit".equals(msg)) {
				break;
			}
			System.out.println("请输入routingkey：");
			String routingKey = new Scanner(System.in).nextLine();
			
			ch.basicPublish("topic_logs", routingKey, null, msg.getBytes());
			System.out.println("消息已经发送"+routingKey+msg);
		}
		c.close();
	}
}
