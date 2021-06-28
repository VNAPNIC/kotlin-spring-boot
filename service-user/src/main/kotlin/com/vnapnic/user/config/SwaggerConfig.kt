package com.vnapnic.user.config

import com.vnapnic.common.config.BaseSwagger2Config
import org.springframework.context.annotation.Configuration
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig : BaseSwagger2Config()