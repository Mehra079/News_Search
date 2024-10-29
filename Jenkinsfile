pipeline {
    agent any

    environment {
        DOCKER_IMAGE_BACKEND = 'my-backend:latest'
        DOCKER_IMAGE_FRONTEND = 'my-frontend:latest'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git url: 'https://github.com/Mehra079/News_Search.git', branch: 'main'
            }
        }
        
        stage('Build Backend') {
            steps {
                script {
                    // Navigate to backend directory and build
                    sh 'cd backend && mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Build Frontend') {
            steps {
                script {
                    // Navigate to frontend directory and build
                    sh 'cd frontend && npm install && ng build --prod'
                }
            }
        }
        
        stage('Dockerize') {
            steps {
                script {
                    // Build Docker images for backend and frontend
                    sh 'docker build -t $DOCKER_IMAGE_BACKEND ./backend'
                    sh 'docker build -t $DOCKER_IMAGE_FRONTEND ./frontend'
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Start containers using Docker Compose
                    sh 'docker-compose up -d'
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
