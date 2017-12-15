Error
=====

    java.lang.NullPointerException: null
            at org.springframework.cloud.dataflow.server.service.impl.DefaultTaskJobService.getTaskJobExecution(DefaultTaskJobService.java:240) ~[spring-cloud-dataflow-server-core-1.2.3.RELEASE.jar!/:1.2.3.RELEASE]
            at org.springframework.cloud.dataflow.server.service.impl.DefaultTaskJobService.getTaskJobExecutionsForList(DefaultTaskJobService.java:233) ~[spring-cloud-dataflow-server-core-1.2.3.RELEASE.jar!/:1.2.3.RELEASE]
            at org.springframework.cloud.dataflow.server.service.impl.DefaultTaskJobService.listJobExecutions(DefaultTaskJobService.java:103) ~[spring-cloud-dataflow-server-core-1.2.3.RELEASE.jar!/:1.2.3.RELEASE]
            at org.springframework.cloud.dataflow.server.controller.JobExecutionController.list(JobExecutionController.java:91) ~[spring-cloud-dataflow-server-core-1.2.3.RELEASE.jar!/:1.2.3.RELEASE]
            at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
            at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(Unknown Source) ~[na:na]
            at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source) ~[na:na]
            at java.base/java.lang.reflect.Method.invoke(Unknown Source) ~[na:na]


How to reproduce error
===

Run SCDF:

    java -jar bin/spring-cloud-dataflow-server-local-1.2.3.RELEASE.jar --spring.datasource.url=jdbc:h2:./bin/output;AUTO_SERVER=TRUE --spring.datasource.username=u --spring.datasource.password=p --spring.datasource.driver-class-name=org.h2.Driver

Start Spring boot job (I'm starting it from IntelliJ via run button)

Go to
    http://localhost:9393/dashboard/index.html#/apps/apps

Check ```Tasks``` > ```executions```

Go to ```Jobs```

Issue: https://github.com/spring-cloud/spring-cloud-dataflow/issues/1822