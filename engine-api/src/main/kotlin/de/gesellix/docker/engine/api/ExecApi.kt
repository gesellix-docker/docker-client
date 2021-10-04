/**
 * Docker Engine API
 * The Engine API is an HTTP API served by Docker Engine. It is the API the Docker client uses to communicate with the Engine, so everything the Docker client can do can be done with the API.  Most of the client's commands map directly to API endpoints (e.g. `docker ps` is `GET /containers/json`). The notable exception is running containers, which consists of several API calls.  # Errors  The API uses standard HTTP status codes to indicate the success or failure of the API call. The body of the response will be JSON in the following format:  ``` {   \"message\": \"page not found\" } ```  # Versioning  The API is usually changed in each release, so API calls are versioned to ensure that clients don't break. To lock to a specific version of the API, you prefix the URL with its version, for example, call `/v1.30/info` to use the v1.30 version of the `/info` endpoint. If the API version specified in the URL is not supported by the daemon, a HTTP `400 Bad Request` error message is returned.  If you omit the version-prefix, the current version of the API (v1.41) is used. For example, calling `/info` is the same as calling `/v1.41/info`. Using the API without a version-prefix is deprecated and will be removed in a future release.  Engine releases in the near future should support this version of the API, so your client will continue to work even if it is talking to a newer Engine.  The API uses an open schema model, which means server may add extra properties to responses. Likewise, the server will ignore any extra query parameters and request body properties. When you write clients, you need to ignore additional properties in responses to ensure they do not break when talking to newer daemons.   # Authentication  Authentication for registries is handled client side. The client has to send authentication details to various endpoints that need to communicate with registries, such as `POST /images/(name)/push`. These are sent as `X-Registry-Auth` header as a [base64url encoded](https://tools.ietf.org/html/rfc4648#section-5) (JSON) string with the following structure:  ``` {   \"username\": \"string\",   \"password\": \"string\",   \"email\": \"string\",   \"serveraddress\": \"string\" } ```  The `serveraddress` is a domain/IP without a protocol. Throughout this structure, double quotes are required.  If you have already got an identity token from the [`/auth` endpoint](#operation/SystemAuth), you can just pass this instead of credentials:  ``` {   \"identitytoken\": \"9cbaf023786cd7...\" } ```
 *
 * The version of the OpenAPI document: 1.41
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package de.gesellix.docker.engine.api

import de.gesellix.docker.engine.RequestMethod.GET
import de.gesellix.docker.engine.RequestMethod.POST
import de.gesellix.docker.engine.client.infrastructure.ApiClient
import de.gesellix.docker.engine.client.infrastructure.ClientError
import de.gesellix.docker.engine.client.infrastructure.ClientException
import de.gesellix.docker.engine.client.infrastructure.LoggingCallback
import de.gesellix.docker.engine.client.infrastructure.MultiValueMap
import de.gesellix.docker.engine.client.infrastructure.RequestConfig
import de.gesellix.docker.engine.client.infrastructure.ResponseType
import de.gesellix.docker.engine.client.infrastructure.ServerError
import de.gesellix.docker.engine.client.infrastructure.ServerException
import de.gesellix.docker.engine.client.infrastructure.Success
import de.gesellix.docker.engine.client.infrastructure.SuccessStream
import de.gesellix.docker.remote.api.ExecConfig
import de.gesellix.docker.remote.api.ExecInspectResponse
import de.gesellix.docker.remote.api.ExecStartConfig
import de.gesellix.docker.remote.api.IdResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import java.time.Duration
import java.time.temporal.ChronoUnit

class ExecApi(basePath: String = defaultBasePath) : ApiClient(basePath) {
  companion object {

    @JvmStatic
    val defaultBasePath: String by lazy {
      System.getProperties().getProperty("docker.client.baseUrl", "http://localhost/v1.41")
    }
  }

  /**
   * Create an exec instance
   * Run a command inside a running container.
   * @param id ID or name of container
   * @param execConfig
   * @return IdResponse
   * @throws UnsupportedOperationException If the API returns an informational or redirection response
   * @throws ClientException If the API returns a client error response
   * @throws ServerException If the API returns a server error response
   */
  @Suppress("UNCHECKED_CAST")
  @Throws(UnsupportedOperationException::class, ClientException::class, ServerException::class)
  fun containerExec(id: String, execConfig: ExecConfig): IdResponse {
    val localVariableConfig = containerExecRequestConfig(id = id, execConfig = execConfig)

    val localVarResponse = request<IdResponse>(
      localVariableConfig
    )

    return when (localVarResponse.responseType) {
      ResponseType.Success -> (localVarResponse as Success<*>).data as IdResponse
      ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
      ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
      ResponseType.ClientError -> {
        val localVarError = localVarResponse as ClientError<*>
        throw ClientException("Client error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
      }
      ResponseType.ServerError -> {
        val localVarError = localVarResponse as ServerError<*>
        throw ServerException("Server error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
      }
    }
  }

  /**
   * To obtain the request config of the operation containerExec
   *
   * @param id ID or name of container
   * @param execConfig
   * @return RequestConfig
   */
  fun containerExecRequestConfig(id: String, execConfig: ExecConfig): RequestConfig {
    val localVariableBody: Any? = execConfig
    val localVariableQuery: MultiValueMap = mutableMapOf()
    val localVariableHeaders: MutableMap<String, String> = mutableMapOf()

    return RequestConfig(
      method = POST,
      path = "/containers/{id}/exec".replace("{" + "id" + "}", id),
      query = localVariableQuery,
      headers = localVariableHeaders,
      body = localVariableBody
    )
  }

  /**
   * Inspect an exec instance
   * Return low-level information about an exec instance.
   * @param id Exec instance ID
   * @return ExecInspectResponse
   * @throws UnsupportedOperationException If the API returns an informational or redirection response
   * @throws ClientException If the API returns a client error response
   * @throws ServerException If the API returns a server error response
   */
  @Suppress("UNCHECKED_CAST")
  @Throws(UnsupportedOperationException::class, ClientException::class, ServerException::class)
  fun execInspect(id: String): ExecInspectResponse {
    val localVariableConfig = execInspectRequestConfig(id = id)

    val localVarResponse = request<ExecInspectResponse>(
      localVariableConfig
    )

    return when (localVarResponse.responseType) {
      ResponseType.Success -> (localVarResponse as Success<*>).data as ExecInspectResponse
      ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
      ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
      ResponseType.ClientError -> {
        val localVarError = localVarResponse as ClientError<*>
        throw ClientException("Client error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
      }
      ResponseType.ServerError -> {
        val localVarError = localVarResponse as ServerError<*>
        throw ServerException("Server error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
      }
    }
  }

  /**
   * To obtain the request config of the operation execInspect
   *
   * @param id Exec instance ID
   * @return RequestConfig
   */
  fun execInspectRequestConfig(id: String): RequestConfig {
    val localVariableBody: Any? = null
    val localVariableQuery: MultiValueMap = mutableMapOf()
    val localVariableHeaders: MutableMap<String, String> = mutableMapOf()

    return RequestConfig(
      method = GET,
      path = "/exec/{id}/json".replace("{" + "id" + "}", id),
      query = localVariableQuery,
      headers = localVariableHeaders,
      body = localVariableBody
    )
  }

  /**
   * Resize an exec instance
   * Resize the TTY session used by an exec instance. This endpoint only works if &#x60;tty&#x60; was specified as part of creating and starting the exec instance.
   * @param id Exec instance ID
   * @param h Height of the TTY session in characters (optional)
   * @param w Width of the TTY session in characters (optional)
   * @return void
   * @throws UnsupportedOperationException If the API returns an informational or redirection response
   * @throws ClientException If the API returns a client error response
   * @throws ServerException If the API returns a server error response
   */
  @Throws(UnsupportedOperationException::class, ClientException::class, ServerException::class)
  fun execResize(id: String, h: Int?, w: Int?) {
    val localVariableConfig = execResizeRequestConfig(id = id, h = h, w = w)

    val localVarResponse = request<Any?>(
      localVariableConfig
    )

    return when (localVarResponse.responseType) {
      ResponseType.Success -> Unit
      ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
      ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
      ResponseType.ClientError -> {
        val localVarError = localVarResponse as ClientError<*>
        throw ClientException("Client error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
      }
      ResponseType.ServerError -> {
        val localVarError = localVarResponse as ServerError<*>
        throw ServerException("Server error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
      }
    }
  }

  /**
   * To obtain the request config of the operation execResize
   *
   * @param id Exec instance ID
   * @param h Height of the TTY session in characters (optional)
   * @param w Width of the TTY session in characters (optional)
   * @return RequestConfig
   */
  fun execResizeRequestConfig(id: String, h: Int?, w: Int?): RequestConfig {
    val localVariableBody: Any? = null
    val localVariableQuery: MultiValueMap = mutableMapOf<String, List<String>>()
      .apply {
        if (h != null) {
          put("h", listOf(h.toString()))
        }
        if (w != null) {
          put("w", listOf(w.toString()))
        }
      }
    val localVariableHeaders: MutableMap<String, String> = mutableMapOf()

    return RequestConfig(
      method = POST,
      path = "/exec/{id}/resize".replace("{" + "id" + "}", id),
      query = localVariableQuery,
      headers = localVariableHeaders,
      body = localVariableBody
    )
  }

  /**
   * Start an exec instance
   * Starts a previously set up exec instance. If detach is true, this endpoint returns immediately after starting the command. Otherwise, it sets up an interactive session with the command.
   * @param id Exec instance ID
   * @param execStartConfig  (optional)
   * @return void
   * @throws UnsupportedOperationException If the API returns an informational or redirection response
   * @throws ClientException If the API returns a client error response
   * @throws ServerException If the API returns a server error response
   */
  @Throws(UnsupportedOperationException::class, ClientException::class, ServerException::class)
  fun execStart(id: String, execStartConfig: ExecStartConfig?) {
    val localVariableConfig = execStartRequestConfig(id = id, execStartConfig = execStartConfig)

    // TODO do we need to inspect the exec, because it might have been created with tty==false?
//    val expectMultiplexedResponse = !(execInspect(id).processConfig?.tty ?: false)
    val expectMultiplexedResponse = !(execStartConfig?.tty ?: false)
    val localVarResponse = requestFrames(
      localVariableConfig, expectMultiplexedResponse
    )

    // TODO the caller of #execStart() should decide about timeout and callback
    val timeout = Duration.of(1, ChronoUnit.HOURS)
    val callback = LoggingCallback()

    when (localVarResponse.responseType) {
      ResponseType.Success -> {
        runBlocking {
          launch {
            withTimeoutOrNull(timeout.toMillis()) {
              callback.onStarting(this@launch::cancel)
              ((localVarResponse as SuccessStream<*>).data as Flow<Frame>).collect { callback.onNext(it) }
              callback.onFinished()
            }
          }
        }
      }
      ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
      ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
      ResponseType.ClientError -> {
        val localVarError = localVarResponse as ClientError<*>
        throw ClientException("Client error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
      }
      ResponseType.ServerError -> {
        val localVarError = localVarResponse as ServerError<*>
        throw ServerException("Server error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
      }
    }
  }

  /**
   * To obtain the request config of the operation execStart
   *
   * @param id Exec instance ID
   * @param execStartConfig  (optional)
   * @return RequestConfig
   */
  fun execStartRequestConfig(id: String, execStartConfig: ExecStartConfig?): RequestConfig {
    val localVariableBody: Any? = execStartConfig
    val localVariableQuery: MultiValueMap = mutableMapOf()
    val localVariableHeaders: MutableMap<String, String> = mutableMapOf()

    return RequestConfig(
      method = POST,
      path = "/exec/{id}/start".replace("{" + "id" + "}", id),
      query = localVariableQuery,
      headers = localVariableHeaders,
      body = localVariableBody
    )
  }
}
