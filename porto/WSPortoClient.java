package porto;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class WSPortoClient {

    public static void main(String[] args) throws Exception {

        URL wsdlUrl = new URL("http://localhost:8080/wsporto?wsdl");
        QName qname = new QName("http://porto/", "WSPortoServerImplService");

        Service service = Service.create(wsdlUrl, qname);
        WSPortoServer porto = service.getPort(WSPortoServer.class);

        // ================= NAVIO =================
        Integer idNavio1 = porto.cadastrar_navio("Navio A", 1000);
        Integer idNavio2 = porto.cadastrar_navio("Navio B", 2000);

        System.out.println("--- NAVIOS ---");
        System.out.println(porto.relatorio_navio());

        porto.remover_navio(idNavio1);

        System.out.println("--- NAVIOS (APOS REMOCAO) ---");
        System.out.println(porto.relatorio_navio());

        // ================= CARGA =================
        Integer idCarga1 = porto.cadastrar_carga("Carga de soja", 500);
        Integer idCarga2 = porto.cadastrar_carga("Carga de milho", 800);

        System.out.println("--- CARGAS ---");
        System.out.println(porto.relatorio_carga());

        porto.remover_carga(idCarga1);

        System.out.println("--- CARGAS (APOS REMOCAO) ---");
        System.out.println(porto.relatorio_carga());

        // ================= EMBARQUE =================
        double volume = porto.embarcar("Embarque via WS");
        System.out.println("Volume embarcado: " + volume);

        System.out.println("--- EMBARQUES ---");
        System.out.println(porto.relatorio_embarque());
    }
}
