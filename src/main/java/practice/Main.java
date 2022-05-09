package practice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Main {

    public static void main(String[] args) {

        try {
            // From https://www.baeldung.com/jackson-object-mapper-tutorial
            ObjectMapper objectMapper = new ObjectMapper();

            System.out.println("------------------------------------------------------------------------");
            System.out.println("Java object to JSON object:");
            Car firstCar = new Car("yellow", "renault");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(firstCar));
            System.out.println("------------------------------------------------------------------------");

            System.out.println("JSON object to Java Object:");
            String jsonOne = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
            Car secondCar = objectMapper.readValue(jsonOne, Car.class);
            System.out.println("Second car: " + secondCar);
            System.out.println("------------------------------------------------------------------------");

            System.out.println("JSON file to Java Object:");
            String filePath = "C:/Users/ryanl/Documents/Development/test.json";
            Car thirdCar = objectMapper.readValue(new File(filePath), Car.class);
            System.out.println("Third car: " + thirdCar);
            System.out.println("------------------------------------------------------------------------");

            // Notice that this is parsing to an arbitrary structure not based on Car()
            System.out.println("JSON to Jackson JsonNode:");
            String jsonTwo = "{ \"color\" : \"Black\", \"type\" : \"FIAT\", \"seats\" : \"2\"}";
            JsonNode jsonNode = objectMapper.readTree(jsonTwo);
            System.out.println("JsonNode: " + jsonNode);
            System.out.println("Number of seats: " + jsonNode.get("seats").asText());
            System.out.println("------------------------------------------------------------------------");

            System.out.println("Parse JSON array to Java Object List<Car>:");
            String jsonCarArray = "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
            List<Car> carList = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>(){});
            System.out.println("Car list:" + carList);
            System.out.println("------------------------------------------------------------------------");

            // From https://www.baeldung.com/java-9-http-client
            System.out.println("Making an HTTP GET request to REST API and parsing JSON response:");
            // Create request
            HttpRequest requestOne = HttpRequest.newBuilder()
                    .uri((new URI("https://postman-echo.com/get")))
                    .timeout(Duration.of(10, SECONDS))
                    .GET()
                    .build();
            // Create client
            HttpClient httpClientOne = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();
            // Get response
            HttpResponse<String> responseOne = httpClientOne.send(requestOne, HttpResponse.BodyHandlers.ofString());
            System.out.println("Raw Response Body: " + responseOne.body());
            // Parsing response to JsonNode
            JsonNode responseNodeOne = objectMapper.readTree(responseOne.body());
            String host = responseNodeOne.get("headers").get("host").toString();
            System.out.println("JsonNode host: " + host);
            System.out.println("------------------------------------------------------------------------");

            System.out.println("Making an HTTP POST request to REST API and parsing JSON response:");
            // Create request
            HttpRequest requestTwo = HttpRequest.newBuilder()
                    .uri((new URI("https://postman-echo.com/post")))
                    .timeout(Duration.of(10, SECONDS))
                    .POST(HttpRequest.BodyPublishers.ofString("Sample Body"))
                    .build();
            // Create client
            HttpClient httpClientTwo = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();
            // Get response
            HttpResponse<String> responseTwo = httpClientTwo.send(requestTwo, HttpResponse.BodyHandlers.ofString());
            System.out.println("Raw Response Body: " + responseTwo.body());
            JsonNode responseNodeTwo = objectMapper.readTree(responseTwo.body());
            String data = responseNodeTwo.get("data").toString();
            System.out.println("JsonNode data: " + data);
            System.out.println("------------------------------------------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
