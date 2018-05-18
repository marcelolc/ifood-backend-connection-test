
## Requirements

* Java 8
* Docker

## Running instructions

Start docker containers (eclipse-mosquitto, postgres):
```
docker-compose up
````

Start the server project
```
gradlew :server:bootRun
```    

## Postman Config. File (To test endpoints: import into Postman)
IFOOD.postman_collection.json

## API:

#### 1) Given a list of Restaurants, whether they are online or offline.

Syntax: 
```
GET /api/v1/restaurant?restaurantId={restaurantId1}&restaurantId={restaurantId2}
```
e.g
```
GET /api/v1/restaurant?restaurantId=1&restaurantId=2&restaurantId=3&restaurantId=4
```

#### 2) Given a specific Restaurant, its unavailability history.
Syntax
```
GET /api/v1/restaurant/{restaurantId}/unavailability/{sinceUtcDate}
```
e.g
```
GET /api/v1/restaurant/1/unavailability/2018-05-15T17:15:30.00Z
```

#### 3) Reports for iFood commercial team so they can measure how our Restaurants are ranked, according to the amount of time they spent offline (see the diagram above, the red section on the "Restaurant Status" timeline).

Syntax:
```
GET /api/v1/ranking/{sinceDate}?restaurantId={restaurantId1}&restaurantId={restaurantId2}
```

Ranking with specified restaurants: 
```
GET /api/v1/ranking/2018-05-14?restaurantId=1&restaurantId=2
```

Full Ranking (with all restaurants): 
```
GET /api/v1/ranking/{sinceDate}?full=true
```
e.g
```
GET /api/v1/ranking/2018-05-14?full=true
```

#### 4) Scheduled Unavailabilities
##### 4.1) Schedule new Unavailability 
Syntax
```
POST /api/v1/restaurant/{restaurantId}/scheduled-unavailability
```
BODY:
```
{
    "reason": "Overloaded",
    "start": "2018-05-15T17:15:30.00Z",
    "end": "2018-05-15T20:15:30.00Z"
}

```
e.g
```
POST /api/v1/restaurant/1/scheduled-unavailability
```
BODY:
```
{
    "reason": "Overloaded",
    "start": "2018-05-15T17:15:30.00Z",
    "end": "2018-05-15T20:15:30.00Z"
}
```
RESPONSE:
```
{
    "id": "41ebd82c-c5de-4834-8392-f2d054137bba",
    "restaurantId": 1,
    "reason": "Overloaded",
    "start": "2018-05-15T17:15:30Z",
    "end": "2018-05-15T20:15:30Z"
}
```
##### 4.2) Delete Scheduled Unavailability
Syntax
```
DEL /api/v1/restaurant/{restaurantId}/scheduled-unavailability/{scheduledUnavailabilityId}
```
e.g
```
DEL /api/v1/restaurant/1/scheduled-unavailability/ea2a9180-7947-4b87-9243-2e427a167e6c
```

##### 4.3) List Scheduled Unavailability
Syntax
```
GET /api/v1/restaurant/{restaurantId}/scheduled-unavailability/{sinceUtcDate}
```
e.g
```
GET /api/v1/restaurant/1/scheduled-unavailability/2018-05-14T00:00:00.00Z
```