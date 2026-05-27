package rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import porto.IServico;

/**
 * Consumidor: lê as 3 filas em threads separadas e invoca o servidor RMI.
 * Fica rodando até ser encerrado (CTRL+C).
 *
 * Compatível com amqp-client 4.0.2 (usa DefaultConsumer em vez de DeliverCallback).
 */
public class FilaConsumer {

    static final String FILA_NAVIO    = "fila_navio";
    static final String FILA_CARGA    = "fila_carga";
    static final String FILA_EMBARQUE = "fila_embarque";

    private static IServico rmi;
    private static long inicio;
    private static AtomicInteger totalProcessadas = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {

        Registry registry = LocateRegistry.getRegistry("localhost", 6600);
        rmi = (IServico) registry.lookup("Hello");
        inicio = System.currentTimeMillis();
        System.out.println("Conectado ao servidor RMI.\n");

        // 1 thread por fila
        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.submit(() -> consumir(FILA_NAVIO,    "NAVIO"));
        executor.submit(() -> consumir(FILA_CARGA,    "CARGA"));
        executor.submit(() -> consumir(FILA_EMBARQUE, "EMBARQUE"));

        System.out.println("Consumidores iniciados. Aguardando mensagens (CTRL+C para sair)...");
    }

    /**
     * Cada thread roda essa função para sua fila.
     * basicQos(1) garante que a thread só pega outra mensagem
     * após confirmar (ACK) a atual — controle de fluxo.
     */
    static void consumir(String fila, String tipo) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");

            // Cada thread tem sua própria conexão e canal
            Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();

            channel.queueDeclare(fila, true, false, false, null);
            channel.basicQos(1); // processa 1 mensagem por vez por thread

            System.out.println("[" + tipo + "] Aguardando mensagens na fila '" + fila + "'...");

            // API 4.x: DefaultConsumer em vez de DeliverCallback
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body)
                        throws IOException {

                    String message = new String(body, "UTF-8");
                    System.out.println(timestamp() + " [" + tipo + "] Recebido: " + message);

                    try {
                        processar(tipo, message);
                        int n = totalProcessadas.incrementAndGet();
                        System.out.println(timestamp() + " [" + tipo + "] Processado OK (" + n + " total)");
                        channel.basicAck(envelope.getDeliveryTag(), false);

                    } catch (Exception e) {
                        System.err.println(timestamp() + " [" + tipo + "] ERRO ao processar: " + e.getMessage());
                        channel.basicNack(envelope.getDeliveryTag(), false, true);
                    }
                }
            };

            // autoAck = false: confirmação manual (mais seguro)
            channel.basicConsume(fila, false, consumer);

        } catch (Exception e) {
            System.err.println("[" + tipo + "] Falha no consumidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String timestamp() {
        long elapsed = System.currentTimeMillis() - inicio;
        long seg = elapsed / 1000;
        long ms  = elapsed % 1000;
        return String.format("[%02d:%02d.%03d]", seg / 60, seg % 60, ms);
    }

    /**
     * Invoca o método RMI correspondente ao tipo da fila.
     *
     * synchronized: protege contra condições de corrida no servidor RMI,
     * já que múltiplas threads chamam este método concorrentemente.
     * (O Server.java usa ArrayList, que não é thread-safe.)
     */
    static synchronized void processar(String tipo, String message) throws Exception {
        switch (tipo) {

            case "NAVIO": {
                // Mensagem: "descricao|capacidade"
                String[] partes   = message.split("\\|");
                String descricao  = partes[0].trim();
                int    capacidade = Integer.parseInt(partes[1].trim());

                Integer id = rmi.cadastrar_navio(descricao, capacidade);
                System.out.println("[NAVIO] Cadastrado com ID: " + id);
                break;
            }

            case "CARGA": {
                // Mensagem: "descricao|volume"
                String[] partes  = message.split("\\|");
                String descricao = partes[0].trim();
                int    volume    = Integer.parseInt(partes[1].trim());

                Integer id = rmi.cadastrar_carga(descricao, volume);
                System.out.println("[CARGA] Cadastrada com ID: " + id);
                break;
            }

            case "EMBARQUE": {
                // Mensagem: "descricao"
                double volume = rmi.embarcar(message.trim());
                System.out.println("[EMBARQUE] Realizado. Volume: " + volume);
                break;
            }

            default:
                System.err.println("Tipo desconhecido: " + tipo);
        }
    }
}
