package core;

import core.module.OperationModule;
import core.module.OperationModule.VGModule;

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

class ModuleLoader {

	static final String MODULES_PACKAGE = "modules";

	static List<OperationModule> loadModules() {
		List<Class> classes = getClasses(MODULES_PACKAGE);
		List<OperationModule> modules = new LinkedList<OperationModule>();
		for(Class c : classes)
			if(check(c))
				try {
					Object o = c.newInstance();
					modules.add((OperationModule) o);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				return modules;
	}

	private static boolean check(Class c) {
		if(OperationModule.class.isAssignableFrom(c) &&
				c.isAnnotationPresent(VGModule.class)) {
			System.out.println("MODULE LOADED: " + c.getCanonicalName());
			return true;
		}
		return false;
	}

	/**
	 * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
	 *
	 * @param packageName The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static List<Class> getClasses(String packageName) {
		ArrayList<Class> classes = new ArrayList<Class>();

		File dir = new File("bin/" + MODULES_PACKAGE);
		System.out.println(dir.getAbsolutePath());
		File[] subdirs = dir.listFiles();

		for (File directory : subdirs) {
			classes.addAll(findClasses(directory, packageName + "." + directory.getName()));
		}
		return classes;
	}

	/**
	 * Recursive method used to find all classes in a given directory and subdirs.
	 *
	 * @param directory   The base directory
	 * @param packageName The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class> findClasses(File directory, String packageName) {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				try {
					ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
					String name = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
					System.out.println(name);
					Class c = classLoader.loadClass(name);
					classes.add(c);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return classes;
	}
}
