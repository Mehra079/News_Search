pipeline {
    agent any
  
    stages {
        stage('Checkout') {
            steps {
                retry(3){
                    script {
                        bat 'git config --global http.version HTTP/1.1'
                        bat 'git config --global http.postBuffer 524288000' // 500 MB
                        bat 'git config --global http.maxRequestBuffer 524288000' // 500 MB
                    }
                    // Clone the repository
                    checkout([$class: 'GitSCM', branches: [[name: '*/main']], userRemoteConfigs: [[url: 'https://github.com/Mehra079/News_Search.git']], extensions: [[$class: 'CloneOption', depth: 1]]])
                }
            }
        }
        
        stage('Backend Build') {
            steps {
                dir('backend') {
                    // Build backend project
                    script {
                        bat 'mvnw.cmd clean install -DskipTests'
                    }
                }
            }
        }
        
        stage('Backend Test') {
            steps {
                dir('backend') {
                    // Run tests
                    script {
                        bat 'mvnw.cmd test' 
                    }
                }
            }
        }
        
        stage('Frontend Build') {
            steps {
                dir('frontend') {
                    // Install Node dependencies
                    script {
                        bat 'npm install'
                    }
                    // Build the Angular project
                    script {
                        bat 'ng build --prod'
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deployment step (to be configured based on your deployment target)'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo 'Build completed successfully!'
        }
        failure {
            echo 'Build failed. Check logs for details.'
        }
    }
}
