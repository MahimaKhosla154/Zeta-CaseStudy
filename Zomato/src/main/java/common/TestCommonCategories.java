package common;


import static org.hamcrest.Matchers.equalTo;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

import org.testng.asserts.SoftAssert;
import utils.TestExecutionManager;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public class TestCommonCategories {

    /**
     * This test validates the response code is 200 and the content type of the API is Json
     * */

    @Test
    public void testCategoriesAPIGivesBackResponse() {
        RestAssured.baseURI = TestExecutionManager.ZOMATO_ENDPOINT;
        Header header = new Header("user-key", TestExecutionManager.USER_KEY);
        given().header(header).
                when().
                get("/api/v2.1/categories").
                then().assertThat().statusCode(200).and().contentType(ContentType.JSON);
    }

    /**
     * This test validates the response body is in accordence with the supported categories.
     * */

    @Test
    public void testCategoriesResponseBody() {
        RestAssured.baseURI = TestExecutionManager.ZOMATO_ENDPOINT;
        final Header header = new Header("user-key", TestExecutionManager.USER_KEY);
        final JsonPath expectedJson = new JsonPath(new File("expectedCategories.json"));
        given().header(header).
                when().
                get("/api/v2.1/categories").
                then().assertThat().body("", equalTo(expectedJson.getMap("")));
    }

    /**
     * This test validates that invalid user key returns 403 error code and status Line.
     * */

    @Test
    public void testCategoriesAPIFailWithInvalidKey() {
        RestAssured.baseURI = TestExecutionManager.ZOMATO_ENDPOINT;
        final Header header = new Header("user-key", TestExecutionManager.DUMMY_USER_KEY);
        given().header(header).
                when().
                get("/api/v2.1/categories").
                then().assertThat().statusCode(403).and().statusLine(TestExecutionManager.UNAUTHORIZED_STATUS);
    }

    /**
     * This test validates that if a search is made for a categoryID, then each of the resultant restaurant be tagged
     * with the corresponding category.
     * */

    @Test
    public void testSearchUsingCategoryId() {
        final SoftAssert softAssert = new SoftAssert() ;
        RestAssured.baseURI = TestExecutionManager.ZOMATO_ENDPOINT;
        final Header header = new Header("user-key", TestExecutionManager.USER_KEY);
        Response response = given().header(header).param("category_id", "1").
                when().
                get("/api/v2.1/search").
                then().extract().response();
        ResponseBody responseBody = response.body();
        List<Object> listOfCategories = new ArrayList<Object>();
        listOfCategories.addAll(responseBody.jsonPath().getList("restaurants.restaurant.highlights"));
        for (Object a: listOfCategories) {
            softAssert.assertTrue(StringUtils.containsIgnoreCase(a.toString(), "Delivery"));
        }
        softAssert.assertAll();
    }

    /**
     * This test validates that the values in the json i.e. the categories contain both id and name and nothing else.
     * */

    @Test
    public void testCategoryIDJsonHasValidData() throws IllegalAccessException {
        final SoftAssert softAssert = new SoftAssert() ;
        RestAssured.baseURI = TestExecutionManager.ZOMATO_ENDPOINT;
        final Header header = new Header("user-key", TestExecutionManager.USER_KEY);
        Response response = given().header(header).param("category_id", "1").
                when().
                get("/api/v2.1/categories").
                then().extract().response();
        ResponseBody responseBody = response.body();
        List<LinkedHashMap> lists = responseBody.jsonPath().getJsonObject("categories");
        for (LinkedHashMap map : lists ) {
            softAssert.assertTrue(map.containsKey("categories"));
        }
        softAssert.assertAll();
    }
}
