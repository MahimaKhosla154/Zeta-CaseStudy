package Location;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.TestExecutionManager;

import static io.restassured.RestAssured.given;

public class TestLocationDetails {
    private static String entityType;
    private static int entityId;

    /**
     * This method will be called before the test to get the entityType and the entityId values.
     * */

    @BeforeClass
    public void getLocationIDAndLocationType() {
        RestAssured.baseURI = TestExecutionManager.ZOMATO_ENDPOINT;
        Header header = new Header("user-key", TestExecutionManager.USER_KEY);
        Response response = given().header(header)
                .param("query", "delhi")
                .param("lat", Double.valueOf(28.6139))
                .param("lon", Double.valueOf(77.2090))
                .param("count", Integer.valueOf(5))
                .when().get("/api/v2.1/locations");
        ResponseBody responseBody = response.getBody();
        entityType = responseBody.jsonPath().get("location_suggestions[0].entity_type");
        entityId = responseBody.jsonPath().get("location_suggestions[0].entity_id");
    }

    /**
     *  This test will verify that the location_details API is giving response.
     * */

    @Test
    public void getLocationDetails() {
        RestAssured.baseURI = TestExecutionManager.ZOMATO_ENDPOINT;
        Header header = new Header("user-key", TestExecutionManager.USER_KEY);
        given().header(header)
                .param("entity_id", entityId)
                .param("entity_type", entityType)
                .when().get("/api/v2.1/location_details").then().assertThat().statusCode(200).and().contentType(ContentType.JSON);
    }
}
