package golan.attack.surface.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public abstract class AbsInputReader {
    private final ServiceInputConfiguration serviceInputConfiguration;

    public abstract Input read() throws IOException;

    String getFileName() {
        return serviceInputConfiguration.getFileName();
    }

    Input parseJson(InputStream inputStream) throws IOException {
        return new ObjectMapper().readValue(inputStream, Input.class);
    }

}
