pipeline {
    agent any

    environment {
        APP_NAME = "running-room"
        IMAGE_NAME = "runningroom-app"
        IMAGE_TAG = "latest"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/infynitelooop/isec.git'
            }
        }

        stage('Build JAR') {
            steps {
                dir('.') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME:$IMAGE_TAG backend'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                  docker compose down
                  docker compose up -d --build
                '''
            }
        }
    }
}
