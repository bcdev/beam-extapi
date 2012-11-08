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
    private final CConfig[] cConfigs;
    private final Map<String, Map<String, MConfig>> mConfigs;

    ApiGeneratorConfigImpl(CConfig[] cConfigs) {
        this.cConfigs = cConfigs;
        this.mConfigs = createMConfigMap(cConfigs);
    }

    private static Map<String, Map<String, MConfig>> createMConfigMap(CConfig[] cConfigs) {
        Map<String, Map<String, MConfig>> cMap = new HashMap<String, Map<String, MConfig>>();
        for (CConfig cConfig : cConfigs) {
            final HashMap<String, MConfig> mMap = new HashMap<String, MConfig>();
            final MConfig[] methods = cConfig.methods;
            for (MConfig method : methods) {
                mMap.put(method.name + method.sig, method);
            }
            cMap.put(cConfig.name, mMap);
        }
        return cMap;
    }

    public static ApiGeneratorConfig load() throws JDOMException, IOException {
        final SAXBuilder saxBuilder = new SAXBuilder();
        final Document document = saxBuilder.build(ApiGeneratorConfigImpl.class.getResourceAsStream("ApiGeneratorDoclet-config.xml"));
        final Element rootElement = document.getRootElement();
        CConfig[] cConfigs = getCConfigs(rootElement);
        return new ApiGeneratorConfigImpl(cConfigs);
    }

    private static CConfig[] getCConfigs(Element rootElement) {
        final List classChildren = rootElement.getChildren("class");
        if (classChildren == null) {
            throw new IllegalArgumentException("missing 'config/class' elements");
        }
        CConfig[] cConfigs = new CConfig[classChildren.size()];
        for (int i = 0; i < classChildren.size(); i++) {
            Element classChild = (Element) classChildren.get(i);
            final String name = classChild.getAttributeValue("name");
            if (name == null) {
                throw new IllegalArgumentException("missing attribute 'config/class/name'");
            }
            cConfigs[i] = new CConfig(name, getMConfigs(classChild));
        }
        return cConfigs;
    }

    private static MConfig[] getMConfigs(Element classChild) {
        final List methodChildren = classChild.getChildren("method");
        if (methodChildren == null) {
            return new MConfig[0];
        }
        MConfig[] mConfigs = new MConfig[methodChildren.size()];
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
            mConfigs[i] = new MConfig(name, sig, ignore, renameTo, parseModifiers(methodChild));
        }
        return mConfigs;
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

    @Override
    public boolean isApiClass(String className) {
        return mConfigs.get(className) != null;
    }

    @Override
    public boolean isApiMethod(String className, String methodName, String methodSignature) {
        Map<String, MConfig> mm = mConfigs.get(className);
        if (mm == null) {
            throw new IllegalArgumentException(className);
        }
        final MConfig mConfig = mm.get(methodName + methodSignature);
        return mConfig == null || !mConfig.ignore;
    }

    @Override
    public ApiParameter.Modifier[] getParameterModifiers(String className, String methodName, String methodSignature) {
        final MConfig mConfig = mConfigs.get(className).get(methodName + methodSignature);
        if (mConfig == null) {
            return null;
        }
        return mConfig.mods;
    }

    @Override
    public String getMethodCName(String className, String methodName, String methodSignature) {
        final MConfig mConfig = mConfigs.get(className).get(methodName + methodSignature);
        if (mConfig == null) {
            return methodName;
        }
        return mConfig.renameTo != null ? mConfig.renameTo : mConfig.name;
    }

    public static class CConfig {
        private final String name;
        private final MConfig[] methods;

        public CConfig(String name, MConfig[] methods) {
            this.name = name;
            this.methods = methods;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class MConfig {
        private final String name;
        private final String sig;
        private final boolean ignore;
        private final String renameTo;
        private final ApiParameter.Modifier[] mods;

        public MConfig(String name, String sig,  boolean ignore, String renameTo, ApiParameter.Modifier[] mods) {
            this.name = name;
            this.sig = sig;
            this.ignore = ignore;
            this.renameTo = renameTo;
            this.mods = mods;
        }

        @Override
        public String toString() {
            return name + sig;
        }
    }
}
