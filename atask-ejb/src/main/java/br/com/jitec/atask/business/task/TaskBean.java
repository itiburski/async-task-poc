package br.com.jitec.atask.business.task;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
@Local(TaskLocal.class)
public class TaskBean implements TaskLocal{

	@PersistenceContext
	private EntityManager em;

	@EJB
	private TaskQueueProducerBeanLocal taskQueueProducerBean;

	@Override
	public List<Task> getAll() {
		return em.createQuery("Select t from Task t", Task.class).getResultList();
	}

	@Override
	public List<Task> getPending() {
		TypedQuery<Task> query = em.createQuery("Select t from Task t where t.status = :pendingStatus", Task.class);
		query.setParameter("pendingStatus", Status.PENDING);
		return query.getResultList();
	}

	@Override
	public Task getTask(Integer id) {
		Task task = em.find(Task.class, id);
		if (task != null && !Status.DONE.equals(task.getStatus())) {
			throw new TaskInProcessException("Task is processing, not Done yet!");
		}
		return task;
	}

	@Override
	public Task newTask() {
		Task task = new Task();
		task.setStatus(Status.PENDING);
		em.persist(task);

		taskQueueProducerBean.send(task);

		return task;
	}

	@Override
	public void processTask(Task task) {
			System.out.println("Start processing task #" + task.getId() + " at " + Calendar.getInstance().getTime());
			slowProcess();
			System.out.println("Finish processing task #" + task.getId() + " at " + Calendar.getInstance().getTime());

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
