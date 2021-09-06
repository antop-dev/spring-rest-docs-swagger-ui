package org.antop.controller;

import com.epages.restdocs.apispec.FieldDescriptors;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.antop.Headers;
import org.antop.RestDocsConfig;
import org.antop.model.User;
import org.antop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.antop.RestDocsCustomFields.TYPE;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfig.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriPort = 9000)
class ApiControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private UserRepository repository;
    @Autowired
    private ObjectMapper mapper;

    private final Schema userSchema = new Schema("User");

    private final FieldDescriptors userFields = new FieldDescriptors(
            fieldWithPath(User.Fields.ID).description("사용자 아이디"),
            fieldWithPath(User.Fields.NAME).description("사용자명"),
            fieldWithPath(User.Fields.ADDRESS).description("주소").optional()
    );

    @Test
    void echo() throws Exception {
        mockMvc.perform(post("/echo").content("Antop!!!"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("echo",
                        resourceDetails()
                                .summary("에코")
                                .description("받은 값을 그대로 돌려준다.")
                ))
        ;
    }

    @Test
    void registerUser() throws Exception {
        RegisterUser request = RegisterUser.builder()
                .name("안탑")
                .address("서울시 강서구 방화동")
                .build();

        mockMvc.perform(post("/user").content(mapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, matchesPattern("^.*/user/[0-9]+$")))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.address").value(request.getAddress()))
                .andDo(print())
                .andDo(document("register-user",
                        resourceDetails()
                                .summary("사용자 등록")
                                .description("사용자를 등록한다.")
                                .requestSchema(new Schema("RegisterUser"))
                                .responseSchema(userSchema),
                        requestFields(
                                fieldWithPath(RegisterUser.Fields.NAME).description("사용자명"),
                                fieldWithPath(RegisterUser.Fields.ADDRESS).description("주소").optional()
                        ),
                        responseFields(userFields.getFieldDescriptors())
                ))
        ;
    }

    @Test
    void getUser() throws Exception {
        long userId = 999;
        User user = User.builder()
                .id(userId)
                .name("유저 " + userId)
                .address("서울시 강서구 방화동")
                .build();

        when(repository.get(anyLong())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.address").isNotEmpty())
                .andDo(print())
                .andDo(document("get-user",
                        resourceDetails()
                                .summary("사용자 조회")
                                .description("사용자를 조회한다.")
                                .responseSchema(userSchema),
                        pathParameters(
                                parameterWithName("id")
                                        .description("사용자 아이디")
                                        .attributes(
                                                Attributes.key(TYPE).value(JsonFieldType.NUMBER)
                                        )
                        ),
                        responseFields(userFields.getFieldDescriptors())
                ))
        ;
    }

    @Test
    void check() throws Exception {
        long userId = 999;

        mockMvc.perform(post("/check", userId).header(Headers.X_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(is(emptyString())))
                .andDo(print())
                .andDo(document("check",
                        resourceDetails().summary("사용자 체크").description("사용자를 체크한다."),
                        requestHeaders(
                                headerWithName(Headers.X_USER_ID)
                                        .description("사용자 아이디")
                                        .attributes(Attributes.key(TYPE).value(JsonFieldType.NUMBER))
                        )
                ))
        ;
    }

}
