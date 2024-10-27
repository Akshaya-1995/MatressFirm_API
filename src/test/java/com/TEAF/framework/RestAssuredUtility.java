package com.TEAF.framework;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.TEAF.framework.HashMapContainer;
import com.TEAF.stepDefinitions.RestAssuredSteps;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestAssuredUtility {
	
	static Logger log = Logger.getLogger(RestAssuredUtility.class.getName());
	Response response;

	public Response Get(String path, RequestSpecification spec) {
		response = spec.get(path);
		return response;
	}

	public Response Post(String url, RequestSpecification spec) {
		response = spec.post(url);
		return response;
	}

	public Response Put(String url, RequestSpecification spec) {

		response = spec.patch(url);
		return response;
	}

	public Response Delete(String url, RequestSpecification spec) {
		response = spec.delete(url);
		return response;
	}

	public Response SOAP(String url, String file) throws Throwable {
		FileInputStream fileinputstream = new FileInputStream(file);
		RestAssured.baseURI = url;
		Response response = given().header("Content-Type", "text/xml").and()
				.body(IOUtils.toString(fileinputstream, "UTF-8")).when().post("");
		return response;
	}

	public static File updateJSONforPOSTRequest(File f, String key, String store) throws Exception {
		DocumentContext parse = JsonPath.parse(f);
		if (store.startsWith("$$")) {
			store = HashMapContainer.get(store);
		}

		DocumentContext set = parse.set(key, store);
		String jsonString = set.jsonString();
		Files.write(Paths.get(f.getAbsolutePath()), jsonString.getBytes());
		return f;
	}

	
	
	public static void retriveJSONforPOSTRequest(Response res, String key, String store) throws Exception {

		Object value = JsonPath.read(res.getBody().asString(), key);
		log.info("Stored value " + String.valueOf(value));
		HashMapContainer.add("$$" + store, String.valueOf(value));
	}

//	public static void retrivexmlforPOSTRequest(Response res, String key, String store) throws Exception {
//
////		Object value = XmlPath.read(res.getBody().asString(), key);
//		log.info("Stored value " + String.valueOf(value));
//		HashMapContainer.add("$$" + store, String.valueOf(value));
//	}
	public static void retriveJSONforPOSTRequestwithObjectIndex(Response res, String key, String store, String index)
			throws Exception {

		List<String> value = JsonPath.read(res.getBody().asString(), key);
		System.out.println("Stroed value " + value);
		HashMapContainer.add("$$" + store, String.valueOf(value.get(Integer.parseInt(index))));
		System.out.println(value);
	}
}
