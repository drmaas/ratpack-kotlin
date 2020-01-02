package config;

import ratpack.func.Action;
import ratpack.server.ServerConfigBuilder;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class SampleServerConfigurer {
    private static final Action<Object> LOG_ACTION = (e) -> {
        log.warn("There was a problem reading a supplied configuration file", e);
    };
    private SampleServerConfigurer() {
    }


    public static ServerConfigBuilder config(ServerConfigBuilder configBuilder) {
        return config(configBuilder, new SourceConfig());
    }


    public static ServerConfigBuilder config(ServerConfigBuilder configBuilder, SourceConfig sourceConfig) {
        return config(configBuilder, sourceConfig, System.getenv());
    }


    public static ServerConfigBuilder config(ServerConfigBuilder configBuilder, SourceConfig sourceConfig, Map<String, String> env) {
        ServerConfigBuilder cb = configBuilder.onError(LOG_ACTION);

        sourceConfig.getClasspathConfigFilenamesWithExtension().forEach((c) -> {
            URL resource = ClassLoader.getSystemResource(c);
            if (resource != null) {
                if (isYaml(c)) {
                    cb.yaml(resource);
                } else if (isProperties(c)) {
                    cb.props(resource);
                }
            } else {
                log.info(String.format("Classpath configuration file %s not found. Skipping as source.", c));
            }
        });

        if (Files.exists(sourceConfig.getExternalConfigFullPath())) {
            if (isYaml(sourceConfig.getExternalConfigFilenameWithExtension())) {
                cb.yaml(sourceConfig.getExternalConfigFullPath());
            } else if (isProperties(sourceConfig.getExternalConfigFilenameWithExtension())) {
                cb.props(sourceConfig.getExternalConfigFullPath());
            }
        } else {
            log.info(String.format("External configuration file %s not found. Skipping as source.",
                    sourceConfig.getExternalConfigFilenameWithExtension()));
        }

        /*
         * A RATPACK_MODULES_ADDITIONAL_PROPERTIES env variable or -DadditionalProperties system property
         * specifying a full path to a config file, with preference to the env variable.
         * This config file will override the classpath and external configuration properties.
         * Show primarily be used for development purposes so you don't have to override them in sysProps or env
         */
        String additionalPropertiesFile = env.get(SourceConfig.ADDITIONAL_PROPERTY_FILE_ENV);
        if (additionalPropertiesFile == null || additionalPropertiesFile.isEmpty()) {
            additionalPropertiesFile = System.getProperty(SourceConfig.ADDITIONAL_PROPERTY_FILE);
        }
        if (additionalPropertiesFile != null && !additionalPropertiesFile.isEmpty()) {

            Path additionalPropertiesPath = Paths.get(additionalPropertiesFile);
            if (Files.exists(additionalPropertiesPath)) {
                if (isYaml(additionalPropertiesFile)) {
                    cb.yaml(additionalPropertiesPath);
                } else if (isProperties(additionalPropertiesFile)) {
                    cb.props(additionalPropertiesPath);
                }
            } else {
                log.info(String.format("Additional properties file %s not found. Skipping as source.",
                        additionalPropertiesFile));
            }
        }

        cb.sysProps(sourceConfig.getSystemConfigPrefixFilter()).env(sourceConfig.getEnvironmentConfigPrefixFilter());

        return cb;
    }

    private static boolean isYaml(String filenameWithExtension) {
        String fileExtension = com.google.common.io.Files.getFileExtension(filenameWithExtension);
        return fileExtension.equalsIgnoreCase("yml") || fileExtension.equalsIgnoreCase("yaml");
    }

    private static boolean isProperties(String filenameWithExtension) {
        String fileExtension = com.google.common.io.Files.getFileExtension(filenameWithExtension);
        return fileExtension.equalsIgnoreCase("properties");
    }
}
