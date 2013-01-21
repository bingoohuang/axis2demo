package org.n3r.axis2.helloworld;


/**
 * The service implementation class
 */
public class SimpleService {

    /**
     * The echo method which will be exposed as the echo operation of the web
     * service
     */
    public String echo(String value) {
        return "<bingoo>" + value + "</bingoo>";
    }
}
