package com.itsmite.novels.core.configs;

import com.itsmite.novels.core.RequestContext;
import com.itsmite.novels.core.configs.loaders.XmlResourceLoader;
import com.itsmite.novels.core.errors.exceptions.RuntimeCoreException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.Filter;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.itsmite.novels.core.constants.ResourcesConstants.FILTERS_CONFIGURATION_FILE;

/**
 * Responsible for building filters beans
 */
@Configuration
public class FiltersConfig {

    private static final String PRE_FILTERS_XPATH  = "/filters/pre/filter";
    private static final String POST_FILTERS_XPATH = "/filters/post/filter";
    private static final String NAME               = "name";
    private static final String CLASS              = "class";
    private static final String ORDER              = "order";

    private static final Set<String> disableFilters;

    static {
        disableFilters = new HashSet<>();
    }

    @Bean
    public BeanFactoryPostProcessor filtersPostProcessorBean() {
        XmlResourceLoader<List<FilterConfiguration>> xmlResourceLoader = new XmlResourceLoader<>(this::processFiltersConfigurations);
        List<FilterConfiguration> configurations = xmlResourceLoader.process(FILTERS_CONFIGURATION_FILE);
        return new BeanFactoryPostProcessor() {

            @SneakyThrows
            @Override
            public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
                if (beanFactory instanceof DefaultListableBeanFactory) {
                    disableSpringBootDefaultFilters(beanFactory);
                    for (FilterConfiguration configuration : configurations) {
                        Filter filter = createFilter(configuration);
                        BeanDefinition registrationDefinition = BeanDefinitionBuilder.genericBeanDefinition(FilterRegistrationBean.class)
                                                                                     .addPropertyValue("filter", filter)
                                                                                     .addPropertyValue("order", configuration.getOrder())
                                                                                     .getBeanDefinition();
                        ((DefaultListableBeanFactory)beanFactory).registerBeanDefinition(configuration.getName(), registrationDefinition);
                    }
                }
            }

            /**
             * Disables springboot default filters such as https filter if #disableFilter(filterBeanName, beanFactory) = true
             */
            private void disableSpringBootDefaultFilters(ConfigurableListableBeanFactory beanFactory) {
                for (String filterBeanName : beanFactory.getBeanNamesForType(Filter.class, true, false)) {
                    if (disableFilter(filterBeanName)) {
                        BeanDefinition registrationDefinition = BeanDefinitionBuilder.genericBeanDefinition(FilterRegistrationBean.class)
                                                                                     .addConstructorArgReference(filterBeanName)
                                                                                     .addConstructorArgValue(new ServletRegistrationBean[0])
                                                                                     .addPropertyValue("enabled", false)
                                                                                     .getBeanDefinition();
                        ((DefaultListableBeanFactory)beanFactory).registerBeanDefinition(filterBeanName + "Registration", registrationDefinition);
                    }
                }
            }

            /**
             * Should contain disable logic
             */
            private boolean disableFilter(String filterBeanName) {
                return disableFilters.contains(filterBeanName);
            }
        };
    }

    private Filter createFilter(FilterConfiguration filterConfiguration) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
        InvocationTargetException, InstantiationException {
        Class classType = Class.forName(filterConfiguration.getClassPath());
        Constructor<?> constructor = classType.getConstructor(RequestContext.class);
        return (Filter)constructor.newInstance(new RequestContext());
    }

    /**
     * Process xml configuration file
     */
    private List<FilterConfiguration> processFiltersConfigurations(Document document) {
        List<FilterConfiguration> filtersConfigurations = getFiltersConfigurations(document, PRE_FILTERS_XPATH);
        filtersConfigurations.addAll(getFiltersConfigurations(document, POST_FILTERS_XPATH));
        return filtersConfigurations;
    }

    private List<FilterConfiguration> getFiltersConfigurations(Document document, String xpath) {
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            NodeList nodeList = (NodeList)xPath.compile(xpath).evaluate(document, XPathConstants.NODESET);
            List<FilterConfiguration> configurations = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element)nodeList.item(i);

                configurations.add(new FilterConfiguration(
                    element.getElementsByTagName(NAME).item(0).getTextContent(),
                    element.getElementsByTagName(CLASS).item(0).getTextContent(),
                    Integer.parseInt(element.getElementsByTagName(ORDER).item(0).getTextContent())
                ));
            }
            return configurations;
        } catch (Exception XPathExpressionException) {
            throw new RuntimeCoreException(XPathExpressionException.getMessage());
        }
    }

    @Setter
    @Getter
    @AllArgsConstructor
    private static class FilterConfiguration {
        private String name;
        private String classPath;
        private int    order;
    }
}
