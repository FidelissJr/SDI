package porto;

import javax.xml.ws.Endpoint;

public class WSPortoPublisher {

    public static void main(String[] args) {
        String url = "http://localhost:8080/wsporto";
        Endpoint.publish(url, new WSPortoServerImpl());
        System.out.println("WS Porto publicado em: " + url + "?wsdl");
    }

}
