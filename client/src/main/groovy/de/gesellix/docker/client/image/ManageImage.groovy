package de.gesellix.docker.client.image

import de.gesellix.docker.client.DockerAsyncCallback
import de.gesellix.docker.client.Timeout
import de.gesellix.docker.engine.EngineResponse

interface ManageImage {

//    build       Build an image from a Dockerfile

    def buildWithLogs(InputStream buildContext)

    def buildWithLogs(InputStream buildContext, query)
    
    def buildWithLogs(InputStream buildContext, query, Timeout timeout)

    def buildWithLogs(InputStream buildContext, query, Timeout timeout, Map<String, String> buildOptions)

    def build(BuildConfig config)

    def build(InputStream buildContext)

    def build(InputStream buildContext, query)

    def build(InputStream buildContext, query, DockerAsyncCallback callback)

    def build(InputStream buildContext, query, DockerAsyncCallback callback, Map<String, String> buildOptions)

//    history     Show the history of an image

    EngineResponse history(image)

//    import      Import the contents from a tarball to create a filesystem image

    def importUrl(url)

    def importUrl(url, repository)

    def importUrl(url, repository, tag)

    def importStream(stream)

    def importStream(stream, repository)

    def importStream(stream, repository, tag)

//    inspect     Display detailed information on one or more images

    EngineResponse inspectImage(image)

//    load        Load an image from a tar archive or STDIN

    EngineResponse load(stream)

//    ls          List images

    EngineResponse images()

    EngineResponse images(query)

//    prune       Remove unused images

    EngineResponse pruneImages()

    EngineResponse pruneImages(query)

//    create      Create an image by either pulling it from a registry or importing it

    EngineResponse create(Map query)

    EngineResponse create(Map query, Map createOptions)

//    pull        Pull an image or a repository from a registry

    /**
     * @deprecated please use #create(query, createOptions)
     * @see #create(Map, Map)
     */
    @Deprecated
    String pull(image)

    /**
     * @deprecated please use #create(query, createOptions)
     * @see #create(Map, Map)
     */
    @Deprecated
    String pull(image, String tag)

    /**
     * @deprecated please use #create(query, createOptions)
     * @see #create(Map, Map)
     */
    @Deprecated
    String pull(image, String tag, String authBase64Encoded)

    /**
     * @deprecated please use #create(query, createOptions)
     * @see #create(Map, Map)
     */
    @Deprecated
    String pull(image, String tag, String authBase64Encoded, String registry)

//    push        Push an image or a repository to a registry

    EngineResponse push(String image)

    EngineResponse push(String image, String authBase64Encoded)

    EngineResponse push(String image, String authBase64Encoded, String registry)

//    rm          Remove one or more images

    EngineResponse rmi(image)

//    save        Save one or more images to a tar archive (streamed to STDOUT by default)

    EngineResponse save(... images)

//    tag         Create a tag TARGET_IMAGE that refers to SOURCE_IMAGE

    EngineResponse tag(image, repository)
}
