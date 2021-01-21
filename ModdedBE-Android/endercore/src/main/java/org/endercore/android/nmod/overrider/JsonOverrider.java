package org.endercore.android.nmod.overrider;

import org.endercore.android.utils.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public final class JsonOverrider extends BaseOverrider {
    public JsonOverrider(File overridePath) {
        super(overridePath);
    }

    @Override
    public void performOverride(File root, String name, String mode) throws IOException, JSONException {
        if (mode.equals("merge")) {
            String json1 = FileUtils.readFileAsString(new File(root, name));
            String json2 = FileUtils.readFileAsString(new File(overridePath, name));
            FileUtils.copy(new ByteArrayInputStream(new JSONMerger(json1, json2).merge().getBytes()), new File(overridePath, name));
        } else if (mode.equals("replace")) {
            FileUtils.copy(new File(root, name), new File(overridePath, name));
        }
    }

    private static class JSONMerger {
        private final String sourceJson;
        private final String dstJson;

        JSONMerger(String src1, String src2) {
            this.sourceJson = src1;
            this.dstJson = src2;
        }

        String merge() throws JSONException {
            if (isJSONObject(sourceJson) && isJSONObject(dstJson)) {
                return mergeJSONObjects(new JSONObject(sourceJson), new JSONObject(dstJson)).toString();
            } else if (isJSONArray(sourceJson) && isJSONArray(dstJson)) {
                return mergeJSONArrays(new JSONArray(sourceJson), new JSONArray(dstJson)).toString();
            } else
                throw new JSONException("Failed to merge json: Cannot merge a json array with a json object.");
        }

        private static boolean isJSONArray(String json) {
            try {
                new JSONArray(json);
                return true;
            } catch (JSONException ignored) {
                return false;
            }
        }

        private static boolean isJSONObject(String json) {
            try {
                new JSONObject(json);
                return true;
            } catch (JSONException ignored) {
                return false;
            }
        }

        private static JSONObject mergeJSONObjects(JSONObject object1, JSONObject object2) throws JSONException {
            Iterator<String> iterator = object2.keys();
            while (iterator.hasNext()) {
                String name = iterator.next();
                mergeJSONObjectItem(object1, object2, name);
            }
            return object1;
        }

        private static JSONArray mergeJSONArrays(JSONArray array1, JSONArray array2) throws JSONException {
            for (int index = 0; index < array2.length(); ++index) {
                mergeJSONArrayItem(array1, array2, index);
            }
            return array1;
        }


        private static void mergeJSONArrayItem(JSONArray array1, JSONArray array2, int index) throws JSONException {
            if (isJSONArray(array2, index)) {
                array1.put(array2.getJSONArray(index));
            } else if (isJSONObject(array2, index)) {
                array1.put(array2.getJSONObject(index));
            } else if (isJSONString(array2, index)) {
                array1.put(array2.getString(index));
            } else if (isJSONInteger(array2, index)) {
                array1.put(array2.getInt(index));
            } else
                throw new JSONException("Failed to merge json: Found two different element types with the same tag.");
        }

        private static void mergeJSONObjectItem(JSONObject object1, JSONObject object2, String key) throws JSONException {
            if (isJSONArray(object2, key)) {
                if (object1.has(key)) {
                    object1.put(key, mergeJSONArrays(object1.getJSONArray(key), object2.getJSONArray(key)));
                } else {
                    object1.put(key, object2.getJSONArray(key));
                }
            } else if (isJSONObject(object2, key)) {
                if (object1.has(key)) {
                    object1.put(key, mergeJSONObjects(object1.getJSONObject(key), object2.getJSONObject(key)));
                } else {
                    object1.put(key, object2.getJSONObject(key));
                }
            } else if (isJSONString(object2, key)) {
                object1.put(key, object2.getString(key));
            } else if (isJSONInteger(object2, key)) {
                object1.put(key, object2.getInt(key));
            } else
                throw new JSONException("Failed to merge json: Found two different element types with the same tag.");
        }

        private static boolean isJSONArray(JSONObject src, String key) {
            try {
                src.getJSONArray(key);
                return true;
            } catch (JSONException jsonE) {
                return false;
            }
        }

        private static boolean isJSONObject(JSONObject src, String key) {
            try {
                src.getJSONObject(key);
                return true;
            } catch (JSONException jsonE) {
                return false;
            }
        }

        private static boolean isJSONString(JSONObject src, String key) {
            try {
                src.getString(key);
                return true;
            } catch (JSONException jsonE) {
                return false;
            }
        }

        private static boolean isJSONInteger(JSONObject src, String key) {
            try {
                src.getInt(key);
                return true;
            } catch (JSONException jsonE) {
                return false;
            }
        }

        private static boolean isJSONArray(JSONArray src, int index) {
            try {
                src.getJSONArray(index);
                return true;
            } catch (JSONException jsonE) {
                return false;
            }
        }

        private static boolean isJSONObject(JSONArray src, int index) {
            try {
                src.getJSONObject(index);
                return true;
            } catch (JSONException jsonE) {
                return false;
            }
        }

        private static boolean isJSONString(JSONArray src, int index) {
            try {
                src.getString(index);
                return true;
            } catch (JSONException jsonE) {
                return false;
            }
        }

        private static boolean isJSONInteger(JSONArray src, int index) {
            try {
                src.getInt(index);
                return true;
            } catch (JSONException jsonE) {
                return false;
            }
        }
    }
}
