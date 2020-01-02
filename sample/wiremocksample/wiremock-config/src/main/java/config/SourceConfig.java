package config;

import lombok.Data;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Data
public class SourceConfig {

    public static final String ADDITIONAL_PROPERTY_FILE = "additionalProperties";
    public static final String ADDITIONAL_PROPERTY_FILE_ENV = "RATPACK_MODULES_ADDITIONAL_PROPERTIES";
    private String systemConfigPrefixFilter = "apicfg.";

    private String environmentConfigPrefixFilter = "APICFG__";

    private List<String> classpathConfigFilenamesWithExtension = Collections.singletonList("application.yml");

    private String externalConfigFilenameWithExtension = "application.yml";

    private String externalConfigFilePath = Paths.get(".").toAbsolutePath().normalize().toString();

    public Path getExternalConfigFullPath() {
        return Paths.get(externalConfigFilePath, externalConfigFilenameWithExtension);
    }
}
