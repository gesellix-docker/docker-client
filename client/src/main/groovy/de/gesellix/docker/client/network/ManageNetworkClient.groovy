package de.gesellix.docker.client.network

import de.gesellix.docker.client.DockerResponseHandler
import de.gesellix.docker.engine.EngineClient
import de.gesellix.docker.engine.EngineResponse
import de.gesellix.util.QueryUtil
import groovy.util.logging.Slf4j

@Slf4j
class ManageNetworkClient implements ManageNetwork {

  private EngineClient client
  private DockerResponseHandler responseHandler
  private QueryUtil queryUtil

  ManageNetworkClient(EngineClient client, DockerResponseHandler responseHandler) {
    this.client = client
    this.responseHandler = responseHandler
    this.queryUtil = new QueryUtil()
  }

  @Override
  EngineResponse networks(query = [:]) {
    log.info "docker network ls"
    def actualQuery = query ?: [:]
    queryUtil.jsonEncodeFilters(actualQuery)
    def response = client.get([path : "/networks",
                               query: actualQuery])
    responseHandler.ensureSuccessfulResponse(response, new IllegalStateException("docker network ls failed"))
    return response
  }

  @Override
  EngineResponse inspectNetwork(name) {
    log.info "docker network inspect"
    def response = client.get([path: "/networks/$name".toString()])
    responseHandler.ensureSuccessfulResponse(response, new IllegalStateException("docker network inspect failed"))
    return response
  }

  @Override
  EngineResponse createNetwork(name, config = [:]) {
    log.info "docker network create"
    def actualConfig = config ?: [:]
    def defaults = [Name          : name,
                    CheckDuplicate: true]
    queryUtil.applyDefaults(actualConfig, defaults)
    def response = client.post([path              : "/networks/create",
                                body              : actualConfig ?: [:],
                                requestContentType: "application/json"])
    responseHandler.ensureSuccessfulResponse(response, new IllegalStateException("docker network create failed"))
    return response
  }

  @Override
  EngineResponse connectNetwork(network, container) {
    log.info "docker network connect"
    def response = client.post([path              : "/networks/$network/connect".toString(),
                                body              : [container: container],
                                requestContentType: "application/json"])
    responseHandler.ensureSuccessfulResponse(response, new IllegalStateException("docker network connect failed"))
    return response
  }

  @Override
  EngineResponse disconnectNetwork(network, container) {
    log.info "docker network disconnect"
    def response = client.post([path              : "/networks/$network/disconnect".toString(),
                                body              : [container: container],
                                requestContentType: "application/json"])
    responseHandler.ensureSuccessfulResponse(response, new IllegalStateException("docker network disconnect failed"))
    return response
  }

  @Override
  EngineResponse rmNetwork(name) {
    log.info "docker network rm"
    def response = client.delete([path: "/networks/$name".toString()])
    responseHandler.ensureSuccessfulResponse(response, new IllegalStateException("docker network rm failed"))
    return response
  }

  @Override
  EngineResponse pruneNetworks(query = [:]) {
    log.info "docker network prune"
    def actualQuery = query ?: [:]
    queryUtil.jsonEncodeFilters(actualQuery)
    def response = client.post([path : "/networks/prune",
                                query: actualQuery])
    responseHandler.ensureSuccessfulResponse(response, new IllegalStateException("docker network prune failed"))
    return response
  }
}
