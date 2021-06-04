call mvn clean compile package
call mvn install:install-file -Dfile=target/sync-lib-0.0.2.jar -DpomFile=pom.xml