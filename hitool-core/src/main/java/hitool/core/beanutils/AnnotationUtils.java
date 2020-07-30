/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package hitool.core.beanutils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.beanutils.annotation.AnnotationAttributes;
import hitool.core.beanutils.reference.BridgeMethodResolver;
import hitool.core.beanutils.reference.ConcurrentReferenceHashMap;
import hitool.core.beanutils.reflection.ObjectUtils;
import hitool.core.beanutils.reflection.ReflectionUtils;
import hitool.core.lang3.ArrayUtils;
import hitool.core.lang3.Assert;
import hitool.core.lang3.StringUtils;


@SuppressWarnings("unchecked")
public abstract class AnnotationUtils {
	
	/**
     * 日志.
     */
	protected static Logger LOG = LoggerFactory.getLogger(AnnotationUtils.class);

	private static final Pattern SETTER_PATTERN = Pattern.compile("set([A-Z][A-Za-z0-9]*)$");
    private static final Pattern GETTER_PATTERN = Pattern.compile("(get|is|has)([A-Z][A-Za-z0-9]*)$");
	/** The attribute name for annotations with a single element */
	public static final String VALUE = "value";
	private static final Map<Class<?>, Boolean> annotatedInterfaceCache = new WeakHashMap<Class<?>, Boolean>();
	private static final Map<AnnotationCacheKey, Annotation> findAnnotationCache = new ConcurrentReferenceHashMap<AnnotationCacheKey, Annotation>(256);

	
	

	/**
     * Adds all fields with the specified Annotation of class clazz and its superclasses to allFields
     *
     * @param annotationClass the {@link Annotation}s to find
     * @param clazz The {@link Class} to inspect
     * @param allFields list of all fields
     */
    public static void addAllFields(Class<? extends Annotation> annotationClass, Class clazz, List<Field> allFields) {

        if (clazz == null) {
            return;
        }

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            Annotation ann = field.getAnnotation(annotationClass);
            if (ann!=null) {
                allFields.add(field);
            }
        }
        addAllFields(annotationClass, clazz.getSuperclass(), allFields);
    }

    /**
     * Adds all methods with the specified Annotation of class clazz and its superclasses to allFields
     *
     * @param annotationClass the {@link Annotation}s to find
     * @param clazz The {@link Class} to inspect
     * @param allMethods list of all methods
     */
    public static void addAllMethods(Class<? extends Annotation> annotationClass, Class clazz, List<Method> allMethods) {

        if (clazz == null) {
            return;
        }

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            Annotation ann = method.getAnnotation(annotationClass);
            if (ann!=null) {
                allMethods.add(method);
            }
        }
        addAllMethods(annotationClass, clazz.getSuperclass(), allMethods);
    }

    /**
     *
     * @param clazz The {@link Class} to inspect
     * @param allInterfaces list of all interfaces
     */
    public static void addAllInterfaces(Class clazz, List<Class> allInterfaces) {
        if (clazz == null) {
            return;
        }

        Class[] interfaces = clazz.getInterfaces();
        allInterfaces.addAll(Arrays.asList(interfaces));
        addAllInterfaces(clazz.getSuperclass(), allInterfaces);
    }

	/**
	 * For the given <code>Class</code> get a collection of the the {@link AnnotatedElement}s 
	 * that match the given <code>annotation</code>s or if no <code>annotation</code>s are 
	 * specified then return all of the annotated elements of the given <code>Class</code>. 
	 * Includes only the method level annotations.
	 * 
	 * @param clazz The {@link Class} to inspect
	 * @param annotation the {@link Annotation}s to find
	 * @return A {@link Collection}&lt;{@link AnnotatedElement}&gt; containing all of the
	 *  method {@link AnnotatedElement}s matching the specified {@link Annotation}s
	 */
	public static Collection<Method> getAnnotatedMethods(Class clazz, Class<? extends Annotation>... annotation){
        Collection<Method> toReturn = new HashSet<Method>();

        for (Method m : clazz.getMethods()) {
            if (ArrayUtils.isNotEmpty(annotation) && isAnnotatedBy(m, annotation)) {
                toReturn.add(m);
            } else if (ArrayUtils.isEmpty(annotation) && ArrayUtils.isNotEmpty(m.getAnnotations())) {
                toReturn.add(m);
            }
		}
		
		return toReturn;
	}

	/**
	 * Varargs version of <code>AnnotatedElement.isAnnotationPresent()</code>
     * @param annotatedElement element to check
     * @param annotation the {@link Annotation}s to find
     * @return true is element is annotated by one of the annotation
	 * @see AnnotatedElement
	 */
	public static boolean isAnnotatedBy(AnnotatedElement annotatedElement, Class<? extends Annotation>... annotation) {
        if (ArrayUtils.isEmpty(annotation)) {
            return false;
        }

		for( Class<? extends Annotation> c : annotation ){
			if( annotatedElement.isAnnotationPresent(c) ) return true;
		}

		return false;
	}

    /**
     * Returns the property name for a method.
     * This method is independent from property fields.
     *
     * @param method The method to get the property name for.
     * @return the property name for given method; null if non could be resolved.
     */
    public static String resolvePropertyName(Method method) {

        Matcher matcher = SETTER_PATTERN.matcher(method.getName());
        if (matcher.matches() && method.getParameterTypes().length == 1) {
            String raw = matcher.group(1);
            return raw.substring(0, 1).toLowerCase() + raw.substring(1);
        }

        matcher = GETTER_PATTERN.matcher(method.getName());
        if (matcher.matches() && method.getParameterTypes().length == 0) {
            String raw = matcher.group(2);
            return raw.substring(0, 1).toLowerCase() + raw.substring(1);
        }

        return null;
    }

    /**
     * Returns the annotation on the given class or the package of the class. This searchs up the
     * class hierarchy and the package hierarchy for the closest match.
     *
     * @param   <T> class type
     * @param   clazz The class to search for the annotation.
     * @param   annotationClass The Class of the annotation.
     * @return  The annotation or null.
     */
    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationClass) {
        T ann = clazz.getAnnotation(annotationClass);
        while (ann == null && clazz != null) {
            ann = clazz.getAnnotation(annotationClass);
            if (ann == null) {
                ann = clazz.getPackage().getAnnotation(annotationClass);
            }
            if (ann == null) {
                clazz = clazz.getSuperclass();
                if (clazz != null) {
                    ann = clazz.getAnnotation(annotationClass);
                }
            }
        }

        return ann;
    }

    public static <T extends Annotation> boolean hasAnnotation(Class<?> klass, Class<T> annotationClass){
    	return findAnnotation(klass,annotationClass) != null;
    }
    
    public static <T extends Annotation> boolean hasAnnotation(Method method, Class<T> annotationClass){
    	return findAnnotation(method, annotationClass) != null;
    }
    
	/**
	 * 返回一个注解的类型
	 */
	public static <R> R getAnnotation(Annotation[] annotations, Class<R> type) {
		Object t = null;
		for (int j = 0; j < annotations.length; ++j) {
			if (annotations[j].annotationType().equals(type)) {
				t = annotations[0];
			}
		}
		return (R) t;
	}

	/**
	 * Get a single {@link Annotation} of {@code annotationType} from the supplied
	 * annotation: either the given annotation itself or a meta-annotation thereof.
	 * @param ann the Annotation to check
	 * @param annotationType the annotation type to look for, both locally and as a meta-annotation
	 * @return the matching annotation, or {@code null} if none found
	 * @since 4.0
	 */
	public static <T extends Annotation> T getAnnotation(Annotation ann, Class<T> annotationType) {
		if (annotationType.isInstance(ann)) {
			return (T) ann;
		}
		try {
			return ann.annotationType().getAnnotation(annotationType);
		}
		catch (Exception ex) {
			// Assuming nested Class values not resolvable within annotation attributes...
			logIntrospectionFailure(ann.annotationType(), ex);
			return null;
		}
	}

	/**
	 * Get a single {@link Annotation} of {@code annotationType} from the supplied
	 * Method, Constructor or Field. Meta-annotations will be searched if the annotation
	 * is not declared locally on the supplied element.
	 * @param annotatedElement the Method, Constructor or Field from which to get the annotation
	 * @param annotationType the annotation type to look for, both locally and as a meta-annotation
	 * @return the matching annotation, or {@code null} if none found
	 * @since 3.1
	 */
	public static <T extends Annotation> T getAnnotation(AnnotatedElement annotatedElement, Class<T> annotationType) {
		try {
			T ann = annotatedElement.getAnnotation(annotationType);
			if (ann == null) {
				for (Annotation metaAnn : annotatedElement.getAnnotations()) {
					ann = metaAnn.annotationType().getAnnotation(annotationType);
					if (ann != null) {
						break;
					}
				}
			}
			return ann;
		}
		catch (Exception ex) {
			// Assuming nested Class values not resolvable within annotation attributes...
			logIntrospectionFailure(annotatedElement, ex);
			return null;
		}
	}

	/**
	 * Get all {@link Annotation Annotations} from the supplied Method, Constructor or Field.
	 * @param annotatedElement the Method, Constructor or Field to retrieve annotations from
	 * @return the annotations found, or {@code null} if not resolvable (e.g. because nested
	 * Class values in annotation attributes failed to resolve at runtime)
	 * @since 4.0.8
	 */
	public static Annotation[] getAnnotations(AnnotatedElement annotatedElement) {
		try {
			return annotatedElement.getAnnotations();
		}
		catch (Exception ex) {
			// Assuming nested Class values not resolvable within annotation attributes...
			logIntrospectionFailure(annotatedElement, ex);
			return null;
		}
	}

	/**
	 * Get all {@link Annotation Annotations} from the supplied {@link Method}.
	 * <p>Correctly handles bridge {@link Method Methods} generated by the compiler.
	 * @param method the Method to retrieve annotations from
	 * @return the annotations found
	 * @see org.springframework.core.BridgeMethodResolver#findBridgedMethod(Method)
	 */
	public static Annotation[] getAnnotations(Method method) {
		try {
			return BridgeMethodResolver.findBridgedMethod(method).getAnnotations();
		}
		catch (Exception ex) {
			// Assuming nested Class values not resolvable within annotation attributes...
			logIntrospectionFailure(method, ex);
			return null;
		}
	}

	/**
	 * Get a single {@link Annotation} of {@code annotationType} from the supplied {@link Method}.
	 * <p>Correctly handles bridge {@link Method Methods} generated by the compiler.
	 * @param method the method to look for annotations on
	 * @param annotationType the annotation type to look for
	 * @return the annotations found
	 * @see org.springframework.core.BridgeMethodResolver#findBridgedMethod(Method)
	 */
	public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
		Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
		return getAnnotation((AnnotatedElement) resolvedMethod, annotationType);
	}

	/**
	 * Get the possibly repeating {@link Annotation}s of {@code annotationType} from the
	 * supplied {@link Method}. Deals with both a single direct annotation and repeating
	 * annotations nested within a containing annotation.
	 * <p>Correctly handles bridge {@link Method Methods} generated by the compiler.
	 * @param method the method to look for annotations on
	 * @param containerAnnotationType the class of the container that holds the annotations
	 * @param annotationType the annotation type to look for
	 * @return the annotations found
	 * @since 4.0
	 * @see org.springframework.core.BridgeMethodResolver#findBridgedMethod(Method)
	 */
	public static <A extends Annotation> Set<A> getRepeatableAnnotation(Method method,
			Class<? extends Annotation> containerAnnotationType, Class<A> annotationType) {

		Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
		return getRepeatableAnnotation((AnnotatedElement) resolvedMethod, containerAnnotationType, annotationType);
	}

	/**
	 * Get the possibly repeating {@link Annotation}s of {@code annotationType} from the
	 * supplied {@link AnnotatedElement}. Deals with both a single direct annotation and
	 * repeating annotations nested within a containing annotation.
	 * <p>Correctly handles bridge {@link Method Methods} generated by the compiler.
	 * @param annotatedElement the element to look for annotations on
	 * @param containerAnnotationType the class of the container that holds the annotations
	 * @param annotationType the annotation type to look for
	 * @return the annotations found
	 * @since 4.0
	 * @see org.springframework.core.BridgeMethodResolver#findBridgedMethod(Method)
	 */
	public static <A extends Annotation> Set<A> getRepeatableAnnotation(AnnotatedElement annotatedElement,
			Class<? extends Annotation> containerAnnotationType, Class<A> annotationType) {

		try {
			if (annotatedElement.getAnnotations().length > 0) {
				return new AnnotationCollector<A>(containerAnnotationType, annotationType).getResult(annotatedElement);
			}
		}
		catch (Exception ex) {
			// Assuming nested Class values not resolvable within annotation attributes...
			logIntrospectionFailure(annotatedElement, ex);
		}
		return Collections.emptySet();
	}

	/**
	 * Find a single {@link Annotation} of {@code annotationType} from the supplied
	 * {@link Method}, traversing its super methods (i.e., from superclasses and
	 * interfaces) if no annotation can be found on the given method itself.
	 * <p>Annotations on methods are not inherited by default, so we need to handle
	 * this explicitly.
	 * @param method the method to look for annotations on
	 * @param annotationType the annotation type to look for
	 * @return the annotation found, or {@code null} if none
	 */
	public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationType) {
		AnnotationCacheKey cacheKey = new AnnotationCacheKey(method, annotationType);
		A result = (A) findAnnotationCache.get(cacheKey);
		if (result == null) {
			result = getAnnotation(method, annotationType);
			Class<?> clazz = method.getDeclaringClass();
			if (result == null) {
				result = searchOnInterfaces(method, annotationType, clazz.getInterfaces());
			}
			while (result == null) {
				clazz = clazz.getSuperclass();
				if (clazz == null || clazz.equals(Object.class)) {
					break;
				}
				try {
					Method equivalentMethod = clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
					result = getAnnotation(equivalentMethod, annotationType);
				}
				catch (NoSuchMethodException ex) {
					// No equivalent method found
				}
				if (result == null) {
					result = searchOnInterfaces(method, annotationType, clazz.getInterfaces());
				}
			}
			if (result != null) {
				findAnnotationCache.put(cacheKey, result);
			}
		}
		return result;
	}

	private static <A extends Annotation> A searchOnInterfaces(Method method, Class<A> annotationType, Class<?>... ifcs) {
		A annotation = null;
		for (Class<?> iface : ifcs) {
			if (isInterfaceWithAnnotatedMethods(iface)) {
				try {
					Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
					annotation = getAnnotation(equivalentMethod, annotationType);
				}
				catch (NoSuchMethodException ex) {
					// Skip this interface - it doesn't have the method...
				}
				if (annotation != null) {
					break;
				}
			}
		}
		return annotation;
	}

	private static boolean isInterfaceWithAnnotatedMethods(Class<?> iface) {
		Boolean flag = annotatedInterfaceCache.get(iface);
		if (flag != null) {
			return flag;
		}
		boolean found = false;
		for (Method ifcMethod : iface.getMethods()) {
			try {
				if (ifcMethod.getAnnotations().length > 0) {
					found = true;
					break;
				}
			}
			catch (Exception ex) {
				// Assuming nested Class values not resolvable within annotation attributes...
				logIntrospectionFailure(ifcMethod, ex);
			}
		}
		annotatedInterfaceCache.put(iface, found);
		return found;
	}

	/**
	 * Find a single {@link Annotation} of {@code annotationType} on the
	 * supplied {@link Class}, traversing its interfaces, annotations, and
	 * superclasses if the annotation is not <em>present</em> on the given class
	 * itself.
	 * <p>This method explicitly handles class-level annotations which are not
	 * declared as {@link java.lang.annotation.Inherited inherited} <em>as well
	 * as meta-annotations and annotations on interfaces</em>.
	 * <p>The algorithm operates as follows:
	 * <ol>
	 * <li>Search for the annotation on the given class and return it if found.
	 * <li>Recursively search through all interfaces that the given class declares.
	 * <li>Recursively search through all annotations that the given class declares.
	 * <li>Recursively search through the superclass hierarchy of the given class.
	 * </ol>
	 * <p>Note: in this context, the term <em>recursively</em> means that the search
	 * process continues by returning to step #1 with the current interface,
	 * annotation, or superclass as the class to look for annotations on.
	 * @param clazz the class to look for annotations on
	 * @param annotationType the type of annotation to look for
	 * @return the annotation if found, or {@code null} if not found
	 */
	public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
		AnnotationCacheKey cacheKey = new AnnotationCacheKey(clazz, annotationType);
		A result = (A) findAnnotationCache.get(cacheKey);
		if (result == null) {
			result = findAnnotation(clazz, annotationType, new HashSet<Annotation>());
			if (result != null) {
				findAnnotationCache.put(cacheKey, result);
			}
		}
		return result;
	}

	/**
	 * Perform the search algorithm for {@link #findAnnotation(Class, Class)},
	 * avoiding endless recursion by tracking which annotations have already
	 * been <em>visited</em>.
	 * @param clazz the class to look for annotations on
	 * @param annotationType the type of annotation to look for
	 * @param visited the set of annotations that have already been visited
	 * @return the annotation if found, or {@code null} if not found
	 */
	private static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType, Set<Annotation> visited) {
		Assert.notNull(clazz, "Class must not be null");

		try {
			Annotation[] anns = clazz.getDeclaredAnnotations();
			for (Annotation ann : anns) {
				if (ann.annotationType().equals(annotationType)) {
					return (A) ann;
				}
			}
			for (Annotation ann : anns) {
				if (!isInJavaLangAnnotationPackage(ann) && visited.add(ann)) {
					A annotation = findAnnotation(ann.annotationType(), annotationType, visited);
					if (annotation != null) {
						return annotation;
					}
				}
			}
		}
		catch (Exception ex) {
			// Assuming nested Class values not resolvable within annotation attributes...
			logIntrospectionFailure(clazz, ex);
			return null;
		}

		for (Class<?> ifc : clazz.getInterfaces()) {
			A annotation = findAnnotation(ifc, annotationType, visited);
			if (annotation != null) {
				return annotation;
			}
		}

		Class<?> superclass = clazz.getSuperclass();
		if (superclass == null || superclass.equals(Object.class)) {
			return null;
		}
		return findAnnotation(superclass, annotationType, visited);
	}

	/**
	 * Find the first {@link Class} in the inheritance hierarchy of the specified {@code clazz}
	 * (including the specified {@code clazz} itself) which declares an annotation for the
	 * specified {@code annotationType}, or {@code null} if not found. If the supplied
	 * {@code clazz} is {@code null}, {@code null} will be returned.
	 * <p>If the supplied {@code clazz} is an interface, only the interface itself will be checked;
	 * the inheritance hierarchy for interfaces will not be traversed.
	 * <p>The standard {@link Class} API does not provide a mechanism for determining which class
	 * in an inheritance hierarchy actually declares an {@link Annotation}, so we need to handle
	 * this explicitly.
	 * @param annotationType the annotation type to look for, both locally and as a meta-annotation
	 * @param clazz the class on which to check for the annotation (may be {@code null})
	 * @return the first {@link Class} in the inheritance hierarchy of the specified {@code clazz}
	 * which declares an annotation for the specified {@code annotationType}, or {@code null}
	 * if not found
	 * @see Class#isAnnotationPresent(Class)
	 * @see Class#getDeclaredAnnotations()
	 * @see #findAnnotationDeclaringClassForTypes(List, Class)
	 * @see #isAnnotationDeclaredLocally(Class, Class)
	 */
	public static Class<?> findAnnotationDeclaringClass(Class<? extends Annotation> annotationType, Class<?> clazz) {
		Assert.notNull(annotationType, "Annotation type must not be null");
		if (clazz == null || clazz.equals(Object.class)) {
			return null;
		}
		if (isAnnotationDeclaredLocally(annotationType, clazz)) {
			return clazz;
		}
		return findAnnotationDeclaringClass(annotationType, clazz.getSuperclass());
	}

	/**
	 * Find the first {@link Class} in the inheritance hierarchy of the specified
	 * {@code clazz} (including the specified {@code clazz} itself) which declares
	 * at least one of the specified {@code annotationTypes}, or {@code null} if
	 * none of the specified annotation types could be found.
	 * <p>If the supplied {@code clazz} is {@code null}, {@code null} will be
	 * returned.
	 * <p>If the supplied {@code clazz} is an interface, only the interface itself
	 * will be checked; the inheritance hierarchy for interfaces will not be traversed.
	 * <p>The standard {@link Class} API does not provide a mechanism for determining
	 * which class in an inheritance hierarchy actually declares one of several
	 * candidate {@linkplain Annotation annotations}, so we need to handle this
	 * explicitly.
	 * @param annotationTypes the list of Class objects corresponding to the
	 * annotation types
	 * @param clazz the Class object corresponding to the class on which to check
	 * for the annotations, or {@code null}
	 * @return the first {@link Class} in the inheritance hierarchy of the specified
	 * {@code clazz} which declares an annotation of at least one of the specified
	 * {@code annotationTypes}, or {@code null} if not found
	 * @since 3.2.2
	 * @see Class#isAnnotationPresent(Class)
	 * @see Class#getDeclaredAnnotations()
	 * @see #findAnnotationDeclaringClass(Class, Class)
	 * @see #isAnnotationDeclaredLocally(Class, Class)
	 */
	public static Class<?> findAnnotationDeclaringClassForTypes(List<Class<? extends Annotation>> annotationTypes, Class<?> clazz) {
		Assert.notEmpty(annotationTypes, "The list of annotation types must not be empty");
		if (clazz == null || clazz.equals(Object.class)) {
			return null;
		}
		for (Class<? extends Annotation> annotationType : annotationTypes) {
			if (isAnnotationDeclaredLocally(annotationType, clazz)) {
				return clazz;
			}
		}
		return findAnnotationDeclaringClassForTypes(annotationTypes, clazz.getSuperclass());
	}

	/**
	 * Determine whether an annotation for the specified {@code annotationType} is
	 * declared locally on the supplied {@code clazz}. The supplied {@link Class}
	 * may represent any type.
	 * <p>Note: This method does <strong>not</strong> determine if the annotation is
	 * {@linkplain java.lang.annotation.Inherited inherited}. For greater clarity
	 * regarding inherited annotations, consider using
	 * {@link #isAnnotationInherited(Class, Class)} instead.
	 * @param annotationType the Class object corresponding to the annotation type
	 * @param clazz the Class object corresponding to the class on which to check for the annotation
	 * @return {@code true} if an annotation for the specified {@code annotationType}
	 * is declared locally on the supplied {@code clazz}
	 * @see Class#getDeclaredAnnotations()
	 * @see #isAnnotationInherited(Class, Class)
	 */
	public static boolean isAnnotationDeclaredLocally(Class<? extends Annotation> annotationType, Class<?> clazz) {
		Assert.notNull(annotationType, "Annotation type must not be null");
		Assert.notNull(clazz, "Class must not be null");
		boolean declaredLocally = false;
		try {
			for (Annotation ann : clazz.getDeclaredAnnotations()) {
				if (ann.annotationType().equals(annotationType)) {
					declaredLocally = true;
					break;
				}
			}
		}
		catch (Exception ex) {
			// Assuming nested Class values not resolvable within annotation attributes...
			logIntrospectionFailure(clazz, ex);
		}
		return declaredLocally;
	}

	/**
	 * Determine whether an annotation for the specified {@code annotationType} is present
	 * on the supplied {@code clazz} and is {@linkplain java.lang.annotation.Inherited inherited}
	 * (i.e., not declared locally for the class).
	 * <p>If the supplied {@code clazz} is an interface, only the interface itself will be checked.
	 * In accordance with standard meta-annotation semantics, the inheritance hierarchy for interfaces
	 * will not be traversed. See the {@linkplain java.lang.annotation.Inherited Javadoc} for the
	 * {@code @Inherited} meta-annotation for further details regarding annotation inheritance.
	 * @param annotationType the Class object corresponding to the annotation type
	 * @param clazz the Class object corresponding to the class on which to check for the annotation
	 * @return {@code true} if an annotation for the specified {@code annotationType} is present
	 * on the supplied {@code clazz} and is <em>inherited</em>
	 * @see Class#isAnnotationPresent(Class)
	 * @see #isAnnotationDeclaredLocally(Class, Class)
	 */
	public static boolean isAnnotationInherited(Class<? extends Annotation> annotationType, Class<?> clazz) {
		Assert.notNull(annotationType, "Annotation type must not be null");
		Assert.notNull(clazz, "Class must not be null");
		return (clazz.isAnnotationPresent(annotationType) && !isAnnotationDeclaredLocally(annotationType, clazz));
	}

	/**
	 * Determine if the supplied {@link Annotation} is defined in the core JDK
	 * {@code java.lang.annotation} package.
	 * @param annotation the annotation to check (never {@code null})
	 * @return {@code true} if the annotation is in the {@code java.lang.annotation} package
	 */
	public static boolean isInJavaLangAnnotationPackage(Annotation annotation) {
		Assert.notNull(annotation, "Annotation must not be null");
		return annotation.annotationType().getName().startsWith("java.lang.annotation");
	}

	/**
	 * Retrieve the given annotation's attributes as a {@link Map}, preserving all
	 * attribute types as-is.
	 * <p>Note: This method actually returns an {@link AnnotationAttributes} instance.
	 * However, the {@code Map} signature has been preserved for binary compatibility.
	 * @param annotation the annotation to retrieve the attributes for
	 * @return the Map of annotation attributes, with attribute names as keys and
	 * corresponding attribute values as values
	 */
	public static Map<String, Object> getAnnotationAttributes(Annotation annotation) {
		return getAnnotationAttributes(annotation, false, false);
	}

	/**
	 * Retrieve the given annotation's attributes as a {@link Map}. Equivalent to
	 * calling {@link #getAnnotationAttributes(Annotation, boolean, boolean)} with
	 * the {@code nestedAnnotationsAsMap} parameter set to {@code false}.
	 * <p>Note: This method actually returns an {@link AnnotationAttributes} instance.
	 * However, the {@code Map} signature has been preserved for binary compatibility.
	 * @param annotation the annotation to retrieve the attributes for
	 * @param classValuesAsString whether to turn Class references into Strings (for
	 * compatibility with {@link org.springframework.core.type.AnnotationMetadata}
	 * or to preserve them as Class references
	 * @return the Map of annotation attributes, with attribute names as keys and
	 * corresponding attribute values as values
	 */
	public static Map<String, Object> getAnnotationAttributes(Annotation annotation, boolean classValuesAsString) {
		return getAnnotationAttributes(annotation, classValuesAsString, false);
	}

	/**
	 * Retrieve the given annotation's attributes as an {@link AnnotationAttributes}
	 * map structure.
	 * <p>This method provides fully recursive annotation reading capabilities on par with
	 * the reflection-based {@link org.springframework.core.type.StandardAnnotationMetadata}.
	 * @param annotation the annotation to retrieve the attributes for
	 * @param classValuesAsString whether to turn Class references into Strings (for
	 * compatibility with {@link org.springframework.core.type.AnnotationMetadata}
	 * or to preserve them as Class references
	 * @param nestedAnnotationsAsMap whether to turn nested Annotation instances into
	 * {@link AnnotationAttributes} maps (for compatibility with
	 * {@link org.springframework.core.type.AnnotationMetadata} or to preserve them as
	 * Annotation instances
	 * @return the annotation attributes (a specialized Map) with attribute names as keys
	 * and corresponding attribute values as values
	 * @since 3.1.1
	 */
	public static AnnotationAttributes getAnnotationAttributes(Annotation annotation, boolean classValuesAsString,
			boolean nestedAnnotationsAsMap) {

		AnnotationAttributes attrs = new AnnotationAttributes();
		Method[] methods = annotation.annotationType().getDeclaredMethods();
		for (Method method : methods) {
			if (method.getParameterTypes().length == 0 && method.getReturnType() != void.class) {
				try {
					Object value = method.invoke(annotation);
					attrs.put(method.getName(), adaptValue(value, classValuesAsString, nestedAnnotationsAsMap));
				}
				catch (Exception ex) {
					throw new IllegalStateException("Could not obtain annotation attribute values", ex);
				}
			}
		}
		return attrs;
	}

	/**
	 * Adapt the given value according to the given class and nested annotation settings.
	 * @param value the annotation attribute value
	 * @param classValuesAsString whether to turn Class references into Strings (for
	 * compatibility with {@link org.springframework.core.type.AnnotationMetadata}
	 * or to preserve them as Class references
	 * @param nestedAnnotationsAsMap whether to turn nested Annotation instances into
	 * {@link AnnotationAttributes} maps (for compatibility with
	 * {@link org.springframework.core.type.AnnotationMetadata} or to preserve them as
	 * Annotation instances
	 * @return the adapted value, or the original value if no adaptation is needed
	 */
	static Object adaptValue(Object value, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
		if (classValuesAsString) {
			if (value instanceof Class) {
				value = ((Class<?>) value).getName();
			}
			else if (value instanceof Class[]) {
				Class<?>[] clazzArray = (Class[]) value;
				String[] newValue = new String[clazzArray.length];
				for (int i = 0; i < clazzArray.length; i++) {
					newValue[i] = clazzArray[i].getName();
				}
				value = newValue;
			}
		}
		if (nestedAnnotationsAsMap && value instanceof Annotation) {
			return getAnnotationAttributes((Annotation) value, classValuesAsString, true);
		}
		else if (nestedAnnotationsAsMap && value instanceof Annotation[]) {
			Annotation[] realAnnotations = (Annotation[]) value;
			AnnotationAttributes[] mappedAnnotations = new AnnotationAttributes[realAnnotations.length];
			for (int i = 0; i < realAnnotations.length; i++) {
				mappedAnnotations[i] = getAnnotationAttributes(realAnnotations[i], classValuesAsString, true);
			}
			return mappedAnnotations;
		}
		else {
			return value;
		}
	}

	/**
	 * Retrieve the <em>value</em> of the {@code &quot;value&quot;} attribute of a
	 * single-element Annotation, given an annotation instance.
	 * @param annotation the annotation instance from which to retrieve the value
	 * @return the attribute value, or {@code null} if not found
	 * @see #getValue(Annotation, String)
	 */
	public static Object getValue(Annotation annotation) {
		return getValue(annotation, VALUE);
	}

	/**
	 * Retrieve the <em>value</em> of a named attribute, given an annotation instance.
	 * @param annotation the annotation instance from which to retrieve the value
	 * @param attributeName the name of the attribute value to retrieve
	 * @return the attribute value, or {@code null} if not found
	 * @see #getValue(Annotation)
	 */
	public static Object getValue(Annotation annotation, String attributeName) {
		if (annotation == null || !StringUtils.hasLength(attributeName)) {
			return null;
		}
		try {
			Method method = annotation.annotationType().getDeclaredMethod(attributeName);
			ReflectionUtils.makeAccessible(method);
			return method.invoke(annotation);
		}
		catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Retrieve the <em>default value</em> of the {@code &quot;value&quot;} attribute
	 * of a single-element Annotation, given an annotation instance.
	 * @param annotation the annotation instance from which to retrieve the default value
	 * @return the default value, or {@code null} if not found
	 * @see #getDefaultValue(Annotation, String)
	 */
	public static Object getDefaultValue(Annotation annotation) {
		return getDefaultValue(annotation, VALUE);
	}

	/**
	 * Retrieve the <em>default value</em> of a named attribute, given an annotation instance.
	 * @param annotation the annotation instance from which to retrieve the default value
	 * @param attributeName the name of the attribute value to retrieve
	 * @return the default value of the named attribute, or {@code null} if not found
	 * @see #getDefaultValue(Class, String)
	 */
	public static Object getDefaultValue(Annotation annotation, String attributeName) {
		if (annotation == null) {
			return null;
		}
		return getDefaultValue(annotation.annotationType(), attributeName);
	}

	/**
	 * Retrieve the <em>default value</em> of the {@code &quot;value&quot;} attribute
	 * of a single-element Annotation, given the {@link Class annotation type}.
	 * @param annotationType the <em>annotation type</em> for which the default value should be retrieved
	 * @return the default value, or {@code null} if not found
	 * @see #getDefaultValue(Class, String)
	 */
	public static Object getDefaultValue(Class<? extends Annotation> annotationType) {
		return getDefaultValue(annotationType, VALUE);
	}

	/**
	 * Retrieve the <em>default value</em> of a named attribute, given the
	 * {@link Class annotation type}.
	 * @param annotationType the <em>annotation type</em> for which the default value should be retrieved
	 * @param attributeName the name of the attribute value to retrieve.
	 * @return the default value of the named attribute, or {@code null} if not found
	 * @see #getDefaultValue(Annotation, String)
	 */
	public static Object getDefaultValue(Class<? extends Annotation> annotationType, String attributeName) {
		if (annotationType == null || !StringUtils.hasLength(attributeName)) {
			return null;
		}
		try {
			return annotationType.getDeclaredMethod(attributeName).getDefaultValue();
		}
		catch (Exception ex) {
			return null;
		}
	}


	private static void logIntrospectionFailure(AnnotatedElement annotatedElement, Exception ex) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Failed to introspect annotations on [" + annotatedElement + "]: " + ex);
		}
	}


	/**
	 * Cache key for the AnnotatedElement cache.
	 */
	private static class AnnotationCacheKey {

		private final AnnotatedElement element;

		private final Class<? extends Annotation> annotationType;

		public AnnotationCacheKey(AnnotatedElement element, Class<? extends Annotation> annotationType) {
			this.element = element;
			this.annotationType = annotationType;
		}

		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof AnnotationCacheKey)) {
				return false;
			}
			AnnotationCacheKey otherKey = (AnnotationCacheKey) other;
			return (this.element.equals(otherKey.element) &&
					ObjectUtils.nullSafeEquals(this.annotationType, otherKey.annotationType));
		}

		@Override
		public int hashCode() {
			return (this.element.hashCode() * 29 + this.annotationType.hashCode());
		}
	}


	private static class AnnotationCollector<A extends Annotation> {

		private final Class<? extends Annotation> containerAnnotationType;

		private final Class<A> annotationType;

		private final Set<AnnotatedElement> visited = new HashSet<AnnotatedElement>();

		private final Set<A> result = new LinkedHashSet<A>();

		public AnnotationCollector(Class<? extends Annotation> containerAnnotationType, Class<A> annotationType) {
			this.containerAnnotationType = containerAnnotationType;
			this.annotationType = annotationType;
		}

		public Set<A> getResult(AnnotatedElement element) {
			process(element);
			return Collections.unmodifiableSet(this.result);
		}

		private void process(AnnotatedElement annotatedElement) {
			if (this.visited.add(annotatedElement)) {
				for (Annotation ann : annotatedElement.getAnnotations()) {
					if (ObjectUtils.nullSafeEquals(this.annotationType, ann.annotationType())) {
						this.result.add((A) ann);
					}
					else if (ObjectUtils.nullSafeEquals(this.containerAnnotationType, ann.annotationType())) {
						this.result.addAll(getValue(ann));
					}
					else if (!isInJavaLangAnnotationPackage(ann)) {
						process(ann.annotationType());
					}
				}
			}
		}

		private List<A> getValue(Annotation annotation) {
			try {
				Method method = annotation.annotationType().getDeclaredMethod("value");
				ReflectionUtils.makeAccessible(method);
				return Arrays.asList((A[]) method.invoke(annotation));
			}
			catch (Exception ex) {
				// Unable to read value from repeating annotation container -> ignore it.
				return Collections.emptyList();
			}
		}
	}
	
}
