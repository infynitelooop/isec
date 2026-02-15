pipeline {
    agent any

    environment {
        DOCKERHUB_USER = "infynitelooop"
        BACKEND_IMAGE  = "infynitelooop/isec:latest"
        FRONTEND_IMAGE = "infynitelooop/runningroom:latest"

        EC2_USER = "ubuntu"
        EC2_HOST = "13.233.113.214"
        DEPLOY_DIR = "/home/ubuntu/deploy"
    }

    stages {

        stage("Checkout Backend") {
            steps {
                git branch: "master",
                    url: "https://github.com/infynitelooop/isec.git"
            }
        }

        stage("Checkout Frontend") {
            steps {
                dir("frontend") {
                    git branch: "master",
                        url: "https://github.com/infynitelooop/runningroom.git"
                }
            }
        }

        stage("Build Backend Docker Image") {
            steps {
                sh """
                docker build -t $BACKEND_IMAGE .
                """
            }
        }

        stage("Build Frontend Docker Image") {
            steps {
                sh """
                docker build -t $FRONTEND_IMAGE ./frontend
                """
            }
        }

        stage("DockerHub Login") {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: "dockerhub-creds",
                    usernameVariable: "USER",
                    passwordVariable: "PASS"
                )]) {
                    sh """
                    echo $PASS | docker login -u $USER --password-stdin
                    """
                }
            }
        }

        stage("Push Images to DockerHub") {
            steps {
                sh """
                docker push $BACKEND_IMAGE
                docker push $FRONTEND_IMAGE
                """
            }
        }

        stage("Deploy to EC2") {
            steps {
                sshagent(['ec2-key']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no $EC2_USER@$EC2_HOST '
                        cd $DEPLOY_DIR &&
                        docker compose pull &&
                        docker compose up -d
                    '
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ Deployment Successful!"
        }

        failure {
            echo "❌ Pipeline Failed!"
        }
    }
}
