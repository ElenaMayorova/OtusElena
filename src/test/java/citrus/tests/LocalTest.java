package citrus.tests;

import apiHelpers.pojo.Data;
import apiHelpers.pojo.RegisterResponse;
import apiHelpers.pojo.Support;
import apiHelpers.pojo.User;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.http.server.HttpServer;
import com.consol.citrus.message.MessageType;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;


public class LocalTest extends TestNGCitrusTestRunner {
    @Autowired
    private HttpClient restClientLocal;
    private HttpServer restServerLocal;

    private TestContext context;


    @Test(description = "Получение информации о пользователе")
    @CitrusTest
    public void getTestUser() {
        this.context = citrus.createTestContext();

        http(httpActionBuilder -> httpActionBuilder
                .client(restClientLocal)
                .send()
                .get("users/2")
                .fork(true)
        );
        http(httpActionBuilder -> httpActionBuilder
                .server("restServerLocal")
                .receive()
                .get()
        );
        http(httpActionBuilder -> httpActionBuilder
                .server("restServerLocal")
                .send()
                .response()
                .messageType(MessageType.JSON)
                .payload(getUserById(), "objectMapper")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClientLocal)
                .receive()
                .response()
                .messageType(MessageType.JSON)
                .payload("{\n" +
                        "   \"data\":{\n" +
                        "      \"id\":3,\n" +
                        "      \"email\":\"test@reqres.in\",\n" +
                        "      \"first_name\":\"Elena\",\n" +
                        "      \"last_name\":\"Mayorova\",\n" +
                        "      \"avatar\":\"https://reqres.in/img/faces/3-image.jpg\"\n" +
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
                .client(restClientLocal)
                .send()
                .get("users/23")
                .fork(true)
        );
        http(httpActionBuilder -> httpActionBuilder
                .server("restServerLocal")
                .receive()
                .get()
        );
        http(httpActionBuilder -> httpActionBuilder
                .server("restServerLocal")
                .send()
                .response(org.springframework.http.HttpStatus.valueOf(HttpStatus.SC_NOT_FOUND))
                .messageType(MessageType.JSON)
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClientLocal)
                .receive()
                .response(org.springframework.http.HttpStatus.valueOf(HttpStatus.SC_NOT_FOUND))
                .messageType(MessageType.JSON)

        );

    }

    @Test(description = "Удаляем пользователя")
    @CitrusTest
    public void deleteUser() {
        this.context = citrus.createTestContext();

        http(httpActionBuilder -> httpActionBuilder
                .client(restClientLocal)
                .send()
                .delete("users/2")
                .fork(true)
        );
        http(httpActionBuilder -> httpActionBuilder
                .server("restServerLocal")
                .receive()
                .delete()
        );
        http(httpActionBuilder -> httpActionBuilder
                .server("restServerLocal")
                .send()
                .response(org.springframework.http.HttpStatus.valueOf(HttpStatus.SC_NO_CONTENT))
                .messageType(MessageType.JSON)
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClientLocal)
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
                .client(restClientLocal)
                .send()
                .post("register")
                .payload("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}")
                .fork(true)
        );

        http(httpActionBuilder -> httpActionBuilder
                .server("restServerLocal")
                .receive()
                .post()

        );

        http(httpActionBuilder -> httpActionBuilder
                .server("restServerLocal")
                .send()
                .response(org.springframework.http.HttpStatus.valueOf(HttpStatus.SC_OK))
                .messageType(MessageType.JSON)
                .payload(getToken(), "objectMapper")
        );


        http(httpActionBuilder -> httpActionBuilder
                .client(restClientLocal)
                .receive()
                .response()
                .messageType(MessageType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .payload("{\n" +
                        "    \"id\": 1,\n" +
                        "    \"token\": \"Fgtrred458iGGTHJ\"\n" +
                        "}")
        );

    }


    public User getUserById() {
        User user = new User();

        Data data = new Data();
        data.setId(3);
        data.setEmail("test@reqres.in");
        data.setFirstName("Elena");
        data.setLastName("Mayorova");
        data.setAvatar("https://reqres.in/img/faces/3-image.jpg");
        user.setData(data);

        Support support = new Support();
        support.setUrl("https://reqres.in/#support-heading");
        support.setText("To keep ReqRes free, contributions towards server costs are appreciated!");

        user.setSupport(support);
        return user;
    }


    public RegisterResponse getToken() {
        RegisterResponse registerResponse = new RegisterResponse();

        registerResponse.setId(1);
        registerResponse.setToken("Fgtrred458iGGTHJ");

        return registerResponse;
    }

}

