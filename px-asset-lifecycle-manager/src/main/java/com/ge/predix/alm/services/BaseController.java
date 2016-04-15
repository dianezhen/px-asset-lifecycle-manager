package com.ge.predix.alm.services;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public abstract class BaseController {

	private static final Logger log = Logger.getLogger(BaseController.class);

	/**
	 * 
	 * @param throwable
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody String handleSystemException(Exception ex,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.error("Unhandled Exception:" + ex.getLocalizedMessage(), ex);
		return "Internal Server Error";

	}
	/**
	 * 
	 * @param throwable
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public @ResponseBody String handleUnAuthorizedException(Exception ex,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.error("Unauthorized for Resource:" + ex.getLocalizedMessage(), ex);
		return "401-Unauthorized for Resource";

	}

	/**
	 * Returns the appropriate response when resource not found.
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	// 404
	public @ResponseBody String handleResourceNotFound(Exception ex,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.error("Resource Not Found Exception" + ex.getLocalizedMessage(), ex);
		return "Not Found Exception:" + ex.getMessage();
	}
}
