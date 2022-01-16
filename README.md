# README

based on: 
* https://www.infoworld.com/article/3630107/how-to-secure-rest-with-spring-security.html
* Modern API Development with Spring and Spring Boot (Kindle)
* https://www.baeldung.com/spring-security-oauth-resource-server

## H2 console

http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:authdb
User Name: sa
Password: hemmelig


## Run PostgreSQL database in docker

Start PostgreSQL:
```
$ docker-compose -f src/main/resources/docker/docker-compose.yml up -d
```

Stop PostgreSQL:
```
$ docker-compose -f src/main/resources/docker/docker-compose.yml down 
```

### Log into PostgreSQL

```
> docker ps
CONTAINER ID   IMAGE          COMMAND                  CREATED          STATUS          PORTS                                                  NAMES
1126c7158b1b   postgres       "docker-entrypoint.s…"   41 seconds ago   Up 40 seconds   5432/tcp                                               authentication_database_1
ab4b4d702283   mysql:latest   "docker-entrypoint.s…"   8 days ago       Up 22 hours     33060/tcp, 0.0.0.0:3307->3306/tcp, :::3307->3306/tcp   mysql
> docker exec -it authentication_database_1 bash 
root@1126c7158b1b:/# 
root@1126c7158b1b:/# psql --username=sa --dbname=authdb
Password for user sa: hemmelig
psql (14.0 (Debian 14.0-1.pgdg110+1))
Type "help" for help.

authdb=# help
You are using psql, the command-line interface to PostgreSQL.
Type:  \copyright for distribution terms
       \h for help with SQL commands
       \? for help with psql commands
       \g or terminate with semicolon to execute query
       \q to quit
authdb=# 

```


## ETag

```
> curl -H "Accept: application/json" -u bjorn:hemmelig localhost:8081/users -v |jq                                                       
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8081 (#0)
* Server auth using Basic with user 'bjorn'
> GET /users HTTP/1.1
> Host: localhost:8081
> Authorization: Basic Ympvcm46aGVtbWVsaWc=
> User-Agent: curl/7.64.1
> Accept: application/json
> 
< HTTP/1.1 200 
< Vary: Origin
< Vary: Access-Control-Request-Method
< Vary: Access-Control-Request-Headers
< Set-Cookie: JSESSIONID=7AFA8CCDBF3FC2EEF33E8316E8FB52EE; Path=/; HttpOnly
< ETag: "06ec5b4a17cc18119cf02f72f15d5801a"
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
< Content-Type: application/json
< Content-Length: 96
< Date: Wed, 27 Oct 2021 16:06:29 GMT
< 
{ [96 bytes data]
100    96  100    96    0     0    979      0 --:--:-- --:--:-- --:--:--   979
* Connection #0 to host localhost left intact
* Closing connection 0
[
  {
    "emailAddress": "petter@smart.no",
    "firstName": "Petter",
    "lastName": "Smart",
    "mobilePhone": "123"
  }
]
```

Use ETag from above in next request, returns 304 Not Modified:

```
> curl -H "Accept: application/json" -H 'If-None-Match: "06ec5b4a17cc18119cf02f72f15d5801a"'  -u bjorn:hemmelig localhost:8081/users -v |jq

*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8081 (#0)
* Server auth using Basic with user 'bjorn'
> GET /users HTTP/1.1
> Host: localhost:8081
> Authorization: Basic Ympvcm46aGVtbWVsaWc=
> User-Agent: curl/7.64.1
> Accept: application/json
> If-None-Match: "06ec5b4a17cc18119cf02f72f15d5801a"
> 
< HTTP/1.1 304 
< Vary: Origin
< Vary: Access-Control-Request-Method
< Vary: Access-Control-Request-Headers
< Set-Cookie: JSESSIONID=B44603D1E341FE42698D20E805443B74; Path=/; HttpOnly
< ETag: "06ec5b4a17cc18119cf02f72f15d5801a"
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< X-Frame-Options: DENY
< Date: Wed, 27 Oct 2021 16:07:14 GMT
< 
* Connection #0 to host localhost left intact
* Closing connection 0
``

## Timestamp stored at UTC

local time:
```
authdb=# select created_at, created_at at time zone 'Europe/Oslo' from users;
          created_at           |          timezone          
-------------------------------+----------------------------
 2021-10-27 16:43:16.251888+00 | 2021-10-27 18:43:16.251888
(1 row)
````


## Generate private and public keys

```
> keytool -genkey -alias "jwt-sign-key" -keyalg RSA -keystore jwt-keystore.jks -keysize 4096 
Enter keystore password: hemmelig 
Re-enter new password: hemmelig
What is your first and last name?
  [Unknown]:  Bjørn Hjelle
What is the name of your organizational unit?
  [Unknown]:  privat
What is the name of your organization?
  [Unknown]:  privat
What is the name of your City or Locality?
  [Unknown]:  Oslo
What is the name of your State or Province?
  [Unknown]:  Oslo
What is the two-letter country code for this unit?
  [Unknown]:  NO
Is CN=Bjørn Hjelle, OU=privat, O=privat, L=Oslo, ST=Oslo, C=NO correct?
  [no]:  yes

Generating 4,096 bit RSA key pair and self-signed certificate (SHA384withRSA) with a validity of 90 days
	for: CN=Bjørn Hjelle, OU=privat, O=privat, L=Oslo, ST=Oslo, C=NO

```
Check the contents: 
```
> keytool -list -keystore jwt-keystore.jks
Enter keystore password:  
Keystore type: PKCS12
Keystore provider: SUN

Your keystore contains 1 entry

jwt-sign-key, 28 Oct 2021, PrivateKeyEntry, 
Certificate fingerprint (SHA-256): 32:58:18:4C:17:07:FC:78:08:DC:33:72:D8:E0:C3:97:3D:6D:31:3B:5E:4F:C0:FC:3E:A8:03:EE:8D:00:CE:55
```