package org.esa.beam.extapi.gen;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Norman Fomferra
 */
public class ApiGeneratorConfigImpl implements ApiGeneratorConfig {

    private final String[] sourcePaths;
    private final String[] packages;
    private final Map<String, Map<String, MethodConfig>> methodConfigsMap;
    private final boolean includeDeprecatedClasses;
    private final boolean includeDeprecatedMethods;
    private final String version;

    @Override
    public boolean getIncludeDeprecatedClasses() {
        return includeDeprecatedClasses;
    }

    @Override
    public boolean getIncludeDeprecatedMethods() {
        return includeDeprecatedMethods;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String[] getSourcePaths() {
        return sourcePaths;
    }

    @Override
    public String[] getPackages() {
        return packages;
    }


    @Override
    public boolean isApiClass(String className) {
        return methodConfigsMap.get(className) != null;
    }

    @Override
    public boolean isApiMethod(String className, String methodName, String methodSignature) {
        Map<String, MethodConfig> mm = methodConfigsMap.get(className);
        if (mm == null) {
            throw new IllegalArgumentException(className);
        }
        final MethodConfig methodConfig = mm.get(methodName + methodSignature);
        return methodConfig == null || !methodConfig.ignore;
    }

    @Override
    public String getFunctionName(String className, String methodName, String methodSignature) {
        final MethodConfig methodConfig = methodConfigsMap.get(className).get(methodName + methodSignature);
        if (methodConfig == null) {
            return methodName;
        }
        return methodConfig.renameTo != null ? methodConfig.renameTo : methodConfig.name;
    }

    @Override
    public ApiParameter.Modifier[] getParameterModifiers(String className, String methodName, String methodSignature) {
        final MethodConfig methodConfig = methodConfigsMap.get(className).get(methodName + methodSignature);
        if (methodConfig == null) {
            return null;
        }
        return methodConfig.mods;
    }

    @Override
    public String getLengthExpr(String className, String methodName, String methodSignature) {
        final MethodConfig methodConfig = methodConfigsMap.get(className).get(methodName + methodSignature);
        if (methodConfig == null) {
            return null;
        }
        return methodConfig.lengthExpr;
    }

    public static ApiGeneratorConfig load(TemplateEval.KV... pairs) throws JDOMException, IOException {
        final TemplateEval templateEval = TemplateEval.create(pairs);
        final SAXBuilder saxBuilder = new SAXBuilder();
        final Document document = saxBuilder.build(ApiGeneratorConfigImpl.class.getResourceAsStream("ApiGeneratorDoclet-config.xml"));
        final Element rootElement = document.getRootElement();
        final String[] sourcePaths = getSourcePaths(rootElement, templateEval);
        final String[] packages = getPackages(rootElement, templateEval);
        ClassConfig[] classConfigs = parseClassConfigs(rootElement);
        String deprecatedClasses = rootElement.getAttributeValue("deprecatedClasses");
        String deprecatedMethods = rootElement.getAttributeValue("deprecatedMethods");
        String version = rootElement.getChild("version").getValue();
        return new ApiGeneratorConfigImpl(sourcePaths, packages, classConfigs,
                                          deprecatedClasses != null && deprecatedClasses.equals("true"),
                                          deprecatedMethods != null && deprecatedMethods.equals("true"),
                                          version);
    }

    private static Map<String, Map<String, MethodConfig>> createMethodConfigsMap(ClassConfig[] classConfigs) {
        Map<String, Map<String, MethodConfig>> classMap = new HashMap<String, Map<String, MethodConfig>>();
        for (ClassConfig classConfig : classConfigs) {
            final HashMap<String, MethodConfig> methodMap = new HashMap<String, MethodConfig>();
            final MethodConfig[] methods = classConfig.methodConfigs;
            for (MethodConfig method : methods) {
                methodMap.put(method.name + method.sig, method);
            }
            classMap.put(classConfig.name, methodMap);
        }
        return classMap;
    }

    private ApiGeneratorConfigImpl(String[] sourcePaths,
                                   String[] packages,
                                   ClassConfig[] classConfigs,
                                   boolean includeDeprecatedClasses,
                                   boolean includeDeprecatedMethods,
                                   String version) {
        this.sourcePaths = sourcePaths;
        this.packages = packages;
        this.methodConfigsMap = createMethodConfigsMap(classConfigs);
        this.includeDeprecatedClasses = includeDeprecatedClasses;
        this.includeDeprecatedMethods = includeDeprecatedMethods;
        this.version = version;
    }

    private static String[] getSourcePaths(Element rootElement, TemplateEval templateEval) {
        return parseStringArray(rootElement, "sourcePaths", "path", templateEval);
    }

    private static String[] getPackages(Element rootElement, TemplateEval templateEval) {
        return parseStringArray(rootElement, "packages", "package", templateEval);
    }

    private static String[] parseStringArray(Element rootElement, String containerElemName, String itemElemName, TemplateEval templateEval) {
        final Element containerElem = rootElement.getChild(containerElemName);
        if (containerElem == null) {
            throw new IllegalArgumentException(String.format("missing 'config/%s' element", containerElemName));
        }
        final List itemList = containerElem.getChildren(itemElemName);
        final String[] stringArray = new String[itemList.size()];
        for (int i = 0; i < itemList.size(); i++) {
            Element itemElem = (Element) itemList.get(i);
            stringArray[i] = templateEval.eval(itemElem.getValue().trim());
        }
        return stringArray;
    }

    private static ClassConfig[] parseClassConfigs(Element rootElement) {
        final List classChildren = rootElement.getChildren("class");
        if (classChildren == null) {
            throw new IllegalArgumentException("missing 'config/class' elements");
        }
        ClassConfig[] classConfigs = new ClassConfig[classChildren.size()];
        for (int i = 0; i < classChildren.size(); i++) {
            Element classChild = (Element) classChildren.get(i);
            final String name = classChild.getAttributeValue("name");
            if (name == null) {
                throw new IllegalArgumentException("missing attribute 'config/class/name'");
            }
            classConfigs[i] = new ClassConfig(name, parseMethodConfigs(classChild));
        }
        return classConfigs;
    }

    private static MethodConfig[] parseMethodConfigs(Element classChild) {
        final List methodChildren = classChild.getChildren("method");
        if (methodChildren == null) {
            return new MethodConfig[0];
        }
        MethodConfig[] methodConfigs = new MethodConfig[methodChildren.size()];
        for (int i = 0; i < methodChildren.size(); i++) {
            Element methodChild = (Element) methodChildren.get(i);
            final String name = methodChild.getAttributeValue("name");
            if (name == null) {
                throw new IllegalArgumentException("missing attribute 'config/class/method/name'");
            }
            final String sig = methodChild.getAttributeValue("sig");
            if (sig == null) {
                throw new IllegalArgumentException("missing attribute 'config/class/method/sig'");
            }
            final String ignoreValue = methodChild.getAttributeValue("ignore");
            boolean ignore = ignoreValue != null && Boolean.parseBoolean(ignoreValue);
            final String renameTo = methodChild.getAttributeValue("renameTo");
            final String lengthExpr = methodChild.getAttributeValue("lengthExpr");
            methodConfigs[i] = new MethodConfig(name, sig, ignore, renameTo, parseModifiers(methodChild), lengthExpr);
        }
        return methodConfigs;
    }

    private static ApiParameter.Modifier[] parseModifiers(Element methodChild) {
        final String modsValue = methodChild.getAttributeValue("mods");
        ApiParameter.Modifier[] mods = null;
        if (modsValue != null) {
            mods = new ApiParameter.Modifier[modsValue.length()];
            for (int j = 0; j < modsValue.length(); j++) {
                if (modsValue.charAt(j) == 'i') {
                    mods[j] = ApiParameter.Modifier.IN;
                } else if (modsValue.charAt(j) == 'o') {
                    mods[j] = ApiParameter.Modifier.OUT;
                } else if (modsValue.charAt(j) == 'r') {
                    mods[j] = ApiParameter.Modifier.RETURN;
                } else {
                    throw new IllegalArgumentException("illegal attribute 'config/class/method/mods': " + modsValue);
                }
            }
        }
        return mods;
    }

    private static class ClassConfig {

        private final String name;
        private final MethodConfig[] methodConfigs;

        public ClassConfig(String name, MethodConfig[] methodConfigs) {
            this.name = name;
            this.methodConfigs = methodConfigs;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class MethodConfig {

        private final String name;
        private final String sig;
        private final boolean ignore;
        private final String renameTo;
        private final ApiParameter.Modifier[] mods;
        /**
         * Expression that computes an expected length of an array parameter which serves as return value.
         * Understood?  :-)
         */
        private final String lengthExpr;

        public MethodConfig(String name, String sig, boolean ignore, String renameTo, ApiParameter.Modifier[] mods, String lengthExpr) {
            this.name = name;
            this.sig = sig;
            this.ignore = ignore;
            this.renameTo = renameTo;
            this.mods = mods;
            this.lengthExpr = lengthExpr;
        }

        @Override
        public String toString() {
            return name + sig;
        }
    }
}
