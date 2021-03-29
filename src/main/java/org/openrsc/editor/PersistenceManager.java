package org.openrsc.editor;

import com.thoughtworks.xstream.XStream;
import org.openrsc.editor.data.GameObjectLoc;
import org.openrsc.editor.data.ItemLoc;
import org.openrsc.editor.data.NpcLoc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PersistenceManager {
    private static final XStream xstream = new XStream();

    static {
        addAlias("GameObjectLoc", GameObjectLoc.class);
        addAlias("NPCLoc", NpcLoc.class);
        addAlias("ItemLoc", ItemLoc.class);
    }

    private static void addAlias(String name, Class<?> type) {
        xstream.alias(name, type);
    }

    public static Object load(InputStream stream) {
        try {
            InputStream is = new GZIPInputStream(stream);
            return xstream.fromXML(is);
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        return null;
    }

    public static void write(File file, Object o) {
        try {
            OutputStream os = new GZIPOutputStream(new FileOutputStream(file));
            xstream.toXML(o, os);
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }
}
