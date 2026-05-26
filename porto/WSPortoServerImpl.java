package porto;

import javax.jws.WebService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@WebService(endpointInterface = "porto.WSPortoServer")
public class WSPortoServerImpl implements WSPortoServer {

    private IServico rmi;

    public WSPortoServerImpl() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 6600);
            rmi = (IServico) registry.lookup("Hello");
            System.out.println("WS conectado ao servidor RMI.");
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao RMI: " + e.getMessage());
        }
    }

    public Integer cadastrar_navio(String descricao, Integer capacidade) {
        try {
            return rmi.cadastrar_navio(descricao, capacidade);
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            return -1;
        }
    }

    public void remover_navio(Integer id) {
        try {
            rmi.remover_navio(id);
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    public String relatorio_navio() {
        try {
            return rmi.relatorio_navio();
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

    public Integer cadastrar_carga(String descricao, Integer volume) {
        try {
            return rmi.cadastrar_carga(descricao, volume);
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            return -1;
        }
    }

    public void remover_carga(Integer id) {
        try {
            rmi.remover_carga(id);
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    public String relatorio_carga() {
        try {
            return rmi.relatorio_carga();
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

    public double embarcar(String descricao) {
        try {
            return rmi.embarcar(descricao);
        } catch (Exception e) {
            System.err.println("Erro ao embarcar: " + e.getMessage());
            return -1;
        }
    }

    public String relatorio_embarque() {
        try {
            return rmi.relatorio_embarque();
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

}
