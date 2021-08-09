package citrus.tests;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.http.server.HttpServer;
import com.consol.citrus.message.MessageType;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;


public class FirstTest extends TestNGCitrusTestRunner {
    @Autowired
    private HttpClient restClient;
    private HttpServer restServer;

    private TestContext context;


    @Test(description = "Получение информации о пользователе")
    @CitrusTest
    public void getTestUser() {
        this.context = citrus.createTestContext();

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .get("users/2")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response()
                .messageType(MessageType.JSON)
                .payload("{\n" +
                        "   \"data\":{\n" +
                        "      \"id\":2,\n" +
                        "      \"email\":\"janet.weaver@reqres.in\",\n" +
                        "      \"first_name\":\"Janet\",\n" +
                        "      \"last_name\":\"Weaver\",\n" +
                        "      \"avatar\":\"https://reqres.in/img/faces/2-image.jpg\"\n" +
                        "   },\n" +
                        "   \"support\":{\n" +
                        "      \"url\":\"https://reqres.in/#support-heading\",\n" +
                        "      \"text\":\"To keep ReqRes free, contributions towards server costs are appreciated!\"\n" +
                        "   }\n" +
                        "}")
        );

    }


    @Test(description = "Пользователь не найден")
    @CitrusTest
    public void getTestUserNotFound() {
        this.context = citrus.createTestContext();

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .get("users/23")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response()
                .messageType(MessageType.JSON)
                .statusCode(HttpStatus.SC_NOT_FOUND)
        );

    }

    @Test(description = "Получение данных ресурса")
    @CitrusTest
    public void getTestResource() {
        this.context = citrus.createTestContext();

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .get("unknown/2")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response()
                .messageType(MessageType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .payload("{\n" +
                        "    \"data\": {\n" +
                        "        \"id\": 2,\n" +
                        "        \"name\": \"fuchsia rose\",\n" +
                        "        \"year\": 2001,\n" +
                        "        \"color\": \"#C74375\",\n" +
                        "        \"pantone_value\": \"17-2031\"\n" +
                        "    },\n" +
                        "    \"support\": {\n" +
                        "        \"url\": \"https://reqres.in/#support-heading\",\n" +
                        "        \"text\": \"To keep ReqRes free, contributions towards server costs are appreciated!\"\n" +
                        "    }\n" +
                        "}")
        );

    }

    @Test(description = "Удаляем пользователя")
    @CitrusTest
    public void deleteUser() {
        this.context = citrus.createTestContext();

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .delete("users/2")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response()
                .messageType(MessageType.JSON)
                .statusCode(HttpStatus.SC_NO_CONTENT)
        );

    }

    @Test(description = "Получение токина ")
    @CitrusTest
    public void postUser() {
        this.context = citrus.createTestContext();

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .post("register")
                .payload("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response()
                .messageType(MessageType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .payload("{\n" +
                        "    \"id\": 4,\n" +
                        "    \"token\": \"QpwL5tke4Pnpja7X4\"\n" +
                        "}")
        );

    }
}

