# WebApi
This project is a Spring boot prject dealing with calculaiton of customers' points. 

The project has 3 layers : Repository, 
                           Service, 
                           Controller. 
The Repository layer is responisble for CRUD operations and time filteration. 
The Service layer is responsible for handling business logic, such as calculating points in this case. 
The Controller layer is responsible for handling Http requests. 

DTOs were used to transfer data between layers
1. TransactionResponseDTO is used to transfer data from Repository layer to upper layer. 
2. TransactionResultDTO is used to transfer data from service layer to controiller layer.

