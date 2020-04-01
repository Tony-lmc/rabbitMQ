package rabbitmq.test1;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * 简单模式客户端
 * @author lmc
 *
 */
public class Test2 {
	public static void main(String[] args) throws Exception{
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.145.132");
		f.setUsername("admin");
		f.setPassword("admin");
		
		Connection c = f.newConnection();
		Channel ch = c.createChannel();
		
		//ch.queueDeclare(queue, durable, exclusive, autoDelete, arguments)
		ch.queueDeclare("hellorabbitmq",false,false,false,null);
		
		DeliverCallback callback = new DeliverCallback() {
			//consumerTag 为消费者生成唯一标识
			//message 消息的封装对象
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				//处理消息
				String msg = new String(message.getBody());
				System.out.println("收到"+msg);
			}
		};
		
		//取消消息回调
		CancelCallback cancelCallback = new CancelCallback() {
			@Override 
			public void handle(String consumerTag) throws IOException {
				
			}
		};
		
		
		//阻塞方法。程序在这里等待接受消息
		//				queue 指定队列接受参数, callback 接受消息后 处理消息的回调对象,cancelCallback取消消息回调
		ch.basicConsume("hellorabbitmq",callback,cancelCallback);
	}
}
