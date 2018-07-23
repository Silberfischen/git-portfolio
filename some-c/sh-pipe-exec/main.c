/* 
 * @file:   main.c
 * @author: Thorsten Jojart e1327279
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>

/**
 * The standard read size for the buffers
 */
#define READ_SIZE 256

/**
 * Describes a Word:Tag argument that may be passed as an arg
 */
struct {
    char* word;
    char* tag;
} typedef wordTag;

/**
 * Flags all the args that were passed
 */
struct {
    int html;
    int header;
    wordTag tag;
} typedef arguments;

/**
 * The pipe that will be used to communicate between the 2 childs
 */
static int my_pipe[2];
/**
 * Arguments for the program
 */
static arguments args;

/**
 * Parses all the input and fills the args struct
 * @details Also handles error handling
 * @param argc will just be passed from main
 * @param argv will also be passed from main
 */
static void parseInput(int argc, char** argv);

/**
 * @brief Prints the standard usage of the program and the mistake as a param
 * 
 * @param mistake the mistake that was made (will also be printed)
 */
static void printUsage(char* mistake);

/**
 * @brief Forks 2 child processes which will communicate via pipes
 * @details The first child will execute the given programs<br />
 * The second will format the output (given via the pipe from child1) to HTML
 * @param command the command which will be executed
 */
static void doWork(char* command);

/**
 * @brief Executes the given programm via a basic unix shell
 * @param command the command which will be executed
 */
static void execCommand(char* command);

/**
 * @brief Formats the given input to valid html
 * @details Changes all the \n to linebreaks 
 * @param command may be used if a certain flag is set in the args struct
 */
static void format(char* command);

/**
 * @brief Changes last char (if \n) to \0
 * @param line the line which will be trimmed
 */
static void trimLine(char* line);

/**
 * @brief The main reads in several lines and executes all of them as a program
 * in the shell.
 * @details We have following error codes that can occur:<br />
 * 0: everything went fine
 * 1: you did something wrong with the arguments
 * 2: there was some problem with forking
 * 3: your pipes may be broken
 * 4: there was a mistake with executing the command with the child
 * @return the error code, 0 if fine > 0 otherwise
 */
int main(int argc, char** argv) {
    char buf [READ_SIZE];

    parseInput(argc, argv);

    if (args.html) {
        printf("<html>\n<head></head>\n<body>\n");
    }

    //spawn childs for every line written and exit when EOF was found
    while ((fgets(buf, READ_SIZE, stdin)) != NULL) {
        doWork(buf);
    }

    if (args.html) {
        printf("</body>\n</html>");
    }

    return (1);
}

static void doWork(char* command) {

    pid_t pid1, pid2;

    if (pipe(my_pipe) == -1) {
        fprintf(stderr, "Could not create pipe!\n");
        exit(3);
    }

    fflush(stdin);
    fflush(stdout);
    fflush(stderr);

    switch (pid1 = fork()) {
        case -1:
            fprintf(stderr, "Could not fork child1!\n");
            exit(2);
            break;
        case 0:
            execCommand(command);
            break;
        default:
            break;
    }

    switch (pid2 = fork()) {
        case -1:
            fprintf(stderr, "Could not fork child2!\n");
            exit(2);
            break;
        case 0:
            format(command);
            exit(EXIT_SUCCESS);
        default:
            break;
    }

    (void) close(my_pipe[0]);
    (void) close(my_pipe[1]);

    int status;
    waitpid(pid1, &status, 0);
    waitpid(pid1, &status, 0);
}

static void parseInput(int argc, char** argv) {
    int opt;
    int e = 0, h = 0, s = 0;
    char * tag;

    //init the args struct
    args.html = 0;
    args.header = 0;
    args.tag.tag = NULL;
    args.tag.word = NULL;

    while ((opt = getopt(argc, argv, "ehs:")) != -1) {
        switch (opt) {
            case 'e':
                if (e != 0) {
                    printUsage("Duplicate argument -e!");
                }
                //set flag html so we can do proper formatting
                args.html = 1;
                e++;
                break;
            case 'h':
                if (h != 0) {
                    printUsage("Duplicate argument -h!");
                }
                //same as args.html
                args.header = 1;
                h++;
                break;
            case 's':
                if (s != 0) {
                    printUsage("Duplicate argument -s!");
                }
                //seperate the word:tag combination and save it into the struct
                tag = strsep(&optarg, ":");
                printf("Word: %s\n", tag);

                args.tag.word = tag;

                tag = strsep(&optarg, ":");
                printf("Tag: %s\n", tag);

                args.tag.tag = tag;

                s++;
                break;
            default:
                printUsage("False arguments!");
                break;
        }
    }
    //we already read all args, so there shouldn't be more
    if (optind != argc) {
        printUsage("No positional args pls!");
    }
}

static void printUsage(char* mistake) {
    fprintf(stderr, "%s\n", mistake);
    fprintf(stderr, "SYNOPSIS:\n\tUsage: websh [-e] [-h] [-s WORD:TAG]");
    exit(1);
}

static void execCommand(char* command) {
    //close read-"end"
    (void) close(my_pipe[0]);
    //close stdout
    (void) close(STDOUT_FILENO);

    //reopen my_pipe[0] -> the lowest fd is taken -> can only be stdout
    if (dup(my_pipe[1]) != STDOUT_FILENO) /* also look into dup2(2) */ {
        perror("cannot reopen write end as stdout");
    }

    //executes the given command in a basic unix shell
    (void) execlp("/bin/sh", "sh", "-c", command, (char *) 0);

    //the exec failed
    perror("execlp failed");
    exit(4);
}

static void format(char * command) {
    //buffer for fgets
    char buf[READ_SIZE];

    //close write-end
    (void) close(my_pipe[1]);
    //close stdin
    (void) close(STDIN_FILENO);

    /* reopen my_pipe[1] -> the lowest fd is taken -> can only be stdin */
    if (dup(my_pipe[0]) != STDIN_FILENO) {
        perror("cannot reopen write end as stdin");
    }
    //when the header flag was set at the beginning, format the programm call too
    if (args.header) {
        trimLine(command);
        fprintf(stdout, "<h1>%s</h1>\n", command);
    }

    //read every line that comes through the pipe and format it 
    while (fgets(buf, READ_SIZE, stdin) != NULL) {
        trimLine(buf);
        //also search if a word:tag combination was set
        if (args.tag.word != NULL) {
            if (strstr(buf, args.tag.word)) {
                fprintf(stdout, "<%s>%s</%s><br />\n", args.tag.tag, buf, args.tag.tag);
                return;
            }
        }
        fprintf(stdout, "%s<br />\n", buf);
    }
}

static void trimLine(char * line) {
    if (line[strlen(line) - 1 ] == '\n') {
        line[strlen(line) - 1] = '\0';
    }
}