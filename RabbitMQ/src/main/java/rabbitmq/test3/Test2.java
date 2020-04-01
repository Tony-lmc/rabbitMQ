package rabbitmq.test3;

import java.io.IOException;

import javax.security.auth.callback.Callback;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Test2 {
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
		
		//自动生成队列名字   非持久，独占，自动删除     获取name
		String queuename = ch.queueDeclare().getQueue();
		//把上面获取到的队列名字绑定到交换机
		ch.queueBind(queuename, "logs", "");
		
		System.out.println("等待接收数据");
		
		DeliverCallback callback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				String msg = new String(message.getBody());
				System.out.println("收到:"+msg);
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
