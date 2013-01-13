#!/bin/bash

mvn package -pl dspace-springui
mvn package -pl dspace-api

cp dspace-springui/target/dspace-springui-4.0-SNAPSHOT-classes.jar /dspace/webapps/springui/WEB-INF/lib
cp dspace-api/target/dspace-api-4.0-SNAPSHOT.jar /dspace/webapps/springui/WEB-INF/lib


