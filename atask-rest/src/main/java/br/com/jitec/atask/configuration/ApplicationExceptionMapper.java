package br.com.jitec.atask.configuration;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.com.jitec.atask.business.task.TaskInProcessException;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<Exception> {

	private static final String REASON = "Reason";

	@Override
	public Response toResponse(Exception exception) {
		return handleThrowable(exception);
	}

	private Response handleThrowable(Throwable exception) {
		if (exception != null) {
			if (exception instanceof WebApplicationException) {
				return ((WebApplicationException) exception).getResponse();
			}

			if (exception instanceof TaskInProcessException) {
				return Response.status(Status.PRECONDITION_FAILED).header(REASON, exception.getMessage()).build();
			}

			if (exception.getCause() != null) {
				return handleThrowable(exception.getCause());
			}

			return Response.serverError().header(REASON, exception.getMessage()).build();

		} else {
			return handleNullException();
		}
	}

	private Response handleNullException() {
		return Response.serverError().header(REASON, "Unknown").build();
	}

}
