package oth.ics.wtp.readinbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import oth.ics.wtp.readinbackend.dtos.CreateAppUserDto;
import oth.ics.wtp.readinbackend.services.AppUserService;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLoginFlow() throws Exception {
        // 1. Create User
        String username = "testuser";
        String password = "testpassword";
        CreateAppUserDto createDto = new CreateAppUserDto(username, password);

        // Directly create user via service to ensure it exists
        // (Or call the create endpoint)
        try {
            appUserService.delete(username);
        } catch (Exception e) {
            // ignore
        }
        appUserService.create(createDto);

        // 2. Try to Login with Basic Auth
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        mockMvc.perform(post("/appUsers/login")
                .header("Authorization", basicAuth))
                .andExpect(status().isOk());
    }
}
