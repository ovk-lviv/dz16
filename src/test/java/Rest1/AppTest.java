package Rest1;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppTest {

    public static String token = "";

    @BeforeEach
    public static void general() {
        token = App.getToken();
    }

    @Test
    public void requestPOST() {
        App.createBooking(token).then()
                .statusCode(200);
    }

    @Test
    public void requestGET() throws JsonProcessingException {
        Assertions.assertTrue(!App.getBookingIds().isEmpty());
    }

    @Test
    public void requestPATCH() {
        App.changeInfo(token).then()
                .statusCode(200)
                .body("totalprice", Matchers.equalTo("200"));
    }

    @Test
    public void requestPUT() {
        App.updateInfo(token).then()
                .statusCode(200)
                .body("firstname", Matchers.equalTo("Allen"));
    }

    @Test
    public void requestDELETE() {
        App.deleteInfo(token).then();
        App.getIdInfo(Integer.getInteger(App.getBookingIds().get(0)))
                .then().statusCode(404);
    }
}
