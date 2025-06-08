# Mini Dependency Injection Framework

A lightweight dependency injection framework similar to Spring IOC, supporting both XML and annotation-based configuration. This framework allows developers to manage dependencies between components in their applications through either XML configuration or annotations.

## Features

- XML-based configuration using JAXB
- Annotation-based configuration
- Support for multiple injection types:
  - Constructor injection
  - Setter injection
  - Field injection
- Type-safe bean retrieval
- Automatic dependency resolution

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── di/
│   │           └── framework/
│   │               ├── annotation/
│   │               │   ├── Component.java
│   │               │   └── Autowired.java
│   │               ├── config/
│   │               │   ├── Bean.java
│   │               │   ├── Beans.java
│   │               │   ├── Property.java
│   │               │   └── ConstructorArg.java
│   │               ├── context/
│   │               │   └── ApplicationContext.java
│   │               └── example/
│   │                   ├── Main.java
│   │                   ├── MessageService.java
│   │                   ├── EmailService.java
│   │                   └── NotificationService.java
│   └── resources/
│       └── application.xml
```

## Detailed Usage Guide

### 1. XML Configuration

The XML configuration allows you to define your beans and their dependencies in an XML file. This is useful when you want to configure your application without modifying the source code.

#### Example XML Configuration (application.xml):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans>
    <!-- Simple bean definition -->
    <bean id="messageService" class="com.di.framework.example.EmailService"/>
    
    <!-- Bean with constructor injection -->
    <bean id="notificationService" class="com.di.framework.example.NotificationService">
        <constructor-arg ref="messageService"/>
    </bean>

    <!-- Bean with property injection -->
    <bean id="userService" class="com.di.framework.example.UserService">
        <property name="messageService" ref="messageService"/>
    </bean>
</beans>
```

#### Using XML Configuration:
```java
// Initialize the context with XML configuration
ApplicationContext context = new ApplicationContext("path/to/application.xml");

// Retrieve beans
MessageService messageService = context.getBean("messageService", MessageService.class);
NotificationService notificationService = context.getBean("notificationService", NotificationService.class);
```

### 2. Annotation Configuration

Annotation-based configuration is more concise and keeps the configuration close to the code. This approach is preferred when you have control over the source code.

#### Available Annotations:

1. `@Component`: Marks a class as a component that can be managed by the framework
```java
@Component
public class EmailService implements MessageService {
    // Implementation
}
```

2. `@Autowired`: Marks a field, constructor, or setter method for dependency injection
```java
@Component
public class NotificationService {
    // Constructor injection
    @Autowired
    public NotificationService(MessageService messageService) {
        this.messageService = messageService;
    }

    // Field injection
    @Autowired
    private MessageService messageService;

    // Setter injection
    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
```

#### Using Annotation Configuration:
```java
// Initialize the context with annotation scanning
ApplicationContext context = new ApplicationContext("com.your.package", true);

// Retrieve beans by type
MessageService messageService = context.getBean(MessageService.class);
NotificationService notificationService = context.getBean(NotificationService.class);
```

### 3. Dependency Injection Types

The framework supports three types of dependency injection:

#### a. Constructor Injection
```java
@Component
public class NotificationService {
    private final MessageService messageService;

    @Autowired
    public NotificationService(MessageService messageService) {
        this.messageService = messageService;
    }
}
```

#### b. Setter Injection
```java
@Component
public class NotificationService {
    private MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
```

#### c. Field Injection
```java
@Component
public class NotificationService {
    @Autowired
    private MessageService messageService;
}
```

## Building and Running

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Building the Project
```bash
# Clone the repository
git clone https://github.com/Ouma49/Mini-Dependency-Injection-Framework.git

# Navigate to project directory
cd mini-di-framework

# Build the project
mvn clean install
```

### Running the Example
```bash
# Run the example application
mvn exec:java -Dexec.mainClass="com.di.framework.example.Main"
```

Expected output:
```
Using XML configuration:
Sending notification: This is an email message

Using annotation configuration:
Sending notification: This is an email message
```

## Best Practices

1. **Choose the Right Configuration Method**:
   - Use XML configuration when you need to configure the application without modifying the source code
   - Use annotation configuration when you have control over the source code and want more concise configuration

2. **Dependency Injection**:
   - Prefer constructor injection for required dependencies
   - Use setter injection for optional dependencies
   - Use field injection sparingly, as it makes testing more difficult

3. **Bean Naming**:
   - Use meaningful names for your beans
   - Follow Java naming conventions
   - Be consistent with your naming strategy

4. **Error Handling**:
   - Always check for null dependencies
   - Use proper exception handling
   - Provide meaningful error messages

## Troubleshooting

Common issues and solutions:

1. **Bean Not Found**:
   - Check if the bean is properly defined in XML or annotated with @Component
   - Verify the package scanning configuration
   - Check for typos in bean names

2. **Dependency Injection Fails**:
   - Ensure the dependency is properly defined
   - Check if the dependency is annotated with @Component
   - Verify the injection type (constructor/setter/field)

3. **XML Configuration Issues**:
   - Validate the XML file against the schema
   - Check for proper XML formatting
   - Verify class names and package names

