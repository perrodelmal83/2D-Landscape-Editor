package org.openrsc.editor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class PersistenceManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> List<T> load(InputStream stream, Class<T> type) {
        try {
            BufferedInputStream is = new BufferedInputStream(stream);
            return objectMapper.readValue(
                    is,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, type)
            );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static void write(File file, Object o) {
        try {
            //TODO: Persist files
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
