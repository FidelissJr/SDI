package rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Produtor: enfileira requisições nas 3 filas do porto.
 * Executa e encerra — não precisa ficar rodando.
 *
 * Uso: java FilaProducer [delay_ms]
 *   delay_ms = intervalo entre publicações (padrão: 1000ms)
 *
 * Compatível com amqp-client 4.0.2 (sem try-with-resources para Channel).
 */
public class FilaProducer {

    static final String FILA_NAVIO    = "fila_navio";
    static final String FILA_CARGA    = "fila_carga";
    static final String FILA_EMBARQUE = "fila_embarque";

    public static void main(String[] argv) throws Exception {

        int delayMs = 3000;
        if (argv.length > 0) {
            delayMs = Integer.parseInt(argv[0]);
        }

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(FILA_NAVIO,    true, false, false, null);
        channel.queueDeclare(FILA_CARGA,    true, false, false, null);
        channel.queueDeclare(FILA_EMBARQUE, true, false, false, null);

        long inicio = System.currentTimeMillis();
        int totalMensagens = 0;

        // ========== NAVIOS ==========
        // Formato: "descricao|capacidade"
        String[] navios = {
            "Navio Alpha|1000",
            "Navio Beta|2000",
            "Navio Gamma|1500",
            "Navio Delta|800",
            "Navio Epsilon|3000"
        };
        for (String msg : navios) {
            channel.basicPublish("", FILA_NAVIO, null, msg.getBytes("UTF-8"));
            totalMensagens++;
            System.out.println(timestamp(inicio) + " [NAVIO]    Enfileirado: " + msg);
            Thread.sleep(delayMs);
        }

        // ========== CARGAS ==========
        // Formato: "descricao|volume"
        String[] cargas = {
            "Carga de soja|500",
            "Carga de milho|800",
            "Carga de trigo|300",
            "Carga de cafe|1200",
            "Carga de acucar|600",
            "Carga de arroz|400",
            "Carga de feijao|700"
        };
        for (String msg : cargas) {
            channel.basicPublish("", FILA_CARGA, null, msg.getBytes("UTF-8"));
            totalMensagens++;
            System.out.println(timestamp(inicio) + " [CARGA]    Enfileirada: " + msg);
            Thread.sleep(delayMs);
        }

        // ========== EMBARQUES ==========
        // Formato: "descricao"
        String[] embarques = {
            "Embarque Lote 1",
            "Embarque Lote 2",
            "Embarque Lote 3",
            "Embarque Lote 4",
            "Embarque Lote 5"
        };
        for (String msg : embarques) {
            channel.basicPublish("", FILA_EMBARQUE, null, msg.getBytes("UTF-8"));
            totalMensagens++;
            System.out.println(timestamp(inicio) + " [EMBARQUE] Enfileirado: " + msg);
            Thread.sleep(delayMs);
        }

        long duracao = System.currentTimeMillis() - inicio;
        System.out.println("\n========== RESUMO ==========");
        System.out.println("Total de mensagens: " + totalMensagens);
        System.out.println("Tempo total:        " + formatDuracao(duracao));
        System.out.println("Delay por mensagem: " + delayMs + "ms");
        System.out.println("============================");

        channel.close();
        connection.close();
    }

    private static String timestamp(long inicio) {
        long elapsed = System.currentTimeMillis() - inicio;
        long seg = elapsed / 1000;
        long ms  = elapsed % 1000;
        return String.format("[%02d:%02d.%03d]", seg / 60, seg % 60, ms);
    }

    private static String formatDuracao(long ms) {
        long seg = ms / 1000;
        long resto = ms % 1000;
        return String.format("%02d:%02d.%03d", seg / 60, seg % 60, resto);
    }
}
