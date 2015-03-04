package controllers;

import java.io.File;

import models.core.ProcessInstance;
import models.core.ProcessModel;
import models.core.exceptions.ProcessModelNotFoundException;
import models.util.sessions.User;
import play.Logger;
import play.mvc.*;
import views.html.*;

public class Page extends Controller {

    public static Result index() {
    	Application.db.connect();
    	
    	/*Logger.debug("GetStarted!!");
    	
    	//System.out.println(ProcessModel.createFromBPMN_File(new File("test.xml")).getId());
    	try{
    		// Works
    		ProcessModel pm = new ProcessModel("Process_1");
    		
    		// Works
    		ProcessInstance pi = ProcessInstance.create(new User("1", "Fabian"), pm);
    		pi.getSPAProcessInstance().store();
    		
    		// Works
    		ProcessInstance pi2 = new ProcessInstance(pi.getId());
    	}catch(ProcessModelNotFoundException e){
    		Logger.debug("Model not found!");
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	Logger.debug("Created bois!");*/
    	
        return ok(index.render("Your new application is ready."));
    }
    
    public static Result login() {
        return ok(login.render("Your new application is ready."));
    }
    /*
    public static Result main() {
        return ok(main.render("Your new application is ready."));
    }*/

}
