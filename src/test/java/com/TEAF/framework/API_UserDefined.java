package com.TEAF.framework;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class API_UserDefined {

	
	private RequestSpecification req = given().when();
	
	private Response res;
	
	private String Path;
	
	private String tempVariable;

	public String getTempVariable() {
		return tempVariable;
	}

	public void setTempVariable(String tempVariable) {
		this.tempVariable = tempVariable;
	}

	public RequestSpecification getReq() {
		return req;
	}

	public void setReq(RequestSpecification req) {
		this.req = req;
	}

	public Response getRes() {
		return res;
	}

	public void setRes(Response res) {
		this.res = res;
	}

	public String getPath() {
		return Path;
	}

	public void setPath(String path) {
		Path = path;
	}
	
	
	
	
}
