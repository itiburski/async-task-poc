package br.com.jitec.atask.business.task;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@MessageDriven(name="TaskProcessor", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/TaskQueue")
})
public class TaskProcessor implements MessageListener {

	@EJB
	private TaskLocal taskBean;
	
	@Override
	public void onMessage(Message message) {
		try {
			if (message instanceof ObjectMessage) {
				final ObjectMessage objectMessage = (ObjectMessage) message;
				Task task = objectMessage.getBody(Task.class);
				taskBean.processTask(task);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
