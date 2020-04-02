package rabbitmq.test5;

import java.io.IOException;
import java.util.Scanner;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Tset2 {
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
		
		//自动生成队列  获取队列名字
		String queuename = ch.queueDeclare().getQueue();
		
		System.out.println("请输入bindingkey，并用空格隔开");
		String[] bindingKeys = new Scanner(System.in).nextLine().split("\\s");
		//human:tom tony   animals:dog cat      other:knief
		for (String bindingKey : bindingKeys) {
			ch.queueBind(queuename, "topic_logs", bindingKey);
		}
		
		System.out.println("等待接收数据");
		
		DeliverCallback callback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				String msg = new String(message.getBody());
				String routingKey = message.getEnvelope().getRoutingKey();
				System.out.println("收到："+routingKey+"-"+msg);
			}
		};
		
		CancelCallback cancel = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
			}
		};
		ch.basicConsume(queuename, true, callback, cancel);
	}
}
