package com.di.framework.context;

import com.di.framework.annotation.Autowired;
import com.di.framework.annotation.Component;
import com.di.framework.config.Bean;
import com.di.framework.config.Beans;
import org.reflections.Reflections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {
    private Map<String, Object> beans = new HashMap<>();
    private String basePackage;

    public ApplicationContext(String xmlConfigPath) {
        try {
            loadXmlConfig(xmlConfigPath);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to load XML configuration", e);
        }
    }

    public ApplicationContext(String basePackage, boolean useAnnotations) {
        this.basePackage = basePackage;
        if (useAnnotations) {
            loadAnnotationConfig();
        }
    }

    private void loadXmlConfig(String xmlConfigPath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Beans.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Beans config = (Beans) unmarshaller.unmarshal(new File(xmlConfigPath));
        
        for (Bean beanConfig : config.getBeans()) {
            try {
                Class<?> beanClass = Class.forName(beanConfig.getClassName());
                Object instance = createInstance(beanClass, beanConfig);
                beans.put(beanConfig.getId(), instance);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create bean: " + beanConfig.getId(), e);
            }
        }
    }

    private void loadAnnotationConfig() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> componentClasses = reflections.getTypesAnnotatedWith(Component.class);

        for (Class<?> componentClass : componentClasses) {
            try {
                Object instance = createInstance(componentClass, null);
                String beanName = componentClass.getAnnotation(Component.class).value();
                if (beanName.isEmpty()) {
                    beanName = componentClass.getSimpleName();
                }
                beans.put(beanName, instance);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create component: " + componentClass.getName(), e);
            }
        }
    }

    private Object createInstance(Class<?> clazz, Bean beanConfig) throws Exception {
        Object instance;
        
        // Handle constructor injection
        if (beanConfig != null && !beanConfig.getConstructorArgs().isEmpty()) {
            Constructor<?>[] constructors = clazz.getConstructors();
            Constructor<?> constructor = constructors[0]; // Use the first constructor
            Object[] args = new Object[constructor.getParameterCount()];
            
            for (int i = 0; i < beanConfig.getConstructorArgs().size(); i++) {
                String ref = beanConfig.getConstructorArgs().get(i).getRef();
                if (ref != null) {
                    args[i] = beans.get(ref);
                } else {
                    args[i] = convertValue(beanConfig.getConstructorArgs().get(i).getValue(),
                            beanConfig.getConstructorArgs().get(i).getType());
                }
            }
            instance = constructor.newInstance(args);
        } else {
            instance = clazz.getDeclaredConstructor().newInstance();
        }

        // Handle field injection
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                Object dependency = findDependency(fieldType);
                if (dependency != null) {
                    field.set(instance, dependency);
                }
            }
        }

        // Handle setter injection
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Autowired.class) && method.getName().startsWith("set")) {
                Class<?>[] paramTypes = method.getParameterTypes();
                if (paramTypes.length == 1) {
                    Object dependency = findDependency(paramTypes[0]);
                    if (dependency != null) {
                        method.invoke(instance, dependency);
                    }
                }
            }
        }

        return instance;
    }

    private Object findDependency(Class<?> type) {
        for (Object bean : beans.values()) {
            if (type.isAssignableFrom(bean.getClass())) {
                return bean;
            }
        }
        return null;
    }

    private Object convertValue(String value, String type) {
        if (type == null) {
            return value;
        }
        switch (type) {
            case "int":
            case "Integer":
                return Integer.parseInt(value);
            case "long":
            case "Long":
                return Long.parseLong(value);
            case "double":
            case "Double":
                return Double.parseDouble(value);
            case "boolean":
            case "Boolean":
                return Boolean.parseBoolean(value);
            default:
                return value;
        }
    }

    public <T> T getBean(String name, Class<T> type) {
        Object bean = beans.get(name);
        if (bean == null) {
            throw new RuntimeException("Bean not found: " + name);
        }
        if (!type.isAssignableFrom(bean.getClass())) {
            throw new RuntimeException("Bean type mismatch: " + name);
        }
        return type.cast(bean);
    }

    public <T> T getBean(Class<T> type) {
        for (Object bean : beans.values()) {
            if (type.isAssignableFrom(bean.getClass())) {
                return type.cast(bean);
            }
        }
        throw new RuntimeException("Bean not found for type: " + type.getName());
    }
} 