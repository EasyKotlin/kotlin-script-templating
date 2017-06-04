Kotlin Script Template Web Appliciation
===


Spring Boot + Kotlin type safe template rendering with i18n and nested template support.

Requires:
 
 - Spring Framework 5.x and 
 - Kotlin 1.1+.

These templates look like:

```kotlin
import com.easy.kotlin.kotlin_script_template.*

"""
${include("header")}
<h1>${i18n("title")}</h1>
<ul>
	${users.joinToLine{ "<li>${i18n("user")} ${it.firstname} ${it.lastname}</li>" }}
</ul>
${include("footer")}
"""
```

To enable variable resolution in `.kts` files in IDEA, go to menu preferences -> Build, Execution, Deployement -> Compiler -> Kotlin Compiler and set:
 - Script templates class: `kotlin.script.templates.standard.ScriptTemplateWithBindings`
 - Script templates classpath: `/path/to/kotlin-script-runtime.jar`
 
This will be configured automatically in future version of IDEA Kotlin plugin.

Feel free to send pull requests to improve it!


Spring Boot  使用 Kotlin Script Template 模板引擎kts 开发web应用
===

在 Spring Framework 5.0 M4 中引入了一个专门的Kotlin支持。

## Kotlin Script based templates

### ScriptTemplateView
从4.3版本开始，Spring Framework提供了一个 org.springframework.web.servlet.view.script.ScriptTemplateView 使用支持 JSR-223 的脚本引擎来渲染模板。 

```
...

/**
 * An {@link AbstractUrlBasedView} subclass designed to run any template library
 * based on a JSR-223 script engine.
 *
 * <p>If not set, each property is auto-detected by looking up a single
 * {@link ScriptTemplateConfig} bean in the web application context and using
 * it to obtain the configured properties.
 *
 * <p>Nashorn Javascript engine requires Java 8+, and may require setting the
 * {@code sharedEngine} property to {@code false} in order to run properly. See
 * {@link ScriptTemplateConfigurer#setSharedEngine(Boolean)} for more details.
 *
 * @author Sebastien Deleuze
 * @author Juergen Hoeller
 * @since 4.2
 * @see ScriptTemplateConfigurer
 * @see ScriptTemplateViewResolver
 */
public class ScriptTemplateView extends AbstractUrlBasedView {

	public static final String DEFAULT_CONTENT_TYPE = "text/html";

	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private static final String DEFAULT_RESOURCE_LOADER_PATH = "classpath:";


	private static final ThreadLocal<Map<Object, ScriptEngine>> enginesHolder =
			new NamedThreadLocal<>("ScriptTemplateView engines");


	private ScriptEngine engine;

	private String engineName;

	private Locale locale;

	private Boolean sharedEngine;

	private String[] scripts;

	private String renderObject;

	private String renderFunction;

	private Charset charset;

	private String[] resourceLoaderPaths;

	private ResourceLoader resourceLoader;

	private volatile ScriptEngineManager scriptEngineManager;

...

}

```

这样我们就可以使用 kotlinx.html DSL或简单的Kotlin multiline String插值，编写kts类型安全模板。例如：

```
import com.easy.kotlin.kotlin_script_template.*

"""
${include("header")}
<p>Locale:
<a href="/?locale=fr">FR</a> |
<a href="/?locale=en">EN</a> |
<a href="/?locale=zh">中文</a>

</p>
<h1>${i18n("title")}</h1>
${include("users", mapOf(Pair("users", users)))}
${include("footer")}
"""


```


## 新建一个SpringBoot + Kotlin 工程


首先，我们新建一个SpringBoot + Kotlin 工程，使用对应的版本分别是

- kotlinVersion = '1.1.2'
- springBootVersion = '2.0.0.BUILD-SNAPSHOT'



使用gradle构建，配置如下：

```
buildscript {
	ext {
		kotlinVersion = '1.1.2'
		springBootVersion = '2.0.0.BUILD-SNAPSHOT'
	}
	repositories {
		mavenCentral()
		maven { url "http://dl.bintray.com/kotlin/kotlin-eap-1.1" }
		maven { url "https://repo.spring.io/snapshot" }
		maven { url "https://repo.spring.io/milestone" }
	}
	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
		classpath("org.jetbrains.kotlin:kotlin-allopen:${kotlinVersion}")
	}
}

apply plugin: 'kotlin'
apply plugin: 'kotlin-spring'
apply plugin: 'org.springframework.boot'

jar {
	baseName = 'kotlin-script-templating'
	version = '0.0.1-SNAPSHOT'
}

repositories {
	mavenCentral()
	maven { url "http://dl.bintray.com/kotlin/kotlin-eap-1.1" }
	maven { url "https://repo.spring.io/snapshot" }
	maven { url "https://repo.spring.io/milestone" }
}


dependencies {
	compile("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")
	compile("org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion}")
	compile("org.jetbrains.kotlin:kotlin-compiler:${kotlinVersion}")
	compile("org.jetbrains.kotlin:kotlin-script-util:${kotlinVersion}") {
		exclude group: "com.jcabi", module: "jcabi-aether"
		exclude group: "org.apache.maven", module: "maven-core"
		exclude group: "org.sonatype.aether", module: "aether-api"
	}
	testCompile("org.springframework.boot:spring-boot-starter-test:${springBootVersion}")
}

```


## 工程目录结构

工程目录结构如下：

```
.
├── README.md
├── build
│   └── kotlin-build
│       └── caches
│           └── version.txt
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
└── src
    ├── main
    │   ├── java
    │   ├── kotlin
    │   │   └── com
    │   │       └── easy
    │   │           └── kotlin
    │   │               └── kotlin_script_template
    │   │                   ├── Application.kt
    │   │                   ├── Helpers.kt
    │   │                   ├── User.kt
    │   │                   └── ViewController.kt
    │   └── resources
    │       ├── META-INF
    │       │   └── services
    │       │       └── javax.script.ScriptEngineFactory
    │       ├── application.properties
    │       ├── banner.txt
    │       ├── messages.properties
    │       ├── messages_en.properties
    │       ├── messages_fr.properties
    │       ├── messages_zh.properties
    │       ├── scripts
    │       │   └── render.kts
    │       └── templates
    │           ├── footer.kts
    │           ├── header.kts
    │           ├── index.kts
    │           ├── user.kts
    │           └── users.kts
    └── test
        ├── java
        ├── kotlin
        │   └── com
        │       └── easy
        │           └── kotlin
        │               └── kotlin_script_template
        │                   └── ApplicationTests.kt
        └── resources

26 directories, 25 files

```


## 配置脚本解析引擎的实现类

在META-INF/services/javax.script.ScriptEngineFactory文件里面加上其脚本解析引擎的实现类

```
org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngineFactory
```


其中，完成kts模板文件编译解析的类是KotlinJsr223JvmLocalScriptEngineFactory，这个类在kotlin-script-util包里。
因为多了这一层编译解析，感觉速度慢了点。

配置Script Template Engine : 

```

@SpringBootApplication
class Application : WebMvcConfigurerAdapter() {


    /**
     * ScriptTemplateConfigurer
     * scripts/render.kts
     */
    @Bean
    fun kotlinScriptConfigurer(): ScriptTemplateConfigurer {
        val configurer = ScriptTemplateConfigurer()
        configurer.engineName = "kotlin"
        configurer.setScripts("scripts/render.kts")
        configurer.renderFunction = "render"
        configurer.isSharedEngine = false
        return configurer
    }

    @Bean
    fun kotlinScriptViewResolver(): ViewResolver {
        val viewResolver = ScriptTemplateViewResolver()
        viewResolver.setPrefix("templates/")
        viewResolver.setSuffix(".kts")
        return viewResolver
    }

    @Bean
    fun localeResolver() = SessionLocaleResolver().apply {
        setDefaultLocale(Locale.CHINESE)
    }

    @Bean
    fun localeChangeInterceptor() = LocaleChangeInterceptor()

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(localeChangeInterceptor())
    }

}

```
其中，`LocaleChangeInterceptor()` 是Spring框架里面定义的类

org.springframework.web.servlet.i18n.LocaleChangeInterceptor。

```
public class LocaleChangeInterceptor extends HandlerInterceptorAdapter {

	/**
	 * Default name of the locale specification parameter: "locale".
	 */
	public static final String DEFAULT_PARAM_NAME = "locale";


	protected final Log logger = LogFactory.getLog(getClass());

	private String paramName = DEFAULT_PARAM_NAME;

	private String[] httpMethods;

	private boolean ignoreInvalidLocale = false;

	private boolean languageTagCompliant = false;

...
```

从上面的源码，我们可以看出它的默认query参数是`locale`。

## 渲染脚本render.kts

其中，render.kts代码如下：

```
import org.springframework.web.servlet.view.script.RenderingContext
import org.springframework.context.support.ResourceBundleMessageSource
import javax.script.*
import org.springframework.beans.factory.getBean

// TODO Use engine.eval(String, Bindings) when https://youtrack.jetbrains.com/issue/KT-15450 will be fixed
fun render(template: String, model: Map<String, Any>, renderingContext: RenderingContext): String {
	val engine = ScriptEngineManager().getEngineByName("kotlin")
	val bindings = SimpleBindings(model)
	var messageSource = renderingContext.applicationContext.getBean<ResourceBundleMessageSource>()
	bindings.put("i18n", { code: String -> messageSource.getMessage(code, null, renderingContext.locale) })
	bindings.put("include", { path: String -> renderingContext.templateLoader.apply("templates/$path.kts") })
	engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE)
	return engine.eval(template) as String
}
```


## 模板绑定 ScriptTemplateWithBindings

我们上面在bindings里面添加了

```
bindings.put("i18n", { code: String -> messageSource.getMessage(code, null, renderingContext.locale) })
```

在index.kts这样调用
```
import com.easy.kotlin.kotlin_script_template.*

"""
${include("header")}
<p>Locale:
<a href="/?locale=fr">FR</a> |
<a href="/?locale=en">EN</a> |
<a href="/?locale=zh">中文</a>

</p>
<h1>${i18n("title")}</h1>
${include("users", mapOf(Pair("users", users)))}
${include("footer")}
"""

```

这个`i18n`在ScriptTemplateWithBindings类里的扩展函数如下

```
package com.easy.kotlin.kotlin_script_template

import javax.script.ScriptContext
import javax.script.ScriptEngineManager
import javax.script.SimpleBindings
import kotlin.script.templates.standard.ScriptTemplateWithBindings

fun ScriptTemplateWithBindings.include(path: String, model: Map<String, Any>? = null) :String {
    val engine = ScriptEngineManager().getEngineByName("kotlin")
    var includeBindings = if (model != null) {
        val b = SimpleBindings(LinkedHashMap(model))
        b["include"] = bindings["include"]
        b["i18n"] = bindings["i18n"]
        b
    } else {
        val b = SimpleBindings(bindings)
        b.remove("kotlin.script.history")
        b
    }

    engine.setBindings(includeBindings, ScriptContext.ENGINE_SCOPE)
    val template = (bindings["include"] as (String) -> String).invoke(path)
    return engine.eval(template) as String
}

fun ScriptTemplateWithBindings.i18n(code: String) =
    (bindings["i18n"] as (String) -> String).invoke(code)

fun <T> Iterable<T>.joinToLine(function: (foo: T) -> String): String
  { return joinToString(separator = "\n") { foo -> function.invoke(foo) } }

var ScriptTemplateWithBindings.users: List<User>
  get() = bindings["users"] as List<User>
  set(value) { throw UnsupportedOperationException()}

var ScriptTemplateWithBindings.user: User
  get() = bindings["user"] as User
  set(value) { throw UnsupportedOperationException()}

var ScriptTemplateWithBindings.title: String
  get() = bindings["title"] as String
  set(value) { throw UnsupportedOperationException()}

```



写实体类Usr，Controller类，最后写Spring Boot main函数:

```
fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
```


## 运行测试

命令行运行
```
gradle bootRun

```

启动运行，访问：http://localhost:8080/

你将看到如下输出页面


![螢幕快照 2017-06-05 00.13.24.png](http://upload-images.jianshu.io/upload_images/1233356-ba8e69203d07106d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![螢幕快照 2017-06-05 00.14.04.png](http://upload-images.jianshu.io/upload_images/1233356-5d1f4aad991a768e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)




小结
===

本章节工程源代码：

https://github.com/EasyKotlin/kotlin-script-templating


使用Kotlin编写Spring Boot应用程序越多，我们越觉得这两种技术有着共同的目标，让我们广大程序员可以使用

- 富有表达性
- 简短
- 可读的代码

来更高效地编写应用程序，而Spring Framework 5 Kotlin支持将这些技术以更加自然，简单和强大的方式来展现给我们。

Kotlin可以用来编写 基于注解的Spring Boot应用程序 ，但作为一种新的 functional and reactive applications 也将是一种很好的尝试，期待未来Spring Framework 5.0 和 Kotlin 结合的开发实践。







参考资料
===

https://github.com/sdeleuze/kotlin-script-templating






