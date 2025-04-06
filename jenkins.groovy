node {
    // Set up environment variables
    def deployEnv = 'production'
    def buildStatus = 'success'
    
    try {
        stage('Checkout') {
            echo 'Checking out code from the repository...'
            checkout scm
        }

        stage('Build') {
            echo 'Building the project...'
            try {
                echo 'Build successful!'
        }

        stage('Test') {
            echo 'Running tests...'
            try {
                // Run unit tests or integration tests
                sh 'mvn test'  // or 'gradle test'
                echo 'Tests passed!'
            } catch (Exception e) {
                buildStatus = 'failure'
                throw e
            }
        }

        stage('Deploy') {
            if (buildStatus == 'success') {
                echo 'Deploying to production environment...'
                // Deploy to a production environment (e.g., Kubernetes, Docker, AWS)
                sh 'kubectl apply -f deployment.yaml'  // Example for Kubernetes
                echo 'Deployment successful!'
            } else {
                error 'Build or test failed, skipping deployment!'
            }
        }
    } catch (Exception e) {
        currentBuild.result = 'FAILURE'
        throw e
    } finally {
        // Archive build artifacts, test results, etc.
        archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
        junit '**/target/test-*.xml'  // Collect and display test results
        // Set the build result
        if (buildStatus == 'failure') {
            currentBuild.result = 'FAILURE'
        } else {
            currentBuild.result = 'SUCCESS'
        }
    }
}
