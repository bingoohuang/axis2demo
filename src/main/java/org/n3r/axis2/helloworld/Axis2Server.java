package org.n3r.axis2.helloworld;

import java.util.HashMap;
import java.util.Map;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.MessageReceiver;
import org.apache.axis2.rpc.receivers.RPCInOnlyMessageReceiver;
import org.apache.axis2.rpc.receivers.RPCMessageReceiver;
import org.apache.axis2.transport.http.SimpleHTTPServer;

public class Axis2Server {
    public static void main(String[] args) throws Exception {
        ConfigurationContext context = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
        AxisConfiguration cfg = context.getAxisConfiguration();
        Map<String, MessageReceiver> mrMap = new HashMap<String, MessageReceiver>();
        mrMap.put("http://www.w3.org/ns/wsdl/in-only", RPCInOnlyMessageReceiver.class.newInstance());
        mrMap.put("http://www.w3.org/ns/wsdl/in-out", RPCMessageReceiver.class.newInstance());

        String schemaNamespace = "http://www.10010.com/eface/";
        // public static AxisService createService(String implClass,
        // AxisConfiguration axisConfiguration, Map messageReceiverClassMap,
        // String targetNamespace, String schemaNamespace, ClassLoader loader) throws AxisFault
        AxisService service = AxisService.createService(SimpleService.class.getName(), cfg, mrMap,
                "", schemaNamespace, Axis2Server.class.getClassLoader());

        cfg.addService(service);
        context.setContextRoot("axis2");

        SimpleHTTPServer server = new SimpleHTTPServer(context, 8088);
        server.start();
    }
}
