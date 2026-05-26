JAVA_HOME ?= C:/Users/andre/AppData/Local/Programs/Eclipse Adoptium/jdk-8.0.492.9-hotspot
JAVAC = "$(JAVA_HOME)/bin/javac"
JAVA  = "$(JAVA_HOME)/bin/java"

OUT = classes

SOURCES = \
	entities/Navio.java \
	entities/Carga.java \
	entities/Embarque.java \
	porto/IServico.java \
	porto/WSPortoServer.java \
	porto/WSPortoServerImpl.java \
	porto/WSPortoPublisher.java \
	porto/WSPortoClient.java \
	Server.java \
	Doca.java

all: compile

compile:
	if not exist $(OUT) mkdir $(OUT)
	$(JAVAC) -d $(OUT) $(SOURCES)

server:
	$(JAVA) -cp $(OUT) Server

ws:
	$(JAVA) -cp $(OUT) porto.WSPortoPublisher

client:
	$(JAVA) -cp $(OUT) Doca

wsclient:
	$(JAVA) -cp $(OUT) porto.WSPortoClient

clean:
	if exist $(OUT) rmdir /s /q $(OUT)

.PHONY: all compile server ws client clean
