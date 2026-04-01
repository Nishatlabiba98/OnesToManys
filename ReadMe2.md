think of rest endpoints like a menu at a restaurant. the url is the what. http method is the action.
/api/builders -> the builders menu
/api/builders/1 -> just the builder#1
/api/plans -> the plans menu
 so these are the whats.

 http methos- actions:  get is read. show me what you got.
 post is add/create- i want to order something from the menu
 put - update- change my order?
 delete - cancelmy order.

 GET/api/builders = will get all the builders

 GET/api/builders/1 = will get the builder number 1

 POST/api/builder - adds a new builder

 PUT/api/builders/1 - update the builder 1 

 DELETE/api/builders/1 - deletes the builder 1 ; remember to specify otherwise all the builders will be deleted. lol.
 curl http://localhost:8080/api/builders
 so this is a get request - READ
 curl http://localhost:8080/api/builders/1 curl stands for slient url
 curl http://localhost:8080/api/builders/plans

 CREATE
 curl -X POST http://localhost:8080/api/builders/1\



 API is application programming interface. it is a way for two programs to talk to each other. 

 REST is represeantaional state transfer. it is a set of rules that dictates how that conversation should happen. uses urls and http methods(get put post delete)

 so REST API is just a web interface that follows REST rules to talk to the program to send and recieve data.