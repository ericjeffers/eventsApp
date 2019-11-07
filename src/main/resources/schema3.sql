CREATE TABLE Users (Id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
Name VARCHAR(255) NOT NULL, Password VARCHAR(255) NOT NULL, Email VARCHAR(255) NOT NULL);
ALTER TABLE Users ADD CONSTRAINT PK_USERS_ID PRIMARY KEY (Id);

CREATE TABLE Events (Id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
Code VARCHAR(255) NOT NULL, Title VARCHAR(255) NOT NULL, Description VARCHAR(255) NOT NULL);
ALTER TABLE Events ADD CONSTRAINT PK_EVENTS_ID PRIMARY KEY (Id);

CREATE TABLE Registrations (Id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
Event VARCHAR(255) NOT NULL, Customer VARCHAR(255) NOT NULL, Timestamp DATE NOT NULL, Notes VARCHAR(255) NOT NULL);
ALTER TABLE Registrations ADD CONSTRAINT PK_REGISTRATIONS_ID PRIMARY KEY (Id);