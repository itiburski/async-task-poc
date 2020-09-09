package br.com.jitec.atask.business.task;

public class TaskInProcessException extends RuntimeException {

	private static final long serialVersionUID = 5298167204331936681L;

	public TaskInProcessException(String message) {
		super(message);
	}

}
