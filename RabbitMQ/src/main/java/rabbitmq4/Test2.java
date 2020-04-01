package rabbitmq4;

import java.io.IOException;
import java.util.Scanner;

import javax.security.auth.callback.Callback;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * 路由模式消费者
 * @author lmc
 *
 */
public class Test2 {
	public static void main(String[] args) throws Exception {
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.145.132");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		Connection c = f.newConnection();
		Channel ch = c.createChannel();
		
		//创建交换机		交换机名称			交换机类型
		ch.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);
		
		//消费者自动生成队列名，
		String queueName = ch.queueDeclare().getQueue();
		System.out.println("输入接收日志的级别，用空格隔开：");
		String[] a = new Scanner(System.in).nextLine().split("\\s");
		
		//把队列绑定到direct_logs上，可以绑定多个bindingkey
		for (String level : a) {
			ch.queueBind(queueName, "direct_logs", level);
		}
		System.out.println("等待接收数据");
		
		DeliverCallback callback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				String msg = new String(message.getBody());
				String routingKey = message.getEnvelope().getRoutingKey();
				System.out.println("收到："+routingKey+" - "+msg);
			}
		};
		CancelCallback cancel = new CancelCallback() {
			public void handle(String consumerTag) throws IOException {
			}
		};
		
		ch.basicConsume(queueName, true, callback,cancel);
	}
}
