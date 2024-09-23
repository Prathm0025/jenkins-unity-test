def PROJECT_NAME = "jenkins-unity-test"
def CUSTOM_WORKSPACE = "C:\\Jenkins\\Unity_Projects\\${PROJECT_NAME}"
def UNITY_VERSION = "2022.3.46f1"
def UNITY_INSTALLATION = "C:\\Program Files\\Unity\\Hub\\Editor\\${UNITY_VERSION}\\Editor"

pipeline{
    environment{
        PROJECT_PATH = "${CUSTOM_WORKSPACE}\\${PROJECT_NAME}"
        NEXUS_PASSWORD = credentials('NEXUS_PASSWORD')
    }

    agent{
        label{
            label ""
            customWorkspace "${CUSTOM_WORKSPACE}"
        }
    }

    stages{
        stage('Build Windows'){
            when{expression {BUILD_WINDOWS == 'true'}}
            steps{
                script{
                    withEnv(["UNITY_PATH=${UNITY_INSTALLATION}"]){
                        bat '''
                        "%UNITY_PATH%/Unity.exe" -quit -batchmode -projectPath %PROJECT_PATH% -executeMethod BuildScript.BuildWindows -logFile -
                        '''
                    }
                }
            }
        }

        stage('Deploy Windows'){
            when{expression {DEPLOY_WINDOWS == 'true'}}
            steps{
                script{
                    def buildDate = new Date().format("yyyyMMdd_HHmm")
                    env.ARTIFACT_NAME = "Windows_Build_${buildDate}.zip"

                    bat '''
                    curl -u buildqueue:%NEXUS_PASSWORD% --upload-file %PROJECT_PATH%/Builds/Windows.zip http://192.168.1.200:8081/repository/jenkins_unity_test/Windows_Builds/%ARTIFACT_NAME%
                    '''
                }
            }
        }
    }
}