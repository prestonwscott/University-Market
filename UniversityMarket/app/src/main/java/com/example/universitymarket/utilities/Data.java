package com.example.universitymarket.utilities;

import com.example.universitymarket.objects.User;
import com.example.universitymarket.globals.actives.ActiveUser;
/**
 * <b>
 * ANY MODIFICATIONS ABOVE THESE JAVADOC BOUNDS WILL BE OVERWRITTEN ON BUILD 
 * <div>
 * DO NOT REMOVE THIS 
 * </b><p>
 * Autogenerated on: Mar 12, 2024, 7:27:52 PM
*/
import android.app.Activity;
import android.util.Log;
import androidx.annotation.NonNull;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.initialization.qual.Initialized;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class Data {
    public static void mergeHash(Map<String, Object> from, Map<String, Object> to) {
        from.forEach((key, value) -> to.merge(key, value, (oldValue, newValue) ->
                !oldValue.equals(newValue) ? oldValue : newValue));
    }

    private static boolean setCache(@NonNull Activity cur_act, String name, HashMap<String, Object> pojo, boolean clear) {
        return setCache(cur_act, name, pojoToJSON(pojo), clear);
    }

    private static boolean setCache(@NonNull Activity cur_act, String name, String json, boolean clear) {
        String path = cachePath(cur_act);

        if(name == null)
            return false;
        try {
            if(clear) {
                return getCachedFile(cur_act, name).delete();
            } else {
                FileWriter out = new FileWriter(path + "/" + name + ".json");
                out.write(json);
                out.close();
            }
        } catch(IOException e) {
            Log.e("setCache", e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean clearAllCache(@NonNull Activity cur_act) {
        boolean result = true;
        File[] files = getCachedFiles(cur_act);
        if(files == null)
            return false;

        for(File file : files) {
            result = file.delete();
        }
        return result;
    }

    public static File getCachedFile(@NonNull Activity cur_act, @NonNull String name) {
        return new File(cachePath(cur_act) + "/" + name + ".json");
    }

    public static File[] getCachedFiles(@NonNull Activity cur_act) {
        return new File(cachePath(cur_act)).listFiles();
    }

    public static String getCachedToJSON(@NonNull Activity cur_act, @NonNull String name) {
        return fileToJSON(getCachedFile(cur_act, name));
    }

    public static HashMap<String, Object> getCachedToPOJO(@NonNull Activity cur_act, @NonNull String name) {
        return fileToPOJO(getCachedFile(cur_act, name));
    }

    @NonNull
    public static String cachePath(Activity cur_act) {
        File cache_dir = new File(cur_act.getCacheDir().getAbsolutePath() + "/um_cache");
        if(cache_dir.mkdir())
            Log.d("cachePath", "um_cache directory created");
        return cache_dir.getAbsolutePath();
    }

    @NonNull
    public static String readData(@Initialized InputStream inp) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inp, StandardCharsets.UTF_8));
        StringBuilder data = new StringBuilder();

        String line = reader.readLine();

        while(line != null) {
            data.append(line).append("\n");
            line = reader.readLine();
        }

        inp.close();
        return data.toString();
    }

    @NonNull
    public static String resToJSON(@NonNull Activity cur_act, @NonNull int resourceID) {
        String result = "";
        try {
            InputStream inp = cur_act.getResources().openRawResource(resourceID);
            result = readData(inp);
        } catch (IOException e) {
            Log.e("resToJSON", e.getMessage());
        }
        return result;
    }

    @NonNull
    public static String fileToJSON(@NonNull File file) {
        String result = "";
        try {
            InputStream inp = new FileInputStream(file);
            result =  readData(inp);
        } catch (IOException e) {
            Log.e("fileToJSON", e.getMessage());
        }
        return result;
    }

    @NonNull
    public static HashMap<String, Object> resToPOJO(@NonNull Activity cur_act, int resourceID) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            InputStream inp = cur_act.getResources().openRawResource(resourceID);
            result = jsonToPOJO(readData(inp));
        } catch (IOException e) {
            Log.e("resToPOJO", e.getMessage());
        }
        return result;
    }

    @NonNull
    public static HashMap<String, Object> fileToPOJO(@NonNull File file) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            InputStream inp = new FileInputStream(file);
            result = jsonToPOJO(readData(inp));
        } catch (IOException e) {
            Log.e("fileToPOJO", e.getMessage());
        }
        return result;
    }

    @NonNull
    public static HashMap<String, Object> jsonToPOJO(@NonNull String json) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            result = (HashMap<String, Object>) new ObjectMapper().readValue(json.toString(), HashMap.class);
        } catch (Exception e) {
            Log.e("jsonToPOJO", e.getMessage());
        }
        return result;
    }

    @NonNull
    public static String pojoToJSON(@NonNull HashMap<String, Object> pojo) {
        String result = "";
        try {
            result = new ObjectMapper().writeValueAsString(pojo);
        } catch(JsonProcessingException e) {
            Log.e("pojoToJSON", e.getMessage());
        }
        return result;
    }

/**
 * <b>
 * ANY MODIFICATIONS BELOW THESE JAVADOC BOUNDS WILL BE OVERWRITTEN ON BUILD 
 * <div>
 * DO NOT REMOVE THIS 
 * </b><p>
 * Autogenerated on: Mar 12, 2024, 7:27:52 PM
*/


    public static void setActiveUser(@NonNull Activity cur_act, User userOBJ) {
        ActiveUser.about = userOBJ.getAbout();
        ActiveUser.id = userOBJ.getId();
        ActiveUser.interactions = userOBJ.getInteractions();
        ActiveUser.date_created = userOBJ.getDateCreated();
        ActiveUser.last_name = userOBJ.getLastName();
        ActiveUser.middle_name = userOBJ.getMiddleName();
        ActiveUser.first_name = userOBJ.getFirstName();
        ActiveUser.username = userOBJ.getUsername();
        ActiveUser.watch_ids = userOBJ.getWatchIds();
        ActiveUser.transact_ids = userOBJ.getTransactIds();
        ActiveUser.post_ids = userOBJ.getPostIds();

        Log.d("setActiveUser", "Success = " + setCache(cur_act, "ActiveUser", userOBJ, false));
    }

    public static HashMap<String, Object> activeUserToPOJO() {
        return new User(ActiveUser.date_created, ActiveUser.last_name, ActiveUser.middle_name, ActiveUser.first_name, ActiveUser.username, ActiveUser.id, ActiveUser.watch_ids, ActiveUser.transact_ids, ActiveUser.post_ids );
    }
}