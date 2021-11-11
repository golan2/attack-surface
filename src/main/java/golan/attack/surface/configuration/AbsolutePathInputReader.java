package golan.attack.surface.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Component
class AbsolutePathInputReader extends AbsInputReader {
    public AbsolutePathInputReader(ServiceInputConfiguration serviceInputConfiguration) {
        super(serviceInputConfiguration);
    }

    public Input read() throws IOException {
        final String fileName = getFileName();
        final File f = new File(fileName);
        if (f.exists() && !f.isDirectory()) {
            log.info("Configuration file: {}", f.getAbsolutePath());
            final FileInputStream inputStream = new FileInputStream(f);
            return parseJson(inputStream);
        }
        return null;
    }
}
