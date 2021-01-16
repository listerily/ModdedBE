package net.listerily.endercore.android.utils;

import android.content.res.AssetManager;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class Patcher
{
    public static void patchDexFile(ClassLoader classLoader, String dexFilePath, String dexOptFilePath) throws NoSuchFieldException, IllegalAccessException {
        Field field1 = BaseDexClassLoader.class.getDeclaredField("pathList");
        field1.setAccessible(true);
        Object dexPathList = field1.get(classLoader);
        Field field2 = dexPathList.getClass().getDeclaredField("dexElements");
        field2.setAccessible(true);
        Object dexElements = field2.get(dexPathList);

        DexClassLoader dcl = new DexClassLoader(dexFilePath, dexOptFilePath, null, classLoader);
        Object patchDexPathList  = field1.get(dcl);
        Object patchDexElements = field2.get(patchDexPathList);
        Object concatArray;

        int len1 = Array.getLength(patchDexElements);
        int len2 = Array.getLength(dexElements);
        int totalLen = len1 + len2;
        concatArray = Array.newInstance(patchDexElements.getClass().getComponentType(), totalLen);
        for(int i = 0; i < len1; i++) {
            Array.set(concatArray, i, Array.get(patchDexElements, i));
        }
        for(int j = 0; j < len2; j++) {
            Array.set(concatArray, len1 + j, Array.get(dexElements, j));
        }
        field2.set(dexPathList,concatArray);
    }

    public static void patchNativeLibraryDir(ClassLoader classLoader,String nativeLibraryPath) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
            PathClassLoader pathClassLoader = (PathClassLoader) classLoader;
            Class<?> classBaseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
            Field fieldPathList = classBaseDexClassLoader.getDeclaredField("pathList");
            fieldPathList.setAccessible(true);
            Object pathList = fieldPathList.get(pathClassLoader);

            Class<?> nativeLibraryElementClass = Class.forName("dalvik.system.DexPathList$NativeLibraryElement");
            Constructor<?> element = null;
            element = nativeLibraryElementClass.getConstructor(File.class);
            Object systemNativeLibraryDirectories = pathList.getClass()
                    .getDeclaredField("systemNativeLibraryDirectories");
            Object nativeLibraryDirectories = pathList.getClass().getDeclaredField("nativeLibraryDirectories");
            Object nativeLibraryPathElements = pathList.getClass().getDeclaredField("nativeLibraryPathElements");
            ((Field) systemNativeLibraryDirectories).setAccessible(true);
            ((Field) nativeLibraryDirectories).setAccessible(true);
            ((Field) nativeLibraryPathElements).setAccessible(true);
            List<File> systemFiles = (List<File>) ((Field) systemNativeLibraryDirectories).get(pathList);
            List<File> nativeFiles = (List<File>) ((Field) nativeLibraryDirectories).get(pathList);
            Object[] elementFiles = (Object[]) ((Field) nativeLibraryPathElements).get(pathList);
            Object newElementFiles = Array.newInstance(nativeLibraryElementClass, elementFiles.length + 1);

            systemFiles.add(new File(nativeLibraryPath));
            nativeFiles.add(new File(nativeLibraryPath));

            ((Field) systemNativeLibraryDirectories).set(pathList, systemFiles);
            ((Field) nativeLibraryDirectories).set(pathList, nativeFiles);
            if (element != null) {
                element.setAccessible(true);
                Object newInstance = element.newInstance(new File(nativeLibraryPath));
                Array.set(newElementFiles, 0, newInstance);
                for (int i = 1; i < elementFiles.length + 1; i++) {
                    Array.set(newElementFiles, i, elementFiles[i - 1]);
                }
                ((Field) nativeLibraryPathElements).set(pathList, newElementFiles);
            }
    }

    public static void patchAssetsFile(AssetManager assetManager, String filePath) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method addAssetPath = AssetManager.class.getMethod("addAssetPath",String.class);
        addAssetPath.invoke(assetManager,filePath);
    }

    public static AssetManager createNewAssetManager() throws InstantiationException, IllegalAccessException {
        return AssetManager.class.newInstance();
    }

    public static void patchAssetsFile(AssetManager assetManager, ArrayList<String> filePaths) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        int size = filePaths.size();
        for(int i = 0;i < size;++i)
        {
            patchAssetsFile(assetManager,filePaths.get(i));
        }
    }
}