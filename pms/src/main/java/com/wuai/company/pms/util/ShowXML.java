package com.wuai.company.pms.util;

import com.alibaba.fastjson.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShowXML {
    Map<String,Object> map = new HashMap<String,Object>();

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    File f = new File("order/src/main/resources/application.yaml");

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document dt = db.parse(f);
    Element element = dt.getDocumentElement();
    public ShowXML() throws ParserConfigurationException, IOException, SAXException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("根元素",element.getNodeName());
        // 获得根元素下的子节点
        NodeList childNodes = element.getChildNodes();
        // 遍历这些子节点
        for (int i = 0; i < childNodes.getLength(); i++) {
            // 获得每个对应位置i的结点
            Node node1 = childNodes.item(i);
            if ("appender".equals(node1.getNodeName())) {
                // 如果节点的名称为"Account"，则输出Account元素属性type
                jsonObject.put("appenderClass",node1.getAttributes().getNamedItem("class").getNodeValue() + ". ");
                jsonObject.put("appenderName",node1.getAttributes().getNamedItem("name").getNodeValue());
                // 获得<Accounts>下的节点
                NodeList nodeDetail = node1.getChildNodes();
                // 遍历<Accounts>下的节点
                for (int j = 0; j < nodeDetail.getLength(); j++) {
                    // 获得<Accounts>元素每一个节点
                    Node detail = nodeDetail.item(j);
                    if ("File".equals(detail.getNodeName())) { // 输出code
                        jsonObject.put("File",detail.getTextContent());
                    }
                    NodeList nodeList = detail.getChildNodes();
                    for (int k=0;k<nodeList.getLength();k++){
                        Node node = nodeList.item(k);
                        if ("FileNamePattern".equals(node.getNodeName())){
                            jsonObject.put("FileNamePattern",node.getTextContent());
                        }
                    }
                }
            }
            map.put("xml",jsonObject);
        }
        NodeList nodeList = element.getChildNodes();
        for (int k=0;k<nodeList.getLength();k++){
            Node node = nodeList.item(k);
            if ("logger".equals(node.getNodeName())){
                jsonObject.put("loggerName",node.getAttributes().getNamedItem("name").getNodeValue() + ". ");
                jsonObject.put("loggerLevel",node.getAttributes().getNamedItem("level").getNodeValue());

            }
        }
    }




    public static void main(String args[]) {
        Element element = null;
        // 可以使用绝对路劲
        File f = new File("order/src/main/resources/application.yaml");
        System.out.println(f);
        // documentBuilder为抽象不能直接实例化(将XML文件转换为DOM文件)
        DocumentBuilder db = null;
        DocumentBuilderFactory dbf = null;
        try {
            // 返回documentBuilderFactory对象
            dbf = DocumentBuilderFactory.newInstance();
            // 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
            db = dbf.newDocumentBuilder();
            System.out.println(db);
            // 得到一个DOM并返回给document对象
            Document dt = db.parse(f);
            // 得到一个elment根元素
            element = dt.getDocumentElement();
            // 获得根节点
            System.out.println("根元素：" + element.getNodeName());
            // 获得根元素下的子节点
            NodeList childNodes = element.getChildNodes();
            // 遍历这些子节点
            for (int i = 0; i < childNodes.getLength(); i++) {
                // 获得每个对应位置i的结点
                Node node1 = childNodes.item(i);
                if ("appender".equals(node1.getNodeName())) {
                    // 如果节点的名称为"Account"，则输出Account元素属性type
                    System.out.println("appender-->class-->" + node1.getAttributes().getNamedItem("class").getNodeValue() + ". ");
                    System.out.println("appender-->name-->" + node1.getAttributes().getNamedItem("name").getNodeValue());
                    // 获得<Accounts>下的节点
                    NodeList nodeDetail = node1.getChildNodes();
                    // 遍历<Accounts>下的节点
                    for (int j = 0; j < nodeDetail.getLength(); j++) {
                        // 获得<Accounts>元素每一个节点
                        Node detail = nodeDetail.item(j);
                        if ("File".equals(detail.getNodeName())) { // 输出code
                            System.out.println("File--> " + detail.getTextContent());

                        }
                        NodeList nodeList = detail.getChildNodes();
                        for (int k=0;k<nodeList.getLength();k++){
                            Node node = nodeList.item(k);
                            if ("FileNamePattern".equals(node.getNodeName())){
                                System.out.println("FileNamePattern-->"+node.getTextContent());
                            }
                        }
                    }
                }
            }
            NodeList nodeList = element.getChildNodes();
            for (int k=0;k<nodeList.getLength();k++){
                Node node = nodeList.item(k);
                if ("logger".equals(node.getNodeName())){
                    System.out.println("logger-->name-->"+node.getAttributes().getNamedItem("name").getNodeValue() + ". ");
                    System.out.println("logger-->level--->"+node.getAttributes().getNamedItem("level").getNodeValue());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}