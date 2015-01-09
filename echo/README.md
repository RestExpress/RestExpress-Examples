Echo Example
============

This project is a simple RestExpress server that support echo functionality with optional delays. The delays can be used to test consumer libraries with slow responses.  Note that the delay DOES actually block the thread for the specified number of milliseconds.  A delay of 0 (zero) milliseconds will not delay.

The service suite supports the following functionality.

Echo
====

URL: /echo/{delay_ms}
Methods: GET, PUT, POST, DELETE

GET, DELETE: send back the same string that is sent in on the 'echo' query-string parameter.

PUT, POST: send back the body contents.

There is no serialization involved in the response. The body is not parsed.

Success
=======

This URL responds with a JSON payload indicating the action that was performed (e.g. 'read', 'create', 'update', 'delete'), the delay that was passed in and the message that was on the 'echo' query-string parameter (if present). This response involves serialization. Every HTTP method will always return a status code 200 (OK).

URL: /success/{delay_ms}
Methods: GET, PUT, POST, DELETE

Sample Response:
```
curl -i localhost:9000/success/0?echo=the+buck+stops+here

HTTP/1.1 200 OK
Content-Type: application/json; charset=UTF-8
Content-Length: 61

{"action":"read","delayMs":0,"message":"the buck stops here"}
```

Status
======

This URL responds with the same payload as the /success URL, with the added ability to vary the HTTP status code returned. Note that response codes vary how RestExpress responds to clients. For example, returning status code 204 (No Content) will cause RestExpress to not include a body in the response. A 404 (Not Found) will return an error response, etc.

URL: /status/{delay_ms}/{http_response_code}
Methods: GET, PUT, POST, DELETE

Sample Response:
```
curl -i localhost:9000/status/0/201?echo=the+buck+stops+here
HTTP/1.1 201 Created
Content-Type: application/json; charset=UTF-8
Content-Length: 61

{"action":"read","delayMs":0,"message":"the buck stops here"}
```

Sample Error Response:
```
curl -i localhost:9000/status/0/404?echo=the+buck+stops+here
HTTP/1.1 404 Not Found
Content-Type: application/json; charset=UTF-8
Content-Length: 67

{"errorId":"4d067a25-6004-4ba5-8e94-73fd6ab53bb4","httpStatus":404}
```
