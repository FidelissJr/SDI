package rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Produtor: enfileira requisições nas 3 filas do porto.
 * Executa e encerra — não precisa ficar rodando.
 *
 * Compatível com amqp-client 4.0.2 (sem try-with-resources para Channel).
 */
public class FilaProducer {

    static final String FILA_NAVIO    = "fila_navio";
    static final String FILA_CARGA    = "fila_carga";
    static final String FILA_EMBARQUE = "fila_embarque";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Declara as filas (durable=true para coincidir com a configuração do broker)
        channel.queueDeclare(FILA_NAVIO,    true, false, false, null);
        channel.queueDeclare(FILA_CARGA,    true, false, false, null);
        channel.queueDeclare(FILA_EMBARQUE, true, false, false, null);

        // ========== NAVIOS ==========
        // Formato: "descricao|capacidade"
        String[] navios = {
            "Navio Alpha|1000",
            "Navio Beta|2000",
            "Navio Gamma|1500"
        };
        for (String msg : navios) {
            channel.basicPublish("", FILA_NAVIO, null, msg.getBytes("UTF-8"));
            System.out.println("[NAVIO]    Enfileirado: " + msg);
        }

        // ========== CARGAS ==========
        // Formato: "descricao|volume"
        String[] cargas = {
            "Carga de soja|500",
            "Carga de milho|800",
            "Carga de trigo|300"
        };
        for (String msg : cargas) {
            channel.basicPublish("", FILA_CARGA, null, msg.getBytes("UTF-8"));
            System.out.println("[CARGA]    Enfileirada: " + msg);
        }

        // ========== EMBARQUES ==========
        // Formato: "descricao"
        String[] embarques = {
            "Embarque Lote 1",
            "Embarque Lote 2"
        };
        for (String msg : embarques) {
            channel.basicPublish("", FILA_EMBARQUE, null, msg.getBytes("UTF-8"));
            System.out.println("[EMBARQUE] Enfileirado: " + msg);
        }

        System.out.println("\nTodas as mensagens foram enfileiradas.");

        channel.close();
        connection.close();
    }
}
