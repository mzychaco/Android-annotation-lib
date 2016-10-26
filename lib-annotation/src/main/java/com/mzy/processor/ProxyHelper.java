package com.mzy.processor;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by mozhenyong on 16/10/25.
 */
public class ProxyHelper {
    private String packageName;
    private String proxyClassName;
    private TypeElement typeElement;

    Map<Integer, String> grantMethodMap = new HashMap<>();
    Map<Integer, String> deniedMethodMap = new HashMap<>();
    Map<Integer, String> rationaleMethodMap = new HashMap<>();

    public static final String PROXY = "PermissionProxy";

    public ProxyHelper(Elements elementUtils, TypeElement classElement){
        this.typeElement=classElement;
        PackageElement packageElement = elementUtils.getPackageOf(classElement);//获取有目标注解的类的packagename，如XXActiviity
        this.packageName=packageElement.getQualifiedName().toString();
        int packageLen = this.packageName.length() + 1;
        String className = classElement.getQualifiedName().toString().substring(packageLen).replace('.','%');
        this.proxyClassName = className+"$$"+PROXY+"Impl";
    }

    public String getProxyClassFullName() {
        return packageName + "." + proxyClassName;
    }

    public String generateJavaCode(){
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code. Do not modify!\n");
        builder.append("package ").append(packageName).append(";\n\n");
        builder.append("import com.mzy.permissionutils.*;\n");
        builder.append('\n');

        builder.append("public class ").append(proxyClassName).append(" implements " + ProxyHelper.PROXY + "<" + typeElement.getSimpleName() + "> {\n");

        generateMethods(builder);//

        builder.append('\n');
        builder.append("}\n");
        return builder.toString();
    }

    public void generateMethods(StringBuilder builder){
        genPermissionSucc(builder);//对应permissionSucc
        genPermissionFailed(builder);//对应permissionFailed
//        generateRationaleMethod(builder);//对应
    }

    public void genPermissionSucc(StringBuilder builder){
        builder.append("@Override\n ");
        builder.append("public void permissionSucc(" + typeElement.getSimpleName() + " impl , int requestCode) {\n");
        builder.append("switch(requestCode) {");
        for (int code : grantMethodMap.keySet())
        {
            builder.append("case " + code + ":");
            builder.append("impl." + grantMethodMap.get(code) + "();");
            builder.append("break;");
        }

        builder.append("}");
        builder.append("  }\n");
    }

    public void genPermissionFailed(StringBuilder builder){
        builder.append("@Override\n ");
        builder.append("public void permissionFailed(" + typeElement.getSimpleName() + " impl , int requestCode) {\n");
        builder.append("switch(requestCode) {");
        for (int code : deniedMethodMap.keySet())
        {
            builder.append("case " + code + ":");
            builder.append("impl." + deniedMethodMap.get(code) + "();");
            builder.append("break;");
        }

        builder.append("}");
        builder.append("  }\n");
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }
}
