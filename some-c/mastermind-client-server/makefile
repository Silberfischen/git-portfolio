CC=gcc
DEFS=-D_XOPEN_SOURCE=500 -D_BSD_SOURCE
CFLAGS=-Wall -g -std=c99 -pedantic -DENDEBUG $(DEFS)

all: client server

client: client.o
	$(CC) $(LDFLAGS) -o $@ $^

server: server.o
	$(CC) $(LDFLAGS) -o $@ $^

clean:
	rm -f client.o server.o client.exe server.exe
