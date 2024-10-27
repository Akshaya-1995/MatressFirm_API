package com.TEAF.stepDefinitions;

import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;
import static com.jayway.jsonpath.JsonPath.parse;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import com.TEAF.Hooks.Hooks;
import com.TEAF.Json.Pojo.Sample;
import com.TEAF.framework.API_UserDefined;
import com.TEAF.framework.HashMapContainer;
//import com.TEAF.framework.JestUtility;
import com.TEAF.framework.RestAssuredUtility;
import com.jayway.jsonpath.Configuration;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
//import io.searchbox.client.JestClient;
//import io.searchbox.client.JestClientFactory;
//import io.searchbox.client.config.HttpClientConfig;
//import io.searchbox.core.Search;
//import io.searchbox.core.SearchResult;
//import io.searchbox.indices.IndicesExists;
//import static io.restassured.RestAssured.given;

public class RestAssuredSteps {

	RestAssuredUtility Rest = new RestAssuredUtility();
	static API_UserDefined ap = new API_UserDefined();
	static Logger log = Logger.getLogger(RestAssuredSteps.class.getName());

	@Given("^URL '(.*)'$")
	public static void URL(String url) {
		HashMapContainer.add("$$URL", url);
	}

	@Given("EndPoint {string}")
	public static void endPoint(String point) {
		String URL = HashMapContainer.get("$$URL");
		if (URL != null) {
			ap.setPath(URL + point);	
		} else {
			ap.setPath(point);
		}
	}

	public static Response getResponse() {
		return ap.getRes();
	}

	@When("Method {string}")
	public void Method(String Method_Name) throws Exception {
		log.info("===========================" + Method_Name + "=========================");
		String Path = ap.getPath();
		log.info(Path);
		RequestSpecification spec = ap.getReq().log().all();
		Response GetResponse = null;
		if (Method_Name.equalsIgnoreCase("get")) {
			GetResponse = Rest.Get(Path, spec);
		} else if (Method_Name.equalsIgnoreCase("post")) {
			GetResponse = Rest.Post(Path, spec);

		} else if (Method_Name.equalsIgnoreCase("put")) {
			GetResponse = Rest.Put(Path, spec);

		} else if (Method_Name.equalsIgnoreCase("delete")) {
			GetResponse = Rest.Delete(Path, spec); 
		} else {
			throw new Exception("Not a valid method");
		}
		ap.setRes(GetResponse);
		ap.setReq(given().when());

		Hooks.scenario.write("Response : \n" + GetResponse.getBody().asString());
	}
	
	@When("I set name '(.*)' and job '(.*)' to JSON object and pass it as Request body")
    public void i_set_the_value_to_json_object(String name, String job) {
        Sample s = new Sample();
        s.setJob(job);
        s.setName(name);
        
        
        RequestSpecification spec = ap.getReq().body(s).log().all();
        ap.setReq(spec);
        Hooks.scenario.write(Sample.class.getName());
    }
    

	@Then("^Statuscode '(.*)'$")
	public void statuscode(int Expected) {
		int Actual = ap.getRes().andReturn().statusCode();
		assertEquals("Status code Validation", Expected, Actual);
	}

	@Then("Match JSONPath {string} contains {string}")
	public void match_JSONPath_contains(String path, String Expected) {
	if (Expected.startsWith("$$")) {
		    HashMapContainer.get(Expected);
		}
		
		String Actual = ap.getRes().getBody().asString();
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(Actual);

		try {
			List<Object> actual;

			actual = com.jayway.jsonpath.JsonPath.read(document, path); 
			if (actual.size() == 1) {
				String singledata = String.valueOf(actual.get(0));
				assertEquals(" Json Path Value Check ", Expected,
						singledata.toString().replace("[", "").replace("]", "").toString());

			}
		assertEquals(" Json Path Value Check ", Expected,
				actual.toString().replace("[", "").replace("]", "").toString());

		} catch (java.lang.ClassCastException e) {
			String actual;

		actual = String.valueOf(com.jayway.jsonpath.JsonPath.read(document, path));
			assertEquals(" Json Path Value Check ", Expected, actual);

		}
}
//	@Then("Match JSONPath {string} contains {string}")
//	public void match_JSONPath_contains(String path, String Expected) {
//	if (Expected.startsWith("$$")) {
//	Expected = HashMapContainer.get(Expected);
//	}
//
//	 String Actual = ap.getRes().getBody().asString();
//	Object document = Configuration.defaultConfiguration().jsonProvider().parse(Actual);
//
//	 try {
//	List<Object> actual;
//
//	 actual = com.jayway.jsonpath.JsonPath.read(document, path);
//	if (actual.size() == 1) {
//	String singledata = String.valueOf(actual.get(0));
//	assertEquals(" Json Path Value Check ", Expected, singledata);
//
//	 }
//
//	 boolean contains = actual.contains(Expected);
//	assertTrue(contains);
//
//	 } catch (java.lang.ClassCastException e) {
//	String actual;
//
//	 actual = String.valueOf(com.jayway.jsonpath.JsonPath.read(document, path));
//	assertEquals(" Json Path Value Check ", Expected, actual);
//
//	 }
//
//	 }
	@Then("Verify Value {string} present in field {string}")
	public void match_response_contains_the_data_expected(String Expected, String path) throws Exception {
//    String bdy = RestAssuredSteps.getResponse().getBody().asString();
		if (Expected.startsWith("$$"))
				{
			 Expected =HashMapContainer.get(Expected);
				
				}
		System.out.println(Expected);
		if (Expected.length() > 0) {
			String keyValue = RestAssuredSteps.getResponse().getBody().jsonPath().get(path).toString();
			log.info("-------------------------------------------------------------------" + keyValue);
			Assert.assertEquals("Value did not match", Expected.toString(), keyValue.toString());
		}
	}

	@Then("Verify Value {string} present in field {string} for xml")
	public void match_response_contains_the_data_expected_xml(String Expected, String path) throws Exception {
//    String bdy = RestAssuredSteps.getResponse().getBody().asString();
		if (Expected.startsWith("$$"))
		{
	 Expected =HashMapContainer.get(Expected);
		
		}
		System.out.println(Expected);
		if (Expected.length() > 0) {
			String keyValue = RestAssuredSteps.getResponse().getBody().xmlPath().get(path).toString();
			log.info("-------------------------------------------------------------------" + keyValue);
			Assert.assertEquals("Value did not match", Expected.toString(), keyValue.toString());
		}
	}

	
	
	
	@Then("^Match JSONPath with filter '(.*)' : '(.*)' equals '(.*)' contains '(.*)'$")
	public void match_JSONPath_filter_contains(String key, String value, String path, String Expected) {
		String Actual = ap.getRes().getBody().asString();

		com.jayway.jsonpath.Filter cheapFictionFilter = filter(where(key).is(value));

		try {
			List<String> actual;
			actual = parse(Actual).read(path, cheapFictionFilter);
			log.info(actual);
			assertEquals(" Json Path Value Check ", Expected, actual.toString());

		} catch (java.lang.ClassCastException e) {
			String actual;

			actual = parse(Actual).read(path, cheapFictionFilter);
			assertEquals(" Json Path Value Check ", Expected, actual);

		}

	}

	@Then("Match header {string} contains {string}")
	public void match_header_contains(String string, String Expected) {

		String Actual = ap.getRes().getHeader(string);
		if (Objects.equals(Actual, Expected))
			assertEquals("Header Matching", Expected, Actual);

	}

	@Then("^Print responsetime$")
	public void print_responsetime() {
		long temp = ap.getRes().getTime();
		log.info("Response Time" + temp + "ms");
	}

	@Then("Print response")
	public void print_response() {
		log.info("Response:" + ap.getRes().getBody().prettyPrint());
	}

	@Then("^Path '(.*)' is present in Response$")
	public void path_present_in_response(String path) {
		ResponseBody body = ap.getRes().getBody();
		JsonPath jsonPath = body.jsonPath();
		String obj;
		
			obj = jsonPath.get(path);
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&test"+obj);
		
//			obj = null;
		
		Assert.assertNotNull(obj);

	}

	@Then("Print response to file {string}")
	public void print_response_to_file(String path) throws ClassNotFoundException, IOException, ParseException {
		String Response = ap.getRes().getBody().prettyPrint();
		File f = new File(System.getProperty("user.dir") + "\\src\\test\\java\\com\\TEAF\\json\\" + path + ".json");
		boolean Exists = f.exists();
		if (Exists) {
			log.info("Printing response to File");
			this.write(f, Response);
		} else {
			f.createNewFile();
			log.info("New file created");
			this.write(f, Response);
		}

	}

	
	@Then("Print response to file for xml {string}")
	public void print_response_to_file_xml(String path) throws ClassNotFoundException, IOException, ParseException {
		String Response = ap.getRes().getBody().prettyPrint();
		File f = new File(System.getProperty("user.dir") + "\\src\\test\\java\\com\\TEAF\\json\\" + path + ".xml");
		boolean Exists = f.exists();
		if (Exists) {
			log.info("Printing response to File");
			this.write(f, Response);
		} else {
			f.createNewFile();
			log.info("New file created");
			this.write(f, Response);
		}

	}
	
	
	private void write(File f, String Response) {
		try {
			FileOutputStream f1 = new FileOutputStream(f);
			ObjectOutputStream o = new ObjectOutputStream(f1);
			o.writeObject(Response);
			o.close();
			f1.close();
		} catch (FileNotFoundException e) {
			log.error("File not found");
		} catch (IOException e) {
			log.error("Error initializing stream");
		}
	}

	@And("Match XMLPath {string} contains {string}")
	public void XMLPath(String xmlpath, String Expected) {

		XmlPath xml = new XmlPath(ap.getRes().asString());
		String Actual = xml.getString(xmlpath);
		assertEquals("Xml Path String check", Actual, Expected);

	}
	
	@And("Retrieve data '(.*)' from xml response and store in variable '(.*)'$")
	public static void retriveXMLdata( String key, String store) throws Exception {

        XmlPath xml = new XmlPath(ap.getRes().getBody().asString());
        String keypath = xml.getString(key);
        log.info("Stored value " + String.valueOf(keypath));
        HashMapContainer.add("$$" + store, String.valueOf(keypath));
    }

	@And("^Query params with Key '(.*)' and value '(.*)'$")
	public void updatequeryParams(String key, String value) {
		if (value.startsWith("$$")) {
			value = HashMapContainer.get(value);
		}
		RequestSpecification res = ap.getReq().queryParam(key, value);
		ap.setReq(res);

	}

	@Given("Header key {string} value {string}")
	public void header_key_value(String string, String string2) throws Exception {
		if (string2.startsWith("@")) {
			String f = System.getProperty("user.dir") + "\\src\\test\\java\\com\\TEAF\\json\\" + string2.substring(1);
			string2 = FileUtils.readFileToString(new File(f), StandardCharsets.UTF_8);

		}
		RequestSpecification spec = ap.getReq().header(string, string2.trim());
		ap.setReq(spec);
	}

	@Given("Content type {string}")
	public void content_type(String string) {
		RequestSpecification spec = ap.getReq().contentType(string);
		ap.setReq(spec);
	}

	@Given("Request body {string}")
	public void request_body(String string) throws IOException {
		try {
			File f = new File(
					System.getProperty("user.dir") + "\\src\\test\\java\\com\\TEAF\\json\\" + string + ".json");
			RequestSpecification spec = ap.getReq().body(f);
			ap.setReq(spec);
			byte[] readAllBytes = Files.readAllBytes(Paths
					.get(System.getProperty("user.dir") + "\\src\\test\\java\\com\\TEAF\\json\\" + string + ".json"));
			String s = new String(readAllBytes);
			Hooks.scenario.write(s);
		} catch (Exception e) {
			if (string.startsWith("@"))
			{
				RequestSpecification spec = ap.getReq().body(string);
				ap.setReq(spec);
			}
		}
	}
	
	
	@Given("Request body for xml {string}")
	public void request_body_xml(String string) throws IOException {
		try {
			File f = new File(
					System.getProperty("user.dir") + "\\src\\test\\java\\com\\TEAF\\json\\" + string + ".xml");
			RequestSpecification spec = ap.getReq().body(f);
			ap.setReq(spec);
			byte[] readAllBytes = Files.readAllBytes(Paths
					.get(System.getProperty("user.dir") + "\\src\\test\\java\\com\\TEAF\\json\\" + string + ".xml"));
			String s = new String(readAllBytes);
			Hooks.scenario.write(s);
		} catch (Exception e) {
			if (string.startsWith("@"))
			{
				RequestSpecification spec = ap.getReq().body(string);
				ap.setReq(spec);
			}
		}
	}

	@Given("Request body using JSON object {string}")
	public static void request_body_json_object(String string) throws IOException {
		RequestSpecification spec = ap.getReq().body(string);
		ap.setReq(spec);
		Hooks.scenario.write(string);
	}
	
	

	@Given("Authorization Bearer {string}")
	public void authBearerToken(String string) throws IOException {
		if (string.startsWith("$$")) {
			string = HashMapContainer.get(string);
			log.info(string);
		}
		if (string.startsWith("@")) {
			String f = System.getProperty("user.dir") + "\\src\\test\\java\\com\\TEAF\\json\\" + string.substring(1);
			string = FileUtils.readFileToString(new File(f), StandardCharsets.UTF_8);

		}
		RequestSpecification header = ap.getReq().header("Authorization", "Bearer " + string);
		ap.setReq(header);
	}

	@When("^Retrieve data '(.*)' from response and store in variable '(.*)'$")
	public void retrivedata_store(String key, String store) throws Exception {
		Response res = ap.getRes();
		RestAssuredUtility.retriveJSONforPOSTRequest(res, key, store);
	}

	@When("^Retrieve data '(.*)' from object index '(.*)' in response and store in variable '(.*)'$")
	public void retrivedata_store_withobject_index(String key, String index, String store) throws Exception {
		Response res = ap.getRes();
		RestAssuredUtility.retriveJSONforPOSTRequestwithObjectIndex(res, key, store, index);
	}

	@When("^Update data in the Request '(.*)' for key '(.*)' with data '(.*)'$")
	public static void update_data_request(String f, String key, String store) throws Exception {
		File ft = new File(System.getProperty("user.dir") + "\\src/test/java/com/TEAF/json/" + f + ".json");
		RestAssuredUtility.updateJSONforPOSTRequest(ft, key, store);
	}

	@When("^Key '(.*)' count present in the response - '(.*)'$")
	public void key_count_present_in_the_response(String key, String count) {
		List<String> read = com.jayway.jsonpath.JsonPath.read(ap.getRes().getBody().asString(), key);
		Assert.assertEquals(Integer.parseInt(count), read.size());
	}

	@Then("^Response Key '(.*)' length matches '(.*)'$")
	public void response_key_length(String key, int Expected) {
		Object length = com.jayway.jsonpath.JsonPath.read(ap.getRes().getBody().asString(), key + ".length()");
		log.info("Length " + length);
		Assert.assertEquals(Expected, length);
	}

	

}
