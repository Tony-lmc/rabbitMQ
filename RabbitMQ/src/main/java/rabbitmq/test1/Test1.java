package rabbitmq.test1;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * 简单模式服务端
 * @author lmc
 *
 */
public class Test1 {
	public static void main(String[] args) throws Exception{
		//创建连接工厂
		ConnectionFactory f= new ConnectionFactory();
		f.setHost("192.168.145.132");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		//与rabbitmq建立连接
		Connection c = f.newConnection();
		//建立信道
		Channel ch = c.createChannel();
		
		//rabbitmq管理界面或者代码创建队列
		             //-queue: 队列名称;durable: 队列持久化,true表示RabbitMQ重启后队列仍存在;
					//-exclusive: 排他,true表示限制仅当前连接可用;autoDelete: 当最后一个消费者断开后,是否删除队列;arguments: 其他参数
		ch.queueDeclare("hellorabbitmq",false,false,false,null);
		//exchange, routingKey 和队列名相同, props, body消息数据类型，必须为byte数组
		ch.basicPublish("", "hellorabbitmq", null, ("Hello rabbitmq2:时间"+System.currentTimeMillis()).getBytes());
		System.out.println("消息已发送");
		//关闭连接
		c.close();
	}
}
