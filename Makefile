# GNU Makefile — SDI Porto (máquina da faculdade)
JAVA_HOME = /usr/local/jdk1.8.0_131
JAVAC     = $(JAVA_HOME)/bin/javac
JAVA      = $(JAVA_HOME)/bin/java

OUT = classes

# RabbitMQ JARs já instalados na máquina da faculdade
RABBIT_JARS = /usr/local/rabbitmq-jar/amqp-client-4.0.2.jar:/usr/local/rabbitmq-jar/slf4j-api-1.7.21.jar:/usr/local/rabbitmq-jar/slf4j-simple-1.7.22.jar

# Classpath completo: classes compiladas + RabbitMQ
CP = $(OUT):$(RABBIT_JARS)

# Fontes RMI
SOURCES = \
	entities/Navio.java \
	entities/Carga.java \
	entities/Embarque.java \
	porto/IServico.java \
	Server.java \

# Fontes RabbitMQ (dependem das classes acima)
SOURCES_RABBIT = \
	rabbitmq/FilaProducer.java \
	rabbitmq/FilaConsumer.java

# ----- COMPILAÇÃO -----

all: compile-rabbit

# Compila apenas RMI
compile:
	mkdir -p $(OUT)
	$(JAVAC) -d $(OUT) $(SOURCES)

# Compila tudo (RMI + RabbitMQ)
compile-rabbit:
	mkdir -p $(OUT)
	$(JAVAC) -cp $(RABBIT_JARS) -d $(OUT) $(SOURCES) $(SOURCES_RABBIT)

# ----- EXECUÇÃO -----

# Terminal 1: servidor RMI
server:
	$(JAVA) -cp $(OUT) Server

# Terminal 2: consumidor de filas (fica rodando)
consumer:
	$(JAVA) -cp $(CP) rabbitmq.FilaConsumer

# Terminal 3: produtor — enfileira as requisições e encerra
producer:
	$(JAVA) -cp $(CP) rabbitmq.FilaProducer

# ----- LIMPEZA -----

clean:
	rm -rf $(OUT)

.PHONY: all compile compile-rabbit server client producer consumer clean
