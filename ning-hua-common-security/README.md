# 安全模块

`ning-hua-common-security` 是柠花通用组件库中的安全模块，该模块主要用于提供项目的安全相关功能，包括身份认证、权限验证、请求头检查等功能，保障项目的接口安全和数据安全。

## 功能概述
1. **JWT 认证**：使用 Sa-Token 框架结合 JWT 实现无状态认证，提供 `StpLogicJwtForStateless` 逻辑支持。
2. **密码加密**：使用 `BCryptPasswordEncoder` 对密码进行加密处理。
3. **请求头检查**：通过 `HeaderCheckInterceptor` 拦截器检查请求头中的必要信息。
4. **权限拦截**：`AuthenticationInterceptor` 拦截器负责验证请求的权限，确保接口安全。
5. **异常处理**：`SecurityExceptionHandler` 统一处理认证和权限相关的异常。

## 技术栈
- **Spring Boot 3**：基础框架，提供核心功能支持。
- **Sa-Token**：轻量级权限认证框架，用于 JWT 认证。
- **Hutool**：Java 工具类库，辅助处理字符串、JSON 等操作。

## 模块结构
```
ning-hua-common-security/
├── .gitignore
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/ninghua/common/security/
        │       ├── annotation/    # 自定义注解
        │       ├── config/        # 配置类
        │       ├── handler/       # 异常处理类
        │       ├── interceptor/   # 拦截器
        │       └── util/          # 工具类
        └── resources/             # 资源文件
```

## 使用说明
### 添加依赖
在项目的 `pom.xml` 中添加以下依赖：
```xml
<dependency>
    <groupId>com.ninghua</groupId>
    <artifactId>ning-hua-common-security</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 配置文件
在 `application.yml` 或 `application.properties` 中添加安全相关配置：
```yaml
# 配置文件加密根密码
jasypt:
  encryptor:
    password: xxx
    algorithm: PBEWithMD5AndDES
    iv-generator-classname: org.jasypt.iv.NoIvGenerator
sa-token:
  # jwt秘钥 
  jwt-secret-key: xxx
ninghua:
  security:
    auth:
      enable: true
      pathPattern:
        - /portal/**
```

### 忽略认证注解
在不需要认证的方法或类上添加 `@IgnoreAuth` 注解：
```java
@IgnoreAuth(false)
@GetMapping("/public/data")
public NingHuaResult<String> getPublicData() {
    return NingHuaResult.success("Public data");
}
```

## 代码用例分析
### HeaderCheckInterceptor 拦截器
`HeaderCheckInterceptor` 用于检查请求头中的必要信息，确保请求包含 `platform`、`version`、`client` 和 `system` 字段。示例如下：
```java
// 在配置中启用拦截器后，访问匹配路径的接口时会触发该拦截器
// 如果没有添加 @IgnoreAppHeader 注解，会检查请求头信息
@GetMapping("/secure/data")
public NingHuaResult<String> getSecureData() {
    return NingHuaResult.success("Secure data");
}
```
当请求缺少必要头信息时，会抛出 `BizException` 异常，错误码为 `ErrorCode.ILLEGAL_HEADER`。

### AuthenticationInterceptor 拦截器
`AuthenticationInterceptor` 用于权限验证，确保请求携带有效的 `profileId` 和 `client` 信息，并且 token 没有跨端使用。示例如下：
```java
// 在配置中启用拦截器后，访问匹配路径的接口时会触发权限验证
// 如果没有添加 @IgnoreAuth 注解或其值为 true，会验证请求头信息
@GetMapping("/auth/data")
public NingHuaResult<String> getAuthData() {
    return NingHuaResult.success("Auth data");
}
```
当请求缺少 `profileId` 或 `client`，或者 token 跨端使用时，会抛出 `BizException` 异常，错误码为 `ErrorCode.TOKEN_WRONG`。

### SecurityExceptionHandler 异常处理类
`SecurityExceptionHandler` 用于统一处理认证和权限相关的异常，示例如下：
```java
// 当访问需要认证的接口但未登录时，会触发 NotLoginException
// 当访问需要特定权限的接口但权限不足时，会触发 NotPermissionException
@GetMapping("/protected/data")
public NingHuaResult<String> getProtectedData() {
    return NingHuaResult.success("Protected data");
}
```
这两种异常会分别返回 `ErrorCode.TOKEN_WRONG` 和 `ErrorCode.NOT_PERMISSION` 错误码。

## 依赖管理
该模块依赖于以下组件：
- `ning-hua-common-core`：通用核心组件。
- `spring-cloud-starter-openfeign`：用于服务间调用。
- `sa-token-spring-boot3-starter` 和 `sa-token-jwt`：用于 JWT 认证。

## 贡献指南
如果你想为该项目做出贡献，请遵循以下步骤：
1. Fork 该项目。
2. 创建你的特性分支 (`git checkout -b feature/fooBar`)。
3. 提交你的更改 (`git commit -am 'Add some fooBar'`)。
4. 将更改推送到你的分支 (`git push origin feature/fooBar`)。
5. 创建一个新的 Pull Request。

## 版本发布
当前版本为 `1.0-SNAPSHOT`，后续会根据功能迭代和 bug 修复发布新版本。

## 联系方式
如果你有任何问题或建议，可以通过以下方式联系我们：
- 邮箱：[your-email@example.com](mailto:your-email@example.com)