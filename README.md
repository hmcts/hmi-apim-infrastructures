# hmi-apim-infrastructures

## Overview
This project contains terraform code which is used to deploy the HMI endpoints to SDS APIM. Using this code, we define the authentication method, valid source and destination systems and also tagging the endpoints.


### Building the application

The project uses [Gradle](https://gradle.org) as a build tool. It already contains
`./gradlew` wrapper script, so there's no need to install gradle

This project contains two parts:

- Terraform code
- Automation tests
  You can run only the automation tests in this project. Terraform code can be tested by deploying the code to the lower environments like demo.

Before running the automation tests, you need to setup some environment variables in your project which can be retrieved from the key vault. Those variables are:

| Variable      | Description                                                          | Required? |
|:--------------|:---------------------------------------------------------------------|-----------|
| CLIENT_ID     | The client ID                                                        | Yes       |
| CLIENT_SECRET | The client secret                                                    | Yes       |
| SCOPE         | The scope to request                                                 | Yes       |

To run the automation test, execute the following command:

```bash
  ./gradlew clean build 
```
## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details