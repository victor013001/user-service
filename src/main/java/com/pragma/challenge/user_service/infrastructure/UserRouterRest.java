package com.pragma.challenge.user_service.infrastructure;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.pragma.challenge.user_service.domain.constants.ConstantsMsg;
import com.pragma.challenge.user_service.domain.constants.ConstantsRoute;
import com.pragma.challenge.user_service.infrastructure.entrypoints.dto.RegisterBootcampDto;
import com.pragma.challenge.user_service.infrastructure.entrypoints.handler.UserHandler;
import com.pragma.challenge.user_service.infrastructure.entrypoints.util.SwaggerResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class UserRouterRest {

  @Bean
  @RouterOperations({
    @RouterOperation(
        path = ConstantsRoute.BASE_PATH + ConstantsRoute.BOOTCAMP_PATH,
        method = RequestMethod.POST,
        beanClass = UserHandler.class,
        beanMethod = "registerToBootcamp",
        operation =
            @Operation(
                operationId = "registerToBootcamp",
                summary = "Register to a bootcamp",
                requestBody =
                    @RequestBody(
                        required = true,
                        content =
                            @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = RegisterBootcampDto.class))),
                responses = {
                  @ApiResponse(
                      responseCode = "201",
                      description = ConstantsMsg.SUCCESS_REGISTER_TO_BOOTCAMP_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultMessageResponse.class))),
                  @ApiResponse(
                      responseCode = "400",
                      description = ConstantsMsg.BAD_REQUEST_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultErrorResponse.class))),
                  @ApiResponse(
                      responseCode = "500",
                      description = ConstantsMsg.SERVER_ERROR_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultErrorResponse.class)))
                }))
  })
  public RouterFunction<ServerResponse> routerFunction(UserHandler userHandler) {
    return nest(
        path(ConstantsRoute.BASE_PATH),
        route(POST(ConstantsRoute.BOOTCAMP_PATH), userHandler::registerToBootcamp));
  }
}
