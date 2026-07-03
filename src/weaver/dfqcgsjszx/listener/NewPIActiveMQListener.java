package weaver.dfqcgsjszx.listener;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import weaver.general.BaseBean;

public class NewPIActiveMQListener implements ServletContextListener {

	private BaseBean baseBean = new BaseBean();

	public void contextDestroyed(ServletContextEvent sce) {
		try {
			Session session = (Session) sce.getServletContext().getAttribute(
					"piSession");
			if (session != null) {
				session.close();
				session = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Connection conn = (Connection) sce.getServletContext()
					.getAttribute("piConn");
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void contextInitialized(ServletContextEvent sce) {
		String brokerURL = "failover:(tcp://10.4.12.26:61616,tcp://10.4.12.27:61616,tcp://10.4.12.75:61616)?randomize=false";
		String queueName = "queue/dftc-newoa_gw";
		System.out.println("**************************开始启动PI的ActiveMQ监听*********************");
		baseBean.writeLog("**************************开始启动PI的ActiveMQ监听*********************");
		try {
			initListener(brokerURL, queueName, sce);
		} catch (Exception e) {
			e.printStackTrace();
			baseBean.writeLog("**************************启动PI的ActiveMQ监听失败*********************");
			System.out.println("**************************启动PI的ActiveMQ监听失败*********************");
		}
		baseBean.writeLog("**************************启动PI的ActiveMQ监听成功*********************");
		System.out.println("**************************启动PI的ActiveMQ监听成功*********************");
	}

	private void initListener(String brokerURL, String queueName,
			ServletContextEvent sce) throws Exception {
		baseBean.writeLog("**************************开始初始化*********************");
		System.out.println("**************************开始初始化*********************");
		// 创建连接
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				brokerURL);
		
		baseBean.writeLog("**************************创建连接工厂完毕*********************");
		System.out.println("**************************创建连接工厂完毕*********************");
		
		Connection conn = connectionFactory.createConnection();
		
		baseBean.writeLog("**************************创建连接完毕*********************");
		System.out.println("**************************创建连接完毕*********************");
		
		conn.start();
		

		baseBean.writeLog("**************************连接启动完毕*********************");
		System.out.println("**************************连接启动完毕*********************");

		// 创建消息对象，添加消息监听服务
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(queueName);
		MessageConsumer consumer = session.createConsumer(destination);

		consumer.setMessageListener(new MessageListener() {
			public void onMessage(Message message) {
				try {
					baseBean.writeLog("接收到消息：" + message);
					System.out.println("接收到消息：" + message);
					if (message instanceof ActiveMQTextMessage) {
						baseBean.writeLog("ActiveMQTextMessage消息");
						System.out.println("ActiveMQTextMessage消息");
						ActiveMQTextMessage objectMessage = (ActiveMQTextMessage) message;
						String msg = objectMessage.getText();
						
						baseBean.writeLog("Text消息内容：" + msg);
						System.out.println("Text消息内容：" + msg);
					}else if (message instanceof ActiveMQObjectMessage) {
						baseBean.writeLog("ActiveObjectMessage消息");
						System.out.println("ActiveObjectMessage消息");
						ActiveMQObjectMessage objectMessage = (ActiveMQObjectMessage) message;
						Object object = objectMessage.getObject();
						if (object instanceof byte[]) {
							baseBean.writeLog("byte消息内容："
									+ new String((byte[]) object, "UTF-8"));
							System.out.println("byte消息内容："
									+ new String((byte[]) object, "UTF-8"));
						} else if (object instanceof String) {
							baseBean.writeLog("String消息内容：" + object);
							System.out.println("String消息内容：" + object);
						} else {
							if (object == null) {
								Enumeration keys = message.getPropertyNames();
								baseBean.writeLog("消息内容：");
								System.out.println("消息内容：");
								while (keys.hasMoreElements()) {
									String key = (String) keys.nextElement();
									baseBean.writeLog(key
											+ ":"
											+ objectMessage
													.getStringProperty(key));
									System.out.println(key
											+ ":"
											+ objectMessage
													.getStringProperty(key));
								}
							} else {
								baseBean.writeLog("其他不支持的消息类型");
								System.out.println("其他不支持的消息类型");
							}
						}
					} else if (message instanceof ActiveMQMapMessage) {
						ActiveMQMapMessage mapMessage = (ActiveMQMapMessage) message;

						Enumeration keys = mapMessage.getMapNames();

						baseBean.writeLog("消息内容：");
						System.out.println("消息内容：");

						while (keys.hasMoreElements()) {
							String key = (String) keys.nextElement();
							baseBean.writeLog(key + ":"
									+ mapMessage.getString(key));
							System.out.println(key + ":"
									+ mapMessage.getString(key));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					baseBean.writeLog("消息监听出现异常");
					System.out.println("消息监听出现异常");
				}
			}

		});

		sce.getServletContext().setAttribute("piSession", session);
		sce.getServletContext().setAttribute("piConn", conn);
	}

}
