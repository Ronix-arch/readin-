package oth.ics.wtp.readinbackend;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import oth.ics.wtp.readinbackend.dtos.AppUserDto;
import oth.ics.wtp.readinbackend.entities.AppUser;
import oth.ics.wtp.readinbackend.repositories.AppUserRepository;
import oth.ics.wtp.readinbackend.services.AppUserService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class ReadinControllerTestBase {
    protected static final String USER_USERNAME = "user0";
    protected static final String USER_PASSWORD = "1234";
@Autowired protected AppUserRepository appUserRepository;
private final Map<String,HttpSession> sessions = new HashMap<>();

@BeforeEach public void beforeEach(){
    createAppUser(USER_USERNAME,USER_PASSWORD);
    sessions.clear();
}
private void createAppUser(String appUsername, String password) {
    // create app user and store in repository
    String passwordHash = WeakCrypto.hashPassword(password);
    AppUser appUser = new AppUser(appUsername, passwordHash);
    appUserRepository.save(appUser);

}
    protected MockHttpServletRequest mockRequest(String userName, String password) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        // restore session of same user, if any
        if (!sessions.containsKey(userName)) {
            request.addHeader(HttpHeaders.AUTHORIZATION, basic(userName, password));
            sessions.put(userName, request.getSession());
        } else {
            request.setSession(sessions.get(userName));
        }
        return request;
    }
    protected HttpServletRequest user0() {
        return mockRequest( USER_USERNAME,USER_PASSWORD);
    }

    protected String basic(String appUserName, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((appUserName + ":" + password).getBytes(StandardCharsets.UTF_8));
    }



}


