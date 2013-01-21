package org.n3r.axis2.helloworld;

import java.net.SocketTimeoutException;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

public class Axis2Client {
    // http://127.0.0.1:8088/axis2/services/SimpleService?wsdl
    // http://211.94.67.60:9008/svn/ess/trunk/Tools/EfaceTmplCreator/src/main/java/com/ailk/ecs/efm/tools/ws/Axis2Client.java
    public static void main(String[] args) {
        try {
            RPCServiceClient serviceClient = new RPCServiceClient();
            Options options = serviceClient.getOptions();

            // 设置代理
            /*
            HttpTransportProperties.ProxyProperties proxyProperties = new HttpTransportProperties.ProxyProperties();
            proxyProperties.setProxyName("127.0.0.1");
            proxyProperties.setProxyPort(8888);
            options.setProperty(HTTPConstants.PROXY, proxyProperties);
            */

            String endPoint = "http://127.0.0.1:8088/axis2/services/SimpleService";
            EndpointReference targetEPR = new EndpointReference(endPoint);
            options.setTo(targetEPR);
            options.setTimeOutInMilliSeconds(60); // 不设则默认为30秒
            String namespace = "http://www.10010.com/eface/";

            String operationName = "echo";
            QName opEntry = new QName(namespace, operationName );

            Object[] opEntryArgs = new Object[] { "<info>hello world '100', \"222\"</info>" };
            //serviceClient.invokeRobust(opEntry, opAddEntryArgs);
            String str = (String) serviceClient.invokeBlocking(opEntry, opEntryArgs, new Class[] { String.class })[0];

            // 防止3次后出现 org.apache.axis2.AxisFault: Timeout waiting for connection
            serviceClient.cleanupTransport();

            System.out.println(str);
        }
        catch (AxisFault fault) {
            Throwable ex = findTimeoutCause(fault);

            System.out.println(ex instanceof SocketTimeoutException
                    ? "接口调用超时" : "接口调用异常" + fault);
        }
    }

    private static Throwable findTimeoutCause(Throwable fault) {
        Throwable ex = fault;
        while (ex != null) {
            ex = ex.getCause();
            if (ex instanceof SocketTimeoutException) {
                break;
            }
        }

        return ex;
    }
}
