# InformationRetrievalProject
Information Retrieval CSIS400 Siena College

# Requirements
* **MUST** use Java 11 or higher

# Build/Running
This project uses the Gradle build tool for portability across systems.
To cache a local copy of the build tools and compile/execute use:

  sh:
  `./gradlew run`
  
  dos:
  `gradlew.bat run`
  
Source code can be found and moved into another build system if required (e.g. BlueJ, Maven, make etc...).
If you would like another build system configured in the repository, contact mj04copp@siena.edu.
Source code is found in the directory: `src/main/java/InformationRetrievalProject`

# Use
During execution, the program will load the full dataset and index it entirely. This may take considerable time (30-60s).
Afterwards, a prompt will show. Queries support AND, OR and NOT operations. keywords will automatically OR together, unless stated otherwise.
The system will return the number of documents and percentage of corpus that match the query.
Typing `q` will exit the program.
