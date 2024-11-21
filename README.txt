AFC Richmond Java Exercise
===========================

Completed by: Eric Spensieri
Project Structure: Maven
Server Infrastructure: Spring
IDE: VS Code
Local Port: 8080
JSON Parsing Library: Google Gson

Commands:
Build: mvn clean install
Test: mvn clean test
Run: mvn spring-boot:run

Endpoints:
Team Roster: /team
Player Card: /player/{id} (i.e. /player/JT9 for Jamie Tartt)

File Structure
===========================

Application Files
    1. RichmondApplication.Java (Application entry point, calls each loader file on initialization)
    2. RichmondController.Java (Endpoint handler)

Primitive Objects
    1. Season.java
    2. Team.java
    3. Match.java
    4. Player.java
    5. Score.java
    6. Goal.java

JSON Loader Files
    1. TeamLoader.java (Loads Team object from first FantasyPros endpoint and the associated roster)
    2. MatchLoader.java (Takes a Team object as argument and loads the matches and associated goals from the second FP endpoint)

JSON Serializer
    1. JsonSerializer.java (Responsible for converting all Java objects into JSON)

Unit Test Files
    1. TeamSerializationTest.java (Responsible for comparing JSON output for the Team Roster page)
    2. PlayerCardSerializationTest.java (Responsible for comparing JSON output for the Player Card page)
    3. Expected Output JSON files for /team and /player/JT9 (Jamie Tartt)

Exception Files
    1. SerializationFailureException.java (Custom excpetion thrown on loading failure)





