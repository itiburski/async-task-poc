package br.com.jitec.atask.resource.task;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.jitec.atask.business.task.Task;
import br.com.jitec.atask.business.task.TaskLocal;

@Path("/task")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class TaskResource {

	@EJB
	private TaskLocal taskBean;
	
	@POST
	public Response newTask() {
		Task task = taskBean.newTask();
		return Response.ok().entity(task).build();
	}

}
