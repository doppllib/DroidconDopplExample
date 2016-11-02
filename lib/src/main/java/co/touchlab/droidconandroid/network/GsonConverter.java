package co.touchlab.droidconandroid.network;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import co.touchlab.droidconandroid.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by kgalligan on 7/20/14.
 */
public class GsonConverter implements Converter
{
  private final Gson gson;
  private String encoding;

  /**
   * Create an instance using the supplied {@link Gson} object for conversion. Encoding to JSON and
   * decoding from JSON (when no charset is specified by a header) will use UTF-8.
   */
  public GsonConverter(Gson gson) {
    this(gson, "UTF-8");
  }

  /**
   * Create an instance using the supplied {@link Gson} object for conversion. Encoding to JSON and
   * decoding from JSON (when no charset is specified by a header) will use the specified encoding.
   */
  public GsonConverter(Gson gson, String encoding) {
    this.gson = gson;
    this.encoding = encoding;
  }

  @Override public Object fromBody(TypedInput body, Type type) throws ConversionException
  {
    String charset = "UTF-8";
    if (body.mimeType() != null) {
      charset = MimeUtil.parseCharset(body.mimeType());
    }
    InputStreamReader isr = null;
    try {
        InputStream in = body.in();
        byte[] bytes = IOUtils.toByteArray(in);

        isr = new InputStreamReader(new ByteArrayInputStream(bytes), charset);
      return gson.fromJson(isr, type);
    } catch (IOException e) {
      throw new ConversionException(e);
    } catch (JsonParseException e) {
      throw new ConversionException(e);
    } finally {
      if (isr != null) {
        try {
          isr.close();
        } catch (IOException ignored) {
        }
      }
    }
  }

  @Override public TypedOutput toBody(Object object) {
    try {
      return new JsonTypedOutput(gson.toJson(object).getBytes(encoding), encoding);
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError(e);
    }
  }

  private static class JsonTypedOutput implements TypedOutput {
    private final byte[] jsonBytes;
    private final String mimeType;

    JsonTypedOutput(byte[] jsonBytes, String encode) {
      this.jsonBytes = jsonBytes;
      this.mimeType = "application/json; charset=" + encode;
    }

    @Override public String fileName() {
      return null;
    }

    @Override public String mimeType() {
      return mimeType;
    }

    @Override public long length() {
      return jsonBytes.length;
    }

    @Override public void writeTo(OutputStream out) throws IOException {
      out.write(jsonBytes);
    }
  }
}
