# funfit

SQL Queries Used:

CREATE DATABASE FunfitDB;
USE FunfitDB;

CREATE TABLE Batches (
    batchId INT AUTO_INCREMENT PRIMARY KEY,
    batchName VARCHAR(100),
    timing VARCHAR(50)
);

CREATE TABLE Participants (
    participantId INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    batchId INT,
    FOREIGN KEY (batchId) REFERENCES Batches(batchId)
);
