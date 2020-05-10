package Location;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.testng.annotations.Test;
import utils.TestExecutionManager;

import static io.restassured.RestAssured.given;

public class TestLocations {

    /**
     * This test method will verify that the location api is giving 200OK response with corrects params or not.
     * */

    @Test
    public void getLocationDetailsSuccess() {
        RestAssured.baseURI = TestExecutionManager.ZOMATO_ENDPOINT;
        Header header = new Header("user-key", TestExecutionManager.USER_KEY);
        given().header(header)
                .param("query", "delhi")
                .param("lat", Double.valueOf(28.6139))
                .param("lon", Double.valueOf(77.2090))
                .param("count", Integer.valueOf(5))
                .when().get("/api/v2.1/locations")
                .then().assertThat().statusCode(200).and().contentType(ContentType.JSON);
    }

    /**
     * This test method will verify that the location api is giving 200OK response without coordinates or not.
     * */

    @Test
    public void getLocationDetailsSuccess_WithoutCoordintes() {
        RestAssured.baseURI = TestExecutionManager.ZOMATO_ENDPOINT;
        Header header = new Header("user-key", TestExecutionManager.USER_KEY);
        given().header(header)
                .param("query", "delhi")
                .param("count", Integer.valueOf(5))
                .when().get("/api/v2.1/locations")
                .then().assertThat().statusCode(200).and().contentType(ContentType.JSON);
    }

    /**
     * This test method will verify that the location api is giving 200OK response with space in between cities or not.
     * */

    @Test
    public void getLocationDetailsSuccess_WithSpaceInCities() {
        RestAssured.baseURI = TestExecutionManager.ZOMATO_ENDPOINT;
        Header header = new Header("user-key", TestExecutionManager.USER_KEY);
        given().header(header)
                .param("query", "new delhi")
                .param("count", Integer.valueOf(5))
                .when().get("/api/v2.1/locations")
                .then().assertThat().statusCode(200).and().contentType(ContentType.JSON);
    }

    /**
     * This method will verify if the location_Details is giving error response with invalid user-key or not.
     * */

    @Test
    public void getLocationDetailsFails_WithInvalidKey() {
        RestAssured.baseURI = TestExecutionManager.ZOMATO_ENDPOINT;
        Header header = new Header("user-key", TestExecutionManager.DUMMY_USER_KEY);
        given().header(header)
                .param("query", "new delhi")
                .param("count", Integer.valueOf(5))
                .when().get("/api/v2.1/locations")
                .then().assertThat().statusCode(403).and().statusLine(TestExecutionManager.UNAUTHORIZED_STATUS);
    }
}
