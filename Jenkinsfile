pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Clone the repository
                git branch: 'main', url: 'https://github.com/Mehra079/News_Search.git'
            }
        }
        
        stage('Backend Build') {
            steps {
                dir('backend') {
                    // Build backend project
                    script {
                        try {
                            sh './mvnw clean install -DskipTests' // For Linux/MacOS
                        } catch (Exception e) {
                            bat 'mvnw.cmd clean install -DskipTests' // For Windows
                        }
                    }
                }
            }
        }
        
        stage('Backend Test') {
            steps {
                dir('backend') {
                    // Run tests
                    script {
                        try {
                            sh './mvnw test' // For Linux/MacOS
                        } catch (Exception e) {
                            bat 'mvnw.cmd test' // For Windows
                        }
                    }
                }
            }
        }
        
        stage('Frontend Build') {
            steps {
                dir('frontend') {
                    // Install Node dependencies
                    script {
                        try {
                            sh 'npm install' // For Linux/MacOS
                        } catch (Exception e) {
                            bat 'npm install' // For Windows
                        }
                    }
                    // Build the Angular project
                    script {
                        try {
                            sh 'ng build --prod' // For Linux/MacOS
                        } catch (Exception e) {
                            bat 'ng build --prod' // For Windows
                        }
                    }
                }
            }
        }
        
        stage('Archive Artifacts') {
            steps {
                // Archive built artifacts (JAR from backend and Angular dist folder)
                archiveArtifacts artifacts: 'backend/target/*.jar', allowEmptyArchive: true
                archiveArtifacts artifacts: 'frontend/dist/**/*', allowEmptyArchive: true
            }
        }

        stage('Deploy') {
            steps {
                // Here you can add deployment steps, e.g., Docker build & push, SCP to server, etc.
                echo 'Deployment step (to be configured based on your deployment target)'
            }
        }
    }

    post {
        always {
            // Clean up workspace after build
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
