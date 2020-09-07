package br.com.jitec.atask.business.task;

import java.util.Calendar;
import java.util.Random;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Local(TaskLocal.class)
public class TaskBean implements TaskLocal{

	@Resource(mappedName = "java:/jms/queue/TaskQueue")
	private Queue taskQueue;
	
	@Inject
	private JMSContext context;
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Task newTask() {
		Task task = new Task();
		task.setStatus(Status.PENDING);
		em.persist(task);

		context.createProducer().send(taskQueue, task);
		System.out.println("Task " + task.getId() + " sent to taskQueue");

		return task;
	}

	@Override
	public void processTask(Task task) {
			System.out.println("Start processing task " + task.getId() + " at " + Calendar.getInstance().getTime());
			slowProcess();
			System.out.println("Finish processing task " + task.getId() + " at " + Calendar.getInstance().getTime());

			task.setStatus(Status.DONE);
			em.merge(task);
	}

	/**
	 * This method simulate a slow process
	 */
	private void slowProcess() {
		try {
			Thread.sleep(getRandomNumber(5000, 25000));
		} catch (InterruptedException e) {
			throw new RuntimeException("Error while processing task", e);
		}
	}

	private int getRandomNumber(int min, int max) {
		Random random = new Random();
		return random.ints(min, max).findFirst().getAsInt();
	}

}
