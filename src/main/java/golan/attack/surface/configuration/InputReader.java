package golan.attack.surface.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class InputReader {
    private final ServiceInputConfiguration serviceInputConfiguration;
    private final AbsolutePathInputReader absolutePathInputReader;
    private final ClassPathInputReader classPathInputReader;

    public Input read() throws IOException {
        final String fileName = this.serviceInputConfiguration.getFileName();

        Input input;
        input = absolutePathInputReader.read();
        if (input != null) return input;
        input = classPathInputReader.read();
        if (input != null) return input;
        throw new FileNotFoundException("Cannot find a file to load configuration (" + fileName + ")");
    }


}
