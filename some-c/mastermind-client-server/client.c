#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <netinet/in.h>


#define MAX_TRIES (35)
#define SLOTS (5)
#define COLORS (8)

#define SHIFT_WIDTH (3)
#define PARITY_ERR_BIT (6)

static const char *progname = "Client"; /* default name */

static void bail_out(int exitcode, const char *fmt, ...)
{

    fprintf(stderr, "%s: ", progname);
    if (fmt != NULL) {
        fprintf(stderr,"%s", fmt);
    }
    fprintf(stderr, "\n");

    //free_resources();
    exit(exitcode);
}



uint16_t calcParity(uint16_t colors){
	unsigned int msg = 0;
	msg = colors; 
	int parity = 0, bit, i;
	//xor every bit
	for(i = 0; i < 15; i++){
		bit = (msg << (17+i)) >> 31;
		parity ^= bit;
	}
	
	return (uint16_t) (msg | parity << 15);
}


void createSolutions(uint16_t arr[]){
	uint16_t curr = 0;
	uint16_t new  = 0;
	do{
		new = calcParity(curr);
		arr[curr] = new;
		curr++;
	}while(curr < 32767);
}

static int compute_answer(uint16_t req, uint8_t *resp, uint8_t *secret)
{
    int colors_left[COLORS];
    int guess[COLORS];
    uint8_t parity_calc, parity_recv;
    int red, white;
    int j;

    parity_recv = (req >> 15) & 1;

    /* extract the guess and calculate parity */
    parity_calc = 0;
    for (j = 0; j < SLOTS; ++j) {
        int tmp = req & 0x7;
        parity_calc ^= tmp ^ (tmp >> 1) ^ (tmp >> 2);
        guess[j] = tmp;
        req >>= SHIFT_WIDTH;
    }
    parity_calc &= 0x1;

    /* marking red and white */
    (void) memset(&colors_left[0], 0, sizeof(colors_left));
    red = white = 0;
    for (j = 0; j < SLOTS; ++j) {
        /* mark red */
        if (guess[j] == secret[j]) {
            red++;
        } else {
            colors_left[secret[j]]++;
        }
    }
    for (j = 0; j < SLOTS; ++j) {
        /* not marked red */
        if (guess[j] != secret[j]) {
            if (colors_left[guess[j]] > 0) {
                white++;
                colors_left[guess[j]]--;
            }
        }
    }

    /* build response buffer */
    resp[0] = red;
    resp[0] |= (white << SHIFT_WIDTH);
    if (parity_recv != parity_calc) {
        resp[0] |= (1 << PARITY_ERR_BIT);
        return -1;
    } else {
        return red;
    }
}

int main(int argc, char** argv){	
	if(argc != 3){
		bail_out(EXIT_FAILURE, "\nSYNOPSIS\n\tclient <server-hostname> <server-port>\nEXAMLPE\n\tclient localhost 1280");
	}
	
	int status;
	struct addrinfo hints;
	struct addrinfo *servinfo;
	int sockfd;	

	//make sure hints is empty
	memset(&hints, 0, sizeof hints);
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_STREAM;
	
	if((status = getaddrinfo(argv[1],argv[2], &hints , &servinfo)) != 0) {
		bail_out(EXIT_FAILURE, "Failed to get addrinfo!\n");
	}

	if((sockfd = socket(servinfo->ai_family, servinfo->ai_socktype , servinfo->ai_protocol)) < 0){
		bail_out(EXIT_FAILURE, "Socket creation failed!\n");
	}

	if(connect(sockfd, servinfo->ai_addr, servinfo->ai_addrlen) < 0){
		bail_out(EXIT_FAILURE, "Socket connection failed!\n");
	}

	int i,sent = 0, rounds = 1;

	uint8_t servMsg = 0;
	unsigned int shiftHelp = 0;

	//nextguess is the next guess which will be sent to the server
	uint16_t nextguess = 11222;
	uint16_t sol [32767];
	uint8_t sequence [5];
	uint8_t fakeResp = 0;
	createSolutions(sol);
	
	do{
		printf("Round %d\n",rounds);
		//send the 2 byte guess to the server
		if((sent = send(sockfd, &sol[nextguess], 2,0)) != 2){
			printf("sent %d\n",sent);
			perror("send failed");
			bail_out(EXIT_FAILURE, "WHY :(\n");
		}
		
		//receive a 1 byte answer from the server	
		if(recv(sockfd, &servMsg, 1, 0) <= 0){
			bail_out(EXIT_FAILURE, "We lost connection\n");		
		}
		
		shiftHelp = servMsg;
		
		//test whether the parity bit is set
		if((shiftHelp << 25 >> 31) != 0){
			if((shiftHelp << 24 >> 31) != 0){
				bail_out(4, "Parity error\nGame lost\n");
			}else{
				bail_out(2, "Parity error!\n");		
			}
		}
		
		//test if we have 5 or more good guesses
		if((shiftHelp << 29 >> 29) > 4){
			break;
		}
		
		//test if we lost the game :(
		if((shiftHelp << 24 >> 31) != 0){
			//printf("%x\n", servMsg);
			//printf("%x\n", (shiftHelp << 24 >> 31));
			bail_out(3, "Game lost\n");		
		}
		
		//one more round - calculate the next solution		
		shiftHelp = sol[nextguess];
		sequence[0] = shiftHelp << 29 >> 29;		
		sequence[1] = shiftHelp << 26 >> 29;		
		sequence[2] = shiftHelp << 23 >> 29;		
		sequence[3] = shiftHelp << 20 >> 29;		
		sequence[4] = shiftHelp << 17 >> 29;
		
		for(i = 0; i < 32767; i++){
			compute_answer(sol[i], &fakeResp, sequence);
			if(fakeResp != servMsg){
				//printf("Fake Resp @: %d\n", i);
				sol[i] = 0;
			}
		}
		
		sol[nextguess] = 0;
		
		for(i = 1; i < 32767; i++){
			if(sol[i] != 0){
				//printf("Next guess: 0x%x@%d\n",sol[i], i);
				nextguess = i;
				break;
			}
		}
		
		rounds++;
	}while(1);
	
	//close socket
	close(sockfd);
	
	printf("Game won!\n");
	return 0;
}
