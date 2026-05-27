import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import porto.IServico;

public class Doca {

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];

        try {
            // Conecta ao registry
            Registry registry = LocateRegistry.getRegistry(host, 6600);

            // Busca o serviço
            IServico stub = (IServico) registry.lookup("Hello");

            // ================= TESTE =================
            System.out.println("Mensagem: " + stub.mensagem());

            // ================= NAVIO =================
            Integer idNavio1 = stub.cadastrar_navio("Navio A", 1000);
            Integer idNavio2 = stub.cadastrar_navio("Navio B", 2000);

            System.out.println("\n--- RELATORIO NAVIOS ---");
            System.out.println(stub.relatorio_navio());

            stub.remover_navio(idNavio1);

            System.out.println("\n--- RELATORIO NAVIOS (APOS REMOCAO) ---");
            System.out.println(stub.relatorio_navio());

            // ================= CARGA =================
            Integer idCarga1 = stub.cadastrar_carga("Carga de soja", 500);
            Integer idCarga2 = stub.cadastrar_carga("Carga de milho", 800);

            System.out.println("\n--- RELATORIO CARGAS ---");
            System.out.println(stub.relatorio_carga());

            stub.remover_carga(idCarga1);

            System.out.println("\n--- RELATORIO CARGAS (APOS REMOCAO) ---");
            System.out.println(stub.relatorio_carga());

            System.out.println("Dados prontos.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}