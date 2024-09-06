package Rest1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.List;

/**
 * Hello world!
 */
public class App {

    public static String getToken() {
        String jsonString = "{\"username\" : \"admin\",\"password\" : \"password123\"}";
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri("https://restful-booker.herokuapp.com/auth");
        request.body(jsonString);
        Response response = request.post();
        String tokenValue = response.asString().substring(9, 26);
        return tokenValue;

    }

    public static Response createBooking(String token) {
        File jsonFileCreateBooking = new File("src/test/resources/Payloads/bookingCreate.json");
        RequestSpecification requestPost = RestAssured.given();
        requestPost.header("Accept", "application/json");
        requestPost.contentType(ContentType.JSON);
        requestPost.baseUri("https://restful-booker.herokuapp.com/booking");
        requestPost.auth().basic("token", token);
        requestPost.body(jsonFileCreateBooking);
        return requestPost.post();
    }

    public static List<String> getBookingIds(){
        JsonNode jsonNode = null;
        try {RequestSpecification requestGet = RestAssured.given();
        requestGet.baseUri("https://restful-booker.herokuapp.com/booking");
        Response response2 = requestGet.get();
        String resString = response2.asString();
        ObjectMapper objMapper = new ObjectMapper();
         jsonNode = objMapper.readTree(resString);}
        catch (JsonProcessingException e) {
            e.getMessage();
        }
        assert jsonNode != null;
        return jsonNode.findValuesAsText("bookingid");

    }

    public static Response changeInfo(String token) {
        RequestSpecification requestPatch = RestAssured.given();
        String id = "";
        List<String> ids = getBookingIds();
        if (!ids.isEmpty()) {
            id = ids.get(0);
        }

        requestPatch.header("Accept", "application/json");
        requestPatch.auth().basic("token", token);
        requestPatch.contentType(ContentType.JSON);
        requestPatch.baseUri("https://restful-booker.herokuapp.com/booking/" + id);
        requestPatch.body("{\"totalprice\" : \"200\"}");

        return requestPatch.patch();
    }

    public static Response updateInfo(String token) {
        File jsonFileUpdateBooking = new File("src/test/resources/Payloads/bookingUpdate.json");
        RequestSpecification requestPut = RestAssured.given();
        String id = "";
        List<String> ids = getBookingIds();
        if (!ids.isEmpty()) {
            id = ids.get(1);
        }

        requestPut.header("Accept", "application/json");
        requestPut.auth().basic("token", token);
        requestPut.contentType(ContentType.JSON);
        requestPut.baseUri("https://restful-booker.herokuapp.com/booking/" + id);
        requestPut.body(jsonFileUpdateBooking);

        return requestPut.patch();
    }

    public static Response deleteInfo(String token) {
        RequestSpecification requestDelete = RestAssured.given();
        String id = "";
        List<String> ids = getBookingIds();
        if (!ids.isEmpty()) {
            id = ids.get(1);
        }

        requestDelete.header("Accept", "application/json");
        requestDelete.auth().basic("token", token);
        requestDelete.contentType(ContentType.JSON);
        requestDelete.baseUri("https://restful-booker.herokuapp.com/booking/" + id);

        return requestDelete.delete();
    }

    public static Response getIdInfo (int id) {
        RequestSpecification request = RestAssured.given();
        return request.baseUri("https://restful-booker.herokuapp.com/booking/" + id).get();
    }

}
