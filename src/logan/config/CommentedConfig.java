package logan.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommentedConfig {
    private Map<String, String[]> commentToKeyMap = new HashMap<>();
    private YamlConfiguration configuration;
    private File file;

    /**
     * Create a commented config from an existing file.
     */
    public CommentedConfig(File file) {
        this.file = file;
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public void createKeyIfNoneExists(String key, Object defaultValue) {
        if (!configuration.isSet(key)) {
            configuration.set(key, defaultValue);
        }
    }

    /**
     * Add a comment to a configuration key.
     */
    public void addCommentToKey(String key, String... comment) {
        if (!configuration.isSet(key)) {
            System.out.println("No key with name " + key + " found. Skipping comments for this key.");
            return;
        }
        commentToKeyMap.put(key, comment);
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (String key : configuration.getKeys(true)) {
                Object value = configuration.get(key);
                String[] commentArray = commentToKeyMap.get(key);
                if (commentArray != null) {
                    for (String comment : commentArray) {
                        fileWriter.append("# ").append(comment).append(System.lineSeparator());
                    }
                }
                fileWriter.append(key)
                        .append(": ")
                        .append(String.valueOf(value))
                        .append(System.lineSeparator())
                        .append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
