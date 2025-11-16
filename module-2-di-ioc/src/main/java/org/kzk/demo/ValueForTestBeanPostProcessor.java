package org.kzk.demo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

@Component
public class ValueForTestBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        if (fields.length != 0) {

            Arrays.stream(fields).forEach(
                    f -> {
                        ValueForTest annotation = f.getAnnotation(ValueForTest.class);
                        annotationHandler(annotation, f, bean);
                    }
            );
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    private void annotationHandler(ValueForTest annotation, Field field, Object bean) {
        if (Objects.nonNull(annotation)) {
            field.setAccessible(true);
            ReflectionUtils.setField(field, bean, "FROM BEAN ValueForTestBeanPostProcessor");
            field.setAccessible(false);
        }
    }
}
