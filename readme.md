# TagTune

태그 기반 음악 추천 웹 서비스

## Links
- [Website](https://tagtune.uoise.xyz/)
- [Demo on Youtube](https://youtu.be/WZZqXKslz4g)

## Requirements

### Lastfm API

- [Lastfm API KEY](https://www.last.fm/api/account/create)

### MariaDB Docker

```shell
docker run \
  --name mariadb_1 \
  -d \
  --restart unless-stopped \
  -e MARIADB_ROOT_PASSWORD={YOUR ROOT PASSWORD} \
  -e TZ=Asia/Seoul \
  -p 3306:3306 \
  -v /docker_projects/mariadb_1/conf.d:/etc/mysql/conf.d \
  -v /docker_projects/mariadb_1/mysql:/var/lib/mysql \
  -v /docker_projects/mariadb_1/run/mysqld:/run/mysqld/ \
  -v /docker_projects/mariadb_1/password_reset.sql:/password_reset.sql:z \
  mariadb:latest \
  --init-file=/password_reset.sql

```

## Optional

### Nginx Proxy Manager

### Jenkins Pipeline

Modify application-secret.yml for your secrets

```groovy
pipeline {
    agent any
    
    tools {
        jdk 'openjdk-17-jdk'
    }
    
    environment {
        timestamp = "${System.currentTimeMillis() / 1000L}"
    }
    
    stages {
        stage('Prepare') {
            steps {
                script {
                    // Get the ID of the tagtune:latest image
                    def oldImageId = sh(script: "docker images tagtune:latest -q", returnStdout: true).trim()
                    env.oldImageId = oldImageId
                }
            
                git branch: 'main',
                    url: 'https://github.com/tagtune/tagtune'
            }
            
            post {
                success { 
                    sh 'echo "Successfully Cloned Repository"'
                }
                failure {
                    sh 'echo "Fail Cloned Repository"'
                }
            }  
        }
    
        stage('Build Gradle') {
            
            steps {
                
                dir('.') {
                    sh '''
                    cat <<EOF > src/main/resources/application-secret.yml
custom:
  lastfm:
    clientId: ?
    shared-secret: ?
spring:
  security:
    oauth2:
      client:
        registration:
          kakao:
            clientId: ?
          google:
            client-id: ?
            client-secret: ?
          naver:
            client-Id: ?
            client-secret: ?
EOF
                    '''
                }
            
                dir('.') {
                    sh """
                    chmod +x gradlew
                    """
                }
                
                dir('.') {
                    sh """
                    ./gradlew clean build
                    """
                }
            }
            
            post {
                success { 
                    sh 'echo "Successfully Build Gradle Test"'
                }
                 failure {
                    sh 'echo "Fail Build Gradle Test"'
                }
            }    
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t tagtune:${timestamp} ."
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    // Check if the container is already running
                    def isRunning = sh(script: "docker ps -q -f name=tagtune_1", returnStdout: true).trim()

                    if (isRunning) {
                        sh "docker stop tagtune_1"
                        sh "docker rm tagtune_1"
                    }
                    
                    // Run the new container
                    try {
                        sh """
                        docker run \
                          --name=tagtune_1 \
                          -p 8080:8080 \
                          -v /docker_projects/tagtune_1/volumes/gen:/gen \
                          --restart unless-stopped \
                          -e TZ=Asia/Seoul \
                          -d \
                          tagtune:${timestamp}
                        """
                    } catch (Exception e) {
                        // If the container failed to run, remove it and the image
                        isRunning = sh(script: "docker ps -q -f name=tagtune_1", returnStdout: true).trim()
                        
                        if (isRunning) {
                            sh "docker rm -f tagtune_1"
                        }
                        
                        def imageExists = sh(script: "docker images -q tagtune:${timestamp}", returnStdout: true).trim()
                        
                        if (imageExists) {
                            sh "docker rmi tagtune:${timestamp}"
                        }
                        
                        error("Failed to run the Docker container.")
                    }

                    // If there's an existing 'latest' image, remove it
                    def latestExists = sh(script: "docker images -q tagtune:latest", returnStdout: true).trim()
                    
                    if (latestExists) {
                        sh "docker rmi tagtune:latest"
                        
                        if(!oldImageId.isEmpty()) {
                        	sh "docker rmi ${oldImageId}"
                        }
                    }

                    // Tag the new image as 'latest'
                    sh "docker tag tagtune:${env.timestamp} tagtune:latest"
                }
            }
        }
    }
}
```

No user data Requires

- [Kakao OAuth Login](https://developers.kakao.com/)
    - API key
- [Naver OAuth Login](https://developers.naver.com/main)
    - API key, secret
- [Google OAuth Login](https://console.cloud.google.com/home/dashboard)
    - API key, secret
