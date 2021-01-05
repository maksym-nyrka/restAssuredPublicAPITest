package tests;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pojos.UserPojo;

import static io.restassured.RestAssured.*;

public class RestTest {

    @Test
    public void getUsers() {
        when().get("https://gorest.co.in/public-api/users")
                .then().log().body().statusCode(200);
    }

    @Test(dataProvider = "usersProvider")
    public void createUser(UserPojo user) throws JsonProcessingException {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(user);

        given()
                .header("Content-Type", ContentType.JSON)
                .header("Authorization",
                        "Bearer a2d62ecfe2b61dd4efa5e017f2ab587c0ff4a2a4c26c6a70d7696a7c56c637dc")
                .body(jsonString)
        .when()
                .post("https://gorest.co.in/public-api/users")

                .then().log().body().statusCode(200);

    }

    @Test(dataProvider = "usersProvider")
    public void updateUser(UserPojo user) throws JsonProcessingException {
        user.setStatus("Disabled");

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(user);

        given()
                .header("Content-Type", ContentType.JSON)
                .header("Authorization",
                        "Bearer a2d62ecfe2b61dd4efa5e017f2ab587c0ff4a2a4c26c6a70d7696a7c56c637dc")
                .body(jsonString)
                .when()
                .patch("https://gorest.co.in/public-api/users/")

                .then().log().body().statusCode(404);

    }

    @Test
    public void deleteUser() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        given()
                .header("Authorization",
                        "Bearer a2d62ecfe2b61dd4efa5e017f2ab587c0ff4a2a4c26c6a70d7696a7c56c637dc")
                .when()
                .delete("https://gorest.co.in/public-api/users/1")

                .then().statusCode(200);

    }

    @DataProvider(name = "usersProvider")
    public Object[][] getUserPojos() {
        return new Object[][]{
                {new UserPojo("Maksym Nyrka", "maksym.nyrka@example.com", "Male", "Active")},
                {new UserPojo("Other Guy", "other.guy@example.com", "Male", "Active")},
                {new UserPojo("And Another", "and.another@example.com", "Female", "Active")}
        };
    }
}
