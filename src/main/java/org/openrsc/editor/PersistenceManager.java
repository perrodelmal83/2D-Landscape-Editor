package org.openrsc.editor;

import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PersistenceManager {
    private static final XStream xstream = new XStream();

    static {
        addAlias("GameObjectLoc", "org.openrsc.editor.data.GameObjectLoc");
        addAlias("NPCLoc", "org.openrsc.editor.data.NpcLoc");
        addAlias("ItemLoc", "org.openrsc.editor.data.ItemLoc");
    }

    private static void addAlias(String name, String className) {
        try {
            xstream.alias(name, Class.forName(className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Object load(File file) {
        try {
            InputStream is = new GZIPInputStream(new FileInputStream(file));
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
