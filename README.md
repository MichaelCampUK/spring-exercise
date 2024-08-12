## Build and Test
<pre>
./gradlew clean build test
</pre>

## Run
<pre>
./gradlew clean build bootRun
</pre>

## Test when Running
<pre>
 curl -H 'Content-Type: application/json' -H 'x-api-key: given_api_key' -X POST -d '{"companyNumber": "06500244"}' http://localhost:8080/companysearch?onlyActiveCompanies=true
</pre>