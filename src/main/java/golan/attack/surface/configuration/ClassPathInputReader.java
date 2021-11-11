package golan.attack.surface.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
class ClassPathInputReader extends AbsInputReader {
    public ClassPathInputReader(ServiceInputConfiguration serviceInputConfiguration) {
        super(serviceInputConfiguration);
    }

    @Override
    public Input read() throws IOException {
        final String fileName = getFileName();
        final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) return null;
        log.info("Configuration file: {}", this.getClass().getClassLoader().getResource(fileName));
        return parseJson(inputStream);
    }
}
