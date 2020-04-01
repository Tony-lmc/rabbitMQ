package rabbitmq4;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * 路由模式生产商
 * @author lmc
 *
 */
public class Test1 {
	public static void main(String[] args) throws Exception {
		String[] a = {"warning","info","error"};
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.145.132");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		Connection c = f.newConnection();
		Channel ch = c.createChannel();
		
		//创建交换机		交换机名称			交换机类型
		ch.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);
		
		while(true) {
			System.out.println("输入消息：");
			String msg = new Scanner(System.in).nextLine();
			if("exit".equals(msg)) {
				break;
			}
			//随机产生日志级别
			String level = a[new Random().nextInt(a.length)];
			ch.basicPublish("direct_logs", level, null, msg.getBytes());
			System.out.println("消息已经发送："+"["+level+"]"+"-["+msg+"]");
		}
		c.close();
	}
}
