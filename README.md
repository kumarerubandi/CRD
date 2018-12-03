# CRD Server
This subproject contains the CDS Service that can respond to CDS Hook requests. The application is written using [Spring Boot](https://spring.io/projects/spring-boot) and uses [HAPI FHIR](http://hapifhir.io/) for parsing and generating FHIR resources.



## Running the server
Assuming the current directory is still `server`:

`gradle bootRun`

This will start the server running on http://localhost:8090.

## Server endpoints
|Relative URL|Endpoint Description|
|----|----|
|`/`|Web page with basic RI information|
|`/data`|Web-based administrative interface|
|`/cds-services/`|CDS Hook Discovery endpoint|
|`/cds-services/order-review-crd`|CDS Hook endpoint for order-review|
|`/cds-services/medication-prescribe-crd`|CDS Hook endpoint for medication-prescribe|



