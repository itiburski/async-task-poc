package br.com.jitec.atask.business.task;

public interface TaskLocal {

	Task newTask();
	
	void processTask(Task task);
	
}
