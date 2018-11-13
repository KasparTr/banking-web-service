-----------------------------
# Design Decisions & Issues #

This is a simple banking REST Webservice built as a demo application.

See API Description here: https://app.swaggerhub.com/apis/Rubiks/simple-banking-app/1.0 

Paths that implement the functional requirements:
* POST /register                    			← register user
* POST /oauth/token?grant_type=password    	← login (get token)
* GET /services/accounts/{id}/balance        	← get account balance
* GET /services/accounts/{id}/statement       ← get account statement
* POST /services/transactions/withdraw        ← withdraw money
* POST /services/transactions/deposit        	← deposit money

___oAuth authentication’s Token Grant method is Basic Auth!___


## Issues ##
The following are the known issues that either introduce bugs or deviate from the functional requirements. 
Here are also features that have not been fully implemented yet to fully follow the architecture described in chapter 2 of this document. 

### ARCHITECTURAL ###
* Currently the transaction controller manage the transaction request, create a TransactionIntent  and pass it to TransactionService for direct processing. Instead they should just publish the intent to a channel from where a subscriber (TransactionService) would pick it up for processing. 

**Note**: Full PUB/SUB pattern is not yet implemented so the TransactionService.process() is called in the controller itself. All interfaces and controllers are ready for intergration with a PUB/SUB channel and separate (microsevice architecture) deployment. 

**ToDo: Implement PUB/SUB.**
_For Example: TransactionController.deposit() method should not process the TransactionIntent, but should only publish it to a pre-existing channel. The TransactionService would pick that intent up (as a subscriber) from the channel and process it._
* Implement Onion Architeture - started but not fully implemented.

### FUNCTIONALITY ###
* TransactionalEntity can only have a single account attached. 
* Implement clearer error messaging on `[transactions/withdraw]` and `[transactions/deposit]` endpoints.
* In order to support multiple accounts per entity, account id must be implemented on `[transactions/withdraw]` and `[transactions/deposit]` paths.
* UserService interface needs redesign. Specially the BankingUser and AdminUser methods.

### SECURITY ###
* Due to oAuth Authentication Service architecture, user registration [/register] doesn't return the access_token directly. It is needed to be requested from the Authentication Server separately [/oauth/token].
* oAuth authentication’s Token Grant method is Basic Auth. Should be changed to for example JWT in future.
* Username and password are in the URL parameters when requesting Authentication Server. 
	* **ToDo: Move username/password to POST body.**

* Transactions require access_token but validation between client identity and beneficiary/benefactor is not made. 
	* **ToDo: Compare access_token owner (TransactionalEntity)  identity and its rights for the requests operation.**

* Resource service configuration only requires authentication from services on path [/services].
	* Everything else is exposed including the Springs default [/users] path.

### TESTING ###
* Native or Third Party APIs are not unit tested.
* Running tests launches Spring server making tests slow. Running the server is not always needed.
* Some tests that mock Autowired Services  are not registering stub invokations due to JPA and Mockito integration configuration issues.
	* **ToDo: Configure correctly**
