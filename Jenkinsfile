#!groovy
pipeline {
    agent {
        docker {
            image 'adoptopenjdk/openjdk11:jdk-11.0.2.9'
            args '--network ci'
        }
    }
    environment {
        ORG_NAME = "apipessoas"
        APP_NAME = "apipessoas-java-pipeline"
        APP_CONTEXT_ROOT = "/"
        APP_LISTENING_PORT = "8081"
        TEST_CONTAINER_NAME = "ci-${APP_NAME}-${BUILD_NUMBER}"
        DOCKER_HUB = credentials("${ORG_NAME}-docker-hub")
    }
    stages { 
      stage('Compile') {
             steps {
                 echo "-=- compiling project -=-"
                 sh "./mvnw clean compile"
             }
      }
      stage('Package') {
             steps {
                 echo "-=- packaging project -=-"
                 sh "./mvnw package -DskipTests"
                 archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
             }
      }
      stage('Build Docker image') {
             steps {
                 echo "-=- build Docker image -=-"
                 sh "./mvnw docker:build"
             }
      }
    } 
} 