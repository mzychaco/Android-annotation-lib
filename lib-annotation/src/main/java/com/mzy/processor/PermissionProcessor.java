package com.mzy.processor;

import com.mzy.annotation.GrantPermissionFail;
import com.mzy.annotation.GrantPermissionSucc;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import static javax.lang.model.SourceVersion.latestSupported;

/**
 * Created by mozhenyong on 16/10/25.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class PermissionProcessor extends AbstractProcessor {

    private Messager messager;
    private Elements elementUtils;
    private Map<String, ProxyHelper> mProxyMap = new HashMap<String, ProxyHelper>();

    /**
     * 声明支持的注解类，等同于对PermissionProcessor写注解：@SupportedAnnotationTypes("com.example.Seriable")
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportedTypes=new HashSet<>();
        supportedTypes.add(GrantPermissionSucc.class.getCanonicalName());
        supportedTypes.add(GrantPermissionFail.class.getCanonicalName());
        return supportedTypes;
    }

//    @Override
//    public SourceVersion getSupportedSourceVersion()
//    {
//        return latestSupported();
//    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mProxyMap.clear();
        messager.printMessage(Diagnostic.Kind.NOTE, "process...");

        if (!processAnnotations(roundEnv, GrantPermissionSucc.class)) return false;
        if (!processAnnotations(roundEnv, GrantPermissionFail.class)) return false;
//        if (!processAnnotations(roundEnv, ShowRequestPermissionRationale.class)) return false;

        for (String c : mProxyMap.keySet()) {
            ProxyHelper helper = mProxyMap.get(c);
            try {
                JavaFileObject jfo=processingEnv.getFiler().createSourceFile(
                        helper.getProxyClassFullName(),
                        helper.getTypeElement());
                Writer writer = jfo.openWriter();
                writer.write(helper.generateJavaCode().toCharArray());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                error(helper.getTypeElement(),
                        "Unable to write injector for type %s: %s",
                        helper.getTypeElement(), e.getMessage());
            }
        }


        return true;
    }

    public boolean processAnnotations(RoundEnvironment roundEnv, Class<? extends Annotation> clazz){
        for (Element ele : roundEnv.getElementsAnnotatedWith(clazz)) {
            if(!checkMethodValid(ele,clazz)) continue;

            ExecutableElement annotateedMethod = (ExecutableElement) ele;
            //class type
            TypeElement classEle = (TypeElement) annotateedMethod.getEnclosingElement();//注解的方法所在的类
            //Qualified name of the class
            String fullName = classEle.getQualifiedName().toString();

            ProxyHelper helper = mProxyMap.get(fullName);
            if(helper==null){
                helper=new ProxyHelper(elementUtils,classEle);
                mProxyMap.put(fullName,helper);
            }

            Annotation annotation = annotateedMethod.getAnnotation(clazz);
            if(annotation instanceof GrantPermissionSucc){
                int requestCode=((GrantPermissionSucc)annotation).value();
                helper.grantMethodMap.put(requestCode,annotateedMethod.getSimpleName().toString());
            }else if(annotation instanceof GrantPermissionFail){
                int requestCode=((GrantPermissionFail)annotation).value();
                helper.deniedMethodMap.put(requestCode,annotateedMethod.getSimpleName().toString());
            }else{
                error(ele, "%s not support .", clazz.getSimpleName());
                return false;
            }

        }

        return true;
    }

    private boolean checkMethodValid(Element annotatedElement, Class clazz) {
        if (annotatedElement.getKind() != ElementKind.METHOD) {
            error(annotatedElement, "%s must be declared on method.", clazz.getSimpleName());
            return false;
        }
//        if (ClassValidator.isPrivate(annotatedElement) || ClassValidator.isAbstract(annotatedElement)) {
//            error(annotatedElement, "%s() must can not be abstract or private.", annotatedElement.getSimpleName());
//            return false;
//        }

        return true;
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message, element);
    }
}
