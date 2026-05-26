import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.util.*;

import entities.Carga;
import entities.Navio;
import entities.Embarque;
import porto.IServico;

public class Server implements IServico {

    // Estruturas de dados
    private List<Navio> navios = new ArrayList<>();
    private List<Carga> cargas = new ArrayList<>();
    private List<Embarque> embarques = new ArrayList<>();

    private int idNavio = 1;
    private int idEmbarque = 1;

    public Server() {
    }

    public static void main(String[] args) {
        try {
            Server servidor = new Server();

            IServico stub = (IServico) UnicastRemoteObject.exportObject(servidor, 0);

            Registry registry = LocateRegistry.createRegistry(6600);
            registry.bind("Hello", stub);

            System.out.println("Servidor pronto");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= NAVIO =================

    public Integer cadastrar_navio(String descricao, Integer capacidade) throws RemoteException {
        int id = idNavio++;
        Navio navio = new Navio(id, descricao, capacidade);
        navios.add(navio);

        System.out.println("Navio cadastrado: " + descricao);
        return id;
    }

    public void remover_navio(Integer id) throws RemoteException {
        boolean removido = navios.removeIf(n -> n.getId().equals(id));

        if (removido) {
            System.out.println("Navio removido: " + id);
        } else {
            System.out.println("Navio ID " + id + " nao encontrado.");
        }
    }

    public String relatorio_navio() throws RemoteException {
        StringBuilder sb = new StringBuilder();

        for (Navio navio : navios) {
            sb.append("ID: ").append(navio.getId())
                    .append(" | Nome: ").append(navio.getDescricao())
                    .append(" | Capacidade: ").append(navio.getCapacidade())
                    .append("\n");
        }

        return sb.toString();
    }

    // ================= CARGA =================

    public Integer cadastrar_carga(String descricao, Integer volume) throws RemoteException {
        int id = cargas.size() + 1;

        Carga carga = new Carga(id, descricao, volume);
        cargas.add(carga);

        System.out.println("Carga cadastrada: " + descricao);
        return id;
    }

    public void remover_carga(Integer id) throws RemoteException {  
        boolean removido = cargas.removeIf(c -> c.getId().equals(id));

        if (removido) {
            System.out.println("Carga removida: " + id);
        } else {
            System.out.println("ID " + id + " não encontrado.");
        }
    }

    public String relatorio_carga() throws RemoteException {
        StringBuilder sb = new StringBuilder();

        for (Carga carga : cargas) {
            sb.append("ID: ").append(carga.getId())
            .append(" | Descrição: ").append(carga.getDescricao())
            .append(" | Volume: ").append(carga.getVolume())
            .append("\n");
        }

        return sb.toString();
    }

    // ================= EMBARQUE =================

    public double embarcar(String descricao) throws RemoteException {
        if (cargas.isEmpty()) {
            throw new RemoteException("Nenhuma carga disponível para embarque.");
        }

        Carga carga = cargas.get(0);

        for (Navio navio : navios) {
            if (navio.getCapacidadeRestante() >= carga.getVolume()) {
                navio.setCapacidadeRestante(navio.getCapacidadeRestante() - carga.getVolume());

                int id = idEmbarque++;
                Embarque embarque = new Embarque(id, navio.getId(), carga.getId(), descricao);
                embarques.add(embarque);
                cargas.remove(0);

                System.out.println("Embarque realizado: carga " + carga.getId() + " no navio " + navio.getId());
                return carga.getVolume();
            }
        }

        throw new RemoteException("Nenhum navio com espaço suficiente para a carga (volume: " + carga.getVolume() + ").");
    }

    public String relatorio_embarque() throws RemoteException {
        StringBuilder sb = new StringBuilder();

        for (Embarque embarque : embarques) {
            sb.append("ID: ").append(embarque.getId())
                    .append(" | Navio: ").append(embarque.getIdNavio())
                    .append(" | Carga: ").append(embarque.getIdCarga())
                    .append(" | Descricao: ").append(embarque.getDescricao())
                    .append("\n");
        }

        return sb.toString();
    }

    // ================= TESTE =================

    public String mensagem() throws RemoteException {
        System.out.println("executando mensagem()");
        return "teste";
    }
}