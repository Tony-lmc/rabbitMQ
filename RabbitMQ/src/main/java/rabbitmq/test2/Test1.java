package rabbitmq.test2;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * 工作模式生产者
 * @author lmc
 *
 */
public class Test1 {
	public static void main(String[] args) throws Exception{
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.145.132");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		Connection c = f.newConnection();
		Channel ch = c.createChannel();
		
		ch.queueDeclare("hellorabbitmq", false, false, false, null);
		
		while(true) {
			System.out.println("输入消息:");
			String msg = new Scanner(System.in).nextLine();
			if("exit".equals(msg)) {
				break;
			}
			ch.basicPublish("", "hellorabbitmq", null, msg.getBytes());
			System.out.println("消息已经发生："+msg);
		}
		c.close();
	}
}
