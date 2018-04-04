package com.wuai.company;


import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 扫描配置类
 */
public class Scanner {

    private static final String SCANNER_CLASS_KEY = "scanner:class:key";
    private static final String SCANNER_FILE_KEY = "scanner:file:key";
    private static final String SUFFIX_NAME = ".java";
    private static final String JAR_SAVE_FILE = System.getProperty("java.io.tmpdir") + File.separatorChar + "scanner" + File.separatorChar;

    static {
        File file = new File(JAR_SAVE_FILE);
        if (!file.exists()) {
            file.mkdirs();
        }

    }


    /**
     * 生成文档
     *
     * @throws IOException
     */
    public static void getDoc() throws IOException {


        Set<Class<?>> allAnnotations = new HashSet<>();
        allAnnotations.add(RestController.class);
        List<File> files = scannerFile("com.wuai.company", allAnnotations);
        if (files.isEmpty()) {
            return;
        }
        StringBuilder command = new StringBuilder();
        for (File file : files) {
            command.append(file.getAbsolutePath());
            command.append("  ");
        }

        String start = "";

        if (System.getProperty("os.name").equalsIgnoreCase("windows")) {
            start = "cmd /k ";
        }

        Runtime.getRuntime().exec(start + " javadoc -d " + Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1).replaceAll("build/classes/main", "src/main/resources") + "static  " + command.toString() + " -windowtitle 吾爱开发文档 -encoding utf-8 -charset utf-8");

    }


    /**
     * 扫描类
     *
     * @param packageName    包路径
     * @param allAnnotations 需要扫描携带的注解类的包
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */

    private static List<File> scannerFile(String packageName, Set<Class<?>> allAnnotations) {
        try {
            return (List<File>) scanner(packageName, allAnnotations).get(SCANNER_FILE_KEY);
        } catch (Exception e) {
            throw new RuntimeException("解析文件类出错" + e);
        }
    }


    /**
     * 扫描类
     *
     * @param packageName    包路径
     * @param allAnnotations 需要扫描携带的注解类的包
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */

    private static Set<Class<?>> scannerClass(String packageName, Set<Class<?>> allAnnotations) {
        try {
            return (Set<Class<?>>) scanner(packageName, allAnnotations).get(SCANNER_CLASS_KEY);
        } catch (Exception e) {
            throw new RuntimeException("解析类包出错" + e);
        }

    }


    /**
     * 扫描包
     *
     * @param packageName    包路径
     * @param allAnnotations 需要扫描携带的注解类的包
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */

    private static Map<String, Object> scanner(String packageName, Set<Class<?>> allAnnotations) throws IOException, ClassNotFoundException {

        Map<String, Object> resultMap = new HashMap<>();

        Set<Class<?>> allClasses = new HashSet<>();

        List<File> allAccordFiles = new ArrayList<>();


        if (StringUtils.isEmpty(packageName)) {
            resultMap.put(SCANNER_CLASS_KEY, allClasses);
            return resultMap;
        }

        String scannerPackageName = packageName.replaceAll("/", ".");

        Enumeration<URL> resource = Thread.currentThread().getContextClassLoader().getResources(packageName.replaceAll("\\.", "/"));
        URL url;
        String protocol;


        while (resource.hasMoreElements()) {
            url = resource.nextElement();
            if (url == null) {
                continue;
            }
            protocol = url.getProtocol();
            if (protocol.equals("file")) {
                parseClasses(scannerPackageName, url.getFile().substring(1), allClasses, allAccordFiles, allAnnotations);
            } else if (protocol.equals("jar")) {
                parseJarClasses((JarURLConnection) url.openConnection(), allClasses, allAccordFiles, allAnnotations);
            }


        }
        resultMap.put(SCANNER_CLASS_KEY, allClasses);
        resultMap.put(SCANNER_FILE_KEY, allAccordFiles);

        return resultMap;

    }


    /**
     * 解析jar的classes
     *
     * @param jarURLConnection      jar的连接池
     * @param originalClasses       原始的classes类
     * @param originalAccordFiles   原始的符合的类
     * @param parseSpecialClassType 解析指定的类型
     * @return
     * @throws ClassNotFoundException
     */
    private static Set<Class<?>> parseJarClasses(JarURLConnection jarURLConnection, Set<Class<?>> originalClasses, List<File> originalAccordFiles, Set<Class<?>> parseSpecialClassType) throws ClassNotFoundException, IOException {

        Set<Class<?>> _allClasses;

        List<File> _allAccordFiles;


        if (originalClasses != null) {
            _allClasses = originalClasses;
        } else {
            _allClasses = new HashSet<>();
        }

        if (originalAccordFiles != null) {
            _allAccordFiles = originalAccordFiles;
        } else {
            _allAccordFiles = new ArrayList<>();

        }


        if (jarURLConnection == null) {
            return _allClasses;
        }

        JarFile jarFile = jarURLConnection.getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();

        JarEntry jarEntry;
        String jarName;
        while (entries.hasMoreElements()) {
            jarEntry = entries.nextElement();
            if (jarEntry == null) {
                continue;
            }
            jarName = jarEntry.getName();
            if (jarName.endsWith(".class")) {


                int _index = jarName.lastIndexOf("/");


                String fileName = jarName.substring(_index + 1, jarName.lastIndexOf("."));


                if (addClasses(loadClasses(jarName.substring(0, _index).replaceAll("/", "."), fileName),
                        _allClasses, parseSpecialClassType)) {

                    Files.copy(jarFile.getInputStream(jarEntry), Paths.get(JAR_SAVE_FILE + fileName + SUFFIX_NAME), StandardCopyOption.REPLACE_EXISTING);
                    _allAccordFiles.add(new File(JAR_SAVE_FILE + fileName + SUFFIX_NAME));

                }


            }


        }

        return _allClasses;


    }


    /**
     * 解析classes
     *
     * @param scannerPackagerName   原始扫包路径
     * @param parsePackagerName     子解析的路径
     * @param originalClasses       原始的classes类
     * @param originalAccordFiles   原始的符合的类
     * @param parseSpecialClassType 解析指定的类型
     * @return
     * @throws ClassNotFoundException
     */
    private static Set<Class<?>> parseClasses(String scannerPackagerName, String parsePackagerName, Set<Class<?>> originalClasses, List<File> originalAccordFiles, Set<Class<?>> parseSpecialClassType) throws ClassNotFoundException {
        Set<Class<?>> _allClasses;

        if (originalClasses != null) {
            _allClasses = originalClasses;
        } else {
            _allClasses = new HashSet<>();
        }


        List<File> _allAccordFiles;


        if (originalAccordFiles != null) {
            _allAccordFiles = originalAccordFiles;
        } else {
            _allAccordFiles = new ArrayList<>();

        }


        if (StringUtils.isEmpty(parsePackagerName) || StringUtils.isEmpty(scannerPackagerName)) {
            return _allClasses;
        }
        File[] files = new File(parsePackagerName).listFiles(x -> x.isFile() && !x.getName().contains("$") && x.getName().endsWith("class") || x.isDirectory());

        for (File file : files) {
            if (file.isFile()) {
                if (addClasses(loadClasses(scannerPackagerName, file.getName().split("\\.")[0]), _allClasses, parseSpecialClassType)) {

                    _allAccordFiles.add(new File(file.getAbsolutePath().replaceAll("build\\\\classes\\\\main", "src\\\\main\\\\java").split("\\.")[0] + SUFFIX_NAME));


                }

            } else {

                parseClasses(scannerPackagerName + "." + file.getName().split("\\.")[0], file.getPath(), _allClasses, originalAccordFiles, parseSpecialClassType);
            }


        }


        return _allClasses;
    }


    /**
     * 加载class
     *
     * @param scannerPackageName 扫包的路径
     * @param fileName           包的名称
     * @return
     * @throws ClassNotFoundException
     */
    private static Class<?> loadClasses(String scannerPackageName, String fileName) throws ClassNotFoundException {
        return Class.forName(scannerPackageName + "." + fileName);
    }


    /**
     * 添加classes
     *
     * @param aClass                需要添加的classes
     * @param allClasses            需要添加的classes类集合
     * @param parseSpecialClassType 解析指定的类型
     * @param aClass
     */
    private static boolean addClasses(Class<?> aClass, Set<Class<?>> allClasses, Set<Class<?>> parseSpecialClassType) {

        boolean _addSuccess = false;

        if (parseSpecialClassType == null) {
            allClasses.add(aClass);
            _addSuccess = true;
        } else {
            Annotation annotation;
            for (Class cl : parseSpecialClassType) {
                annotation = aClass.getAnnotation(cl);

                if (annotation != null) {
                    allClasses.add(aClass);
                    _addSuccess = true;
                    break;
                }

            }
        }


        return _addSuccess;

    }

}