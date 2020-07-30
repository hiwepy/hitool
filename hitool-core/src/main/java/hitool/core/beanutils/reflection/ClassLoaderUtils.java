package hitool.core.beanutils.reflection;

/**
 * This class is extremely useful for loading resources and classes in a fault tolerant manner
 * that works across different applications servers.
 * <p/>
 * It has come out of many months of frustrating use of multiple application servers at Atlassian,
 * please don't change things unless you're sure they're not going to break in one server or another!
 *
 */
public class ClassLoaderUtils {

    /**
     * Load a class with a given name.
     * <p/>
     * It will try to load the class in the following order:
     * <ul>
     * <li>From {@link Thread#getContextClassLoader() Thread.currentThread().getContextClassLoader()}
     * <li>Using the basic {@link Class#forName(java.lang.String) }
     * <li>From {@link Class#getClassLoader() ClassLoaderUtil.class.getClassLoader()}
     * <li>From the {@link Class#getClassLoader() callingClass.getClassLoader() }
     * </ul>
     *
     * @param className    The name of the class to load
     * @param callingClass The Class object of the calling object
     * @throws ClassNotFoundException If the class cannot be found anywhere.
     */
    public static Class loadClass(String className, Class callingClass) throws ClassNotFoundException {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException ex) {
                try {
                    return ClassLoaderUtils.class.getClassLoader().loadClass(className);
                } catch (ClassNotFoundException exc) {
                    return callingClass.getClassLoader().loadClass(className);
                }
            }
        }
    }

    /**
     * Prints the current classloader hierarchy - useful for debugging.
     */
    public static void printClassLoader() {
        System.out.println("ClassLoaderUtils.printClassLoader");
        printClassLoader(Thread.currentThread().getContextClassLoader());
    }

    /**
     * Prints the classloader hierarchy from a given classloader - useful for debugging.
     */
    public static void printClassLoader(ClassLoader cl) {
        System.out.println("ClassLoaderUtils.printClassLoader(cl = " + cl + ")");

        if (cl != null) {
            printClassLoader(cl.getParent());
        }
    }
    
    /**
     *  得到当前线程的ClassLoader
     */
    public static ClassLoader getCurrentThreadClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
    
    /**
     *  得到指定类的ClassLoader
     * @param clazz
     * @return
     */
    public static ClassLoader getClassLoader(Class<?> clazz) {
		return clazz.getClassLoader();
	}
    
    /**
     *  得到指定Class所在的ClassLoader的ClassPath的绝对路径
     * @param clazz
     * @return
     */
    public static String getAbsoluteClassPath(Class<?> clazz) {
		return getClassLoader(clazz).getResource(".").toString();
	}
    
}
