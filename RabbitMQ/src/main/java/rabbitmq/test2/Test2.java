package rabbitmq.test2;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * 生产模式消费者
 * @author lmc
 *
 */
public class Test2 {
	public static void main(String[] args) throws Exception{
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.145.132");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		Connection c = f.newConnection();
		Channel ch = c.createChannel();
		DeliverCallback callback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				String msg = new String(message.getBody());
				System.out.println("收到:"+msg);
				
				//遍历收到的字符，有一个点则进程暂停一秒
				for(int i = 0;i<msg.length();i++) {
					if(msg.charAt(i)=='.') {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
				System.out.println("处理结束");
				//处理完消息 发送回执
				ch.basicAck(message.getEnvelope().getDeliveryTag(), false);
			}
		};
		CancelCallback cancelCallback = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
				
			}
		};
		//                              autoAck true自动回执，false需要手动发送回执，收到回执rmq删除消息
		ch.basicConsume("hellorabbitmq", false, callback,cancelCallback);
	}
}	
