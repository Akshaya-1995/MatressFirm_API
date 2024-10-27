@Blamberry_Browsers
Feature: Test script 

@GuestUser
Scenario: Verify Guset user able to Add checkout by menu navigation 
Given My WebApp 'Brambleberry_App' is open
And I click 'AgreeButton'
And I should see element 'BrambleBerryLogo' present on page
And I mouse over 'ShopByProduct'
And I click 'AdditivesAndLye'
And I wait for visibility of element 'SodiumHydroxideProduct'
And I click 'SodiumHydroxideProduct'
And I wait for visibility of element 'ProductName'
And I click 'AddToCartButton'
And I click 'ViewCart'
And I wait for visibility of element 'SecureCheckout'
And I wait for '5' seconds
And I click 'SecureCheckout'
And I wait for visibility of element 'CheckoutButton'
And I click 'GuestCheckout'
And I generate random email id 'GuestEmailField'
And I generate random email id 'GuestConfirmMail'
And I enter 'sha' in field 'GuestFirstName'
And I enter 'Ria' in field 'GuestLastName'
And I enter '3120 willington road' in field 'GuestAddress'
And I enter 'Apt N10' in field 'GuestAddress1'
And I enter '19810' in field 'GuestShipCode'
And I select option 'United States' in dropdown 'GuestCountry' by 'Text'
And I select option 'Delaware' in dropdown 'GuestState' by 'Text'
And I enter 'Wilmington' in field 'GuestCity'
And I enter '12025550163' in field 'GuestPhoneNumber'
And I click 'SuggestedAddPopup'
And I click 'GiftNo'
And I click 'GuestSaveContinue'
And I wait for '60' seconds
