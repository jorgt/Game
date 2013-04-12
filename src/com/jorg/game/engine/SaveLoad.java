/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine;

import com.jorg.game.objects.Map;
import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 *
 * @author jthuijls
 */
public abstract class SaveLoad implements Serializable {

    public static String msg;
    private static String path;

    private static void setPath() throws UnsupportedEncodingException {
        String s = SaveLoad.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        s = URLDecoder.decode(s, "UTF-8");
        path = s.substring(0, s.lastIndexOf("/") + 1);
    }

    public static boolean save(Map map) {
        FileOutputStream fOut;
        ObjectOutputStream objOut;
        Savegame save = new Savegame(map);
        msg = "game saved";
        try {
            setPath();
            fOut = new FileOutputStream(path + save.getSaveName() + ".jorg");
            objOut = new ObjectOutputStream(fOut);
            objOut.writeObject(save);
            objOut.flush();
            objOut.close();
            fOut.close();
        } catch (FileNotFoundException ex) {
            msg = "Saving: Cannot find file";
            System.err.println(ex);
            return false;
        } catch (IOException ex) {
            msg = "Saving: Cannot read file";
            System.err.println(ex);
            return false;
        } finally {
            System.err.println(msg);
            return true;
        }
    }

    public static ArrayList<Object> load(String file) {
        FileInputStream fIn;
        ObjectInputStream objIn;
        Savegame load = null;
        msg = "game loaded";
        try {
            setPath();
            fIn = new FileInputStream(path+file);
            objIn = new ObjectInputStream(fIn);
            load = (Savegame) objIn.readObject();
            fIn.close();
            objIn.close();
        } catch (FileNotFoundException ex) {
            msg = "Loading: file not found";
            System.err.println(ex);
        } catch (IOException ex) {
            msg = "Loading: File not read";
            System.err.println(ex);
        } catch (ClassNotFoundException ex) {
            msg = "Loading: No such class";
            System.err.println(ex);
        } finally {
            System.err.println(msg);
            return load.getObjects();
        }
    }

    public static String[] getList() {
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jorg");
            }
        };
        String[] list = null;
        try {
            setPath();
            File dir = new File(path);
            list = dir.list(filter);
        } catch (IOException ex) {
            System.err.println(ex);
            return null;
        } finally {
            return list;
        }

    }
}
