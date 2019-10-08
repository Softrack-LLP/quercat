

Build:

```
mvn clean install -DskipTests && java -jar target/quercat-0.0.1-SNAPSHOT.jar
```

Test:
```
curl localhost:8080/commands/execute -H 'Content-Type: application/json' -d'{"commandAndArgs": ["usersByUserGroupByMinMaxRegistrationDates", "2019-01-01", "2019-10-10"]}'
# or

URL=localhost:8080/commands/execute
function execute-query {
    PARAMS="\"$1\""
    shift
    for var in "$@" ; do
        PARAMS="$PARAMS, \"$var\""
    done
    
    RESULT="$(curl $URL -H 'Content-Type: application/json' -d'{"applicationAuthToken": "d3e19858-9533-4879-a721-dc074c12b3d5", "requesterPhoneNumber": "77072734954", "commandAndArgs": ['"$PARAMS"']}' 2>/dev/null)"
    echo "$RESULT" | jq -r '.message'
    FILES="$(echo "$RESULT" | jq -r '.locallyCreatedFiles[]' 2>/dev/null)"
    if [ "q" != "q$FILES" ] ; then
        echo "Created files:"
        echo "$FILES"
    fi
    
}
execute-query usersByUserGroupByMinMaxRegistrationDates 2019-01-01 2019-10-10
execute-query 
```


Build and distribute a docker
```

```