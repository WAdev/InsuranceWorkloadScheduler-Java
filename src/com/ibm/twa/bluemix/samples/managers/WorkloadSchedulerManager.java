/*********************************************************************
 *
 * Licensed Materials - Property of IBM
 * Product ID = 5698-WSH
 *
 * Copyright IBM Corp. 2015. All Rights Reserved.
 *
 ********************************************************************/ 

package com.ibm.twa.bluemix.samples.managers;

import java.net.MalformedURLException;
import java.util.List;
import com.ibm.twa.applab.client.WorkloadService;
import com.ibm.twa.applab.client.exceptions.InvalidRuleException;
import com.ibm.twa.applab.client.exceptions.WorkloadServiceException;

import com.ibm.tws.simpleui.bus.Task;
import com.ibm.tws.simpleui.bus.TaskLibrary;
import com.ibm.tws.simpleui.common.json.Json2String;
import com.ibm.tws.simpleui.common.json.JsonObject;

public class WorkloadSchedulerManager extends Manager{
	
    final static String workloadServiceName = "WorkloadScheduler";
    final static String processLibraryName = "ts";
    final static String processName = "tsj";
    
	private boolean debugMode;
	
	private boolean submitted;
	private long tasklibraryid;
	private WorkloadService ws;
	
	private long myProcessId;
	
	final static String JOB_SECTION_SEPARATOR = "===============================================================";
	
	public WorkloadSchedulerManager(){
		super(workloadServiceName);
		this.setDebugMode(false);
	}
	
	public void connect() {
		try {
			this.ws = new WorkloadService(this.getUrl());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (WorkloadServiceException e) {
			e.printStackTrace();
		}
	}

	/** 
	 * getProcessLibraryByName
	 * 
	 * @param String libName: process library name used for find the process library
	 * @return TaskLibrary
	 * 
	 * @throws WorkloadServiceException 
	 * 
	 * return a process library found by name or null
	 * 
	 */
	private TaskLibrary getProcessLibraryByName(String libName) throws WorkloadServiceException{
		TaskLibrary taskLib = null;
		List<TaskLibrary> libraries = ws.getAllLibraries();
		for(TaskLibrary l : libraries){
			if(l.getName().equals(libName)){
				taskLib = l;
			}
		}
		return taskLib;
	}
	
	/** 
	 * getProcessByName
	 * 
	 * @param TaskLibrary library: process library where find a process by name
	 * @param String processName: process name used for find the process
	 * @return Task
	 * 
	 * @throws Exception, WorkloadServiceException 
	 * 
	 * return a process in a library found by name or null
	 * 
	 */
	public Task getProcessByName(TaskLibrary library, String processName) throws Exception, WorkloadServiceException{
		Task task = null;
		List<Task> tasks = ws.getTasks(library.getId());
		for(Task t: tasks){
			if(t.getName().equals(processName)){
				task =  t;
			}
		}	
		return task;
	}
	
	/**
	 * runProcess
	 * 
	 * Check if Workload Scheduler process exists and if not exists create it
	 * 
	 * @throws InvalidRuleException, Exception, WorkloadServiceException 
	 */
	public void runProcess(String email, int hp, int birthyear) throws InvalidRuleException, Exception, WorkloadServiceException {
		if (this.isConnected()) 
		{	
			TaskLibrary lib = this.getProcessLibraryByName(WorkloadSchedulerManager.processLibraryName);
			if(lib != null){
				System.out.println(lib.getName() + " process library found");
				this.tasklibraryid = lib.getId();
				Task task = this.getProcessByName(lib, WorkloadSchedulerManager.processName);
				if(task != null){
					System.out.println(task.getName() + " process found");
					this.submitted = true;
					
					JsonObject variables = new JsonObject();
					variables.addMember("email", email);
					variables.addMember("hp", hp);
					variables.addMember("birthyear", birthyear);
					
					//pass "variables" as second argument of the runTask
					//when the twaclient library
					//and the saas will be refresh
					this.ws.runTask(task.getId() /* , variables */ );
					
				} else{
					System.out.println("Process not found");
					System.out.println("(Have you already import the sample process on application lab?)");
				}
			} else{
				System.out.println("Process library not found");
				System.out.println("(Have you al ready created the process library on application lab?)");
			}
		} else {
			System.out.println("Service not connected, please try again");
		}	
	}
	
	public String getAppURI() {
		String uri = null;
		String vcapJSONString = System.getenv("VCAP_APPLICATION");
		if (vcapJSONString != null) {
			com.google.gson.JsonObject app = new com.google.gson.JsonParser().parse(vcapJSONString).getAsJsonObject();
			com.google.gson.JsonArray uris = app.get("application_uris").getAsJsonArray();
			uri = uris.get(0).getAsString();
		}
		return uri;
	}
	
	public WorkloadService getWs() {
		return ws;
	}
	
	public void setWs(WorkloadService ws) {
		this.ws = ws;
	}

	public boolean isSubmitted() {
		return submitted;
	}
	
	public void setSubmitted(boolean isSubmitted) {
		this.submitted = isSubmitted;
	}
	
	public long getTasklibraryid() {
		return tasklibraryid;
	}
	
	public void setTasklibraryid(long tasklibraryid) {
		this.tasklibraryid = tasklibraryid;
	}
	
	public long getMyProcessId() {
		return myProcessId;
	}
	
	public void setMyProcessId(long myProcessId) {
		this.myProcessId = myProcessId;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}	
}
