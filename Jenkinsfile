pipeline {
  agent any
   
    environment { 
        POM = readMavenPom(file:'pom.xml')
        ARTIFACTID=POM.getArtifactId()
        ARTIFACT_VERSION = POM.getVersion()
        BUILD_FINAL_NAME="${ARTIFACTID}-$ARTIFACT_VERSION"
        DOCKER_IMAGE_VERSION = "${env.BUILD_NUMBER}"
        DOCKER_SERVICE_NAME = "clinisys_${ARTIFACTID}" 
    }

     
    tools { 
        maven 'maven' 
        jdk 'jdk-8u131' 
    }
    
  stages { 

         stage('Build') 
        { 

            steps{
               sh "mvn -Pprod -DskipTests=true clean package"
               
               sh "java -jar target/${BUILD_FINAL_NAME}.jar --thin.root=m2 --thin.dryrun"
               }
       
        }

          stage('Docker') 
        { 
            steps{
           sh "mvn dockerfile:build -DGIT_COMMIT=${env.GIT_COMMIT} -Ddockerfile.repository=${ARTIFACTID}"
               }
       
        }

         stage('Swarm') 
        {
       steps{
        sh "docker tag ${ARTIFACTID}:latest ${DOCKER_REGISTRY_URL}/${ARTIFACTID}:latest"
    
        sh "docker tag ${ARTIFACTID}:latest ${DOCKER_REGISTRY_URL}/${ARTIFACTID}:${DOCKER_IMAGE_VERSION}" 
  
        sh "docker push ${DOCKER_REGISTRY_URL}/${ARTIFACTID}:latest"
    
        sh "docker push ${DOCKER_REGISTRY_URL}/${ARTIFACTID}:${DOCKER_IMAGE_VERSION}"   
   
		withKubeConfig([credentialsId: 'kubeconfig-integ']) {
            sh "kubectl set image deployment/clinisys-${ARTIFACTID} ${ARTIFACTID}=${DOCKER_REGISTRY_URL}/${ARTIFACTID}:${DOCKER_IMAGE_VERSION} --record"
          
        }  } 
  
   }
    }
  }
     

     
