# SDI Porto — RMI + Filas (RabbitMQ)

## Compilar

```bash
make            # compila tudo (RMI + RabbitMQ)
```

## Executar

```bash
# Terminal 1 — servidor RMI (porta 6600)
make server

# Terminal 2 — consumidor de filas (fica rodando, lê as 3 filas e invoca RMI)
make consumer

# Terminal 3 — produtor (enfileira requisições e encerra)
make producer
```

## Fluxo

1. O **Producer** enfileira mensagens em 3 filas RabbitMQ: `fila_navio`, `fila_carga`, `fila_embarque`.
2. O **Consumer** lê cada fila em uma thread separada (3 threads), desserializa a mensagem e invoca o método RMI correspondente no Server.
3. O **Server** (RMI) processa os cadastros e mantém os dados em memória.

Fluxo do trabalho (como ficou)

┌──────────────┐ ┌───────────────────┐ ┌──────────────┐
│ Producer │ ──────> │ RabbitMQ │ ──────> │ Consumer │
│ (FilaProducer│ │ 3 filas: │ │(FilaConsumer)│
│ .java) │ │ - fila_navio │ │ 3 threads │
│ │ │ - fila_carga │ │ │
│ Enfileira │ │ - fila_embarque │ │ Lê as filas │
│ mensagens │ │ │ │ e chama RMI │
└──────────────┘ └───────────────────┘ └──────┬───────┘
│ RMI (porta 6600)
▼
┌──────────────┐
│ Server │
│ (Server.java)│
│ │
│ Processa: │
│ - cadastrar │
│ - embarcar │
│ - relatórios │
└──────────────┘
Passo a passo:

make server — Sobe o servidor RMI na porta 6600, registra o serviço "Hello" no registry. Mantém navios, cargas e embarques em memória.

make consumer — Conecta ao servidor RMI via lookup. Cria 3 threads (uma por fila) usando ExecutorService. Cada thread abre sua própria conexão RabbitMQ e fica escutando a fila correspondente. Quando chega uma mensagem, o método processar() (que é synchronized) faz o parse e invoca o método RMI correto (cadastrar_navio, cadastrar_carga ou embarcar).

make producer — Enfileira várias mensagens nas 3 filas (3 navios, 3 cargas, 2 embarques) e encerra. As mensagens usam formato texto com | como separador (ex: "Navio Alpha|1000").

Desacoplamento:

Temporal: o Producer pode rodar e encerrar antes do Consumer iniciar — as mensagens ficam persistidas na fila (durable=true).
Espacial: Producer e Consumer não precisam conhecer um ao outro, só o nome da fila.
Concorrência: o synchronized no método processar() do Consumer garante que apenas uma thread por vez chama o servidor RMI, evitando race conditions no ArrayList do Server.
