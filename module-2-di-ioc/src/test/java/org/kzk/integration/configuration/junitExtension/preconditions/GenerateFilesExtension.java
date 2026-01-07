package org.kzk.integration.configuration.junitExtension.preconditions;

import org.junit.jupiter.api.extension.*;
import org.kzk.dto.AuthRequestDto;
import org.kzk.dto.AuthResponseDto;
import org.kzk.dto.FileInfoDto;
import org.kzk.dto.UserDto;
import org.kzk.integration.configuration.junitExtension.BaseExtension;
import org.kzk.integration.configuration.junitExtension.annotation.ApiLogin;
import org.kzk.integration.configuration.junitExtension.annotation.GenerateFiles;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GenerateFilesExtension extends BaseExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {
    private final String authKey = "auth_%s";

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {


        GenerateFiles generate = context.getRequiredTestMethod().getAnnotation(GenerateFiles.class);
        if(Objects.isNull(generate)) {
            generate = context.getRequiredTestMethod().getAnnotation(ApiLogin.class).files();
        }

        if(Objects.nonNull(generate) && generate.count()!=0) {
            AuthResponseDto token = context.getStore(API_REGISTRATION).get(authKey.formatted(getTestId(context)), AuthResponseDto.class);

            if(Objects.isNull(token)) {
                throw new RuntimeException("Sorry but token is empty, use ApiLogin");
            }

            List<FileInfoDto> files = new ArrayList<>();
            for(int i = 0; i<generate.count(); i++) {
                files.add(generateFile.generateFile(token.getToken(), getWebTestClient(context)));
            }

            context.getStore(FILES).put(getTestId(context), files);
        }


    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();

        if (parameter.getType() != List.class) {
            return false;
        }

        // Проверяем что есть дженерик
        Type genericType = parameter.getParameterizedType();
        if (!(genericType instanceof ParameterizedType pt)) {
            return false;
        }

        // Достаём <T> из List<T>
        Type arg = pt.getActualTypeArguments()[0];

        return arg == FileInfoDto.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        List<FileInfoDto> files = extensionContext
                .getStore(FILES)
                .get(getTestId(extensionContext), List.class);

        return files;
    }
}
