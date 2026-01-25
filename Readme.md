# Hotel Management System
This is a hotel management system built using Java (Spring framework) and a **MICROSERVICE ARCHITECTURE**.
This application is built for managing hotel operations such as booking rooms, reserving rooms, ordering room service etc.
It also has role-based authorization for different users.
We have an **ADMIN**, **MODERATOR** AND **GUEST** ROLE.

We are using JWT authentication for authorization and authentication.

In this doc, I'll be explaining the architecture of the application and how each module
or service connects to each other. I'll aslo be giving endpoints of the application for more details
about each service you can view the each module readme for more details in that corresponding folder.

==================================================================================================================================================
## 25/01/26

What we have currently in our application:

1. A working RoomBooking Service without authentication as yet -> create a room _> make a booking
This is running as an independent application because I've disabled the eureka configuratrion
in the application.properties.
The room booking service is documented and  can be tested with Swagger on your browser at:
http://localhost:8062/swagger-ui.html
I am using the h2 database for this service will later migrate to a postgres database.
2. Today we are working on the food service and on the authentication service.
3. The food order service currently has menu entity and order entity but we are not using it yet.

The PLAN FOR 25/01 and 26/01:
Work on the food service and authentication service.

27/01:
Update Eureka configuration on the service registry for service discovery.
Connect the room booking service to the service registry.
Connect the service with eureka discovery server.

====================================================================================================================================================




