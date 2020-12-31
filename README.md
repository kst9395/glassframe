# What's this repo
This is an experimental web project using vert.x, Google Guice, and Apache Cayenne to do some simple crud task.  

## Why
- I like to have full control over what technology is used in my application. 
- Some framework is almost perfect for my use case but I don't particularly like one of the component chosen in the framework.

## What's inside
### Core
- Vert.x 
    - All component is wired up with vert.x
### Web
- Vert.x Web
    - I used vert.x Web to handle web request. 
### Inversion of Control
- Guice
    - Google Guice helped me to manage my dependencies between components. (Can be easily replaced with spring if you want to.)
### Database
- Apache Cayenne
    - ORM tools that make life easier. 

### Database Migration
- Liquibase
    - Great tools to version your database schema. (Can use flyway as an alternative) 


### Utilities
- Classgraph
    - Used to scan class with annotation and hence setup web route dynamically.

- RX Java 2
    - reactive style programming in vert.x (this is optional if you prefer callback style.)

