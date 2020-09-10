package br.com.jitec.atask.resource.task;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.jitec.atask.business.task.Task;
import br.com.jitec.atask.business.task.TaskLocal;

@Path("/task")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class TaskResource {

	@EJB
	private TaskLocal taskBean;

	@GET
	@Path("/{id}")
	public Response getTask(@PathParam("id") Integer id) {
		Task task = taskBean.getTask(id);
		if (task == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok().entity(task).build();
	}

	@GET
	public Response getTasks() {
		List<Task> tasks = taskBean.getAll();
		return Response.ok().entity(tasks).build();
	}

	@GET
	@Path("/pending")
	public Response getPendingTasks() {
		List<Task> tasks = taskBean.getPending();
		return Response.ok().entity(tasks).build();
	}

	@POST
	public Response newTask() {
		Task task = taskBean.newTask();
		return Response.ok().entity(task).build();
	}

}
