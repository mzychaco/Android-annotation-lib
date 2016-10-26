package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

@SupportedAnnotationTypes("com.example.Seriable")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class MyProcessor extends AbstractProcessor{
    Elements elementUtils;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils=processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 获得被该注解声明的元素
        Set<? extends Element> elements=roundEnv.getElementsAnnotatedWith(Seriable.class);
        // 声明类元素
        TypeElement classElement = null ;
        // 声明一个存放成员变量的列表
        List<VariableElement> fields = null;
        // 存放二者
        Map<String, List<VariableElement>> maps = new HashMap<>();
        //foreach
        for (Element ele:elements) {
            if(ele.getKind() == ElementKind.CLASS){// 判断该元素是否为类
                classElement = (TypeElement) ele;
                fields = new ArrayList<>();
                maps.put(classElement.getQualifiedName().toString(),fields);
            }else if(ele.getKind() == ElementKind.FIELD){// 判断该元素是否为成员变量
                VariableElement varEle = (VariableElement) ele;
                // 获取该元素封装类型
                TypeElement enclosingElement = (TypeElement) varEle.getEnclosingElement();
                // 拿到key
                String key = enclosingElement.getQualifiedName().toString();
                fields = maps.get(key);
                if (fields == null)
                {
                    maps.put(key, fields = new ArrayList<VariableElement>());
                }
                fields.add(varEle);
            }else if(ele.getKind() == ElementKind.METHOD){

            }

            for (String key:maps.keySet()) {
                if(maps.get(key).size()==0){
                    TypeElement typeEle=elementUtils.getTypeElement(key);
                    List<? extends Element> allMember= elementUtils.getAllMembers(typeEle);
                    if(allMember.size()>0){
                        maps.get(key).addAll(ElementFilter.fieldsIn(allMember));
                    }
                }
            }

        }
        System.out.println("GEN!!");
        generateCodes(maps);
        return true;
    }

    private void generateCodes(Map<String, List<VariableElement>> maps)
    {
        File dir = new File("/Users/mozhenyong/Desktop/AnnotationTest");
        if (!dir.exists())
            dir.mkdirs();
        // 遍历map
        for (String key : maps.keySet())
        {

            // 创建文件
            File file = new File(dir, key.replaceAll("\\.", "_") + ".txt");
            try
            {
                /**
                 * 编写json文件内容
                 */
                FileWriter fw = new FileWriter(file);
                fw.append("{").append("class:").append("\"" + key + "\"")
                        .append(",\n ");
                fw.append("fields:\n {\n");
                List<VariableElement> fields = maps.get(key);

                for (int i = 0; i < fields.size(); i++)
                {
                    VariableElement field = fields.get(i);
                    fw.append("  ").append(field.getSimpleName()).append(":")
                            .append("\"" + field.asType().toString() + "\"");
                    if (i < fields.size() - 1)
                    {
                        fw.append(",");
                        fw.append("\n");
                    }
                }
                fw.append("\n }\n");
                fw.append("}");
                fw.flush();
                fw.close();

            } catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }
}
