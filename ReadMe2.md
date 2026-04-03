think of rest endpoints like a menu at a restaurant. the url is the what. http method is the action.
/api/builders -> the builders menu
/api/builders/1 -> just the builder#1
/api/plans -> the plans menu
 so these are the whats.

 http methosd- actions:  get is read. show me what you got.
 post is add/create- i want to order something from the menu
 put - update- change my order?
 delete - cancelmy order.

rest endpoints

 GET/api/builders = will get all the builders

 GET/api/builders/1 = will get the builder number 1

 POST/api/builder - adds a new builder

 PUT/api/builders/1 - update the builder 1 


 DELETE/api/builders/1 - deletes the builder 1 ; remember to specify otherwise all the builders will be deleted. lol.

 #curl cheatsheet
 READ
 curl http://localhost:8080/api/builders

 CREATE
 curl -X POST http://localhost:8080/api/builders \
  -H "Content-Type: application/json" \
  -d '{"name":"Labiba","vision":"Peaceful homes","location":"Ogden, UT"}'
UPDATE
  curl -X PUT http://localhost:8080/api/builders/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Nishat","vision":"Updated vision","location":"Salt Lake City, UT"}'


curl -X DELETE http://localhost:8080/api/builders/1


 API is application programming interface. it is a way for two programs to talk to each other. 

 REST is represeantaional state transfer. it is a set of rules that dictates how that conversation should happen. uses urls and http methods(get put post delete)

 so REST API is just a web interface that follows REST rules to talk to the program to send and recieve data.

 presentation cheat sheet
 lsof -ti:8080 | xargs kill -9
mvn spring-boot:run

http://localhost:8080/api-tester.html

Builder                    BuildingPlan
───────────────────        ──────────────────────
id          PK             id           PK
name                       builder_id   FK
vision          1 ──────► squareFeet
location          many     title
icon                       style
                           notes
                           icon

 Kris requested commands- curl http://localhost:8080/api/builders

               curl http://localhost:8080/api/plans    
               add        
                    curl -X PUT http://localhost:8080/api/builders/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Nishat","vision":"Updated vision","location":"Salt Lake City, UT","icon":"🏡"}'

  update
  curl -X POST http://localhost:8080/api/builders \
  -H "Content-Type: application/json" \
  -d '{"name":"Labiba","vision":"Peaceful homes","location":"Ogden, UT","icon":"🌸"}'
  delete 
  curl -X DELETE http://localhost:8080/api/builders/5

  curl http://localhost:8080/api/builders/1/plans - nested fresh 