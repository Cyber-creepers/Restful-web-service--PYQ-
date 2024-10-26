

Restful Web Service - Previous Year Question Bank (PYQ)
Project Setup Guide
Follow the instructions below to set up and run the project locally.

1. Clone the Repository
   Open IntelliJ IDEA (Community/Ultimate) and clone this repository.

2. Configure Database & Email Properties
   Navigate to src/main/resources/application.properties and configure the following:

Database Configuration
Replace put_your_schema, your_username, and your_password with your actual MySQL database details:

properties
Copy code
# MySQL Database configuration
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.datasource.url=jdbc:mysql://localhost:3306/put_your_schema

spring.datasource.username=your_username

spring.datasource.password=your_password

Optional: Email Properties

If you plan to use email functionalities, update these properties (replace your_email and your_password):

properties
Copy code
# Email properties
spring.mail.host=smtp.gmail.com

spring.mail.port=587

spring.mail.username=your_email

spring.mail.password=your_password

3. Run the Application
   Open QuestionBankBackendApplication.java.
   Click Run.
4. 
   After a successful start, open the following URL to view the Swagger documentation:

plaintext
Copy code
http://localhost:8000/swagger-ui/index.html
4. Additional Configuration (Optional)
   If needed, configure the server port in application.properties:

properties
Copy code
# Server port configuration
server.port=8000
You're All Set! 🎉