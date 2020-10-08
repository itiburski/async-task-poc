package br.com.jitec.atask.business.task;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;

@Stateless
@Local(TaskQueueProducerBeanLocal.class)
public class TaskQueueProducerBean implements TaskQueueProducerBeanLocal {

	@Resource(mappedName = "java:/jms/queue/TaskQueue")
	private Queue taskQueue;

	@Inject
	private JMSContext context;

	@Override
	public void send(Task task) {
		context.createProducer().send(taskQueue, task);
		System.out.println("Task #" + task.getId() + " sent to taskQueue");
	}

}
