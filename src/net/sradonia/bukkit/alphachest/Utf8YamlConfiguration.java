package net.sradonia.bukkit.alphachest;

import org.bukkit.configuration.file.*;
import org.apache.commons.lang.*;
import java.nio.charset.*;
import org.bukkit.configuration.*;
import com.google.common.io.*;
import java.io.*;

public class Utf8YamlConfiguration extends YamlConfiguration
{
    public void load(final InputStream stream) throws IOException, InvalidConfigurationException {
        Validate.notNull((Object)stream, "Stream cannot be null");
        final InputStreamReader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
        final StringBuilder builder = new StringBuilder();
        final BufferedReader input = new BufferedReader(reader);
        try {
            String line;
            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        }
        finally {
            input.close();
        }
        input.close();
        input.close();
        input.close();
        this.loadFromString(builder.toString());
    }
    
    public void save(final File file) throws IOException {
        Validate.notNull((Object)file, "File cannot be null");
        Files.createParentDirs(file);
        final String data = this.saveToString();
        final FileOutputStream stream = new FileOutputStream(file);
        final OutputStreamWriter writer = new OutputStreamWriter(stream, Charset.forName("UTF-8"));
        try {
            writer.write(data);
        }
        finally {
            writer.close();
        }
        writer.close();
        writer.close();
        writer.close();
    }
}
