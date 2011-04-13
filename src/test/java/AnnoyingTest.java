import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Show the annoyances.
 *
 * User: sam
 * Date: 4/13/11
 * Time: 11:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class AnnoyingTest {

  @Test
  public void mapfail() throws IOException {
    Record record = new Record();
    record.stringfield = "testvalue";
    record.mapfield = new HashMap<CharSequence, CharSequence>() {{
      put("testkey", "testvalue");
    }};
    record.arrayfield = new ArrayList<CharSequence>() {{
      add("testvalue");
    }};

    EncoderFactory ef = new EncoderFactory();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    BinaryEncoder be = ef.binaryEncoder(baos, null);
    SpecificDatumWriter<Record> sdw = new SpecificDatumWriter<Record>(Record.class);
    sdw.write(record, be);
    be.flush();

    DecoderFactory df = new DecoderFactory();
    SpecificDatumReader<Record> sdr = new SpecificDatumReader<Record>(Record.class);
    BinaryDecoder bd = df.binaryDecoder(baos.toByteArray(), null);
    record = sdr.read(record, bd);

    System.out.println(record.stringfield.equals("testvalue"));
    System.out.println(record.arrayfield.contains("testvalue"));
    System.out.println("testvalue".equals(record.mapfield.get("testkey")));
  }
}
