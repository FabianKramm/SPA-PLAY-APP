package controllers;

import java.util.ArrayList;

import models.core.exceptions.ProcessInstanceNotFoundException;
import models.core.exceptions.ProcessModelNotFoundException;
import models.core.process.Activity;
import models.core.process.ProcessInstance;
import models.core.process.ProcessModel;
import models.core.util.parsing.ProcessParser;
import models.util.http.Parameters;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.With;
import play.twirl.api.Html;
import views.html.pages.main;
import views.html.process.activity_instances;
import views.html.ajax_concat;

@With(ActionController.class)
public class ProcessExecutor extends Controller {
	public static Result xmlProcess(String processID) {
    	ProcessModel processModel;
		try {
			processModel = new ProcessModel(processID);
			
			return ok(processModel.getBPMN_XML());
		} catch (ProcessModelNotFoundException e) {
			return notFound();
		}
    }
    
    public static Result uploadProcess() {
    	MultipartFormData body = request().body().asMultipartFormData();
    	FilePart filepart = body.getFile("bpmn_file");
    	
    	if (filepart != null) {Logger.debug(Parameters.get("process_name"));
    		ProcessModel.createFromBPMN_File(filepart, Parameters.get("process_name"));
    		
    		return redirect(routes.Page.manageProcessModels());
    	}
    	else {
    		flash("error", "Missing file");
    		return redirect(routes.Page.index());
    	}
    }
    
    public static Result startProcess() {
    	String processID = Parameters.get("process_model");
    	Logger.info("Loading process " + processID);
    	ProcessModel processModel;
    	
		try {
			processModel = new ProcessModel(processID);
		} catch (ProcessModelNotFoundException e) {
			return notFound("Process Model not found! (ID: " + processID + ")");
		}
    	AuthController.getUser().createProcessInstance(processModel);
    	
    	return ok(ajax_concat.render(main.render(), null, routes.Page.index().url()));
    }
    
    public static Result setCurrentProcess() {
    	String processInstanceID = Parameters.get("process_instance");
    	
    	ProcessInstance processInstance;
    	
		try {
			processInstance = new ProcessInstance(processInstanceID);
			AuthController.getUser().setCurrentProcessInstance(processInstance);
			
			return ok(ajax_concat.render(main.render(), null, routes.Page.index().url()));
		} catch (ProcessInstanceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return notFound("Process Instance not found! (ID: " + processInstanceID + ")");
		}
    }
    
    public static Result performActivity() {
    	String processInstanceID = Parameters.get("process_instance");
    	String activityID = Parameters.get("current_activity");
    	
		try {
			ProcessInstance processInstance = new ProcessInstance(processInstanceID);
			
			Activity activity = new Activity(ProcessParser.nsm + activityID, processInstance.getProcessModel());
			
			processInstance.setCurrentActivities(activity.getNextActivities());
			
			return ok(activity_instances.render(processInstance));
		} catch (ProcessInstanceNotFoundException e1) {
			return notFound("Process Instance not found! (ID: " + processInstanceID + ")");
		}
    }
    
    public static Result performGatewayAction() {
    	String processInstanceID = Parameters.get("process_instance");
    	String[] nextActivities = Parameters.get("next_activity").split(",");
    	Logger.info(nextActivities.toString());
		try {
			ProcessInstance processInstance = new ProcessInstance(processInstanceID);
	    	
			ArrayList<Activity> newCurrentActivities = new ArrayList<Activity>();
			
			for (String nextActivityId : nextActivities) {
				newCurrentActivities.add(new Activity(ProcessParser.nsm + nextActivityId, processInstance.getProcessModel()));
			}
			processInstance.setCurrentActivities(newCurrentActivities);
			
			return ok(activity_instances.render(processInstance));
		} catch (ProcessInstanceNotFoundException e) {
			e.printStackTrace();
			return notFound("Process Instance not found! (ID: " + processInstanceID + ")");
		}
    }
}