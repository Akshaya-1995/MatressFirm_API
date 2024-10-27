Feature: Verify Teaf for RestAssured

@Demo_post
Scenario: Verify the post functionality and print response in console
Given EndPoint 'https://reqres.in/api/users'
And Content type 'application/json' 
And Query params with Key 'page' and value '2'
When Method 'post'
And Print response 
Then Statuscode '201'


@Demo_post
Scenario: Verify the post functionality and print responsetime
Given EndPoint 'https://reqres.in/api/users'
And Content type 'application/json' 
And Query params with Key 'page' and value '2'
When Method 'post'
And Print response 
And Print responsetime
Then Statuscode '201'


@Demo_post
Scenario: Verify the post functionality and print response to file
Given EndPoint 'https://reqres.in/api/users'
And Content type 'application/json' 
And Query params with Key 'page' and value '2'
When Method 'post'
And Print response 
And Print response to file 'Test/file'
And Print responsetime
And Statuscode '201'


@Demo_post
Scenario: Verify the post functionality in xml
Given EndPoint 'https://www.w3schools.com/xml/tempconvert.asmx'
And Content type 'text/xml' 
And Request body for xml 'Test/Practice'
When Method 'post'
And Print responsetime
And Statuscode '200'


@Demo_post
Scenario: Verify the post functionality and print response in xml
Given EndPoint 'https://www.w3schools.com/xml/tempconvert.asmx'
And Content type 'text/xml' 
And Request body for xml 'Test/Practice'
When Method 'post'
And Print response to file for xml 'Test/GO'
And Statuscode '200'

@Demo_post
Scenario: Verify the post functionality and match xmlpath
Given EndPoint 'https://www.w3schools.com/xml/tempconvert.asmx'
And Content type 'text/xml' 
And Request body for xml 'Test/Practice'
When Method 'post'
And Statuscode '200'
And Match XMLPath '//CelsiusToFahrenheitResult' contains '246.2'
And Verify Value '246.2' present in field '//CelsiusToFahrenheitResult' for xml
And Retrieve data '//CelsiusToFahrenheitResult' from xml response and store in variable 'user1'
And Verify Value "$$user1" present in field "//CelsiusToFahrenheitResult" for xml
