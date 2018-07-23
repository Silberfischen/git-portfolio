CC=gcc
DEFS=-D_XOPEN_SOURCE=500 -D_BSD_SOURCE
CFLAGS=-Wall -g -std=c99 -pedantic -DENDEBUG $(DEFS)

all: websh

websh: main.o
	$(CC) $(LDFLAGS) -o $@ $^

clean:
	rm -f websh.o websh websh.exe
	
.PHONY: clean