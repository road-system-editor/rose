# ![](https://avatars.githubusercontent.com/u/102614006?s=20&v=4) ROSE

ROSE is a graphical **ro**ad **s**ystem **e**ditor used to set up highway networks that can be 
exported to traffic simulation software.

## File structure
* [`config/`](./config) contains configuration data for development tooling
* [`gradle/wrapper`](./gradle/wrapper) contains the
  [Gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html)
* [`src/main/`](./src/main) contains the source code of the application, including code and data 
  assets
* [`src/test/`](./src/test) contains unit tests and GUI tests as well as the required test data

## Development notes
* tested with [OpenJDK 17](https://openjdk.java.net/projects/jdk/17/)
* use `gradle run` to compile and run the program for experimentation
* use `gradle check` to run unit tests, gui tests and generate code coverage and code style reports
  * the coverage report can then be found in
    [`build/reports/jacoco/test/html/index.html`](./build/reports/jacoco/test/html/index.html)
  * the test report can then be found in
    [`build/reports/tests/test/index.html`](./build/reports/tests/test/index.html)
  * any code style problems will be printed to the console
* you can manually generate a SonarQube code quality report by running the following command 
  with your SonarQube credentials:
  ```shell
  gradle check sonarqube -Dsonar.projectKey=<SONAR_PROJECT_NAME> -Dsonar.host.url=<SONAR_URL> -Dsonar.login=<SONAR_TOKEN>`
  ```

## Building a release
* make sure to install the
  [required software for jpackage](https://www.baeldung.com/java14-jpackage#packaging-prerequisite)
* run `gradle jpackage` to generate an executable
  * you can find the executable and other required files in
    [`build/jpackage/ROSE`](./build/jpackage/ROSE)
  * the installer that is generated in [`build/jpackage`](./build/jpackage) has not been tested 
    and is not recommended to use

## Further resources
ROSE was created as a group project in the "Praxis der Softwareentwicklung" course at the 
Karlsruhe Institute of Technology.
You can find the documents we submitted to our supervisors in
[a separate repository](https://github.com/road-system-editor/pse-documents).
Please note that these documents are mostly written in German.

## License
(c) 2022 Cristian Gorun, Jannes Wagner, Max Schweikart, Philipp Seidel and Yannik Sproll

ROSE is licensed under the GNU General Public License v3.
The license text can be found in [`./LICENSE`](./LICENSE).

If you have used ROSE or its source code for something cool,
[we would love to hear about it](mailto:hello@maxschweik.art,contact@philipp-seidel.de?subject=I%20used%20ROSE%20for%20something%20cool!)!

