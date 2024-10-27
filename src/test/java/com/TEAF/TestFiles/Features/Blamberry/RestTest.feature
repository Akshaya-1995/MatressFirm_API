Feature: Verify Teaf for RestAssured

@Test
Scenario Outline: Verify the get functionality
Given EndPoint 'https://reqres.in/api/users'
And Content type 'application/json' 
#And Request body '@{ "name": "morpheus", "job": "leader"}'
And Request body using JSON object '{ "name": "morpheus", "job": "leader"}'
#And Request body 'Test/practice'
And Query params with Key 'page' and value '2'
When Method 'Get'
Then Statuscode '200'
Then Match JSONPath "<Queryparam>" contains "<Queryvalue>"

Examples: 
|Queryparam|  Queryvalue|
|$.data.[0]email|        michael.lawson@reqres.in|
|$.data.[1]email|        lindsay.ferguson@reqres.in|

						
@Test
Scenario: verifying the PUT functionality
Given EndPoint 'https://reqres.in/api/users'
And Header key 'Content-Type' value 'application/json' 
And I set name 'xxx' and job 'test' to JSON object and pass it as Request body
When Method 'PUT'
Then Statuscode '200'

@Test1
Scenario: Verify the Post functionality
Given EndPoint 'https://reqres.in/api/users'
And Content type 'application/json' 
And Request body using JSON object '{ "id": "1", "email": "george.bluth@reqres.in"}'
#And Request body '@{ "name": "morpheus", "job": "leader" }' 
When Method 'post'
And Print response 
And Statuscode '201'
Then Match JSONPath 'data[0].id' contains '1'
Then Match JSONPath 'data[0].email' contains 'george.bluth@reqres.in'
#And Verify Value "1" present in field 'data[0].id'
#And Verify Value 'george.bluth@reqres.in' present in field 'data[0].email'


@Test
Scenario: Verify the GET functionality
Given EndPoint 'https://reqres.in/api/users/2'
And Content type 'application/json'  
When Method 'Get'
And Print response 
And Statuscode '200'
And Verify Value 'janet.weaver@reqres.in' present in field 'data.email'


@Test
Scenario: Verify the GET functionality
Given EndPoint 'http://localhost:3030/products'
And Content type 'application/json' 
#And Query params with Key 'page' and value '2'
When Method 'GET' 
And Print response
And Print response to file 'Test/File'
And Print responsetime
And Statuscode '200'
Then Match JSONPath "data.[0]name" contains "Duracell - AAA Batteries (4-Pack)"
And Verify Value "43900" present in field "data[0].id"


@Test
Scenario: Verify the GET functionality
Given EndPoint 'https://www.w3schools.com/xml/tempconvert.asmx'
And Content type 'text/xml' 
And Request body for xml 'Test/Practice'
When Method 'post'
And Print response to file for xml 'Test/GO'
And Print responsetime
And Statuscode '200'
And Match XMLPath '//CelsiusToFahrenheitResult' contains '246.2'










