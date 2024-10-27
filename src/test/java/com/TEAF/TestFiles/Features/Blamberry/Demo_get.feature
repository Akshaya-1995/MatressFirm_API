Feature: Verify Teaf for RestAssured

@Demo_get
Scenario: Verify the get functionality and print response in console
Given EndPoint 'https://reqres.in/api/users?page=2'
And Content type 'application/json' 
When Method 'Get'
And Print response 
Then Statuscode '200'


@Demo_get
Scenario: Verify the GET functionality and print response in file
Given EndPoint 'http://localhost:3030/products'
And Content type 'application/json' 
When Method 'Get' 
And Print response
And Print responsetime
And Statuscode '200'


@Demo_get
Scenario: Verify the GET functionality and print responsetime
Given EndPoint 'http://localhost:3030/products'
And Content type 'application/json' 
When Method 'Get' 
And Print response
And Print response to file 'Test/File'
And Print responsetime
And Statuscode '200'

@Demo_get
Scenario: Verify the GET functionality and Match the Json path
Given EndPoint 'http://localhost:3030/products'
And Content type 'application/json' 
When Method 'Get' 
And Print response
And Print response to file 'Test/File'
And Print responsetime
And Statuscode '200'
Then Match JSONPath "data.[0]name" contains "Duracell - AAA Batteries (4-Pack)"


@Demo_get
Scenario: Verify the GET functionality and Verify the value
Given EndPoint 'http://localhost:3030/products'
And Content type 'application/json' 
When Method 'Get' 
And Print response
And Print response to file 'Test/File'
And Print responsetime
And Statuscode '200'
Then Match JSONPath "data.[0]name" contains "Duracell - AAA Batteries (4-Pack)"
And Verify Value "43900" present in field "data[0].id"




@Demo_get
Scenario: Verify the GET functionality, Retrive & Verify the value
Given EndPoint 'http://localhost:3030/products'
And Content type 'application/json' 
When Method 'Get' 
And Print response
And Print response to file 'Test/File'
And Print responsetime
And Statuscode '200'
Then Match JSONPath "data.[0]name" contains "Duracell - AAA Batteries (4-Pack)"
And Verify Value "43900" present in field "data[0].id"



@Demo_get
Scenario: Verify the get functionality in xml
Given EndPoint 'https://www.w3schools.com/xml/tempconvert.asmx'
And Content type 'text/xml' 
When Method 'Get'
And Print response
And Print responsetime
And Statuscode '200'

@Demo_get
Scenario: Verify the get functionality in xml and print response in xml file
Given EndPoint 'https://www.w3schools.com/xml/tempconvert.asmx'
And Content type 'text/xml' 
When Method 'Get'
And Print response
And Print response to file for xml 'Test/File1'
And Print responsetime
And Statuscode '200'



