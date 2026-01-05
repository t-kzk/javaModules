package org.kzk.integration.configuration.junitExtension.preconditions;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.extension.*;
import org.kzk.dto.AuthRequestDto;
import org.kzk.dto.UserDto;
import org.kzk.integration.configuration.junitExtension.BaseExtension;
import org.kzk.integration.configuration.junitExtension.annotation.ApiLogin;
import org.kzk.integration.configuration.junitExtension.annotation.ApiRegistration;


public class ApiRegistrationExtension extends BaseExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    private final String requestKey = "request_%s";
    private final String responseKey = "response_%s";

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApiRegistration registration =
                context.getRequiredTestMethod().getAnnotation(ApiRegistration.class);

        ApiLogin login =
                context.getRequiredTestMethod().getAnnotation(ApiLogin.class);

        if (registration == null && login != null) {
            registration = login.registration();
        }

        if (registration != null) {
            Pair<AuthRequestDto, UserDto> authRequestDtoUserDtoPair = generateUser.generateUser(registration, getWebTestClient(context));
            context.getStore(API_REGISTRATION).put(responseKey.formatted(getTestId(context)), authRequestDtoUserDtoPair.getRight());
            context.getStore(API_REGISTRATION).put(requestKey.formatted(getTestId(context)), authRequestDtoUserDtoPair.getLeft());
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        // удалить юзера
    }

    @Override
    public boolean supportsParameter(ParameterContext pc, ExtensionContext ec) {
        Class<?> type = pc.getParameter().getType();
        return type == UserDto.class || type == AuthRequestDto.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {

        String id = getTestId(extensionContext);

        if (parameterContext.getParameter().getType() == UserDto.class) {
            return extensionContext.getStore(API_REGISTRATION)
                    .get(responseKey.formatted(id), UserDto.class);
        }

        if (parameterContext.getParameter().getType() == AuthRequestDto.class) {
            return extensionContext.getStore(API_REGISTRATION)
                    .get(requestKey.formatted(id), AuthRequestDto.class);
        }

        throw new ParameterResolutionException("Unsupported parameter");
    }


}
