package fr.linkit.tests.mindustry;

import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JarRunner {

    private final static Path jarPath = Paths.get("C:\\Users\\maxim\\IdeaProjects\\Mindustry\\desktop\\build\\libs\\Mindustry.jar");

    public static void main(String[] args) throws Exception {
        URLClassLoader loader = new URLClassLoader(new java.net.URL[]{jarPath.toUri().toURL()});
        Class<?> clazz = loader.loadClass("mindustry.desktop.DesktopLauncher");
        Method method = clazz.getDeclaredMethod("main", String[].class);
        method.invoke(null, (Object) new String[0]);
    }

}
