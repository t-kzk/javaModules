package org.kzk.integration.configuration.junitExtension.preconditions;

import org.junit.jupiter.api.extension.*;
import org.kzk.dto.AuthRequestDto;
import org.kzk.dto.AuthResponseDto;
import org.kzk.dto.UserDto;
import org.kzk.integration.configuration.junitExtension.BaseExtension;
import org.kzk.integration.configuration.junitExtension.annotation.ApiLogin;
import org.kzk.integration.configuration.junitExtension.annotation.ApiRegistration;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class ApiLoginExtension extends BaseExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    private final String requestKey = "request_%s";
    private final String responseKey = "response_%s";
    private final String authKey = "auth_%s";

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        UserDto userInfo;

        ApiLogin annotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (annotation != null) {
            ApiRegistration registration = annotation.registration();
            WebTestClient webTestClient = getWebTestClient(context);
/*
            Pair<AuthRequestDto, UserDto> authRequestDtoUserDtoPair = generateUser.generateUser(registration, webTestClient);
            context.getStore(API_REGISTRATION).put(responseKey.formatted(getTestId(context)), authRequestDtoUserDtoPair.getRight());
            AuthRequestDto authRequest = authRequestDtoUserDtoPair.getLeft();
            context.getStore(API_REGISTRATION).put(requestKey.formatted(getTestId(context)), authRequest);
*/
            AuthRequestDto authRequest = context.getStore(API_REGISTRATION)
                    .get(requestKey.formatted(getTestId(context)), AuthRequestDto.class);

            WebTestClient.ResponseSpec result = webTestClient.post()
                    .uri("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(authRequest).exchange();

            AuthResponseDto token = result
                    .expectBody(AuthResponseDto.class)
                    .returnResult()
                    .getResponseBody();

            context.getStore(API_REGISTRATION).put(authKey.formatted(getTestId(context)), token);

        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        // по идее удалять пользователя
    }
    @Override
    public boolean supportsParameter(ParameterContext pc, ExtensionContext ec) {
        Class<?> type = pc.getParameter().getType();
        return
        //type == UserDto.class || type == AuthRequestDto.class ||
                type == AuthResponseDto.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {

        String id = getTestId(extensionContext);

      /*  if (parameterContext.getParameter().getType() == UserDto.class) {
            return extensionContext.getStore(API_REGISTRATION)
                    .get(responseKey.formatted(id), UserDto.class);
        }

        if (parameterContext.getParameter().getType() == AuthRequestDto.class) {
            return extensionContext.getStore(API_REGISTRATION)
                    .get(requestKey.formatted(id), AuthRequestDto.class);
        }*/

        if (parameterContext.getParameter().getType() == AuthResponseDto.class) {
            return extensionContext.getStore(API_REGISTRATION)
                    .get(authKey.formatted(id), AuthResponseDto.class);
        }

        throw new ParameterResolutionException("Unsupported parameter");
    }
}
