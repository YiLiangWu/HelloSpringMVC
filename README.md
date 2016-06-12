# Hello SpringMVC

---

## 搭建SpringMVC

### 1. 使用maven创建一个web项目 *HelloSpringMVC*
### 2. 在pom.xml文件中加入Spirng MVC的依赖

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.liang</groupId>
	<artifactId>HelloSpringMVC</artifactId>
	<packaging>war</packaging>
	<version>0.0.1-SNAPSHOT</version>
	<name>HelloSpringMVC Maven Webapp</name>
	<url>http://maven.apache.org</url>
	<dependencies>

		<!-- start springmvc -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-webmvc</artifactId>
			<version>3.2.8.RELEASE</version>
		</dependency>
		<!-- end springmvc -->

		<!-- 测试需要加入JSON -->
		<dependency>
			<groupId>net.sf.json-lib</groupId>
			<artifactId>json-lib</artifactId>
			<version>2.4</version>
			<classifier>jdk15</classifier>
		</dependency>
	</dependencies>

	<build>
		<finalName>HelloSpringMVC</finalName>
	</build>
</project>
```

### 3. 在 *src/main/resources/conf* 文件夹下创建SpringMVC的配置文件 `springmvc-config.xml`:

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"  
 xmlns:context="http://www.springframework.org/schema/context"  
 xmlns:p="http://www.springframework.org/schema/p"  
 xmlns:mvc="http://www.springframework.org/schema/mvc"  
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
 xsi:schemaLocation="http://www.springframework.org/schema/beans  
      http://www.springframework.org/schema/beans/spring-beans-3.1.xsd  
      http://www.springframework.org/schema/context  
      http://www.springframework.org/schema/context/spring-context.xsd  
      http://www.springframework.org/schema/mvc
      http://www.springframework.org/schema/mvc/spring-mvc-3.1.xsd">
    
    <mvc:default-servlet-handler/>
    <!-- 扫描包 -->
    <context:component-scan base-package="com.liang.controller"></context:component-scan>
	<!-- 配置注解的优化 -->
	<mvc:annotation-driven />
		
	<!-- 视图解析器 -->
	<bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<!-- 视图的位置 -->
		<property name="prefix" value="/WEB-INF/"></property>
		<!-- 视图的后缀 -->
		<property name="suffix" value=".jsp"></property>
	</bean>

 </beans>  
```

### 4. 在 *web.xml*文件中配置启动SpringMVC的前端控制器，并且添加字符过滤器：

```
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app>
	<display-name>HelloSpringMVC</display-name>

	<!-- 启动springmvc容器 -->
	<servlet>
		<servlet-name>springmvc</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath*:/conf/springmvc-config.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>springmvc</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>
    <!--指定字符过滤器-->
	<filter>
		<filter-name>springCharacterEncodingFilter</filter-name>
		<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
		<init-param>
			<param-name>forceEncoding</param-name>
			<param-value>true</param-value>
		</init-param>
		<init-param>
			<param-name>encoding</param-name>
			<param-value>UTF-8</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>springCharacterEncodingFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
</web-app>
```

### 5. 编写Controller，注意使用 `@Controller` 注解指明交由SpringMVC容器管理，使用 `@RequestMapping` 指定映射的路径， `@ResponseBody`设置将返回字符原样输出，如果不加的话会根据 *springmvc-config.xml* 中的配置加上前缀和后缀去查找对应的视图。

```
@Controller
@RequestMapping(value = "/hello", produces = "text/html;charset=UTF-8")
public class HelloController {

	@RequestMapping("/test")
	@ResponseBody
	public String test(String name) {
		Map<String, Object> result = new HashMap<String, Object>();
		System.out.println("name = " + name);
		result.put("name", name);
		return JSONObject.fromObject(result).toString();
	}
}

```

### 6. 编写简单的页面进行测试：

```
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HelloSpringMVC</title>
</head>
<body>
	<form action="hello/test" method="post" accept-charset="utf-8">
		<label>请输入姓名：</label> <input type="text" name="name" id="input" /> <input
			type="button" value="提交" id="btn" /> <span id="content"></span>
	</form>

	<script>
		var btn = document.getElementById("btn");
		btn.onclick = function() {

			var name = document.getElementById("input").value;
			var span = document.getElementById("content");
			if (name.replace(/(^\s*)|(\s*$)/g, "").length == 0) {
				span.innerHTML="姓名不能为空";
				return ;
			}
			var request = new XMLHttpRequest();
			request.open("POST", "hello/test", "true");
			request.setRequestHeader("Content-type",
					"application/x-www-form-urlencoded");
			request.send("name=" + name);
			request.onreadystatechange = function() {
				if (request.readyState === 4 && request.status === 200) {
					var response = request.responseText;
					var jsonObject = JSON.parse(response);
					span.innerHTML = "你好, " + jsonObject.name;
				}
			};
		}
	</script>
</body>
</html>
```

## 期间遇到的问题

### 1. 后台接收到的中文字符显示为乱码。

原因是SpringMVC默认的编码格式是ISO-8859-1，所以需要将接收到的参数转成UTF-8，我采用的是在web.xml中配置编码过滤器，这样能统一对请求和响应进行转码。

### 2. 加上字符过滤器后前端接收到的中文仍为问号

造成该问题的具体原因尚不清楚，试过转码，也试过在前端重新解码，但仍无法解决，从网上找到的解决方法是在Controller的RequestMapping属性加上 **`produces = "text/html;charset=UTF-8"`** 解决该问题。