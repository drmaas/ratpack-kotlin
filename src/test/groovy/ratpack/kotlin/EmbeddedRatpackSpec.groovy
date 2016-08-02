package ratpack.kotlin

import io.netty.util.CharsetUtil
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import ratpack.http.client.RequestSpec
import ratpack.test.embed.EmbeddedApp
import ratpack.test.http.TestHttpClient
import spock.lang.Specification

import java.nio.charset.Charset

import static ratpack.test.http.TestHttpClient.testHttpClient

abstract class EmbeddedRatpackSpec extends Specification {

  @Rule
  TemporaryFolder temporaryFolder

  @Delegate
  TestHttpClient client

  abstract EmbeddedApp getApplication()

  void configureRequest(RequestSpec requestSpecification) {
    // do nothing
  }

  def setup() {
    client = testHttpClient({ application.address }) {
      configureRequest(it)
    }
  }

  def cleanup() {
    try {
      application.server.stop()
    } catch (Throwable ignore) {

    }
  }

  String rawResponse(Charset charset = CharsetUtil.UTF_8) {
    StringBuilder builder = new StringBuilder()
    Socket socket = new Socket(application.address.host, application.address.port)
    try {
      new OutputStreamWriter(socket.outputStream, "UTF-8").with {
        write("GET / HTTP/1.1\r\n")
        write("Connection: close\r\n")
        write("\r\n")
        flush()
      }

      InputStreamReader inputStreamReader = new InputStreamReader(socket.inputStream, charset)
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader)

      def chunk
      while ((chunk = bufferedReader.readLine()) != null) {
        builder.append(chunk).append("\n")
      }

      builder.toString()
    } finally {
      socket.close()
    }
  }

}
