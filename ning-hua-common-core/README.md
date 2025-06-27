# ning-hua-common-core

`ning-hua-common-core` 是宁华通用组件库的核心模块，提供了一系列通用的工具类、枚举、配置和统一返回结果等功能，方便项目开发和维护。核心组件可以在多个项目间复用，减少重复开发工作，提高开发效率。

## 主要功能
### 统一返回结果
定义了 `NingHuaResult` 类，用于统一接口返回格式，包含成功状态、返回数据、消息和错误信息，方便前后端交互。

### 请求头处理
提供 `NingHuaHeader` 类和相关工具类（如 `RequestUtils`），用于处理客户端请求头信息，支持从不同类型请求中提取自定义头信息。

### Web 工具类
包含 `WebUtils` 工具类，提供 Cookie 操作、请求获取、客户端 ID 解析等功能，简化 Web 开发中的常见操作。

### 转换器
提供 `ConverterContext` 和 `AbstractResConverterHandler` 等类，用于数据转换，方便在不同数据格式之间进行转换。

## 技术栈
- **Java 17**：使用 Java 17 作为开发语言，享受最新的语言特性和性能优化。
- **Spring Boot**：基于 Spring Boot 框架开发，简化项目配置和依赖管理。
- **Lombok**：使用 Lombok 减少样板代码，提高代码可读性和开发效率。
- **Hutool**：集成 Hutool 工具库，提供丰富的工具类和实用方法。

## 模块结构
```plaintext
ning-hua-common-core/
├── .gitignore
├── README.md
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   ├── config/            # 配置类
        │   ├── constants/         # 常量类
        │   ├── converter/         # 转换器相关类
        │   ├── enums/             # 枚举类
        │   ├── exception/         # 异常类
        │   ├── filter/            # 过滤器类
        │   ├── handler/           # 处理器类
        │   ├── jackson/           # Jackson 相关类
        │   ├── request/           # 请求相关类
        │   ├── result/            # 统一返回结果类
        │   └── util/              # 工具类
        └── resources/             # 资源文件
```

## 使用说明
### 添加依赖
在 `pom.xml` 中添加以下依赖：
```xml
<dependency>
    <groupId>com.ninghua</groupId>
    <artifactId>ning-hua-common-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 示例代码
#### `NingHuaResult` 使用示例
以下是使用 `NingHuaResult` 的示例：
```java
import com.ninghua.common.core.result.NingHuaResult;

public class Example {
    public static void main(String[] args) {
        // 返回成功结果
        NingHuaResult<String> successResult = NingHuaResult.ok("操作成功");
        System.out.println(successResult);

        // 返回失败结果
        NingHuaResult<String> failedResult = NingHuaResult.failed("操作失败");
        System.out.println(failedResult);
    }
}
```

#### `RequestUtils` 使用示例
以下是使用 `RequestUtils` 从请求中提取 `NingHuaHeader` 的示例：
```java
import com.ninghua.common.core.request.NingHuaHeader;
import com.ninghua.common.core.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class RequestUtilsExample {
    public static void main(String[] args) {
        // 模拟 HttpServletRequest 对象
        HttpServletRequest request = new MockHttpServletRequest();
        // 从请求中提取 NingHuaHeader
        NingHuaHeader header = RequestUtils.getNingHuaHeaderByRequest(request);
        System.out.println("提取的 NingHuaHeader: " + header);
    }

    // 简单的 Mock 类，仅用于示例
    static class MockHttpServletRequest implements HttpServletRequest {
        // 实现必要的方法...
        @Override
        public Enumeration<String> getHeaderNames() {
            Map<String, String> headers = new HashMap<>();
            headers.put("App-Platform", "WEB");
            headers.put("App-Client", "ADMIN");
            return java.util.Collections.enumeration(headers.keySet());
        }

        @Override
        public String getHeader(String name) {
            // 实现获取头信息的逻辑...
            return "test-value";
        }
        // 其他方法省略...
    }
}
```

## 依赖管理
项目使用 Maven 进行依赖管理，主要依赖如下：
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```

## 贡献指南
### 代码风格
请遵循项目现有的代码风格和规范，使用 Lombok 减少样板代码，添加必要的注释。

### 提交规范
- 提交信息应清晰描述所做的更改。
- 确保代码通过编译和单元测试。

### 拉取请求
- 创建新的分支进行开发，分支命名应具有描述性。
- 提交拉取请求时，说明更改的目的和实现思路。

## 版本发布
项目使用 SNAPSHOT 版本进行开发，正式发布时会更新为稳定版本。
