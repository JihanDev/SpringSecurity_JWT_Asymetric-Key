# SpringSecurity_JWT_Asymmetric-Key-Oauth2-OIDC
# Running this application

To run this application from your IDE run the main method in Application.java or from a terminal run the command ./mvnw spring-boot:run.

Once the application is up and running the first step is to obtain a JWT (Access and Refresh Token).
To do this you will need to make a POST request to the "/token" endpoint with basic auth credentials, username  "Jihane" and a password "1234". 



![token](https://user-images.githubusercontent.com/119127428/213521245-56fab7b2-02c8-49e3-9ea7-2266f23d1283.PNG)



Using your Access-token you can  make a POST request to the "/register" to add a new User.



![add user](https://user-images.githubusercontent.com/119127428/213521308-59eac1a8-e17c-4eb8-b70c-5f587f552eda.PNG)



Using your Access-token you can  make a GET request to the "/listUsers" to view the User's list.



![list users](https://user-images.githubusercontent.com/119127428/213521459-b703701a-70a3-47bd-a063-01585191544b.PNG)
