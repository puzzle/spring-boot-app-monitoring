### Spring Boot Application Monitoring

This is minimal spring-boot example project (forked spring-boot-crud-booster application) with 
 1) glowroot integration (https://glowroot.org/)
 2) actuator metrics enabled for prometheus


### Create OpenShift Project
run 

    oc new-app -f  https://raw.githubusercontent.com/puzzle/spring-boot-app-monitoring/master/.openshiftio/application.yaml -p SOURCE_REPOSITORY_URL=https://github.com/puzzle/spring-boot-app-monitoring
    

    
