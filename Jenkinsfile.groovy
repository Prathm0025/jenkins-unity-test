def PROJECT_NAME = "jenkins-unity-test"
def CUSTOM_WORKSPACE = "C:\\Users\\morep\\OneDrive\\Documents\\Practice\\new\\jenkins-unity-test${PROJECT_NAME}"
def UNITY_VERSION = "2022.3.47f1"
def UNITY_INSTALLATION = "C:\\Program Files\\Unity\\Hub\\Editor\\${UNITY_VERSION}\\Editor"

pipeline{
    environment{
        PROJECT_PATH = "${CUSTOM_WORKSPACE}\\${PROJECT_NAME}"
    }

    agent{
        label{
            label ""
            customWorkspace "${CUSTOM_WORKSPACE}"
        }
    }

    stages{
        stage('Build WebGL'){
            when{expression {BUILD_WebGL == 'true'}}
            steps{
                script{
                    withEnv(["UNITY_PATH=${UNITY_INSTALLATION}"]){
                        bat '''
                        "%UNITY_PATH%/Unity.exe" -quit -batchmode -projectPath %PROJECT_PATH% -executeMethod BuildScript.BuildWebGL -logFile -
                        '''
                    }
                }
            }
        }
    }
}
