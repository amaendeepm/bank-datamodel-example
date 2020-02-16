package dk.martincallesen.datamodel.event;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;

public class SpecificRecordDeserializer<T extends SpecificRecordBase> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecificRecordDeserializer.class);

    protected final Class<T> targetType;

    public SpecificRecordDeserializer(Class<T> targetType) {
        this.targetType = targetType;
    }

    @SuppressWarnings("unchecked")
    public T deserialize(String topic, byte[] data) throws IllegalAccessException, InstantiationException, IOException {
        T result = null;

        if (data != null) {
            LOGGER.debug("data='{}'", DatatypeConverter.printHexBinary(data));

            DatumReader<GenericRecord> datumReader =
                    new SpecificDatumReader<>(targetType.newInstance().getSchema());
            Decoder decoder = DecoderFactory.get().binaryDecoder(data, null);

            result = (T) datumReader.read(null, decoder);
            LOGGER.debug("deserialized data='{}'", result);
        }

        return result;
    }
}