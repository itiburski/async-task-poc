package br.com.jitec.atask.business.task;

import java.util.List;

public interface TaskLocal {

	List<Task> getAll();

	List<Task> getPending();

	Task getTask(Integer id);

	Task newTask();
	
	void processTask(Task task);
	
}
