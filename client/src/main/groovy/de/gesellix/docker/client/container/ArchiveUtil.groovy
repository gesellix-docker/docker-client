package de.gesellix.docker.client.container

import de.gesellix.util.IOUtils
import okio.Okio
import okio.Sink
import okio.Source
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ArchiveUtil {

  private final Logger log = LoggerFactory.getLogger(ArchiveUtil)

  byte[] extractSingleTarEntry(InputStream tarContent, String filename) {
    TarArchiveInputStream stream = new TarArchiveInputStream(new BufferedInputStream(tarContent))

    TarArchiveEntry entry = stream.nextTarEntry
    log.debug("entry size: ${entry.size}")

    String entryName = entry.name
    if (!filename.endsWith(entryName)) {
      log.warn("entry name '${entryName}' doesn't match expected filename '${filename}'")
    }
    else {
      log.debug("entry name: ${entryName}")
    }

    byte[] content = new byte[(int) entry.size]
    log.debug("going to read ${content.length} bytes")

    stream.read(content, 0, content.length)
    IOUtils.closeQuietly(Okio.source(stream))

    return content
  }

  int copySingleTarEntry(InputStream tarContent, String filename, OutputStream target) {
    TarArchiveInputStream stream = new TarArchiveInputStream(new BufferedInputStream(tarContent))

    TarArchiveEntry entry = stream.nextTarEntry
    log.debug("entry size: ${entry.size}")

    String entryName = entry.name
    if (!filename.endsWith(entryName)) {
      log.warn("entry name '${entryName}' doesn't match expected filename '${filename}'")
    }
    else {
      log.debug("entry name: ${entryName}")
    }

    byte[] content = new byte[(int) entry.size]
    log.debug("going to read ${content.length} bytes")

    Source source = Okio.source(stream)
    Sink sink = Okio.sink(target)
    IOUtils.copy(source, sink)
    sink.flush()
    sink.close()
    return entry.size
  }
}
