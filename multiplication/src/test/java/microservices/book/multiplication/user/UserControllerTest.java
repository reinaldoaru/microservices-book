package microservices.book.multiplication.user;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureJsonTesters
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<List<Long>> jsonRequestIdList;

    @Autowired
    private JacksonTester<List<User>> jsonResponseUsers;

    @Test
    void getUsersByIdList() throws Exception {
        // given
        List<Long> idList = List.of(1L, 2L, 3L, 4L);
        List<User> expectedUsers = List.of(
                new User(1L, "User1"),
                new User(2L, "User2"),
                new User(3L, "User3"),
                new User(4L, "User4")
            );

        given(userRepository.findAllByIdIn(idList)).willReturn(expectedUsers);

        // when
        MockHttpServletResponse response = mvc
            .perform(get("/users/1,2,3,4").contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(jsonResponseUsers.write(expectedUsers).getJson());

        verify(userRepository).findAllByIdIn(idList);
    }

}
