sequenceDiagram
    participant User as User
    participant Browser as Browser (HTML/JS)
    participant ClientJS as client.js
    participant Server as Express Server
    participant Routes as routes.js
    participant Controller as controller.js

    User ->> Browser: Clicks Button (e.g., "7", "+", "=")
    Browser ->> ClientJS: Calls JS Function (e.g., numberPressed(), operationPressed())
    ClientJS ->> Browser: Updates Display (for numbers and operations)
    ClientJS ->> Server: Sends REST API Request (e.g., POST /add)
    Server ->> Routes: Routes Request to Controller
    Routes ->> Controller: Calls Arithmetic Function (e.g., add(), subtract())
    Controller -->> Routes: Returns Result
    Routes -->> Server: Sends Response
    Server -->> ClientJS: Returns Result to Frontend
    ClientJS ->> Browser: Updates Calculator Display with Result