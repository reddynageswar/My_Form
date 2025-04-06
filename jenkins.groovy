node {
    stage('Build') {
        echo 'Building the project...'
        // Run Maven build (you can replace this with your own build tool)
        sh 'mvn clean install'
        echo 'Build successful!'
    }
}
