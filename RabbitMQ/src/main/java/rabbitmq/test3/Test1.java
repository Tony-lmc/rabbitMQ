package rabbitmq.test3;

import java.io.IOException;
import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * 发布订阅者模式生产者
 * @author lmc
 *
 */
public class Test1 {
	public static void main(String[] args) throws Exception {
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.145.132");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		Connection c = f.newConnection();
		Channel ch = c.createChannel();
		//创建交换机  扇出交换机fenout
		ch.exchangeDeclare("logs", "fanout");
		
		while(true) {
			System.out.print("请输入消息:");
			String s = new Scanner(System.in).nextLine();
			if("exit".equals(s)) {
				break;
			}
			ch.basicPublish("logs", "", null, s.getBytes());
			System.out.println("消息已经发出"+s);
		}
		c.close();
	}
}
