package dependencyInjectionLibrary;

import dependencyInjectionLibrary.annotations.*;
import dependencyInjectionLibrary.customExceptions.*;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class DIEngine {


    public static HashMap<String, Object> objects = new HashMap<String, Object>();
    public static List<Object> classesForInject = new ArrayList<>();

    public static DependencySupplier ds = new DependencySupplier();

    public static void inject(Object target) {
        Class<?> cl = target.getClass();

        Field[] fields = cl.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                Object value = null;
                try {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    Class<?> type = field.getType();

                    if(isSingleton(type)){
                        value = objects.get(type.getTypeName());
                        System.out.println(objects);
                    } else {
                        objects.put(type.getTypeName(), type);
                        value = type.newInstance();
                    }

                    if (value != null) {
                        field.set(target, value);
                    }
                    field.setAccessible(accessible);

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isSingleton(Class cl) {
        if(objects.containsKey(cl.getTypeName())){
            return true;
        }

        return false;
    }

    public static boolean isBean(Class cl) {
        if(cl.isAnnotationPresent(Bean.class)) {
            return true;
        }

        return false;
    }


    public static boolean isService(Class cl) {
        if(cl.isAnnotationPresent(Service.class)) {
            return true;
        }

        return false;
    }

    public static boolean isComponent(Class cl) {
        if(cl.isAnnotationPresent(Component.class)) {
            return true;
        }

        return false;
    }


    public static void injectAllAnnotatedFields(Object target) {
        Class<?> cl = target.getClass();

        Field[] fields = cl.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {

                if(field.getType().isInterface()) {
                    try {
                        throw new InterfaceAnnotatedWithAutowired("Interface annotated with @Autowired. Interface could only be annotated with @Qualifier.");
                    } catch (InterfaceAnnotatedWithAutowired interfaceAnnotatedWithAutowired) {
                        interfaceAnnotatedWithAutowired.printStackTrace();
                    }
                }

                Boolean fieldVerbose = field.getDeclaredAnnotation(Autowired.class).verbose();

                Object value = null;
                try {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);

                    Class<?> type = field.getType();

                    value = getClassObject(type);

                    if (value != null) {
                        field.set(target, value);

                        if(fieldVerbose) {
                            System.out.println("Initialized <" + type.getTypeName() + "> <" + field.getName() + "> in <" + target.getClass() + "> on <" + LocalDateTime.now() + "> with <" + value.hashCode() + ">");
                        }
                    }

                    injectAllAnnotatedFields(value);
                    field.setAccessible(accessible);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if(field.isAnnotationPresent(Qualifier.class)) {
                String qualifierValue = field.getDeclaredAnnotation(Qualifier.class).value();

                if(ds.qualifierObjects.get(qualifierValue) == null ){
                    try {
                        throw new NoBeanWithSpecificQualifier("There is no bean with qualifier " + qualifierValue);
                    } catch (NoBeanWithSpecificQualifier noBeanWithSpecificQualifier) {
                        noBeanWithSpecificQualifier.printStackTrace();
                    }
                }

                if(!field.getType().isInterface()) {
                    try {
                        throw new AnnotationQualifierCouldOnlyAnnotateInterface("Attribute annotated with @Qualifier must be an interface.");
                    } catch (AnnotationQualifierCouldOnlyAnnotateInterface annotationQualifierCouldOnlyAnnotateInterface) {
                        annotationQualifierCouldOnlyAnnotateInterface.printStackTrace();
                    }
                }

                Object value = null;

                try {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);

                    Class<?> type = ds.qualifierObjects.get(qualifierValue);

                    value = getClassObject(type);

                    if (value != null) {
                        field.set(target, value);

                        System.out.println("Initialized <" + type.getTypeName() + "> <" + field.getName() + "> in <" + target.getClass() + "> on <" + LocalDateTime.now() + "> with <" + value.hashCode() + ">");
                    }

                    injectAllAnnotatedFields(value);
                    field.setAccessible(accessible);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static Object getClassObject(Class type) {
        Object value = null;

        try {
            if (isBean(type)) {
                String scope = ((Bean)type.getDeclaredAnnotation(Bean.class)).scope();

                if (scope.equals(Scope.SINGLETON.toString())) {
                    value = objects.get(type.getTypeName());
                } else if (scope.equals(Scope.PROTOTYPE.toString())) {
                    value = type.newInstance();
                }
            } else if (isService(type)) {
                String scope = ((Service)type.getDeclaredAnnotation(Service.class)).scope();

                if (scope.equals(Scope.SINGLETON.toString())) {
                    value = objects.get(type.getTypeName());
                } else if (scope.equals(Scope.PROTOTYPE.toString())) {
                    value = type.newInstance();
                }
            } else if (isComponent(type)) {
                String scope = ((Component)type.getDeclaredAnnotation(Component.class)).scope();

                if (scope.equals(Scope.SINGLETON.toString())) {
                    value = objects.get(type.getTypeName());
                } else if (scope.equals(Scope.PROTOTYPE.toString())) {
                    value = type.newInstance();
                }
            } else {
                throw new ClassHasNoAnnotationForInject("Class of attribute which is annotated with @Autowired, must be annotated with @Bean, @Service or @Compoenent.");
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassHasNoAnnotationForInject classOfAttributeAnnotatedWithAutowiredMustBeAnnotatedWithAnnotationForInject) {
            classOfAttributeAnnotatedWithAutowiredMustBeAnnotatedWithAnnotationForInject.printStackTrace();
        }

        return value;
    }

    public static void scanAllClasses(Object target) {
        List<Class> classes = getClassesInPackage("testPackage");

        for(Class cl : classes) {
            if(cl.isAnnotationPresent(Bean.class) || cl.isAnnotationPresent(Service.class)) {
                try {
                    objects.put(cl.getTypeName(), cl.newInstance());
                    classesForInject.add(cl.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            if(cl.isAnnotationPresent(Qualifier.class)) {

                if(cl.isAnnotationPresent(Bean.class) || cl.isAnnotationPresent(Service.class) || cl.isAnnotationPresent(Component.class)){
                    Qualifier qualifier = (Qualifier) cl.getDeclaredAnnotation(Qualifier.class);
                    String qualifierValue = qualifier.value();

                    if(ds.qualifierObjects.get(qualifierValue) != null) {
                        try {
                            throw new MultipleClassesWithSamoQualifier("Multiple classes with same qualifier! Qualifier must be unique for every class!");
                        } catch (MultipleClassesWithSamoQualifier classWithThatQualifierAlreadyExists) {
                            classWithThatQualifierAlreadyExists.printStackTrace();
                        }
                    }

                    ds.qualifierObjects.put(qualifierValue, cl);
                } else {
                    try {
                        throw new ClassHasNoAnnotationForInjecttion("Class annotated with @Qualifier, but not annotated with @Bean, or @Service, or @Component. " +
                                "Every class which is annotated with @Qualifier, also must be annotated with @Bean, or @Service, or @Component.");
                    } catch (ClassHasNoAnnotationForInjecttion classHasNoAnnotationForInjecttion) {
                        classHasNoAnnotationForInjecttion.printStackTrace();
                    }
                }
            }
        }

        injectAllAnnotatedFields(target);
    }

    /** This method returns all classes from package 'packageName' */
    public static final List<Class> getClassesInPackage(String packageName) {
        String path = packageName.replaceAll("//.", File.separator);
        List<Class> classes = new ArrayList<>();
        String[] classPathEntries = System.getProperty("java.class.path").split(
                System.getProperty("path.separator")
        );

        String name;
        for (String classpathEntry : classPathEntries) {
            if (!classpathEntry.endsWith(".jar")) {

                try {
                    File base = new File(classpathEntry + File.separatorChar + path);
                    for (File file : base.listFiles()) {

                        name = file.getName();

                        if (name.endsWith(".class")) {
                            name = name.substring(0, name.length() - 6);
                            packageName = packageName.replace("\\", ".");
                            classes.add(Class.forName(packageName + "." + name));
                        } else {
                            List<Class> subClasses = getClassesInPackage(packageName + "\\" + name);

                            for(Class cl : subClasses) {
                                classes.add(cl);
                            }
                        }
                    }
                } catch (Exception ex) {
                    // Silence is gold
                }
            }
        }

        return classes;
    }

}
