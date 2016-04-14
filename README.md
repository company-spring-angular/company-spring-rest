# company-spring-rest — an example of integration AngularJS client with SpringMVC REST service 

This project is an example of integration of a [SpringMVC](http://spring.io/) REST service application and a typical [AngularJS](http://angularjs.org/) web app.

The project contains a simple SpringMVC service application and is configured to store data in memory instead of database.

AngularJS client code can be found at [company-angular-client](https://github.com/company-spring-angular/company-angular-client).

## Getting Started

To get you started you can simply clone the company-spring-rest repository and install the dependencies:

### Prerequisites

You need git to clone the company-spring-rest repository. You can get git from
[http://git-scm.com/](http://git-scm.com/).

### Clone company-spring-rest

Clone the company-spring-rest repository using [git][git]:

```
git clone --depth=1 https://github.com/company-spring-angular/company-spring-rest.git
cd company-spring-rest
```

### Server application

#### Install Dependencies

The tools help us manage and test the application.

* We get the java dependecies via `mvn`, the [a software project management][maven].

We have preconfigured `mvn` to automatically download java libraries, build service code and run tests.
From company-rest directory we can simply do:

```
mvn clean install
```

#### Run the Application

We have preconfigured the service project with a simple development web server. The simplest way to start
this server is (command must be run from company-rest directory):

```
JAVA_OPTS=-Dserver.port=80 target/company/bin/run
```

Now the service is available at `http://localhost/`

*server.port parameter is passed to application server and it's used to bind our application to this port*

#### Directory Layout

```
company-parent/                     --> main Spring project directory
    company-model/                    --> model project module
        src/main/resources/schema       --> JSON Schemas for projects data model
            address.json                   --> JSON Schema for address data
            beneficial.json                --> JSON Schema for beneficial data
            company.json                   --> JSON Schema for company data
            phone.json                     --> JSON Schema for phone data
        target/                         --> directory containing generated data
            java-gen/                      --> directory containing generated model classes
    company-rest/                     --> rest project module
        src/assembly                    --> assembly maven plugin configuration directory
            assembly.xml                --> assembly maven plugin configuration file
        src/main/java/                  --> projects Java sources
            com.company/                   --> main package directory
                dao/                         --> package containing DAO classes
                    CompanyDAO.java            --> company DAO interface
                    CompanyDAOImpl.java        --> company DAO interface implementation
                rest/                        --> package containing REST service related classes
                    controller/                --> package containing Company controller related classes
                        CompanyController.java   --> company controller class
                        ExceptionHandlingController.java    --> execption handling class
                    exceptions/                --> package containing exception definitions
                        CompanyNotFoundEception.java        --> company not found exception
                        ValidationExeption.java             --> model validation exception
                    validation/                --> package containing validation classes
                        CompanyValidator.java    --> company validator class
        test/com/company/rest/controler/       --> controlers tests directory
            CompanyControllerTest.java           --> tests for company controller
            CompanyTestData.java                 --> helper class for generating company data
            TestContext.java                     --> mocking helper class
        pom.xml                                 --> maven configuration file
        Procfile                                --> entrypoint script for Heroku (linux)
        Procfile.windows                        --> entrypoint script for Heroku (windows)
```


#### Testing

##### Running Unit Tests

The company-rest app comes preconfigured with unit tests. These are written in
[JUnit][junit]. Tests are run during build.

The easiest way to run the unit tests is to execute command:

```
mvn test
```


#### Service resources

You can query REST service directly via a command line client like [cUrl](https://curl.haxx.se/).
You can find example query request below.

##### Create a company

###### Request

```
curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -d '{
    "name" : "Test Company",
    "email" : "contact@testcompany.com",
    "address" : {
        "city" : "City Name",
        "country" : "Country"
    },
    "owners" : [
        {
            "firstName" : "First",
            "lastName" : "Last"
        },
        {
            "firstName" : "Other",
            "lastName" : "Owner"
        }
    ]
}' "http://localhost/companies"
```

###### Response

A response is a created company object without empty fields.

```
{
  "cid": "1",
  "name": "Test Company",
  "address": {
    "city": "City Name",
    "country": "Country"
  },
  "email": "contact@testcompany.com",
  "owners": [
    {
      "firstName": "First",
      "lastName": "Last"
    },
    {
      "firstName": "Other",
      "lastName": "Owner"
    }
  ]
}
```


##### List companies

###### Request

```
curl -X GET -H "Content-Type: application/json" -H "Cache-Control: no-cache" "http://localhost/companies"
```

###### Response

The response contains a list of company objects (created in previous step).

```
[
  {
    "cid": "1",
    "name": "Test Company",
    "address": {
      "city": "City Name",
      "country": "Country"
    },
    "email": "contact@testcompany.com",
    "owners": [
      {
        "firstName": "First",
        "lastName": "Last"
      },
      {
        "firstName": "Other",
        "lastName": "Owner"
      }
    ]
  }
]
```

##### Get company details

###### Request

The request url contains a company id correponding to company which details are requested.

```
curl -X GET -H "Content-Type: application/json" -H "Cache-Control: no-cache" "http://localhost/companies/1"
```

###### Response

```
{
  "cid": "1",
  "name": "Test Company",
  "address": {
    "city": "City Name",
    "country": "Country"
  },
  "email": "contact@testcompany.com",
  "owners": [
    {
      "firstName": "First",
      "lastName": "Last"
    },
    {
      "firstName": "Other",
      "lastName": "Owner"
    }
  ]
}
```

##### Update company details

The request url contains a company id correponding to company which is being updated.

###### Request

```
curl -X PUT -H "Content-Type: application/json" -H "Cache-Control: no-cache" -d '{
        "name" : "Test Company Changed",
        "email" : "contact@testcompany.com",
        "address" : {
            "city" : "City Name",
            "country" : "Country"
        },
        "owners" : [
            {
                "firstName" : "First",
                "lastName" : "Last"
            }
        ]
}' "http://localhost/companies/1"
```

###### Response

Response contains updated company object (without empty fields).

```
{
  "cid": "1",
  "name": "Test Company Changed",
  "address": {
    "city": "City Name",
    "country": "Country"
  },
  "email": "contact@testcompany.com",
  "owners": [
    {
      "firstName": "First",
      "lastName": "Last"
    }
  ]
}
```


#### Running the App

The company-spring-rest project comes preconfigured with a local development webserver.  It starts a [tomcat][tomcat]
application server and deploys. You can start your own development app server from the `target` folder by running:

```
company/bin/run
```


## Considerations

### Authentication

Client app nor REST service doesn't implement any authentication mechanism. 
I'd recommend implementing such a mechanism, because of serving API for public audience. Every REST resource should be secured
just to be sure that only authorized clients can invoke it. One of the simplest ways to do it is to implement HTTP Basic 
authentication, but this method should be used only when secure connection is provided (HTTPS instead HTTP) - this authentication
method can have many security issues when is implemented incorrectly within client application. A more secure and powerfull 
(but still pretty simple to implement) authentication mechanism could be a session based one with distinction between API calls
and checking permissions to execute requested command.
More over every API call should be secured against [Cross-Site Request Forgery (CSRF)](https://www.owasp.org/index.php/Cross-Site_Request_Forgery_%28CSRF%29) attacks.


### Redundancy

To make server (either web and app server) redundant you can be duplicated it and put them right after a loadbalancer server (like [HAProxy](http://www.haproxy.org/)).
Another solution can be a preconfgured DNS server with a set multiple CNAME records - this will act as a Round Robin Algorithm for loadbalancing
requests (see http://docstore.mik.ua/orelly/networking_2ndEd/dns/ch10_07.htm).

For application servers you must provide a mechanism for synchronizing data between nodes and ensure that your production environment 
contains consistent data (changes in data made by first server are available on the second one, and prevent making changes to 
data by many servers at the same time - see https://en.wikipedia.org/wiki/ACID). An example for designing an architecture for this 
ensuring data consistency can be found here: https://auth0.com/blog/2015/11/07/introduction-to-microservices-part-4-dependencies/.

## Contact

For more information on SpringMVC please check out http://spring.io/

[git]: http://git-scm.com/
[maven]: https://maven.apache.org
[junit]: http://junit.org
[tomcat]: https://tomcat.apache.org/
