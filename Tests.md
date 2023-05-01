# TEST CASES

## TEST 1: *SELLER SIGN-UP*
### Steps:
- launch the application
- select "signup"
- select "seller signup"
- enter your preferred email
- enter your preferred username
- enter your preferred password
### Expected result: application is launched, buttons to list/search customers, add/delete stores, and manage account should be present
#### Status: SUCCESS

## TEST 2: *SELLER LOGIN*
### Steps:
- launch application
- select "login"
- select "seller login"
- enter your username
- enter your password
### Expected result: application is launched, buttons to list/search customers, add/delete stores, and manage account should be present
#### Status: SUCCESS

## TEST 3: *SELLER STORE ADD*
### Steps:
- launch application
- select "login"
- select "seller login"
- enter your username
- enter your password
- select "Store Options"
- select "Add Store"
- choose a unique name for your new store (cannot be the name of a preexisting store or user)
### Expected result: Message dialog indicating store has been successfully added, stores.txt should have new store appended to the line containing the seller name
#### Status: SUCCESS

## TEST 4: *CUSTOMER SIGN-UP*
### Steps:
- launch the application
- select "signup"
- select "customer signup"
- enter your preferred email
- enter your preferred username
- enter your preferred password
### Expected result: application is launched, buttons to search sellers, list stores, and manage account should be present
#### Status: SUCCESS

## TEST 5: *CUSTOMER LOGIN*
### Steps:
- launch application
- select "login"
- select "customer login"
- enter your username
- enter your password
### Expected result: application is launched, buttons to search sellers, list stores, and manage account should be present
#### Status: SUCCESS

## TEST 5: *CUSTOMER ACCOUNT EDIT*
### Steps:
- launch application
- select "login"
- select "customer login"
- enter your username
- enter your password
- select "Account Options"
- select "edit password"
### Expected result: Message dialog indicating password is updated should pop up, minimize window and customers.txt user password should be updated
### recall format is: username, password, email
#### Status: SUCCESS



