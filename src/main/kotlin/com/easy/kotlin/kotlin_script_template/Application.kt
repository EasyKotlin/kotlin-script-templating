package com.easy.kotlin.kotlin_script_template

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.view.script.ScriptTemplateConfigurer
import org.springframework.web.servlet.view.script.ScriptTemplateViewResolver
import java.util.Locale
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry


fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

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


